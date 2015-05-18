package pt.ua.bioinformatics.diseasecard.domain;

import pt.ua.bioinformatics.coeus.api.ItemFactory;

/**
 * HPO mirror class.
 *
 * @author pedrolopes
 * @deprecated
 */
public class HPO {

    private String id;
    private String uri;
    private OMIM omim;
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

    public OMIM getOmim() {
        return omim;
    }

    public void setOmim(OMIM omim) {
        this.omim = omim;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public HPO() {
    }

    public HPO(String uri, OMIM omim) {
        this.uri = uri;
        this.id = ItemFactory.getTokenFromItem(uri);
        this.omim = omim;
        this.disease = omim.getDisease();
    }

    public boolean load() {
        return true;
    }
}
