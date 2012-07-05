package pt.ua.bioinformatics.diseasecard.domain;

import java.util.HashMap;

/**
 *
 * @author pedrolopes
 */
public class Study {

    private HashMap<String, ClinicalTrial> clinicaltrials = new HashMap<String, ClinicalTrial>();
    private Disease disease;

    public HashMap<String, ClinicalTrial> getClinicaltrials() {
        return clinicaltrials;
    }

    public void setClinicaltrials(HashMap<String, ClinicalTrial> clinicaltrials) {
        this.clinicaltrials = clinicaltrials;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public Study() {
    }

    public Study(Disease disease) {
        this.disease = disease;
    }
}
