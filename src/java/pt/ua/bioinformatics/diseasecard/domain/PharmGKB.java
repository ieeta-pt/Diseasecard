package pt.ua.bioinformatics.diseasecard.domain;

import pt.ua.bioinformatics.coeus.api.ItemFactory;

/**
 *  ParhmGKB mirror class.
 * 
 * @author pedrolopes
 * @deprecated
 */
public class PharmGKB {

    private String id;
    private String label;
    private String uri;
    private EntrezGene entrezgene;
    private Disease disease;

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public EntrezGene getEntrezgene() {
        return entrezgene;
    }

    public void setEntrezgene(EntrezGene entrezgene) {
        this.entrezgene = entrezgene;
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

    public PharmGKB() {
    }

    public PharmGKB(String uri, EntrezGene entrezgene) {
        this.uri = uri;
        this.id = ItemFactory.getTokenFromItem(uri);
        this.entrezgene = entrezgene;
        this.disease = entrezgene.getDisease();
    }
}
