package pt.ua.bioinformatics.diseasecard.domain;

import java.util.ArrayList;
import pt.ua.bioinformatics.coeus.api.ItemFactory;

/**
 *
 * @author pedrolopes
 */
public class HPO {

    private String id;
    private String uri;
    private OMIM omim;
    private Disease disease;
    private ArrayList<UMLS> umls = new ArrayList<UMLS>();

    public ArrayList<UMLS> getUmls() {
        return umls;
    }

    public void setUmls(ArrayList<UMLS> umls) {
        this.umls = umls;
    }
    
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
