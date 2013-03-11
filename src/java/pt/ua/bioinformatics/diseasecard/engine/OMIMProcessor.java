/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.diseasecard.engine;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.coeus.api.ItemFactory;
import pt.ua.bioinformatics.coeus.common.Boot;

/**
 *
 * @author pedrolopes
 */
public class OMIMProcessor implements Runnable {

    private HashMap<String, String> map;
    private String key;
    private  ExecutorService es;

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String symbol) {
        this.key = symbol;
    }

    public OMIMProcessor(HashMap<String, String> map, String symbol, ExecutorService es) {
        this.map = map;
        this.key = symbol;
        this.es = es;
    }

    public void run() {
        try {
            Boot.start();
            ResultSet results = Boot.getAPI().selectRS("SELECT ?concept ?label\n{ diseasecard:omim_" + key + " coeus:isAssociatedTo ?item . ?item rdfs:label ?label . ?item coeus:hasConcept ?c . ?c rdfs:label ?concept}", false);
            while (results.hasNext()) {
                QuerySolution row = results.next();
                System.out.println(row.get("label").toString() + " -> " + row.get("concept").toString());
                map.put(row.get("label").toString(), row.get("concept").toString());
                /*if(row.get("concept").toString().contains("hgnc")) {
                    ICD10Processor hgnc = new ICD10Processor(map, ItemFactory.getTokenFromItem(row.get("label").toString() ));
                    Thread t_o = new Thread(hgnc);
                    t_o.start();
                }*/
                
            }
        } catch (Exception ex) {
            Logger.getLogger(OMIMProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
