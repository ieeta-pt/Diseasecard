package pt.ua.bioinformatics.diseasecard.services;

import com.hp.hpl.jena.query.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;
import pt.ua.bioinformatics.coeus.api.DB;
import pt.ua.bioinformatics.coeus.api.ItemFactory;
import pt.ua.bioinformatics.coeus.common.Boot;
import pt.ua.bioinformatics.coeus.common.Config;
import pt.ua.bioinformatics.diseasecard.domain.Disease;

/**
 *
 * @author pedrolopes
 */
public class Finder {

    private String query;
    private ArrayList<Disease> diseases;
    private Matcher match_omim_id;
    private Pattern omimid = Pattern.compile("[0-9]{6}");
    private DB db = new DB("DC4");
    private JSONObject result = new JSONObject();
    private HashMap<Integer, Disease> map = new HashMap<Integer, Disease>();

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public HashMap<Integer, Disease> getMap() {
        return map;
    }

    public void setMap(HashMap<Integer, Disease> map) {
        this.map = map;
    }

    public Matcher getMatch_omim_id() {
        return match_omim_id;
    }

    public void setMatch_omim_id(Matcher match_omim_id) {
        this.match_omim_id = match_omim_id;
    }

    public Pattern getOmimid() {
        return omimid;
    }

    public void setOmimid(Pattern omimid) {
        this.omimid = omimid;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    public ArrayList<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(ArrayList<Disease> diseases) {
        this.diseases = diseases;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Finder(String query) {
        this.query = query;
        this.diseases = new ArrayList<Disease>();
        Boot.start();
    }

    /**
     * Finds disease matches in COEUS S3DB.
     * <p>
     *  Used in search.
     * </p>
     * 
     * @return 
     */
    public ArrayList<Disease> find() {
        try {
            match_omim_id = omimid.matcher(query);
            while (match_omim_id.find()) {
                map.put(Integer.parseInt(query), new Disease(Integer.parseInt(match_omim_id.group())));
            }
            if (!match_omim_id.find()) {
                db.connect(DC4.getIndexString());
                String q = "SELECT omim FROM dc4_index WHERE info like ? ORDER BY omim";
                PreparedStatement p = db.getConnection().prepareStatement(q);
                p.setString(1, "%" + query.replace("%20", " ") + "%");
                java.sql.ResultSet results = p.executeQuery();
                while (results.next()) {
                    if (!map.containsKey(results.getInt("omim"))) {
                        map.put(results.getInt("omim"), new Disease(results.getInt("omim")));
                    }
                }
                db.close();
            }
        } catch (Exception ex) {
            if (Config.isDebug()) {
                System.out.println("[COEUS][Diseasecard][Finder] Unable to find disease");
                Logger.getLogger(Finder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return (ArrayList<Disease>) new ArrayList(map.values());
    }

    /**
     * Finds disease matches in DC4 index.
     * <p>
     *  Used in Autocomplete.
     * </p>
     * 
     * @return 
     */
    public String get() {
        JSONArray list = new JSONArray();
        try {
            db.connect(DC4.getIndexString());
            String q = "SELECT DISTINCT info, omim FROM dc4_index WHERE info like ? ORDER BY omim;";
            PreparedStatement p = db.getConnection().prepareStatement(q);
            p.setString(1, "%" + query.replace("%20", " ") + "%");
            java.sql.ResultSet results = p.executeQuery();
            while (results.next()) {
                JSONObject obj = new JSONObject();
                obj.put("info", results.getString("info"));
                obj.put("omim", results.getString("omim"));
                list.put(obj);
            }
            db.close();
            result.put("results", list);
        } catch (Exception ex) {

            if (Config.isDebug()) {
                System.out.println("[COEUS][Diseasecard][Finder] Unable to get disease");
                Logger.getLogger(Finder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return (String) list.toString();
    }

    public ArrayList<Disease> browse() {
        try {
            ResultSet results = Boot.getAPI().selectRS("SELECT DISTINCT ?d {?d coeus:hasConcept coeus:concept_OMIM . ?d  dc:description ?desc . FILTER regex(?desc, \"(" + query + ")\", \"i\")} ORDER BY ASC(?desc) LIMIT 100", false);
            while (results.hasNext()) {
                map.put(Integer.parseInt(ItemFactory.getTokenFromItem(results.next().get("d").toString())), new Disease(Integer.parseInt(ItemFactory.getTokenFromItem(results.next().get("d").toString()))));
            }

            diseases = new ArrayList(map.values());
        } catch (Exception ex) {

            if (Config.isDebug()) {
                System.out.println("[COEUS][Diseasecard][Finder] Unable to browse disease");
                Logger.getLogger(Finder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return (ArrayList<Disease>) diseases;
    }
}
