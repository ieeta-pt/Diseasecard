
package pt.ua.bioinformatics.diseasecard.services;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.coeus.api.DB;
import pt.ua.bioinformatics.coeus.api.ItemFactory;
import pt.ua.bioinformatics.diseasecard.common.Boot;
import pt.ua.bioinformatics.diseasecard.common.Config;

/**
 *
 * Not everything in GeNS is updated... move to HUMMER for updates.
 * 
 * @author pedrolopes
 */
public class GeNSUpdater {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            Boot.start();
            DB gens = new DB();
            DB hummer = new DB();
            ResultSet rs = Boot.getAPI().selectRS("SELECT ?u WHERE { ?u coeus:hasConcept diseasecard:concept_UniProt }", false);
            System.out.println("Processing Uniprot dataset...");
            while(rs.hasNext()) {
                QuerySolution row = rs.next();
                gens.connect("jdbc:sqlserver://biodatacenter.ieeta.pt:8098;database=GeNS2;user=root;password=telematica");
                String uniprot = ItemFactory.getTokenFromItem(ItemFactory.getTokenFromURI(row.get("u").toString()));
                System.out.println("\tuniprot:" + uniprot);
                java.sql.ResultSet gens_rs = gens.getData("select distinct BE.BioEntityName as entry from Protein P inner join ProteinBioEntity PBO on PBO.ProteinId = P.ProteinId inner join BioEntity BE on BE.BioEntityId = PBO.BioEntityId where BE.DataTypeId = 114 and P.ProteinId in (select P.ProteinId from Protein P inner join Identifier I on I.ProteinId = P.ProteinId where P.TaxonomicId = 9606 and I.DataTypeId = 3 and I.Alias like '" + uniprot + "');");
                while(gens_rs.next()) {
                    hummer.connect(Config.getConnectionInfo("hummer_root"));
                    //hummer.insert(uniprot, "INSERT INTO kegg (uniprot, kegg) VALUES ('" + uniprot + "','" + gens_rs.getString(1) + "');");
                    System.out.println("\t\t\t- " + uniprot + " -> " + gens_rs.getString(1));
                    hummer.close();
                }
            }
            
        } catch (Exception ex) {
             if (Config.isDebug()) {
                    System.out.println("[COEUS][GeNS Updater] ");
                    Logger.getLogger(GeNSUpdater.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        
        
    }
}
