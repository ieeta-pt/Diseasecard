package pt.ua.bioinformatics.coeus.common;

import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.coeus.api.PrefixFactory;

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
            Boot.start();
            System.out.println(PrefixFactory.encode("http://bioinformatics.ua.pt/diseasecard/resource/malacards_vohwinkel_syndrome_lol"));
            // LEVEL 0
           //Boot.start();
            /*   SingleImport single = new SingleImport("resource_omim");
             Thread t = new Thread(single);
             t.start();*/
            //*/ 

            // LEVEL 1
            // Orphanet
            // UniProt
            /*
             SingleImport single_uniprot = new SingleImport("resource_uniprot");
             SingleImport single_orphanet = new SingleImport("resource_orphanet");
             Thread uniprot = new Thread(single_uniprot);
             Thread orphanet = new Thread(single_orphanet);
             uniprot.start();
             orphanet.start();
             //*/

            // LEVEL 2
            // ICD 10
            // MeSH
            // Interpro
            // PDB
            // Enzyme
            // KEGG
            /*
            SingleImport single_icd10 = new SingleImport("resource_icd10");
            SingleImport single_mesh = new SingleImport("resource_mesh");
            SingleImport single_interpro = new SingleImport("resource_interpro");
            SingleImport single_pdb = new SingleImport("resource_pdb");
            SingleImport single_enzyme = new SingleImport("resource_enzyme");
            SingleImport single_kegg = new SingleImport("resource_kegg");
            SingleImport single_genecards = new SingleImport("resource_genecards");
            
            Thread icd10 = new Thread(single_icd10);
            Thread mesh = new Thread(single_mesh);
            Thread interpro = new Thread(single_interpro);
            Thread pdb = new Thread(single_pdb);
            Thread enzyme = new Thread(single_enzyme);
            Thread kegg = new Thread(single_kegg);
            Thread genecards = new Thread(single_genecards);
            
           
             icd10.start();
           interpro.start();
            mesh.start();
            pdb.start();
          enzyme.start();
            kegg.start();
            genecards.start();
            //*/             


            // LEVEL 3
            // GWASCentral
            // ClinicalTrials
            // LSDB
            // PharmGKB
            // GO
            // PROSITE
            /*
             SingleImport single_gwas = new SingleImport("resource_gwascentral");
             SingleImport single_ct = new SingleImport("resource_clinicaltrials");
             SingleImport single_lsdb = new SingleImport("resource_lsdb");
             SingleImport single_pharmgkb = new SingleImport("resource_pharmgkb");
             SingleImport single_prosite = new SingleImport("resource_prosite");
             SingleImport single_go = new SingleImport("resource_go");

             Thread gwas = new Thread(single_gwas);
             Thread clinical = new Thread(single_ct);
             Thread lsdb = new Thread(single_lsdb);
             Thread pharmgkb = new Thread(single_pharmgkb);
             Thread go = new Thread(single_go);
             Thread prosite = new Thread(single_prosite);


             //gwas.start();
             //clinical.start();
             lsdb.start();
             //pharmgkb.start();
             //go.start();
             //prosite.start();
             //*/
            
            // LEVEL 4
            // 
            //*/
            
            //*/
            
            // FULL&
            // Boot.start();
            //Builder.build();
            // Indexer.index();
            //  SingleImport single_drugbank = new SingleImport("resource_drugbank");
            // Thread drugbank = new Thread(single_drugbank);
            //drugbank.start();
            //                SingleImport single_ipi = new SingleImport("resource_ipi");
            //                Thread ipi = new Thread(single_ipi);
            //                ipi.start();
            //            
            //            SingleImport s_ensembl = new SingleImport("resource_ensembl");
            //            Thread ensembl = new Thread(s_ensembl);
            //            ensembl.start();
            // Update Strings
            //            String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX coeus: <http://bioinformatics.ua.pt/coeus/> PREFIX diseasecard: <http://bioinformatics.ua.pt/diseasecard/resource/> DELETE WHERE { ?r rdfs:label 'resource_entrez' ; ?property ?value}";
            //   String query = "PREFIX dc: <http://purl.org/dc/elements/1.1/> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX coeus: <http://bioinformatics.ua.pt/coeus/> PREFIX diseasecard: <http://bioinformatics.ua.pt/diseasecard/resource/> INSERT DATA { diseasecard:resource_Ensembl coeus:extends diseasecard:concept_UniProt}";
            //            UpdateRequest ur = UpdateFactory.create(query);
            //            UpdateAction.execute(ur.getOperations().get(0), Storage.getModel());
        } catch (Exception ex) {
            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
