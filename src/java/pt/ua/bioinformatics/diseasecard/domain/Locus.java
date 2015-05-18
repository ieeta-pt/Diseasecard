package pt.ua.bioinformatics.diseasecard.domain;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.coeus.common.Config;

/**
 * Locus entity mirror class.
 * 
 * @author pedrolopes
 * @deprecated
 */
public class Locus {

    private Disease disease;
    private HashMap<String, HGNC> hgnc = new HashMap<String, HGNC>();
    private HashMap<String, Ensembl> ensembl = new HashMap<String, Ensembl>();
    private HashMap<String, EntrezGene> entrezgene = new HashMap<String, EntrezGene>();

    public HashMap<String, EntrezGene> getEntrezgene() {
        return entrezgene;
    }

    public void setEntrezgene(HashMap<String, EntrezGene> entrezgene) {
        this.entrezgene = entrezgene;
    }

    public HashMap<String, Ensembl> getEnsembl() {
        return ensembl;
    }

    public void setEnsembl(HashMap<String, Ensembl> ensembl) {
        this.ensembl = ensembl;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public HashMap<String, HGNC> getHgnc() {
        return hgnc;
    }

    public void setHgnc(HashMap<String, HGNC> hgnc) {
        this.hgnc = hgnc;
    }

    Locus(Disease aThis) {
        this.disease = aThis;
    }

    public void load() {
        try {
            if (!hgnc.isEmpty()) {
                for (HGNC gene : hgnc.values()) {
                    gene.load();
                }
            }
        } catch (Exception ex) {
            if (Config.isDebug()) {
                System.out.println("[COEUS][API] Unable to execute load HGNC data");
                Logger.getLogger(Locus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            if (!entrezgene.isEmpty()) {
                for (EntrezGene entrez : entrezgene.values()) {
                    entrez.load();
                }
            }
        } catch (Exception ex) {
            if (Config.isDebug()) {
                System.out.println("[COEUS][API] Unable to execute load Entrez data");
                Logger.getLogger(Locus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
