package pt.ua.bioinformatics.diseasecard.domain;

import pt.ua.bioinformatics.coeus.api.ItemFactory;

/**
 * ICD mirror class.
 * 
 * @author pedrolopes
 * @deprecated
 */
public class ICD {
        private String id;
    private String uri;
    private Orphanet orphanet;

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

    public Orphanet getOrphanet() {
        return orphanet;
    }

    public void setOrphanet(Orphanet orphanet) {
        this.orphanet = orphanet;
    }

    public ICD(String uri, Orphanet orphanet) {
        this.id = ItemFactory.getTokenFromItem(uri);
        this.uri = uri;
        this.orphanet = orphanet;
    }
    
    
    
}
