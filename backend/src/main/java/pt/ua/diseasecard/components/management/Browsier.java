package pt.ua.diseasecard.components.management;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import pt.ua.diseasecard.components.Boot;
import pt.ua.diseasecard.components.data.DB;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.utils.Finder;
import pt.ua.diseasecard.utils.ItemFactory;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import redis.clients.jedis.Jedis;
import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class Browsier {

    private final SparqlAPI api;
    private final DB db;
    private final String connectionString;

    public Browsier(SparqlAPI api, DiseasecardProperties diseasecardProperties) {
        Objects.requireNonNull(api);
        Objects.requireNonNull(diseasecardProperties);
        this.api = api;
        this.connectionString = diseasecardProperties.getDatabase().get("url") + "?user=" + diseasecardProperties.getDatabase().get("username") + "&password=" + diseasecardProperties.getDatabase().get("password");
        this.db = new DB("DC4", this.connectionString);
    }

    public void start() {
        Logger.getLogger(Browsier.class.getName()).log(Level.INFO,"[Diseasecard][Browsier] Starting process of browsing");

        // Load Diseases
        toDB();

        //Cache diseases
        toCache();
    }

    /**
     * Load entry list summary into temporary database.
     */
    private void toDB() {
        Jedis jedis = Boot.getJedis();
        ResultSet rs = this.api.selectRS("SELECT ?u WHERE { ?u coeus:hasConcept diseasecard:concept_OMIM } ORDER BY ?u", false);
        while (rs.hasNext()) {
            try {
                QuerySolution row = rs.next();
                JSONObject disease = new JSONObject(jedis.get("omim:" + ItemFactory.getTokenFromItem(ItemFactory.getTokenFromURI(row.get("u").toString()))));
                db.connect();
                String q = "INSERT INTO Diseases(omim, c, name) VALUES(?, ? ,?);";

                PreparedStatement p = db.getConnection().prepareStatement(q);
                p.setString(1, disease.get("omim").toString());
                p.setString(2, disease.get("size").toString());
                try {
                    p.setString(3, disease.get("description").toString());
                } catch (Exception ex) {
                    p.setString(3, "");
                }
                p.execute();
                db.close();
            } catch (Exception e) {
                Logger.getLogger(Browsier.class.getName()).log(Level.INFO,"[Diseasecard][Browsier] " + e.getMessage());
            }
        }
        Logger.getLogger(Browsier.class.getName()).log(Level.INFO,"[Diseasecard][Browsier] Process of load do DB finished");
    }

    /*
     * Cache disease list for each starting character in Redis
     */
    private void toCache() {
        String[] list = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        Finder f = new Finder(this.connectionString);
        Jedis jedis = Boot.getJedis();
        for (String start : list) {
            try {
                jedis.set("browse:" + start, f.browse(start));
            } catch (Exception ex) {
                Logger.getLogger(Browsier.class.getName()).log(Level.INFO,"[Diseasecard][Browsier] Unable to cache browsing information.");
                Logger.getLogger(Browsier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        jedis.save();
        Logger.getLogger(Browsier.class.getName()).log(Level.INFO,"[Diseasecard][Browsier] Process of cashing finished");
    }
}
