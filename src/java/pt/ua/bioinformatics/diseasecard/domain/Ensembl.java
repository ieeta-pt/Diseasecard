package pt.ua.bioinformatics.diseasecard.domain;

/**
 * Ensembl mirror class.
 * 
 * @author pedrolopes
 * @deprecated
 */
public class Ensembl {
    private String id;
    private String label;
    private String uri;
    private HGNC hgnc;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Ensembl(String id, String uri, HGNC hgnc) {
        this.id = id;
        this.uri = uri;
        this.hgnc = hgnc;
    }
    
  }
