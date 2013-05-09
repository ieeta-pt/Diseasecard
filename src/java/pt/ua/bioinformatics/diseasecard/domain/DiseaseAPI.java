/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.diseasecard.domain;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pt.ua.bioinformatics.coeus.api.API;
import pt.ua.bioinformatics.coeus.api.ItemFactory;
import pt.ua.bioinformatics.coeus.common.Boot;
import pt.ua.bioinformatics.coeus.common.Config;

/**
 *
 * @author pedrolopes
 */
public class DiseaseAPI {

    private String omim;
    private API api;
    private ArrayList<String> list = new ArrayList<String>();
    private JSONObject map = new JSONObject();

    public String getOmim() {
        return omim;
    }

    public void setOmim(String omim) {
        this.omim = omim;
    }

    public DiseaseAPI(String omim) {
        this.omim = omim;
        this.api = Boot.getAPI();
        map.put("omim",omim);
    }

    public JSONObject load() {
        try {
            ResultSet rs = api.selectRS("SELECT * WHERE { diseasecard:omim_" + omim + " dc:description ?d . diseasecard:omim_" + omim + " coeus:isAssociatedTo ?a1 . ?a1 coeus:isAssociatedTo ?a2 . { OPTIONAL {diseasecard:omim_" + omim + " diseasecard:hasGenotype ?g} . OPTIONAL { diseasecard:omim_" + omim + " diseasecard:hasPhenotype ?p }. OPTIONAL { diseasecard:omim_" + omim + " diseasecard:name ?n }}}", false);
            JSONArray synonyms = new JSONArray();            
            JSONArray results = new JSONArray();
            while (rs.hasNext()) {
                QuerySolution row = rs.next();
                String a1 = ItemFactory.getTokenFromURI(row.get("a1").toString()).replace("_",":");
                String a2 = ItemFactory.getTokenFromURI(row.get("a2").toString()).replace("_",":");
                map.put("description", row.get("d").toString());
                if (!list.contains(a1)) {
                    list.add(a1);
                    if(a1.contains("hgnc")) {
                        list.add("wave:" + a1.replace("hgnc:",""));
                    }
                    if(a1.contains("uniprot")) {
                        list.add("string:" + a1.replace("uniprot:",""));
                    }
                }
                if (!a2.contains("omim")) {
                    if (!list.contains(a2)) {
                        list.add(a2);
                    }
                }
                if (row.contains("p")) {
                    String p = ItemFactory.getTokenFromURI(row.get("p").toString()).replace("_",":");
                    if (!list.contains(p)) {
                        list.add(p);
                    }
                }
                if (row.contains("g")) {
                    String g = ItemFactory.getTokenFromURI(row.get("g").toString()).replace("_",":");
                    if (!list.contains(g)) {
                        list.add(g);
                    }
                }
                if (row.contains("n")) {
                    String n = "pubmed:" + row.get("n").toString();
                    if (!list.contains(n)) {
                        list.add(n);
                       synonyms.add(row.get("n").toString());
                    }
                }
            }
            results.addAll(list);
            map.put("synonyms", synonyms);
            map.put("network", results);
    }
    catch (Exception ex ) {
            if (Config.isDebug()) {
            System.out.println("[COEUS][API] Unable to load data for " + omim);
            Logger.getLogger(DiseaseAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    return map ;
}
}
