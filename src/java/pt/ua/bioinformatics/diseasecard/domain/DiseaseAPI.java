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
 * Disease data loading API. Loads network information both diseases (based on
 * OMIM code) and genes (based on HGNC symbol). Data are queried to
 * Diseasecard's SPARQL endpoint, results are preprocessed based on the desired
 * network configuration and returned as a single JSON object.
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

    public DiseaseAPI() {
        Boot.start();
        this.api = Boot.getAPI();
    }

    public DiseaseAPI(String omim) {
        Boot.start();
        this.omim = omim;
        this.api = Boot.getAPI();
    }

    /**
     * Load disease network information from knowledge base.
     *
     * @return
     */
    public JSONObject load() {
        try {
            map.put("omim", omim);

            // the SPARQL query
            ResultSet rs = api.selectRS("SELECT * WHERE { diseasecard:omim_" + omim + " diseasecard:chromosomalLocation ?chromo . diseasecard:omim_" + omim + " dc:description ?d . diseasecard:omim_" + omim + " coeus:isAssociatedTo ?a1 . ?a1 coeus:isAssociatedTo ?a2 . { OPTIONAL {diseasecard:omim_" + omim + " diseasecard:hasGenotype ?g} . OPTIONAL { diseasecard:omim_" + omim + " diseasecard:hasPhenotype ?p }. OPTIONAL { diseasecard:omim_" + omim + " diseasecard:name ?n } . OPTIONAL {diseasecard:omim_" + omim + " diseasecard:phenotype ?pheno} . OPTIONAL {diseasecard:omim_" + omim + " diseasecard:genotype ?geno .}}}", false);
            JSONArray synonyms = new JSONArray();
            JSONArray results = new JSONArray();
            String description = "";

            // process results (select what matters for the network)
            while (rs.hasNext()) {
                String a1 = "";
                String a2 = "";
                QuerySolution row = rs.next();
                if (row.get("a1").toString().contains("malacards")) {
                    a1 = row.get("a1").toString().replace("http://bioinformatics.ua.pt/diseasecard/resource/malacards_", "malacards:");
                } else {
                    a1 = ItemFactory.getTokenFromURI(row.get("a1").toString()).replace("_", ":");
                }

                if (row.get("a2").toString().contains("mesh")) {
                    a2 = ItemFactory.getTokenFromURI(row.get("a2").toString()).replace("_", ":");
                    if (!a2.contains("mesh")) {
                        a2 = "mesh:" + a2;
                    }
                } else {
                    a2 = ItemFactory.getTokenFromURI(row.get("a2").toString()).replace("_", ":");
                }

                description = row.get("d").toString().replace("{", "").replace("}", "").replace("[", "").replace("]", "");
                map.put("description", description);
                if (row.contains("pheno")) {
                    map.put("phenotype", row.get("pheno").toString());
                }
                if (row.contains("geno")) {
                    map.put("genotype", row.get("geno").toString());
                }
                map.put("location", row.get("chromo").toString());
                if (!list.contains(a1)) {
                    list.add(a1);
                    if (a1.contains("hgnc")) {
                        list.add("wave:" + a1.replace("hgnc:", ""));
                    }
                    if (a1.contains("uniprot")) {
                        list.add("string:" + a1.replace("uniprot:", ""));
                    }
                }
                if (!a2.contains("omim")) {
                    if (!list.contains(a2)) {
                        list.add(a2);
                    }
                }
                if (row.contains("p")) {
                    String p = ItemFactory.getTokenFromURI(row.get("p").toString()).replace("_", ":");
                    if (!list.contains(p)) {
                        list.add(p);
                    }
                }
                if (row.contains("g")) {
                    String g = ItemFactory.getTokenFromURI(row.get("g").toString()).replace("_", ":");
                    if (!list.contains(g)) {
                        list.add(g);
                    }
                }
                if (row.contains("n")) {
                    if (!synonyms.contains(row.get("n").toString())) {
                        synonyms.add(row.get("n").toString());
                    }
                }

            }
            list.add("pubmed:" + description);
            results.addAll(list);

            // add meta
            map.put("size", list.size());
            map.put("synonyms", synonyms);
            map.put("network", results);
        } catch (Exception ex) {
            if (Config.isDebug()) {
                System.err.println("[COEUS][API] Unable to load data for " + omim);
                Logger.getLogger(DiseaseAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return map;
    }

    /**
     *
     * Load gene network from knowledge base.
     *
     * @param hgnc Gene symbol.
     * @return
     */
    public JSONObject loadHGNC(String hgnc) {
        map.put("hgnc", hgnc);

        try {
            ResultSet rs = api.selectRS("SELECT DISTINCT ?a1 ?a2 ?a3 WHERE { diseasecard:hgnc_" + hgnc + " coeus:isAssociatedTo ?a1 .\n"
                    + "    ?a1 coeus:isAssociatedTo ?a2 . ?a2 coeus:isAssociatedTo ?a3}", false);

            JSONArray results = new JSONArray();

            while (rs.hasNext()) {
                String a1 = "";
                String a2 = "";
                String a3 = "";
                QuerySolution row = rs.next();

                // Process column 1
                a1 = ItemFactory.getTokenFromURI(row.get("a1").toString()).replace("_", ":");

                if (!list.contains(a1)) {
                    list.add(a1);
                }

                // Process column 2
                if (row.get("a2").toString().contains("malacards")) {
                    a2 = row.get("a2").toString().replace("http://bioinformatics.ua.pt/diseasecard/resource/malacards_", "malacards:");
                } else {
                    a2 = ItemFactory.getTokenFromURI(row.get("a2").toString()).replace("_", ":");
                }

                if (!a2.contains("malacards") && !a2.contains("omim")) {
                    if (!list.contains(a2)) {
                        list.add(a2);
                    }
                }

                // Process column 3
                if (row.get("a2").toString().contains("uniprot")) {
                    if (row.get("a3").toString().contains("mesh")) {
                        a3 = ItemFactory.getTokenFromURI(row.get("a3").toString()).replace("_", ":");
                        if (!a3.contains("mesh")) {
                            a3 = "mesh:" + a3;
                        }
                    } else {
                        a3 = ItemFactory.getTokenFromURI(row.get("a3").toString()).replace("_", ":");
                    }

                    if (a3.contains("genecards") || a3.contains("mesh")) {
                        if (!list.contains(a3)) {
                            list.add(a3);
                        }
                    }
                }
            }

            list.add("wave:" + hgnc);
            results.addAll(list);
            map.put("size", list.size());

            map.put("network", results);
        } catch (NullPointerException ex) {
            if (Config.isDebug()) {
                System.err.println("[COEUS][API] Unable to load data for " + omim);
                Logger.getLogger(DiseaseAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
            map.put("message", "[Diseasecard] error loading HGNC data from knowledge base.");
        } catch (Exception ex) {
            if (Config.isDebug()) {
                System.err.println("[COEUS][API] Unable to load data for " + omim);
                Logger.getLogger(DiseaseAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
            map.put("message", "[Diseasecard] gene does not exist in knowledge base.");
        }
        return map;
    }
}
