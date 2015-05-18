package pt.ua.bioinformatics.diseasecard.domain;

import pt.ua.bioinformatics.coeus.api.ItemFactory;

/**
 * Clinical Trials mirror class.
 * 
 * @author pedrolopes
 * @deprecated
 */
public class ClinicalTrial {
    private HGNC hgnc;
    private String id;
    private String uri;
    private Disease disease;

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ClinicalTrial() {
    }

    public ClinicalTrial(String id, String uri) {
        this.id = id;
        this.uri = uri;
    }

    public ClinicalTrial(String uri, HGNC hgnc) {
        this.hgnc = hgnc;
        this.disease = hgnc.getDisease();
        this.uri = uri;
        this.id = ItemFactory.getTokenFromItem(uri);
    }
    
}
