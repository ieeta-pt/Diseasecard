package pt.ua.bioinformatics.coeus.actions.diseasecard;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pt.ua.bioinformatics.coeus.common.Config;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;
import pt.ua.bioinformatics.diseasecard.domain.ClinicalTrial;
import pt.ua.bioinformatics.diseasecard.domain.Disease;
import pt.ua.bioinformatics.diseasecard.domain.DrugBank;
import pt.ua.bioinformatics.diseasecard.domain.Ensembl;
import pt.ua.bioinformatics.diseasecard.domain.EntrezGene;
import pt.ua.bioinformatics.diseasecard.domain.Enzyme;
import pt.ua.bioinformatics.diseasecard.domain.GWASCentral;
import pt.ua.bioinformatics.diseasecard.domain.HGNC;
import pt.ua.bioinformatics.diseasecard.domain.HPO;
import pt.ua.bioinformatics.diseasecard.domain.ICD;
import pt.ua.bioinformatics.diseasecard.domain.InterPro;
import pt.ua.bioinformatics.diseasecard.domain.KEGG;
import pt.ua.bioinformatics.diseasecard.domain.MeSH;
import pt.ua.bioinformatics.diseasecard.domain.OMIM;
import pt.ua.bioinformatics.diseasecard.domain.Orphanet;
import pt.ua.bioinformatics.diseasecard.domain.PDB;
import pt.ua.bioinformatics.diseasecard.domain.PROSITE;
import pt.ua.bioinformatics.diseasecard.domain.PharmGKB;
import pt.ua.bioinformatics.diseasecard.domain.SwissVar;
import pt.ua.bioinformatics.diseasecard.domain.UniProt;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/content/{key}.{$event}")
public class ContentActionBean implements ActionBean {

    private COEUSActionBeanContext context;
    private String key;
    private Disease disease;
    private String jsondata;
    private JSONObject content;

    public JSONObject getContent() {
        return content;
    }

    public void setContent(JSONObject content) {
        this.content = content;
    }

    public String getJsondata() {
        return jsondata;
    }

    public void setJsondata(String jsondata) {
        this.jsondata = jsondata;
    }

    public void setContext(ActionBeanContext context) {
        this.context = (COEUSActionBeanContext) context;
    }

    public COEUSActionBeanContext getContext() {
        return context;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    @DefaultHandler
    public Resolution html() {
        return new ForwardResolution("/beta/view/content.jsp");
    }

    public Resolution js() {
        try {
            disease = (Disease) getContext().getDisease("omim:" + key);
            if (disease == null) {
                disease = new Disease(Integer.parseInt(key));
                getContext().setDisease("omim:" + key, disease);
            }
            content = (JSONObject) getContext().getObject("content:" + key);
            if (content == null) {
                content = getJSON();
                getContext().setObject("content:" + key, content);
            }
            jsondata = content.toString(2);
            return new StreamingResolution("application/json", jsondata);
        } catch (Exception ex) {
            if (Config.isDebug()) {
                Logger.getLogger(ContentActionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new StreamingResolution("application/json", "");
    }

    private JSONObject getJSON() throws JSONException {
        JSONObject data = new JSONObject();
        JSONObject objUp = null;
        try {
            data.put("id", key);
            data.put("name", "<h1>" + disease.getOmim().getDescription() + "<h1>");
            JSONArray entities = new JSONArray();

            // processing Entity Protein
            JSONObject protein = new JSONObject();
            protein.put("id", "entity:protein");
            protein.put("name", "<h2>Protein</h2>");
            JSONArray proteins = new JSONArray();

            // interpro
            JSONObject ip = new JSONObject();
            ip.put("id", "concept:interpro");
            ip.put("name", "<h3>InterPro</h3>");
            if (!disease.getProtein().getInterpro().isEmpty()) {
                ip.put("size", disease.getProtein().getInterpro().size());
                JSONArray ip_children = new JSONArray();
                for (InterPro up : disease.getProtein().getInterpro().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "interpro:" + up.getId());
                    objUp.put("name", "<a data-id=\"interpro:" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    ip_children.put(objUp);
                }
                ip.put("children", ip_children);
            }
            proteins.put(ip);

            // pdb
            JSONObject pdb = new JSONObject();
            pdb.put("id", "concept:pdb");
            pdb.put("name", "<h3>PDB</h3>");
            if (!disease.getProtein().getPdb().isEmpty()) {
                pdb.put("size", disease.getProtein().getPdb().size());
                JSONArray pdb_children = new JSONArray();
                int i = 0;
                for (PDB up : disease.getProtein().getPdb().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "pdb:" + up.getId());
                    objUp.put("name", "<a data-id=\"pdb:" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    pdb_children.put(objUp);
                    if (i++ > 32) {
                        break;
                    }
                }
                pdb.put("children", pdb_children);
            }
            proteins.put(pdb);

            // prosite
            JSONObject prosite = new JSONObject();
            prosite.put("id", "concept:prosite");
            prosite.put("name", "<h3>PROSITE</h3>");
            if (!disease.getProtein().getProsite().isEmpty()) {
                prosite.put("size", disease.getProtein().getProsite().size());
                JSONArray prosite_children = new JSONArray();
                for (PROSITE up : disease.getProtein().getProsite().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "prosite:" + up.getId());
                    objUp.put("name", "<a data-id=\"prosite:" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    prosite_children.put(objUp);
                }
                prosite.put("children", prosite_children);
            }
            proteins.put(prosite);

            // uniprot
            JSONObject uniprot = new JSONObject();
            uniprot.put("id", "concept:uniprot");
            uniprot.put("name", "<h3>UniProt</h3>");
            if (!disease.getProtein().getUniprot().isEmpty()) {
                uniprot.put("size", disease.getProtein().getUniprot().size());
                JSONArray uniprot_children = new JSONArray();
                for (UniProt up : disease.getProtein().getUniprot().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "uniprot:" + up.getId());
                    objUp.put("name", "<a data-id=\"uniprot:" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    uniprot_children.put(objUp);
                }
                uniprot.put("children", uniprot_children);
            }
            proteins.put(uniprot);

            // finalize Protein
            protein.put("children", proteins);

            // processing Entity Locus
            JSONObject locus = new JSONObject();
            locus.put("id", "entity:locus");
            locus.put("name", "<h2>Locus</h2>");
            JSONArray loci = new JSONArray();

            // Ensembl
            JSONObject ensembl = new JSONObject();
            ensembl.put("id", "concept:ensembl");
            ensembl.put("name", "<h3>Ensembl</h3>");
            if (!disease.getLocus().getEnsembl().isEmpty()) {
                ensembl.put("size", disease.getLocus().getEnsembl().size());
                JSONArray ensembl_children = new JSONArray();
                for (Ensembl up : disease.getLocus().getEnsembl().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "ensembl:" + up.getId());
                    objUp.put("name", "<a data-id=\"ensembl:" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    ensembl_children.put(objUp);
                }
                ensembl.put("children", ensembl_children);
            }
            loci.put(ensembl);

            // EntrezGene
            JSONObject entrez = new JSONObject();
            entrez.put("id", "concept:entrez");
            entrez.put("name", "<h3>Entrez</h3>");
            if (!disease.getLocus().getEntrezgene().isEmpty()) {
                entrez.put("size", disease.getLocus().getEntrezgene().size());
                JSONArray entrez_children = new JSONArray();
                for (EntrezGene up : disease.getLocus().getEntrezgene().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "entrez:" + up.getId());
                    objUp.put("name", "<a data-id=\"entrez:" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    entrez_children.put(objUp);
                }
                entrez.put("children", entrez_children);
            }
            loci.put(entrez);

            // GeneCards
            JSONObject genecards = new JSONObject();
            genecards.put("id", "concept:genecards");
            genecards.put("name", "<h3>GeneCards</h3>");
            if (!disease.getLocus().getHgnc().isEmpty()) {
                genecards.put("size", disease.getLocus().getHgnc().size());
                JSONArray genecards_children = new JSONArray();
                for (HGNC up : disease.getLocus().getHgnc().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "genecards:" + up.getId());
                    objUp.put("name", "<a data-id=\"genecards:" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    genecards_children.put(objUp);
                }
                genecards.put("children", genecards_children);
            }
            loci.put(genecards);

            // GeneNames
            JSONObject hgnc = new JSONObject();
            hgnc.put("id", "concept:hgnc");
            hgnc.put("name", "<h3>HGNC</h3>");
            if (!disease.getLocus().getHgnc().isEmpty()) {
                hgnc.put("size", disease.getLocus().getHgnc().size());
                JSONArray hgnc_children = new JSONArray();
                for (HGNC up : disease.getLocus().getHgnc().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "hgnc:" + up.getId());
                    objUp.put("name", "<a data-id=\"hgnc:" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    hgnc_children.put(objUp);
                }
                hgnc.put("children", hgnc_children);
            }
            loci.put(hgnc);

            locus.put("children", loci);


            // processing Entity Drug
            JSONObject drug = new JSONObject();
            drug.put("id", "entity:drug");
            drug.put("name", "<h2>Drug</h2>");
            JSONArray drugs = new JSONArray();

            // PharmGKB
            JSONObject pharmgkb = new JSONObject();
            pharmgkb.put("id", "concept:pharmgkb");
            pharmgkb.put("name", "<h3>PharmGKB</h3>");
            if (!disease.getDrug().getPharmgkb().isEmpty()) {
                pharmgkb.put("size", disease.getDrug().getPharmgkb().size());
                JSONArray pharmgkb_children = new JSONArray();
                for (PharmGKB up : disease.getDrug().getPharmgkb().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "pharmgkb:" + up.getId());
                    objUp.put("name", "<a data-id=\"pharmgkb:" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    pharmgkb_children.put(objUp);
                }
                pharmgkb.put("children", pharmgkb_children);
            }
            drugs.put(pharmgkb);
            
            // DrugBank
            JSONObject drugbank = new JSONObject();
            drugbank.put("id", "concept:drugbank");
            drugbank.put("name", "<h3>DrugBank</h3>");
            if (!disease.getDrug().getDrugbank().isEmpty()) {
                drugbank.put("size", disease.getDrug().getDrugbank().size());
                JSONArray db_children = new JSONArray();
                for (DrugBank up : disease.getDrug().getDrugbank().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "drugbank:" + up.getId());
                    objUp.put("name", "<a data-id=\"drugbank:" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    db_children.put(objUp);
                }
                drugbank.put("children", db_children);
            }
            drugs.put(drugbank);            
            drug.put("children", drugs);


            // processing Entity Ontology
            JSONObject ontology = new JSONObject();
            ontology.put("id", "entity:ontology");
            ontology.put("name", "<h2>Ontology</h2>");
            JSONArray ontologies = new JSONArray();

            // HPO
            JSONObject hpo = new JSONObject();
            hpo.put("id", "concept:hpo");
            hpo.put("name", "<h3>HPO</h3>");
            if (!disease.getOntology().getHpo().isEmpty()) {
                hpo.put("size", disease.getOntology().getHpo().size());
                JSONArray hpo_children = new JSONArray();
                for (HPO up : disease.getOntology().getHpo().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "" + up.getId());
                    objUp.put("name", "<a data-id=\"" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    hpo_children.put(objUp);
                }
                hpo.put("children", hpo_children);
            }
            ontologies.put(hpo);

            // MeSH
            JSONObject mesh = new JSONObject();
            mesh.put("id", "concept:mesh");
            mesh.put("name", "<h3>MeSH</h3>");
            if (!disease.getOntology().getMesh().isEmpty()) {
                mesh.put("size", disease.getOntology().getMesh().size());
                JSONArray mesh_children = new JSONArray();
                for (MeSH up : disease.getOntology().getMesh().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "mesh:" + up.getId());
                    objUp.put("name", "<a data-id=\"mesh:" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    mesh_children.put(objUp);
                }
                mesh.put("children", mesh_children);
            }
            ontologies.put(mesh);

            // ICD10
            JSONObject icd10 = new JSONObject();
            icd10.put("id", "concept:icd10");
            icd10.put("name", "<h3>ICD 10</h3>");
            if (!disease.getOntology().getIcd().isEmpty()) {
                icd10.put("size", disease.getOntology().getIcd().size());
                JSONArray icd_children = new JSONArray();
                for (ICD up : disease.getOntology().getIcd().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "icd10:" + up.getId());
                    objUp.put("name", "<a data-id=\"icd10:" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    icd_children.put(objUp);
                }
                icd10.put("children", icd_children);
            }
            ontologies.put(icd10);

            ontology.put("children", ontologies);

            // processing Entity Pathway
            JSONObject pathway = new JSONObject();
            pathway.put("id", "entity:pathway");
            pathway.put("name", "<h2>Pathway</h2>");
            JSONArray pathways = new JSONArray();

            // KEGG
            JSONObject kegg = new JSONObject();
            kegg.put("id", "concept:kegg");
            kegg.put("name", "<h3>KEGG</h3>");
            if (!disease.getPathway().getKegg().isEmpty()) {
                kegg.put("size", disease.getPathway().getKegg().size());
                JSONArray kegg_children = new JSONArray();
                for (KEGG up : disease.getPathway().getKegg().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "kegg:" + up.getId());
                    objUp.put("name", "<a data-id=\"kegg:" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    kegg_children.put(objUp);
                }
                kegg.put("children", kegg_children);
            }
            pathways.put(kegg);

            // Enzyme
            JSONObject enzyme = new JSONObject();
            enzyme.put("id", "concept:enzyme");
            enzyme.put("name", "<h3>Enzyme</h3>");
            if (!disease.getPathway().getEnzyme().isEmpty()) {
                enzyme.put("size", disease.getPathway().getEnzyme().size());
                JSONArray enzyme_children = new JSONArray();
                for (Enzyme up : disease.getPathway().getEnzyme().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "enzyme:" + up.getId());
                    objUp.put("name", "<a data-id=\"enzyme:" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    enzyme_children.put(objUp);
                }
                enzyme.put("children", enzyme_children);
            }
            pathways.put(enzyme);
            pathway.put("children", pathways);

            // processing Entity Study
            JSONObject study = new JSONObject();
            study.put("id", "entity:study");
            study.put("name", "<h2>Study</h2>");
            JSONArray studies = new JSONArray();

            // GWAS Central
            JSONObject gwascentral = new JSONObject();
            gwascentral.put("id", "concept:gwacentral");
            gwascentral.put("name", "<h3>GWASCentral</h3>");
            if (!disease.getStudy().getGwascentral().isEmpty()) {
                gwascentral.put("size", disease.getStudy().getGwascentral().size());
                JSONArray gwas_children = new JSONArray();
                for (GWASCentral up : disease.getStudy().getGwascentral().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "gwascentral:" + up.getId());
                    objUp.put("name", "<a data-id=\"gwascentral:" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    gwas_children.put(objUp);
                }
                gwascentral.put("children", gwas_children);
            }
            studies.put(gwascentral);

            // Clinical Trials
            JSONObject clinicaltrials = new JSONObject();
            clinicaltrials.put("id", "concept:clinicaltrials");
            clinicaltrials.put("name", "<h3>Cinical Trials</h3>");
            if (!disease.getStudy().getClinicaltrials().isEmpty()) {
                clinicaltrials.put("size", disease.getStudy().getClinicaltrials().size());
                JSONArray ct_children = new JSONArray();
                for (ClinicalTrial up : disease.getStudy().getClinicaltrials().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "clinicaltrials:" + up.getId());
                    objUp.put("name", "<a data-id=\"clinicaltrials:" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    ct_children.put(objUp);
                }
                clinicaltrials.put("children", ct_children);
            }
            studies.put(clinicaltrials);
            study.put("children", studies);

            // processing Entity Variome
            JSONObject variome = new JSONObject();
            variome.put("id", "entity:variome");
            variome.put("name", "<h2>Variome</h2>");
            JSONArray variomes = new JSONArray();

            // WAVe
            JSONObject wave = new JSONObject();
            wave.put("id", "concept:wave");
            wave.put("name", "<h3>WAVe</h3>");
            if (!disease.getLocus().getHgnc().isEmpty()) {
                wave.put("size", disease.getLocus().getHgnc().size());
                JSONArray wave_children = new JSONArray();
                for (HGNC up : disease.getLocus().getHgnc().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "wave:" + up.getId());
                    objUp.put("name", "<a data-id=\"wave:" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    wave_children.put(objUp);
                }
                wave.put("children", wave_children);
            }
            variomes.put(wave);

            // SwissVar
            JSONObject swissvar = new JSONObject();
            swissvar.put("id", "concept:swissvar");
            swissvar.put("name", "<h3>SwissVar</h3>");
            if (!disease.getVariome().getSwissvar().isEmpty()) {
                swissvar.put("size", disease.getVariome().getSwissvar().size());
                JSONArray swissvar_children = new JSONArray();
                for (SwissVar sv : disease.getVariome().getSwissvar().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "swissvar:" + sv.getId());
                    objUp.put("name", "<a data-id=\"swissvar:" + sv.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + sv.getId() + "</a>");
                    swissvar_children.put(objUp);
                }
                swissvar.put("children", swissvar_children);
            }
            variomes.put(swissvar);
            variome.put("children", variomes);


            // processing Entity Literature
            JSONObject literature = new JSONObject();
            literature.put("id", "entity:literature");
            literature.put("name", "<h2>Literature</h2>");
            JSONArray literatures = new JSONArray();

            // PubMed
            JSONObject pubmed = new JSONObject();
            pubmed.put("id", "concept:pubmed");
            pubmed.put("name", "<h3>PubMed</h3>");
            pubmed.put("size", 1);
            JSONArray pubmed_children = new JSONArray();
            objUp = new JSONObject();
            objUp.put("id", "pubmedsearch:" + disease.getOmim().getDescription());
            objUp.put("name", "<a data-id=\"pubmedsearch:" + disease.getOmim().getDescription() + "\" target=\"_content\" class=\"framer dc4_ht\">" + disease.getOmim().getDescription() + "</a>");
            pubmed_children.put(objUp);
            pubmed.put("children", pubmed_children);

            // OMIM references
            JSONObject omim_ref = new JSONObject();
            omim_ref.put("id", "concept:omimref");
            omim_ref.put("name", "<h3>OMIM</h3>");
            omim_ref.put("size", 1);
            JSONArray omim_ref_children = new JSONArray();
            objUp = new JSONObject();
            objUp.put("id", "omimref:" + disease.getOmim().getDescription());
            objUp.put("name", "<a data-id=\"omimref:" + disease.getOmim().getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + disease.getOmim().getId() + "</a>");
            omim_ref_children.put(objUp);
            omim_ref.put("children", omim_ref_children);

            literatures.put(omim_ref);
            literatures.put(pubmed);
            literature.put("children", literatures);

            // processing Entity Disease
            JSONObject j_disease = new JSONObject();
            j_disease.put("id", "entity:disease");
            j_disease.put("name", "<h2>Disease</h2>");
            JSONArray diseases = new JSONArray();

            // NORD
            JSONObject nord = new JSONObject();
            nord.put("id", "concept:nord");
            nord.put("name", "<h3>NORD</h3>");
            nord.put("size", 1);
            JSONArray nord_children = new JSONArray();
            objUp = new JSONObject();
            objUp.put("id", "nord:" + disease.getOmim().getDescription());
            objUp.put("name", "<a data-id=\"nord:" + disease.getOmim().getDescription() + "\" target=\"_content\" class=\"framer dc4_ht\">" + disease.getOmim().getDescription() + "</a>");
            nord_children.put(objUp);
            nord.put("children", nord_children);
            diseases.put(nord);

            // OMIM
            JSONObject omim = new JSONObject();
            omim.put("id", "concept:omim");
            omim.put("name", "<h3>OMIM</h3>");
            if (!disease.getDiseaseMap().isEmpty()) {
                omim.put("size", disease.getDiseaseMap().size());
                JSONArray omim_children = new JSONArray();
                for (OMIM up : disease.getDiseaseMap().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "omim:" + up.getId());
                    objUp.put("name", "<a data-id=\"omim:" + up.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + up.getId() + "</a>");
                    omim_children.put(objUp);
                }
                omim.put("children", omim_children);
            }
            diseases.put(omim);

            // Orphanet
            JSONObject orphanet = new JSONObject();
            orphanet.put("id", "concept:orphanet");
            orphanet.put("name", "<h3>Orphanet</h3>");
            if (!disease.getOmim().getOrphanet().isEmpty()) {
                orphanet.put("size", disease.getOmim().getOrphanet().size());
                JSONArray orp_children = new JSONArray();
                for (Orphanet sv : disease.getOmim().getOrphanet().values()) {
                    objUp = new JSONObject();
                    objUp.put("id", "orphanet:" + sv.getId());
                    objUp.put("name", "<a data-id=\"orphanet:" + sv.getId() + "\" target=\"_content\" class=\"framer dc4_ht\">" + sv.getId() + "</a>");
                    orp_children.put(objUp);
                }
                orphanet.put("children", orp_children);
            }
            diseases.put(orphanet);

            j_disease.put("children", diseases);

            // finish him
            entities.put(j_disease);
            entities.put(drug);
            entities.put(literature);
            entities.put(pathway);
            entities.put(locus);
            entities.put(study);
            entities.put(ontology);
            entities.put(protein);
            entities.put(variome);
            data.put("children", entities);
        } catch (Exception ex) {
            if (Config.isDebug()) {
                Logger.getLogger(ContentActionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return data;
    }
}
