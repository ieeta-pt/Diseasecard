package pt.ua.bioinformatics.diseasecard.domain;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.coeus.api.ItemFactory;
import pt.ua.bioinformatics.coeus.api.PrefixFactory;
import pt.ua.bioinformatics.coeus.common.Boot;
import pt.ua.bioinformatics.coeus.common.Config;

/**
 * UniProt mirror class.
 * 
 * @author pedrolopes
 * @deprecated
 */
public class UniProt {

    private String id;
    private HGNC hgnc;
    private String uri;
    private String label;
    private ArrayList<PDB> pdb = new ArrayList<PDB>();
    private ArrayList<InterPro> interpro = new ArrayList<InterPro>();
    private ArrayList<PROSITE> prosite = new ArrayList<PROSITE>();
    private ArrayList<MeSH> mesh = new ArrayList<MeSH>();
    private Disease disease;

    public ArrayList<MeSH> getMesh() {
        return mesh;
    }

    public void setMesh(ArrayList<MeSH> mesh) {
        this.mesh = mesh;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public ArrayList<InterPro> getInterpro() {
        return interpro;
    }

    public void setInterpro(ArrayList<InterPro> interpro) {
        this.interpro = interpro;
    }

    public ArrayList<PDB> getPdb() {
        return pdb;
    }

    public void setPdb(ArrayList<PDB> pdb) {
        this.pdb = pdb;
    }

    public ArrayList<PROSITE> getProsite() {
        return prosite;
    }

    public void setProsite(ArrayList<PROSITE> prosite) {
        this.prosite = prosite;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public HGNC getHgnc() {
        return hgnc;
    }

    public void setHgnc(HGNC hgnc) {
        this.hgnc = hgnc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UniProt() {
    }

    public UniProt(HGNC hgnc, String id, String uri) {
        this.hgnc = hgnc;
        this.id = id;
        this.uri = uri;
        this.disease = hgnc.getDisease();
    }

    public boolean load() {
        boolean success = false;
        try {
            ResultSet results = Boot.getAPI().selectRS("SELECT ?p ?o {diseasecard:uniprot_" + id + " ?p ?o }", false);
            while (results.hasNext()) {
                QuerySolution row = results.next();
                if (PrefixFactory.encode(row.get("p").toString()).equals("rdfs:label")) {
                    this.label = row.get("o").toString();
                } else if (PrefixFactory.encode(row.get("p").toString()).equals("dc:title")) {
                    this.id = row.get("o").toString();
                } else if (PrefixFactory.encode(row.get("p").toString()).equals("coeus:isAssociatedTo")) {
                    if (row.get("o").toString().contains("pdb")) {
                        String code = ItemFactory.getTokenFromItem(row.get("o").toString());
                        if (!this.disease.getProtein().getPdb().containsKey(code)) {
                            PDB prot = new PDB(ItemFactory.getTokenFromItem(row.get("o").toString()), row.get("o").toString(), this);
                            pdb.add(prot);
                            this.disease.getProtein().getPdb().put(prot.getId(), prot);
                        } else {
                            pdb.add(this.disease.getProtein().getPdb().get(code));
                        }
                    } else if (row.get("o").toString().contains("interpro")) {
                        String code = ItemFactory.getTokenFromItem(row.get("o").toString());

                        if (!this.disease.getProtein().getInterpro().containsKey(code)) {
                            InterPro prot = new InterPro(ItemFactory.getTokenFromItem(row.get("o").toString()), row.get("o").toString(), this);
                            interpro.add(prot);
                            this.disease.getProtein().getInterpro().put(prot.getId(), prot);
                        } else {
                            this.interpro.add(this.disease.getProtein().getInterpro().get(code));
                        }
                    } else if (row.get("o").toString().contains("prosite")) {
                        String code = ItemFactory.getTokenFromItem(row.get("o").toString());
                        if (!this.disease.getProtein().getProsite().containsKey(code)) {
                            PROSITE prot = new PROSITE(ItemFactory.getTokenFromItem(row.get("o").toString()), row.get("o").toString(), this);
                            prosite.add(prot);
                            this.disease.getProtein().getProsite().put(prot.getId(), prot);
                        } else {
                            prosite.add(this.disease.getProtein().getProsite().get(code));
                        }
                    } else if (row.get("o").toString().contains("mesh")) {
                        String code = ItemFactory.getTokenFromItem(row.get("o").toString());
                        if (!this.disease.getOntology().getMesh().containsKey(code)) {
                            MeSH msh = new MeSH(row.get("o").toString(), this);
                            mesh.add(msh);
                            this.disease.getOntology().getMesh().put(msh.getId(), msh);
                        } else {
                            mesh.add(this.disease.getOntology().getMesh().get(code));
                        }
                    } 
                }
            }
            success = true;
        } catch (Exception ex) {
            if (Config.isDebug()) {
                System.out.println("[COEUS][DiseaseCard][UniProt] Unable to load UniProt data");
                Logger.getLogger(UniProt.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return success;
    }
}
