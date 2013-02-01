package pt.ua.bioinformatics.diseasecard.domain;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.util.HashMap;
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
public class Ontology {

    private Disease disease;
    private HashMap<String, MeSH> mesh = new HashMap<String, MeSH>();
    private HashMap<String, HPO> hpo = new HashMap<String, HPO>();
    private HashMap<String, ICD> icd = new HashMap<String, ICD>();
    private HashMap<String, UMLS> umls = new HashMap<String, UMLS>();

    public HashMap<String, UMLS> getUmls() {
        return umls;
    }

    public void setUmls(HashMap<String, UMLS> umls) {
        this.umls = umls;
    }

    public HashMap<String, ICD> getIcd() {
        return icd;
    }

    public void setIcd(HashMap<String, ICD> icd) {
        this.icd = icd;
    }

    public HashMap<String, HPO> getHpo() {
        return hpo;
    }

    public void setHpo(HashMap<String, HPO> hpo) {
        this.hpo = hpo;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public HashMap<String, MeSH> getMesh() {
        return mesh;
    }

    public void setMesh(HashMap<String, MeSH> mesh) {
        this.mesh = mesh;
    }

    public Ontology() {
    }

    public Ontology(Disease disease) {
        this.disease = disease;
    }

    public boolean load() {
        boolean success = false;
        try {
            if (!this.disease.getOmim().getOrphanet().isEmpty()) {
                for (Orphanet o : this.disease.getOmim().getOrphanet().values()) {
                    ResultSet results = Boot.getAPI().selectRS("SELECT ?p ?o {diseasecard:orphanet_" + o.getId() + " ?p ?o }", false);
                    while (results.hasNext()) {
                        QuerySolution row = results.next();
                        if (PrefixFactory.encode(row.get("p").toString()).equals("coeus:isAssociatedTo")) {
                            if (row.get("o").toString().contains("icd10")) {
                                String code = ItemFactory.getTokenFromItem(row.get("o").toString());
                                if (!this.disease.getOntology().getIcd().containsKey(code)) {
                                    ICD icd10 = new ICD(row.get("o").toString(), o);
                                    o.getIcds().add(icd10);
                                    this.disease.getOntology().getIcd().put(icd10.getId(), icd10);
                                } else {
                                    o.getIcds().add(this.disease.getOntology().getIcd().get(code));
                                }
                            } 
                        }
                    }
                }
            }
            if (!this.disease.getOmim().getHpo().isEmpty()) {
                for (HPO o : this.disease.getOmim().getHpo()) {
                    System.out.println("\t --> " + o.getId());
                    ResultSet results = Boot.getAPI().selectRS("SELECT ?p ?o {<http://bioinformatics.ua.pt/diseasecard/resource/hpo_" + o.getId() + "> ?p ?o }", false);
                    while (results.hasNext()) {
                        QuerySolution row = results.next();
                        if (PrefixFactory.encode(row.get("p").toString()).equals("coeus:isAssociatedTo")) {
                            if (row.get("o").toString().contains("umls")) {
                                System.out.println("\t\t-> " + row.get("o"));
                                String code = ItemFactory.getTokenFromItem(row.get("o").toString());
                                if (!this.disease.getOntology().getUmls().containsKey(code)) {
                                    UMLS u = new UMLS(row.get("o").toString(), o);
                                    this.disease.getOntology().getUmls().put(u.getId(), u);
                                } else {
                                    o.getUmls().add(this.disease.getOntology().getUmls().get(code));
                                }
                            } 
                        }
                    }
                }
            }
            success = true;
        } catch (Exception ex) {
            if (Config.isDebug()) {
                System.out.println("[COEUS][DiseaseCard][Ontology] Unable to load Ontology data");
                Logger.getLogger(Ontology.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return success;
    }
}
