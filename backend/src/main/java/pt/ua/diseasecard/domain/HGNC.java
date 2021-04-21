package pt.ua.diseasecard.domain;

import java.util.List;

/**
 *
 * @author mfs98
 */
public class HGNC {
    /*
        HGNC ID
        Approved symbol
        Previous symbols
        Alias symbols
        Chromosome
        RefSeq IDs
        Enzyme IDs
        NCBI Gene ID
        Ensembl gene ID
        Locus type
        OMIM ID(supplied by OMIM)
        RefSeq(supplied by NCBI)
        UniProt ID(supplied by UniProt)
        Pubmed IDs
    */
    private String hgncID;
    private String approvedSymbol;
    private String chromosomes;
    private String refseqID;
    private String enzymeID;
    private String ncbi;
    private String ensembl;
    private List<String> omims;
    private String uniprot;
    private List<String> pubmedIDs;
    private boolean loaded;

    public HGNC(String hgncID)
    {
        this.hgncID = hgncID;
        this.loaded = false;
    }

    public String getApprovedSymbol() {
        return approvedSymbol;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean getLoaded() {
        return this.loaded;
    }

    public void setApprovedSymbol(String approvedSymbol) {
        this.approvedSymbol = approvedSymbol;
    }

    public String getHgncID() {
        return hgncID;
    }

    public void setHgncID(String hgncID) {
        this.hgncID = hgncID;
    }

    public String getChromosomes() {
        return chromosomes;
    }

    public void setChromosomes(String chromosomes) {
        this.chromosomes = chromosomes;
    }

    public String getRefseqID() {
        return refseqID;
    }

    public void setRefseqID(String refseqID) {
        this.refseqID = refseqID;
    }

    public String getEnzymeID() {
        return enzymeID;
    }

    public void setEnzymeID(String enzymeID) {
        this.enzymeID = enzymeID;
    }

    public String getNcbi() {
        return ncbi;
    }

    public void setNcbi(String ncbi) {
        this.ncbi = ncbi;
    }

    public String getEnsembl() {
        return ensembl;
    }

    public void setEnsembl(String ensembl) {
        this.ensembl = ensembl;
    }

    public List<String> getOmims() {
        return omims;
    }

    public void setOmims(List<String> omims) {
        this.omims = omims;
    }

    public String getUniprot() {
        return uniprot;
    }

    public void setUniprot(String uniprot) {
        this.uniprot = uniprot;
    }

    public List<String> getPubmedIDs() {
        return pubmedIDs;
    }

    public void setPubmedIDs(List<String> pubmedIDs) {
        this.pubmedIDs = pubmedIDs;
    }



}
