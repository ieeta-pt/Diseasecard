package pt.ua.bioinformatics.diseasecard.domain;

import java.util.ArrayList;

/**
 *
 * @author pedrolopes
 */
public class SearchResult {
    
    private String omim;
    private String name = "";
    private ArrayList<String> alias = new ArrayList<String>() {};
    private ArrayList<String> network = new ArrayList<String>();

    public SearchResult(String omim, String name) {
        this.omim = omim;
        this.name = name;
    }

    public String getOmim() {
        return omim;
    }

    public void setOmim(String omim) {
        this.omim = omim;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getAlias() {
        return alias;
    }

    public void setAlias(ArrayList<String> alias) {
        this.alias = alias;
    }

    public ArrayList<String> getNetwork() {
        return network;
    }

    public void setNetwork(ArrayList<String> network) {
        this.network = network;
    }

    public SearchResult(String omim) {
        this.omim = omim;
    }
    
}
