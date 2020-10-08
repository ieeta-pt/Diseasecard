package pt.ua.bioinformatics.diseasecard.services;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import pt.ua.bioinformatics.coeus.api.DB;
import pt.ua.bioinformatics.coeus.api.ItemFactory;
import pt.ua.bioinformatics.diseasecard.common.Boot;
import pt.ua.bioinformatics.diseasecard.common.Config;
import redis.clients.jedis.Jedis;

/**
 * Utility class to build and cache disease browsing list into Redis.
 *
 * @author pedrolopes
 */
public class Browsier {

    private static DB db = new DB("DC4", Config.getConnectionInfo("diseasecard_diseasecard"));;

    
    public static void start() {

        toDB();
        toCache();
    }

    /**
     * Load entry list summary into temporary database.
     */
    public static void toDB() {
        Boot.start();
        Jedis jedis = Boot.getJedis();  
<<<<<<< HEAD

=======
        db = new DB("DC4", Config.getConnectionInfo("diseasecard_diseasecard"));
>>>>>>> 2b44b690fc77cce62ee513cbea06d8eecfe15115
        ResultSet rs = Boot.getAPI().selectRS("SELECT ?u WHERE { ?u coeus:hasConcept diseasecard:concept_OMIM } ORDER BY ?u", false);
        while (rs.hasNext()) {

            try {
                QuerySolution row = rs.next();
<<<<<<< HEAD
                //System.out.println("omim:" + ItemFactory.getTokenFromItem(ItemFactory.getTokenFromURI(row.get("u").toString())));
=======
                System.out.println("omim:" + ItemFactory.getTokenFromItem(ItemFactory.getTokenFromURI(row.get("u").toString())));
>>>>>>> 2b44b690fc77cce62ee513cbea06d8eecfe15115
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
                System.out.println("[BROWSIER] " + e.getMessage());
            }
        }
    }

    /*
     * Cache disease list for each starting character in Redis
     */
    public static void toCache() {
        String[] list = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
//        Boot.start();
        Finder f = new Finder();
        Jedis jedis = Boot.getJedis();
        for (String start : list) {
            try {
                //Boot.getJedis().set("browse:" + start, f.browse(start));
                jedis.set("browse:" + start, f.browse(start));
            } catch (Exception ex) {
                if (Config.isDebug()) {
                    System.out.println("[COEUS][Diseasecard][Browsier] Unable to cache browsing information.");
                    Logger.getLogger(Browsier.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        jedis.save();
    }
}