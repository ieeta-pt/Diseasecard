package pt.ua.bioinformatics.diseasecard.domain;

import java.util.HashMap;

/**
 *
 * @author pedrolopes
 */
public class Ontology {

    private Disease disease;
    private HashMap<String, MeSH> mesh = new HashMap<String, MeSH>();
    private HashMap<String, HPO> hpo = new HashMap<String, HPO>();

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
        return true;
    }
}
