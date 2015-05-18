package pt.ua.bioinformatics.diseasecard.domain;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.coeus.common.Config;

/**
 * Protein Entity mirror class.
 * 
 * @author pedrolopes
 * @deprecated
 */
public class Protein {

    private HashMap<String, UniProt> uniprot = new HashMap<String, UniProt>();
    private Disease disease;
    private HashMap<String, PDB> pdb = new HashMap<String, PDB>();
    private HashMap<String, InterPro> interpro = new HashMap<String, InterPro>();
    private HashMap<String, PROSITE> prosite = new HashMap<String, PROSITE>();

    public HashMap<String, InterPro> getInterpro() {
        return interpro;
    }

    public void setInterpro(HashMap<String, InterPro> interpro) {
        this.interpro = interpro;
    }

    public HashMap<String, PDB> getPdb() {
        return pdb;
    }

    public void setPdb(HashMap<String, PDB> pdb) {
        this.pdb = pdb;
    }

    public HashMap<String, PROSITE> getProsite() {
        return prosite;
    }

    public void setProsite(HashMap<String, PROSITE> prosite) {
        this.prosite = prosite;
    }

    public Protein(Disease disease) {
        this.disease = disease;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public HashMap<String, UniProt> getUniprot() {
        return uniprot;
    }

    public void setUniprot(HashMap<String, UniProt> uniprot) {
        this.uniprot = uniprot;
    }

    public boolean load() {
        boolean success = false;
        try {
            if (!uniprot.isEmpty()) {
                for (UniProt up : uniprot.values()) {
                    up.load();
                }
            }
            success = true;
        } catch (Exception ex) {
            if (Config.isDebug()) {
                System.out.println("[COEUS][API] Unable to load UniProt data");
                Logger.getLogger(Protein.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return success;
    }
}
