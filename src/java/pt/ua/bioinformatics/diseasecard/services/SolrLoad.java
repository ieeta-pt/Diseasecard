package pt.ua.bioinformatics.diseasecard.services;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
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

    public SolrLoad(String c) {
        this.concept = c;
    }

    public SolrLoad(String c, ArrayList<SolrObject> list) {
        this.concept = c;
        this.list = list;
    }

    /**
     * Launch single Resource import process.
     */
    public void run() {
        try {
            if (concept.equals("name")) {
                System.out.println("[Diseasecard][Solr] Processing...." + this.concept);
                SolrServer server = new HttpSolrServer("http://localhost:8080/solr");

                Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();

                for (SolrObject o : list) {
                    try {
                        URL item = new URL("http://bioinformatics.ua.pt/cdn/" + this.concept + ":" + o.getOmim());
                        SolrInputDocument d = new SolrInputDocument();
                        d.addField("id", o.getOmim() + ":name:" + o.getId());
                        d.addField("title", o.getId());
                        d.addField("omim", o.getOmim());
                       // d.addField("url", item.toURI());
                        docs.add(d);
                        if(docs.size() == 100) {
                            server.add(docs);
                            server.commit();
                            docs.clear();
                        }

                    } catch (Exception ex) {
                        System.out.print(ex.getMessage());
                    }
                }
                

            } else {
                System.out.println("[Diseasecard][Solr] Processing...." + this.concept);
                SolrServer server = new HttpSolrServer("http://localhost:8080/solr");
                Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();

                for (SolrObject o : list) {
                    try {
                        URL item = new URL("http://bioinformatics.ua.pt/cdn/" + this.concept + ":" + o.getId());
                        System.out.println("[Diseasecard][Solr] Loading: " + item.toURI().toString());

                        SolrInputDocument d = new SolrInputDocument();
                        d.addField("id", o.getOmim() + ":" + this.concept + ":" + o.getId());
                        d.addField("title", this.concept + ":" + o.getId());
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
