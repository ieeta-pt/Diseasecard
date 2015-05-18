package pt.ua.bioinformatics.diseasecard.domain;

import pt.ua.bioinformatics.coeus.api.ItemFactory;

/**
 * MeSH mirror class.
 * 
 * @author pedrolopes
 * 
 * @deprecated
 */
public class MeSH {

    private String id;
    private String uri;
    private UniProt uniprot;
    private Disease disease;

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UniProt getUniprot() {
        return uniprot;
    }

    public void setUniprot(UniProt uniprot) {
        this.uniprot = uniprot;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    MeSH(String uri, UniProt uniprot) {
        this.uri = uri;
        this.id = ItemFactory.getTokenFromItem(uri);
        this.uniprot = uniprot;
        this.disease = uniprot.getDisease();
    }
}
