package pt.ua.bioinformatics.coeus.common;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.hp.hpl.jena.update.UpdateRequest;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateAction;
import pt.ua.bioinformatics.coeus.data.Storage;
import pt.ua.bioinformatics.coeus.api.plugins.SingleImport;

/**
 *
 * @author pedrolopes
 */
public class Run {

    /**
     * COEUS importer runner class.
     * <p>
     * Organized in import levels for dependency handling.
     * </p>
     * <p>
     * <strong>LEVEL 0</strong><br />Initialize system (mandatory on all runs).
     * </p>
     * <p>
     * <strong>LEVEL 1</strong><br />Loads first level.
     * </p>
     * <p>
     * <strong>LEVEL n</strong><br />Loads n level.
     * </p>
     * <p>
     * <strong>FULL</strong><br />Loads everything.
     * </p>
     * <p>
     * (This needs refactoring!)
     * </p>
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // LEVEL 0

            Boot.start();
            /*   SingleImport single = new SingleImport("resource_lsdb");
             Thread t = new Thread(single);
             t.start();*/
            // 

            String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX coeus: <http://bioinformatics.ua.pt/coeus/> PREFIX diseasecard: <http://bioinformatics.ua.pt/diseasecard/resource/> DELETE WHERE { ?r rdfs:label 'resource_entrez' ; ?property ?value}";
      //   String query = "PREFIX dc: <http://purl.org/dc/elements/1.1/> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX coeus: <http://bioinformatics.ua.pt/coeus/> PREFIX diseasecard: <http://bioinformatics.ua.pt/diseasecard/resource/> INSERT DATA { diseasecard:resource_Ensembl coeus:extends diseasecard:concept_UniProt}";
            UpdateRequest ur = UpdateFactory.create(query);
            UpdateAction.execute(ur.getOperations().get(0), Storage.getModel());

            //SingleImport single_clinical = new SingleImport("resource_clinicaltrials");
            //Thread clinical = new Thread(single_clinical);
            //clinical.start();
//             SingleImport single_kegg = new SingleImport("resource_kegg");
//             SingleImport s_gc = new SingleImport("resource_genecards");
//            Thread kegg = new Thread(single_kegg);
//            Thread gc = new Thread(s_gc);
//            gc.start();
//            kegg.start(); 
/* 
             // LEVEL 1
             SingleImport single_uniprot = new SingleImport("resource_uniprot");
             SingleImport single_orphanet = new SingleImport("resource_orphanet");
             Thread uniprot = new Thread(single_uniprot);
             Thread orphanet = new Thread(single_orphanet);
             uniprot.start();
             orphanet.start();

             // LEVEL 2

             
             SingleImport single_drugbank = new SingleImport("resource_drugbank");
             SingleImport single_interpro = new SingleImport("resource_interpro");
             SingleImport single_mesh = new SingleImport("resource_mesh");
             SingleImport single_pdb = new SingleImport("resource_pdb");    
             SingleImport single_enzyme = new SingleImport("resource_enzyme");

            
             // 
             Thread interpro = new Thread(single_interpro);
             Thread mesh = new Thread(single_mesh);
             Thread pdb = new Thread(single_pdb);
             Thread enzyme = new Thread(single_enzyme);            

            
             // drugbank.start();
             interpro.start();
             mesh.start();
             pdb.start();
             enzyme.start(); /*
             SingleImport single_lsdb = new SingleImport("resource_lsdb");
             Thread lsdb = new Thread(single_lsdb);
             lsdb.start(); 
             /*SingleImport single_icd10 = new SingleImport("resource_icd10");
             Thread icd10 = new Thread(single_icd10);
             icd10.start();/*
             SingleImport single_clinical = new SingleImport("resource_clinicaltrials");
             Thread clinical = new Thread(single_clinical);
             clinical.start();

             // LEVEL 3
             *
             SingleImport single_pharmgkb = new SingleImport("resource_pharmgkb");             
             SingleImport single_prosite = new SingleImport("resource_prosite");
             SingleImport single_pdb = new SingleImport("resource_pdb"); 
             Thread pharmgkb = new Thread(single_pharmgkb);
         
             Thread prosite = new Thread(single_prosite);
            
            
             pharmgkb.start();

             prosite.start();
             Thread pdb = new Thread(single_pdb);
             
             pdb.start();
             //SingleImport single_gwas = new SingleImport("resource_gwascentral");
             // Thread gwas  = new Thread(single_gwas);
             //   gwas.start();
             SingleImport single_go = new SingleImport("resource_go");
             Thread go = new Thread(single_go);
             go.start();
             
             //Thread single_ipi = new SingleImport("resourc")
             
&
             // FULL&
             // Boot.start();
             //Builder.build();

             // Indexer.index();
             */

            //  SingleImport single_drugbank = new SingleImport("resource_drugbank");
            // Thread drugbank = new Thread(single_drugbank);
            //drugbank.start();
            //SingleImport single_gwas = new SingleImport("resource_gwascentral");
            // Thread gwas  = new Thread(single_gwas);
            // gwas.start();
//                SingleImport single_ipi = new SingleImport("resource_ipi");
//                Thread ipi = new Thread(single_ipi);
//                ipi.start();
//            
//            SingleImport s_ensembl = new SingleImport("resource_ensembl");
//            Thread ensembl = new Thread(s_ensembl);
//            ensembl.start();
        } catch (Exception ex) {
            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
