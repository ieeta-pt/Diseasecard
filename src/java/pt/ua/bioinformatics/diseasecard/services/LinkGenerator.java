/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.diseasecard.services;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import pt.ua.bioinformatics.coeus.api.PrefixFactory;
import pt.ua.bioinformatics.coeus.common.Boot;
import pt.ua.bioinformatics.coeus.common.Config;

/**
 *
 * @author pedrolopes
 */
public class LinkGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String[] list = {"HGNC", "UniProt", "MeSH", "PharmGKB", "InterPro", "PROSITE", "Reactome", "PDB", "Ensembl"};
        Boot.start();
        for (String s : list) {
            FileWriter fstream = new FileWriter(Config.getPath() + s);
            BufferedWriter out = new BufferedWriter(fstream);
            ResultSet results = Boot.getAPI().selectRS("SELECT ?item {?item coeus:hasConcept coeus:concept_" + s + " }", false);
            while (results.hasNext()) {
                QuerySolution row = results.next();
                out.write(PrefixFactory.encode(row.get("item").toString()).replace("coeus:", "http://bioinformatics.ua.pt/dc4/services/linkout/").replace("_", ":") + "/\n");
            }
            out.close();
        }

    }
}
