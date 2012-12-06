/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.diseasecard.domain;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.coeus.common.Config;

/**
 *
 * @author pedrolopes
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
