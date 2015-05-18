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
 * HGNC mirror class.
 * 
 * @author pedrolopes
 * @deprecated
 */
public class HGNC {

    private String id;
    private String name;
    private Disease disease;
    private String uri;
    private String label;
    private ArrayList<UniProt> uniprot = new ArrayList<UniProt>();
    private ArrayList<Ensembl> ensembl = new ArrayList<Ensembl>();
    private ArrayList<OMIM> omim = new ArrayList<OMIM>();
    private ArrayList<ClinicalTrial> clinicaltrial = new ArrayList<ClinicalTrial>();
    private ArrayList<GWASCentral> gwascentral = new ArrayList<GWASCentral>();
    private ArrayList<Enzyme> enzyme = new ArrayList<Enzyme>();
    private ArrayList<KEGG> kegg =  new ArrayList<KEGG>();

    public ArrayList<Enzyme> getEnzyme() {
        return enzyme;
    }

    public void setEnzyme(ArrayList<Enzyme> enzyme) {
        this.enzyme = enzyme;
    }

    public ArrayList<KEGG> getKegg() {
        return kegg;
    }

    public void setKegg(ArrayList<KEGG> kegg) {
        this.kegg = kegg;
    }

    public ArrayList<GWASCentral> getGwascentral() {
        return gwascentral;
    }

    public void setGwascentral(ArrayList<GWASCentral> gwascentral) {
        this.gwascentral = gwascentral;
    }

    public ArrayList<ClinicalTrial> getClinicaltrial() {
        return clinicaltrial;
    }

    public void setClinicaltrial(ArrayList<ClinicalTrial> clinicaltrial) {
        this.clinicaltrial = clinicaltrial;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public ArrayList<Ensembl> getEnsembl() {
        return ensembl;
    }

    public void setEnsembl(ArrayList<Ensembl> ensembl) {
        this.ensembl = ensembl;
    }

    public ArrayList<UniProt> getUniprot() {
        return uniprot;
    }

    public void setUniprot(ArrayList<UniProt> proteins) {
        this.uniprot = proteins;
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

    public ArrayList<OMIM> getOmim() {
        return omim;
    }

    public void setOmim(ArrayList<OMIM> omim) {
        this.omim = omim;
    }

    public String getName() {
        return name;
    }

    public void setName(String hgnc_name) {
        this.name = hgnc_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HGNC() {
    }

    public HGNC(OMIM omi, String uri) {
        this.omim.add(omi);
        this.uri = uri.replace(" ","");
        this.id = ItemFactory.getTokenFromItem(uri);
        this.disease = omi.getDisease();
    }

    public boolean load() {
        boolean success = false;
        try {
            String query = "";
            if (uri.startsWith("http://")) {
                query = "SELECT ?p ?o {<" + this.uri + "> ?p ?o }";
            } else {
                query = "SELECT ?p ?o {diseasecard:hgnc_" + this.id + " ?p ?o }";
            }
            ResultSet results = Boot.getAPI().selectRS(query, false);
            while (results.hasNext()) {
                QuerySolution row = results.next();
                if (PrefixFactory.encode(row.get("p").toString()).equals("rdfs:label")) {
                    this.label = row.get("o").toString();
                } else if (PrefixFactory.encode(row.get("p").toString()).equals("dc:title")) {
                    this.id = row.get("o").toString();
                } else if (PrefixFactory.encode(row.get("p").toString()).equals("coeus:isAssociatedTo")) {
                    if (row.get("o").toString().contains("ensembl")) {
                        String code = ItemFactory.getTokenFromItem(row.get("o").toString());
                        if (!this.disease.getLocus().getEnsembl().containsKey(code)) {
                            Ensembl ens = new Ensembl(ItemFactory.getTokenFromItem(row.get("o").toString()), row.get("o").toString(), this);
                            ensembl.add(ens);
                            this.disease.getLocus().getEnsembl().put(ens.getId(), ens);
                        } else {
                            ensembl.add(this.disease.getLocus().getEnsembl().get(code));
                        }
                    } else if (row.get("o").toString().contains("uniprot")) {
                        String code = ItemFactory.getTokenFromItem(row.get("o").toString());
                        if (!this.disease.getProtein().getUniprot().containsKey(code)) {
                            UniProt up = new UniProt(this, ItemFactory.getTokenFromItem(row.get("o").toString()), row.get("o").toString());
                            this.uniprot.add(up);
                            this.disease.getProtein().getUniprot().put(up.getId(), up);
                        } else {
                            this.uniprot.add(this.disease.getProtein().getUniprot().get(code));
                        }
                    } else if (row.get("o").toString().contains("clinicaltrial")) {
                        String code = ItemFactory.getTokenFromItem(row.get("o").toString());
                        if (!this.disease.getStudy().getClinicaltrials().containsKey(code)) {
                            ClinicalTrial ct = new ClinicalTrial(row.get("o").toString(), this);
                            this.clinicaltrial.add(ct);
                            this.disease.getStudy().getClinicaltrials().put(ct.getId(), ct);
                        } else {
                            this.clinicaltrial.add(this.disease.getStudy().getClinicaltrials().get(code));
                        }
                    }  else if (row.get("o").toString().contains("gwascentral")) {
                        String code = ItemFactory.getTokenFromItem(row.get("o").toString());
                        if (!this.disease.getStudy().getGwascentral().containsKey(code)) {
                            GWASCentral gc = new GWASCentral(row.get("o").toString(), this);
                            this.gwascentral.add(gc);
                            this.disease.getStudy().getGwascentral().put(gc.getId(), gc);
                        } else {
                            this.gwascentral.add(this.disease.getStudy().getGwascentral().get(code));
                        }
                    }  else if (row.get("o").toString().contains("enzyme")) {
                        String code = ItemFactory.getTokenFromItem(row.get("o").toString());
                        if (!this.disease.getPathway().getEnzyme().containsKey(code)) {
                            Enzyme e = new Enzyme(row.get("o").toString(), this);
                            this.enzyme.add(e);
                            this.disease.getPathway().getEnzyme().put(e.getId(), e);
                        } else {
                            this.enzyme.add(this.disease.getPathway().getEnzyme().get(code));
                        }
                    } else if (row.get("o").toString().contains("kegg")) {
                        String code = ItemFactory.getTokenFromItem(row.get("o").toString());
                        if (!this.disease.getPathway().getKegg().containsKey(code)) {
                            KEGG k = new KEGG(row.get("o").toString(), this);
                            this.kegg.add(k);
                            this.disease.getPathway().getKegg().put(k.getId(), k);
                        } else {
                            this.kegg.add(this.disease.getPathway().getKegg().get(code));
                        }
                    }
                }
            }
            success = true;
        } catch (Exception ex) {
            if (Config.isDebug()) {
                System.out.println("[COEUS][API] Unable to execute SPARQL select");
                Logger.getLogger(HGNC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return success;
    }
}
