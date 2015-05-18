package pt.ua.bioinformatics.diseasecard.domain;

import java.util.HashMap;

/**
 * Study Entity mirror class.
 * 
 * @author pedrolopes
 * @deprecated
 */
public class Study {

    private HashMap<String, ClinicalTrial> clinicaltrials = new HashMap<String, ClinicalTrial>();
    private HashMap<String, GWASCentral> gwascentral = new HashMap<String, GWASCentral>();
    private Disease disease;

    public HashMap<String, GWASCentral> getGwascentral() {
        return gwascentral;
    }

    public void setGwascentral(HashMap<String, GWASCentral> gwascentral) {
        this.gwascentral = gwascentral;
    }

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
