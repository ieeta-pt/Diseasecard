/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.diseasecard.services;

import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.diseasecard.domain.Disease;
import pt.ua.bioinformatics.diseasecard.domain.OMIM;

/**
 *
 * @author pedrolopes
 */
public class DiseaseLoad implements Runnable {

private Disease disease;

    public DiseaseLoad() {
    }

    public DiseaseLoad(Disease d) {
        this.disease = d;
    }

    @Override
    public void run() {
        try {
            disease.setOmim(new OMIM(String.valueOf(disease.getId()), disease));
            disease.getOmim().load();
            disease.getLocus().load();
            disease.getProtein().load();
            disease.getOntology().load();
        } catch (Exception ex) {
            Logger.getLogger(DiseaseLoad.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
        }
    }
} 
