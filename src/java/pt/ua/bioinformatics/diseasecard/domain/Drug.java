package pt.ua.bioinformatics.diseasecard.domain;

import java.util.HashMap;

/**
 *
 * @author pedrolopes
 * @deprecated
 */
public class Drug {
    private Disease disease;
    private HashMap<String,PharmGKB> pharmgkb = new HashMap<String, PharmGKB>();
    
    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public HashMap<String, PharmGKB> getPharmgkb() {
        return pharmgkb;
    }

    public void setPharmgkb(HashMap<String, PharmGKB> pharmgkb) {
        this.pharmgkb = pharmgkb;
    }

    public Drug() {
    }

    public Drug(Disease disease) {
        this.disease = disease;
    }
    
}
