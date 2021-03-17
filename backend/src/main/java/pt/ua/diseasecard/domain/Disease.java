package pt.ua.diseasecard.domain;

import java.util.ArrayList;

public class Disease {
    private String name;
    private String omimId;
    private String location;
    private ArrayList<Disease> genotypes;
    private ArrayList<Disease> phenotypes;
    private ArrayList<String> genes;
    private ArrayList<String> names;


    public Disease(String name, String omim) {
        this.name = name;
        this.omimId = omim;
        this.genotypes = new ArrayList<>();
        this.phenotypes = new ArrayList<>();
        this.genes = new ArrayList<>();
        this.names = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getOmimId() {
        return omimId;
    }
    public void setOmimId(String omimId) {
        this.omimId = omimId;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<Disease> getGenotypes() {
        return genotypes;
    }
    public void setGenotypes(ArrayList<Disease> genotypes) {
        this.genotypes = genotypes;
    }

    public ArrayList<Disease> getPhenotypes() {
        return phenotypes;
    }
    public void setPhenotypes(ArrayList<Disease> phenotypes) {
        this.phenotypes = phenotypes;
    }

    public ArrayList<String> getGenes() {
        return genes;
    }
    public void setGenes(ArrayList<String> genes) {
        this.genes = genes;
    }

    public ArrayList<String> getNames() {
        return names;
    }
    public void setNames(ArrayList<String> names) {
        this.names = names;
    }
}
