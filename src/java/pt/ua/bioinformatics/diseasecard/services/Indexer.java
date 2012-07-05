/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.diseasecard.services;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import pt.ua.bioinformatics.coeus.api.DB;
import pt.ua.bioinformatics.coeus.common.Boot;

/**
 *
 * @author pedrolopes
 */
public class Indexer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        loadNames();
        loadOMIMs();
        loadGenes();
    }

    static void loadNames() {
        String query = "SELECT ?omim ?name {?d coeus:hasConcept coeus:concept_OMIM . ?d diseasecard:omim ?omim . ?d diseasecard:name ?name}";
        Boot.start();
        ResultSet results = Boot.getAPI().selectRS(query, false);
        while (results.hasNext()) {
            QuerySolution row = results.next();
            try {
                DB db = new DB("DC4", "jdbc:mysql://localhost:3306/DC4?user=root&password=telematica");
                db.connect("jdbc:mysql://localhost:3306/DC4?user=root&password=telematica");
                db.insert(row.get("omim").toString(), "INSERT INTO dc4_index(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + row.get("name").toString() + "',1)");
                db.close();
            } catch (Exception ex) {
            }
        }
    }

    static void loadOMIMs() {
        String query = "SELECT DISTINCT ?omim{?d coeus:hasConcept coeus:concept_OMIM . ?d diseasecard:omim ?omim}";
        Boot.start();
        ResultSet results = Boot.getAPI().selectRS(query, false);
        while (results.hasNext()) {
            QuerySolution row = results.next();
            try {
                DB db = new DB("DC4", "jdbc:mysql://localhost:3306/DC4?user=root&password=telematica");
                db.connect("jdbc:mysql://localhost:3306/DC4?user=root&password=telematica");
                db.insert(row.get("omim").toString(), "INSERT INTO dc4_index(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + row.get("omim").toString() + "',2)");
                db.close();
            } catch (Exception ex) {
            }
        }
    }

    static void loadGenes() {
        String query = "SELECT DISTINCT ?omim{?d coeus:hasConcept coeus:concept_OMIM . ?d diseasecard:omim ?omim}";
        Boot.start();
        ResultSet results = Boot.getAPI().selectRS(query, false);
        while (results.hasNext()) {
            QuerySolution row = results.next();
            try {
                String qquery = "SELECT ?o {<http://bioinformatics.ua.pt/coeus/omim_" + row.get("omim").toString() + "> coeus:isAssociatedTo ?o}";
                ResultSet rresults = Boot.getAPI().selectRS(qquery, false);
                while (rresults.hasNext()) {
                    QuerySolution rrow = rresults.next();

                    DB db = new DB("DC4", "jdbc:mysql://localhost:3306/DC4?user=root&password=telematica");
                    db.connect("jdbc:mysql://localhost:3306/DC4?user=root&password=telematica");
                    String item = rrow.get("o").toString();
                    if (item.contains("hgnc")) {
                        item = item.replace("http://bioinformatics.ua.pt/coeus/hgnc_", "");
                        db.insert(row.get("omim").toString(), "INSERT INTO dc4_index(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + item + "',3)");
                    } else if (item.contains("hpo")) {
                        item = item.replace("http://bioinformatics.ua.pt/coeus/hpo_", "");
                        db.insert(row.get("omim").toString(), "INSERT INTO dc4_index(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + item + "',4)");
                    }
                    db.close();
                }
            } catch (Exception ex) {
            }
        }
    }
}
