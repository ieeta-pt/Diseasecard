/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.diseasecard.domain;

import pt.ua.bioinformatics.coeus.api.ItemFactory;

/**
 *
 * @author pedrolopes
 */
public class UMLS {
        private String id;
    private String uri;
    private HPO hpo;

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

    public HPO getHpo() {
        return hpo;
    }

    public void setHpo(HPO hpo) {
        this.hpo = hpo;
    }

    public UMLS(String uri, HPO hpo) {
        this.id = ItemFactory.getTokenFromItem(uri);
        this.uri = uri;
        this.hpo = hpo;
    }
    
    
    
}
