package pt.ua.bioinformatics.diseasecard.domain;

import java.util.HashMap;

/**
 * Pathway Entity mirror class.
 * 
 * @author pedrolopes
 * @deprecated
 */
public class Pathway {
    private Disease disease;
    private HashMap<String, Enzyme> enzyme = new HashMap<String, Enzyme>();
    private HashMap<String, KEGG> kegg =  new HashMap<String, KEGG>();

    public Pathway(Disease disease) {
        this.disease = disease;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public HashMap<String, Enzyme> getEnzyme() {
        return enzyme;
    }

    public void setEnzyme(HashMap<String, Enzyme> enzyme) {
        this.enzyme = enzyme;
    }

    public HashMap<String, KEGG> getKegg() {
        return kegg;
    }

    public void setKegg(HashMap<String, KEGG> kegg) {
        this.kegg = kegg;
    }    
}
