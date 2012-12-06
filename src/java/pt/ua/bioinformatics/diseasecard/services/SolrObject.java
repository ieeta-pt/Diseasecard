/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.diseasecard.services;

/**
 *
 * @author pedrolopes
 */
public class SolrObject {
    private String omim;
    private String id;

    public String getOmim() {
        return omim;
    }

    public void setOmim(String omim) {
        this.omim = omim;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SolrObject(String omim, String id) {
        this.omim = omim;
        this.id = id;
    }
    
}
