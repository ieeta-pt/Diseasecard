package pt.ua.bioinformatics.diseasecard.domain;

import pt.ua.bioinformatics.coeus.api.ItemFactory;

/**
 * GWASCentral mirror class.
 * 
 * @author pedrolopes
 * @deprecated
 */
public class GWASCentral {
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

    public GWASCentral(String uri, HGNC hgnc) {
        this.id = ItemFactory.getTokenFromItem(uri);
        this.uri = uri;
        this.hgnc = hgnc;
    }
    
    
    
}
