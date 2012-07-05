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
import pt.ua.bioinformatics.coeus.api.ItemFactory;
import pt.ua.bioinformatics.coeus.api.PrefixFactory;
import pt.ua.bioinformatics.coeus.common.Boot;
import pt.ua.bioinformatics.coeus.common.Config;

/**
 *
 * @author pedrolopes
 */
public class OMIM {

    private String id;
    private String label = "";
    private String uri;
    private String description;
    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<EntrezGene> entrezgene = new ArrayList<EntrezGene>();
    private ArrayList<HGNC> hgnc = new ArrayList<HGNC>();
    private ArrayList<HPO> hpo = new ArrayList<HPO>();
    private String location;
    private boolean phenotype;
    private Disease disease;
    private boolean loaded = false;

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public ArrayList<HPO> getHpo() {
        return hpo;
    }

    public void setHpo(ArrayList<HPO> hpo) {
        this.hpo = hpo;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isPhenotype() {
        return phenotype;
    }

    public void setPhenotype(boolean phenotype) {
        this.phenotype = phenotype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<EntrezGene> getEntrezgene() {
        return entrezgene;
    }

    public void setEntrezgene(ArrayList<EntrezGene> entrezgene) {
        this.entrezgene = entrezgene;
    }

    public ArrayList<HGNC> getHgnc() {
        return hgnc;
    }

    public void setHgnc(ArrayList<HGNC> hgnc) {
        this.hgnc = hgnc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public OMIM() {
    }

    public OMIM(String key, Disease disease) {
        this.disease = disease;
        this.uri = key;
        this.id = ItemFactory.getTokenFromItem(key);
    }

    public boolean load() {
        try {
            String query = "";
            if (uri.startsWith("http://")) {
                query = "SELECT ?p ?o {<" + this.uri + "> ?p ?o }";
            } else {
                query = "SELECT ?p ?o {coeus:omim_" + this.id + " ?p ?o }";
            }
            ResultSet results = Boot.getAPI().selectRS(query, false);
            while (results.hasNext()) {
                QuerySolution row = results.next();
                if (PrefixFactory.encode(row.get("p").toString()).equals("rdfs:label")) {
                    this.label = row.get("o").toString();
                } else if (PrefixFactory.encode(row.get("p").toString()).equals("diseasecard:chromosomalLocation")) {
                    this.location = row.get("o").toString();
                } else if (PrefixFactory.encode(row.get("p").toString()).equals("diseasecard:name")) {
                    this.names.add(row.get("o").toString());
                } else if (PrefixFactory.encode(row.get("p").toString()).equals("dc:description")) {
                    this.description = row.get("o").toString();
                    this.label = row.get("o").toString();
                } else if (PrefixFactory.encode(row.get("p").toString()).equals("diseasecard:hasGenotype")) {
                    String code = ItemFactory.getTokenFromItem(row.get("o").toString());
                    if (!this.disease.getDiseaseMap().containsKey(code)) {
                        this.disease.getDiseaseMap().put(code, new OMIM(row.get("o").toString(), disease));
                    }
                } else if (PrefixFactory.encode(row.get("p").toString()).equals("diseasecard:hasPhenotype")) {
                    String code = ItemFactory.getTokenFromItem(row.get("o").toString());
                    if (!this.disease.getDiseaseMap().containsKey(code)) {
                        this.disease.getDiseaseMap().put(code, new OMIM(row.get("o").toString(), disease));
                    }
                } else if (PrefixFactory.encode(row.get("p").toString()).equals("diseasecard:phenotype")) {
                    this.phenotype = Boolean.parseBoolean(row.get("o").toString());
                } else if (PrefixFactory.encode(row.get("p").toString()).equals("coeus:isAssociatedTo")) {
                    if (row.get("o").toString().contains("hgnc")) {
                        String code = ItemFactory.getTokenFromItem(row.get("o").toString());
                        if (!this.disease.getLocus().getHgnc().containsKey(code)) {
                            HGNC gene = new HGNC(this, row.get("o").toString());
                            gene.setDisease(disease);
                            this.hgnc.add(gene);
                            this.disease.getLocus().getHgnc().put(gene.getId(), gene);
                        } else {
                            this.hgnc.add(this.disease.getLocus().getHgnc().get(code));
                        }
                    } else if (row.get("o").toString().contains("entrez")) {
                        String code = ItemFactory.getTokenFromItem(row.get("o").toString());
                        if(!this.disease.getLocus().getEntrezgene().containsKey(code)) {
                            EntrezGene entrez = new EntrezGene(this, row.get("o").toString());
                            this.entrezgene.add(entrez);
                            this.disease.getLocus().getEntrezgene().put(code, entrez);
                        } else {
                            this.entrezgene.add(this.disease.getLocus().getEntrezgene().get(code));
                        }
                    } else if (row.get("o").toString().contains("hpo_")) {
                        String code = ItemFactory.getTokenFromItem(row.get("o").toString());
                        if(!this.disease.getOntology().getHpo().containsKey(code)) {
                            HPO hp = new HPO(row.get("o").toString(), this);
                            this.hpo.add(hp);
                            this.disease.getOntology().getHpo().put(code, hp);
                        } else {
                            this.hpo.add(this.disease.getOntology().getHpo().get(code));
                        }
                    }
                }
            }
            loaded = true;
        } catch (Exception ex) {
            if (Config.isDebug()) {
                System.out.println("[COEUS][DiseaseCard][OMIM] Unable to load data");
            }
            Logger.getLogger(OMIM.class.getName()).log(Level.SEVERE, null, ex);
        }
        return loaded;

    }
}
