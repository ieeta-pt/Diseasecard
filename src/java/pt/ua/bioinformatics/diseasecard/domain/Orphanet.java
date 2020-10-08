/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.diseasecard.domain;

import java.util.ArrayList;
import pt.ua.bioinformatics.coeus.api.ItemFactory;

/**
 * Orphanet mirror class.
 * 
 * @author pedrolopes
 * @deprecated
 */
public class Orphanet {
    private String id;
    private String uri;
    private OMIM omim;
    private ArrayList<ICD> icds = new ArrayList<ICD>();

    public ArrayList<ICD> getIcds() {
        return icds;
    }

    public void setIcds(ArrayList<ICD> icds) {
        this.icds = icds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public OMIM getOmim() {
        return omim;
    }

    public void setOmim(OMIM omim) {
        this.omim = omim;
    }

    public Orphanet(String uri, OMIM omim) {
        this.id = ItemFactory.getTokenFromItem(uri);
        this.uri = uri;
        this.omim = omim;
    }
    
}
