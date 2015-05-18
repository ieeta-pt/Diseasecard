package pt.ua.bioinformatics.diseasecard.domain;

import java.util.HashMap;

/**
 * Variome Entity mirror class.
 *
 * @author pedrolopes
 * @deprecated
 */
public class Variome {

    private Disease disease;
    private HashMap<String, SwissVar> swissvar = new HashMap<String, SwissVar>();

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public HashMap<String, SwissVar> getSwissvar() {
        return swissvar;
    }

    public void setSwissvar(HashMap<String, SwissVar> swissvar) {
        this.swissvar = swissvar;
    }

    public Variome(Disease disease) {
        this.disease = disease;
    }

    public boolean load() {
        boolean success = false;

        return success;
    }

}
