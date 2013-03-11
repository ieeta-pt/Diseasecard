package pt.ua.bioinformatics.coeus.common;

import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.coeus.api.plugins.SingleImport;
import pt.ua.bioinformatics.coeus.data.Storage;

/**
 *
 * @author pedrolopes
 */
public class Run {

    /**
     * COEUS importer runner class.
     * <p>
     *      Organized in import levels for dependency handling.
     * </p>
     * <p>
     *      <strong>LEVEL 0</strong><br />Initialize system (mandatory on all runs).
     * </p>
     * <p>
     *      <strong>LEVEL 1</strong><br />Loads first level.
     * </p>
     * <p>
     *      <strong>LEVEL n</strong><br />Loads n level.
     * </p>
     * <p>
     *      <strong>FULL</strong><br />Loads everything.
     * </p>
     * <p>
     *      (This needs refactoring!)
     * </p>
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // LEVEL 0

            Boot.start();
            String query = "PREFIX coeus: <http://bioinformatics.ua.pt/coeus/>\nPREFIX diseasecard: <http://bioinformatics.ua.pt/diseasecard/resource/>\nDELETE WHERE \n{ ?hpo coeus:hasConcept diseasecard:concept_PubMed; ?p ?v }";
            //Boot.getAPI().select(query, "json", false);
            UpdateRequest ur = UpdateFactory.create(query);  
UpdateAction.execute(ur.getOperations().get(0),Storage.getModel());
            
            //SingleImport single_clinical = new SingleImport("resource_clinicaltrials");
            //Thread clinical = new Thread(single_clinical);
            //clinical.start();
           // SingleImport single_kegg = new SingleImport("resource_kegg");
            //Thread kegg = new Thread(single_kegg);
            //kegg.start();
            
            // LEVEL 1
        /*  SingleImport single_uniprot = new SingleImport("resource_uniprot");
            SingleImport single_entrez = new SingleImport("resource_entrezgene");
            SingleImport single_hpo = new SingleImport("resource_hpo");
            SingleImport single_orphanet = new SingleImport("resource_orphanet");
            
            Thread uniprot = new Thread(single_uniprot);
            Thread entrez = new Thread(single_entrez);
            Thread hpo = new Thread(single_hpo);
            Thread orphanet = new Thread(single_orphanet);
            uniprot.start(); 
            entrez.start();     
            orphanet.start();      
            hpo.start(); */

            // LEVEL 2
            
          
            
         /*  
            SingleImport single_drugbank = new SingleImport("resource_drugbank");
            SingleImport single_interpro = new SingleImport("resource_interpro");
            SingleImport single_mesh = new SingleImport("resource_mesh");
            SingleImport single_pdb = new SingleImport("resource_pdb");
            SingleImport single_enzyme = new SingleImport("resource_enzyme");
            SingleImport single_pubmed = new SingleImport("resource_pubmed");
            
            Thread drugbank = new Thread(single_drugbank);
            Thread interpro = new Thread(single_interpro);
            Thread mesh = new Thread(single_mesh);
            Thread pdb = new Thread(single_pdb);
            Thread enzyme = new Thread(single_enzyme);            
            Thread pubmed = new Thread(single_pubmed);
            
            drugbank.start();
            interpro.start();
            mesh.start();
            pdb.start();
            enzyme.start();
            pubmed.start();
             
             SingleImport single_icd10 = new SingleImport("resource_icd10");
            Thread icd10 = new Thread(single_icd10);
            icd10.start();

            
            // LEVEL 3
            /*
            SingleImport single_pharmgkb = new SingleImport("resource_pharmgkb");            
            SingleImport single_ensembl = new SingleImport("resource_ensembl");
            SingleImport single_umls = new SingleImport("resource_umls");
            SingleImport single_prosite = new SingleImport("resource_prosite");
            
            Thread pharmgkb = new Thread(single_pharmgkb);
            Thread ensembl = new Thread(single_ensembl);
            Thread umls = new Thread(single_umls);           
            Thread prosite = new Thread(single_prosite);
            
            pharmgkb.start();
            ensembl.start();
            umls.start();
            prosite.start();
             
           SingleImport single_gwas = new SingleImport("resource_gwascentral");
            Thread gwas  = new Thread(single_gwas);
            gwas.start();
            */
            
       /*    SingleImport single_swiss = new SingleImport("resource_swissvar");
           Thread swissvar = new Thread(single_swiss);
            swissvar.start();
            */

            // FULL&
            // Boot.start();
            //Builder.build();
            
           // Indexer.index();
            
        } catch (Exception ex) {
            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
