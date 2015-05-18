package pt.ua.bioinformatics.diseasecard.domain;

/**
 * PROSITE mirror class.
 * 
 * @author pedrolopes
 * @deprecated
 */
public class PROSITE {

    private String id;
    private String label;
    private String uri;
    private Disease disease;
    private UniProt uniprot;

    PROSITE(String id, String uri, UniProt uniprot) {
        this.id = id;
        this.uri = uri;
        this.uniprot = uniprot;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public UniProt getUniprot() {
        return uniprot;
    }

    public void setUniprot(UniProt uniprot) {
        this.uniprot = uniprot;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
