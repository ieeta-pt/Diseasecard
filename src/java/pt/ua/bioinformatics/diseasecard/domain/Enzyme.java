package pt.ua.bioinformatics.diseasecard.domain;

import pt.ua.bioinformatics.coeus.api.ItemFactory;

/**
 * Enzyme mirror class.
 * 
 * @author pedrolopes
 * @deprecated
 */
public class Enzyme {
    private String id;
    private String uri;
    private HGNC hgnc;

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

    public HGNC getHgnc() {
        return hgnc;
    }

    public void setHgnc(HGNC hgnc) {
        this.hgnc = hgnc;
    }

    public Enzyme(String uri, HGNC hgnc) {
        this.uri = uri;
        this.hgnc = hgnc;
        this.id = ItemFactory.getTokenFromItem(uri);
    }
    
}
