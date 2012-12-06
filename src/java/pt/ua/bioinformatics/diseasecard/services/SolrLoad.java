package pt.ua.bioinformatics.diseasecard.services;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

/**
 * Single thread handler for importing single resources.
 *
 * @author pedrolopes
 */
public class SolrLoad implements Runnable {

    private String concept;
    private ArrayList<SolrObject> list = new ArrayList<SolrObject>();
    private HashMap<String, String> map = new HashMap<String, String>();

    public SolrLoad(String c) {
        this.concept = c;
    }

    public SolrLoad(String c, ArrayList<SolrObject> list) {
        this.concept = c;
        this.list = list;
        
        map.put("omim", "Linked from OMIM #replace#");
        map.put("hgnc", "Linked from HGNC #replace#");
        map.put("genecards", "Linked from GeneCards #replace#");
        map.put("pubmed", "Linked from Medline #replace#");
        map.put("entrez", "Linked from EntrezGene #replace#");
        map.put("pdb", "Linked from PDB #replace#");
        map.put("uniprot", "Linked from UniProt #replace#");
        map.put("prosite", "Linked from Prosite #replace#");
        map.put("interpro", "Linked from InterPro #replace#");
        map.put("pharmgkb", "Linked from PharmGKB #replace#");
        map.put("drugbank", "Linked from DrugBank #replace#");
        map.put("hpo", "Linked from HPO #replace#");
        map.put("umls", "Linked from UMLs #replace#");
        map.put("icd10", "Linked from ICD10 #replace#");
        map.put("orphanet", "Linked from  Orphanet#replace#");
        map.put("ensembl", "Linked from Ensembl #replace#");
        map.put("enzyme", "Linked from Enzyme #replace#");
        map.put("clinicaltrials", "Linked from KEGG #replace#");
        map.put("swissvar", "Linked from SwissVar #replace#");
        map.put("gwascentral", "Linked from GWASCentral #replace#");
        map.put("name", "#replace#");
        map.put("mesh", "Linked from MeSH #replace#");
    }

    /**
     * Launch single Resource import process.
     */
    public void run() {
        try {
            if (concept.equals("name")) {
                 System.out.println("[Diseasecard][Solr] Processing...." + this.concept);
                SolrServer server = new HttpSolrServer("http://localhost:8983/solr");

                Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();

                for (SolrObject o : list) {
                    try {
                        URL item = new URL("http://localhost:8084/diseasecard/services/linkout/" + this.concept + ":" + o.getOmim());
                        SolrInputDocument d = new SolrInputDocument();
                        d.addField("id", o.getOmim() + ":name:" + o.getId());
                        d.addField("title", o.getId());
                        d.addField("omim", o.getOmim());
                        d.addField("url", item.toURI());
                        docs.add(d);

                    } catch (Exception ex) {
                        System.out.print(ex.getMessage());
                    }
                }
                server.add(docs);
                server.commit();
            
            } else {
                System.out.println("[Diseasecard][Solr] Processing...." + this.concept);
                SolrServer server = new HttpSolrServer("http://localhost:8983/solr");
                Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();

                for (SolrObject o : list) {
                    try {
                        URL item = new URL("http://localhost:8084/diseasecard/services/linkout/" + this.concept + ":" + o.getId());
                        System.out.println("[Diseasecard][Solr] Loading: " + item.toURI().toString());

                        SolrInputDocument d = new SolrInputDocument();
                        d.addField("id", o.getOmim() + ":" + this.concept + ":" + o.getId());
                        d.addField("title", map.get(this.concept).replace("#replace#", this.concept + ":" + o.getId()));
                        d.addField("content", IOUtils.toString(item), 1);
                        d.addField("omim", o.getOmim());
                        d.addField("url", item.toURI());
                        docs.add(d);

                    } catch (Exception ex) {
                        System.out.print(ex.getMessage());
                    }
                }
                server.add(docs);
                server.commit();
            }
        } catch (Exception ex) {
            Logger.getLogger(SolrLoad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
