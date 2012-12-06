/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.diseasecard.services;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import pt.ua.bioinformatics.coeus.common.Boot;

/**
 *
 * @author pedrolopes
 */
public class Tester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Boot.start();
        String query = "SELECT DISTINCT ?p ?o {coeus:omim_114480 ?p ?o }";
        //Concept c = Internal.getConcept("concept_OMIM");
        //System.out.println(c.toString() + "\n" + c.getHasEntity().toString());
        ResultSet results = Boot.getAPI().selectRS(query, true);
        while (results.hasNext()) {
            QuerySolution row = results.next();
            System.out.println(row.get("p").toString() + " - " + row.get("o").toString());
        }
        
    }
}
