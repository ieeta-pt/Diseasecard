package pt.ua.bioinformatics.diseasecard.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.coeus.domain.Item;

/**
 *
 * @author pedrolopes
 */
public class Disease {
    private int id = 0;
    private String name = "";
    private String omimId = "";
    private OMIM omim = null;
    private String location = "";
    private String entry = "";
    private Protein protein = new Protein(this);
    private Locus locus = new Locus(this);
    private Ontology ontology = new Ontology(this);
    private Drug drug = new Drug(this);
    private Study study = new Study(this);
    private Pathway pathway = new Pathway(this);
    private Variome variome = new Variome(this);
    private ArrayList<String> genes = new ArrayList<String>();
    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<Disease> genotypes = new ArrayList<Disease>();
    private ArrayList<Disease> phenotypes = new ArrayList<Disease>();
    private ArrayList<EntrezGene> entrezgene = new ArrayList<EntrezGene>();
    private ArrayList<Orphanet> orphanet = new ArrayList<Orphanet>();
    private HashMap<String, OMIM> mapGenotype = new HashMap<String, OMIM>();
    private HashMap<String, OMIM> mapPhenotype = new HashMap<String, OMIM>();
    private HashMap<String, OMIM> diseaseMap = new HashMap<String, OMIM>();

    public ArrayList<Orphanet> getOrphanet() {
        return orphanet;
    }

    public void setOrphanet(ArrayList<Orphanet> orphanet) {
        this.orphanet = orphanet;
    }

    public Pathway getPathway() {
        return pathway;
    }

    public void setPathway(Pathway pathway) {
        this.pathway = pathway;
    }

    public Variome getVariome() {
        return variome;
    }

    public void setVariome(Variome variome) {
        this.variome = variome;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public Ontology getOntology() {
        return ontology;
    }

    public void setOntology(Ontology ontology) {
        this.ontology = ontology;
    }

    public HashMap<String, OMIM> getDiseaseMap() {
        return diseaseMap;
    }

    public void setDiseaseMap(HashMap<String, OMIM> diseaseMap) {
        this.diseaseMap = diseaseMap;
    }

    public Locus getLocus() {
        return locus;
    }

    public void setLocus(Locus locus) {
        this.locus = locus;
    }

    public Protein getProtein() {
        return protein;
    }

    public void setProtein(Protein protein) {
        this.protein = protein;
    }

    public ArrayList<String> getGenes() {
        return genes;
    }

    public HashMap<String, OMIM> getMapGenotype() {
        return mapGenotype;
    }

    public void setMapGenotype(HashMap<String, OMIM> mapGenotype) {
        this.mapGenotype = mapGenotype;
    }

    public HashMap<String, OMIM> getMapPhenotype() {
        return mapPhenotype;
    }

    public void setMapPhenotype(HashMap<String, OMIM> mapPhenotype) {
        this.mapPhenotype = mapPhenotype;
    }

    public String getOmimId() {
        return omimId;
    }

    public void setOmimId(String omim_id) {
        this.omimId = omim_id;
    }

    public void setGenes(ArrayList<String> genes) {
        this.genes = genes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<EntrezGene> getEntrezgene() {
        return entrezgene;
    }

    public void setEntrezgene(ArrayList<EntrezGene> entrezgene) {
        this.entrezgene = entrezgene;
    }

    public OMIM getOmim() {
        return omim;
    }

    public void setOmim(OMIM omim) {
        this.omim = omim;
    }

    public ArrayList<Disease> getPhenotypes() {
        return phenotypes;
    }

    public void setPhenotypes(ArrayList<Disease> phenotypes) {
        this.phenotypes = phenotypes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    /**
     * Loads Disease information from SDB based on its key - OMIM code.
     * 
     * @param key 
     */
    public Disease(int key) {
        this.id = key;
        this.omimId = String.valueOf(key);
        try {
            this.omim = new OMIM(String.valueOf(key), this);
            omim.load();
            locus.load();
            protein.load();
            ontology.load();
            variome.load();
        } catch (Exception ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Disease(String name, String omim) {
        this.name = name;
        this.genotypes = new ArrayList<Disease>();
        this.phenotypes = new ArrayList<Disease>();
        this.names = new ArrayList<String>();
        this.omimId = omim;
    }

    public ArrayList<Disease> getGenotypes() {
        return genotypes;
    }

    public void setGenotypes(ArrayList<Disease> genotypes) {
        this.genotypes = genotypes;
    }

    @Override
    public String toString() {
        String response = "";

        response += this.omim + "\t" + this.name;
        for (Disease s : genotypes) {
            response += "\n\t" + s.getName() + " > " + s.getOmim();
        }
        response += "\n\tGenes";

        return response;
    }
}
