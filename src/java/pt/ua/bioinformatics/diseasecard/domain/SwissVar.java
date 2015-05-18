package pt.ua.bioinformatics.diseasecard.domain;

import pt.ua.bioinformatics.coeus.api.ItemFactory;

/**
 * SwissVar mirror class.
 * 
 * @author pedrolopes
 * @deprecated
 */
public class SwissVar {
    private String id;
    private String uri;
    private Variome variome;
    private OMIM omim;

    public OMIM getOmim() {
        return omim;
    }

    public void setOmim(OMIM omim) {
        this.omim = omim;
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

    public Variome getVariome() {
        return variome;
    }

    public void setVariome(Variome variome) {
        this.variome = variome;
    }

    public SwissVar(String id, String uri, Variome variome) {
        this.id = id;
        this.uri = uri;
        this.variome = variome;
    }
    
        public SwissVar(String uri, OMIM omim) {
        this.id = ItemFactory.getTokenFromItem(uri);
        this.uri = uri;
        this.omim = omim;
    }    
}
