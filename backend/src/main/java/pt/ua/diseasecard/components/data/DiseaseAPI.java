package pt.ua.diseasecard.components.data;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pt.ua.diseasecard.utils.ItemFactory;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiseaseAPI {

    private String omim;
    private SparqlAPI api;
    private ArrayList<String> list;
    private JSONObject map;

    public DiseaseAPI(SparqlAPI api) {
        this.api=api;
        this.list = new ArrayList<>();
        this.map = new JSONObject();
    }

    public DiseaseAPI(SparqlAPI api, String omim) {
        this.api=api;
        this.omim = omim;
        this.list = new ArrayList<>();
        this.map = new JSONObject();
    }

    @SuppressWarnings("unchecked")
    public JSONObject load() {
        try {
            this.map.put("omim", this.omim);

            // the SPARQL query
            ResultSet rs = api.selectRS("SELECT * WHERE { " +
                    "diseasecard:OMIM_" + omim + " diseasecard:chromosomalLocation ?chromo . " +
                    "diseasecard:OMIM_" + omim + " dc:description ?d . " +
                    "diseasecard:OMIM_" + omim + " coeus:isAssociatedTo ?a1 . " +
                    "?a1 coeus:isAssociatedTo ?a2 . " +
                    "{ OPTIONAL { diseasecard:OMIM_" + omim + " diseasecard:hasGenotype ?g} . " +
                    "OPTIONAL { diseasecard:OMIM_" + omim + " diseasecard:hasPhenotype ?p }. " +
                    "OPTIONAL { diseasecard:OMIM_" + omim + " diseasecard:name ?n } . " +
                    "OPTIONAL { diseasecard:OMIM_" + omim + " diseasecard:phenotype ?pheno} . " +
                    "OPTIONAL { diseasecard:OMIM_" + omim + " diseasecard:genotype ?geno .}}}", false);


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
                this.map.put("description", description);
                if (row.contains("pheno")) {
                    this.map.put("phenotype", row.get("pheno").toString());
                }
                if (row.contains("geno")) {
                    this.map.put("genotype", row.get("geno").toString());
                }
                this.map.put("location", row.get("chromo").toString());
                if (!this.list.contains(a1)) {
                    this.list.add(a1);
                    /*if (a1.contains("hgnc")) {
                        this.list.add("wave:" + a1.replace("hgnc:", ""));
                    }*/
                    if (a1.contains("UniProt")) {
                        this.list.add("String:" + a1.replace("uniprot:", ""));
                    }
                }
                if (!a2.contains("OMIM")) {
                    if (!this.list.contains(a2)) {
                        this.list.add(a2);
                    }
                }
                if (!this.list.contains("String:" + a2.replace("UniProt:", "")) && a2.contains("UniProt")) {
                    this.list.add("String:" + a2.replace("UniProt:", ""));
                }

                if (row.contains("p")) {
                    String p = ItemFactory.getTokenFromURI(row.get("p").toString()).replace("_", ":");
                    if (!this.list.contains(p)) {
                        this.list.add(p);
                    }
                }
                if (row.contains("g")) {
                    String g = ItemFactory.getTokenFromURI(row.get("g").toString()).replace("_", ":");
                    if (!this.list.contains(g)) {
                        this.list.add(g);
                    }
                }
                if (row.contains("n")) {
                    if (!synonyms.contains(row.get("n").toString())) {
                        synonyms.add(row.get("n").toString());
                    }
                }

            }
            //this.list.add("pubmed:" + description);
            results.addAll(this.list);

            // add meta
            this.map.put("size", this.list.size());
            this.map.put("synonyms", synonyms);
            this.map.put("network", results);
        } catch (Exception ex) {
            Logger.getLogger(DiseaseAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.map;
    }

    @SuppressWarnings("unchecked")
    public JSONObject loadHGNC(String hgnc) {
        this.map.put("hgnc", hgnc);

        try {
            ResultSet rs = this.api.selectRS("SELECT DISTINCT ?a1 ?a2 ?a3 WHERE { diseasecard:HGNC_" + hgnc + " coeus:isAssociatedTo ?a1 . ?a1 coeus:isAssociatedTo ?a2 . ?a2 coeus:isAssociatedTo ?a3}", false);

            JSONArray results = new JSONArray();

            while (rs.hasNext()) {
                String a1;
                String a2;
                String a3;
                QuerySolution row = rs.next();

                // Process column 1
                a1 = ItemFactory.getTokenFromURI(row.get("a1").toString()).replace("_", ":");

                if (!this.list.contains(a1)) {
                    this.list.add(a1);
                }

                // Process column 2
                if (row.get("a2").toString().contains("malacards")) {
                    a2 = row.get("a2").toString().replace("http://bioinformatics.ua.pt/diseasecard/resource/malacards_", "malacards:");
                } else {
                    a2 = ItemFactory.getTokenFromURI(row.get("a2").toString()).replace("_", ":");
                }

                if (!a2.contains("malacards") && !a2.contains("omim")) {
                    if (!this.list.contains(a2)) {
                        this.list.add(a2);
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
                        if (!this.list.contains(a3)) {
                            this.list.add(a3);
                        }
                    }
                }
            }

            //this.list.add("wave:" + hgnc);
            results.addAll(this.list);

            this.map.put("size", this.list.size());
            this.map.put("network", results);

        } catch (NullPointerException ex) {
            System.err.println("[COEUS][API] Unable to load data for " + omim);
            Logger.getLogger(DiseaseAPI.class.getName()).log(Level.SEVERE, null, ex);
            this.map.put("message", "[Diseasecard] error loading HGNC data from knowledge base.");
        } catch (Exception ex) {
            System.err.println("[COEUS][API] Unable to load data for " + omim);
            Logger.getLogger(DiseaseAPI.class.getName()).log(Level.SEVERE, null, ex);
            this.map.put("message", "[Diseasecard] gene does not exist in knowledge base.");
        }
        return this.map;
    }

}
