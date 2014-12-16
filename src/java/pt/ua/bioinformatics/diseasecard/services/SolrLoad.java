package pt.ua.bioinformatics.diseasecard.services;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

/**
 * Single thread handler for importing single resources.
 *
 * @author pedrolopes
 */
public class SolrLoad implements Runnable {

    private String concept;
    private String value;
    private String omim;
    private String uri;
    private String type;
    private HttpSolrServer server;

    public SolrLoad(String omim, String type, HttpSolrServer server) {
        this.omim = omim;
        this.type = type;
        this.server = server;
    }

    public HttpSolrServer getServer() {
        return server;
    }

    public void setServer(HttpSolrServer server) {
        this.server = server;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SolrLoad() {
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getOmim() {
        return omim;
    }

    public void setOmim(String omim) {
        this.omim = omim;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SolrLoad(String c) {
        this.concept = c;
    }

    /**
     * Run indexing process for each term.
     */
    public void run() {
        if (type.equals("omim")) {
            try {
                Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
                SolrInputDocument d = new SolrInputDocument();
                d.addField("id", omim + ":omim:" + value);
                d.addField("title", value);
                d.addField("omim", omim);
                d.addField("content", value);
                docs.add(d);
                server.add(docs);
                server.commit();
            } catch (Exception ex) {
                Logger.getLogger(SolrLoad.class.getName()).log(Level.SEVERE, null, ex);
                 Activity.log(omim, "error", value , "SolrLoad",  "127.0.0.1");
            }
        } else
        if (type.equals("name")) {
            try {
                Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
                SolrInputDocument d = new SolrInputDocument();
                d.addField("id", omim + ":name:" + value);
                d.addField("title", value);
                d.addField("omim", omim);
                d.addField("content", value);
                docs.add(d);
                server.add(docs);
                server.commit();
            } catch (Exception ex) {
                Logger.getLogger(SolrLoad.class.getName()).log(Level.SEVERE, null, ex);
                Activity.log(omim, "error", value , "SolrLoad",  "127.0.0.1");
            }
        } else if (type.equals("link")) {
            URL item = null;
            try {
                Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
                item = new URL("http://localhost:8084/diseasecard/services/linkout/" + uri.replace(" ", "%20"));
                SolrInputDocument d = new SolrInputDocument();
                if(uri.contains(":")) {
                String[] uris = uri.split(":");
                this.concept = uris[0];
                if (uris.length == 3) {
                    this.value = uris[1] + ":" + uris[2];
                } else {
                    this.value = uris[1];
                }
                } else {
                    
                }
                d.addField("id", omim + ":" + uri);
                d.addField("title", uri);
                d.addField("omim", omim);
                if(!uri.contains("go:") && !uri.contains("interpro:"))
                    d.addField("content", IOUtils.toString(item), 1);
                d.addField("url", item.toURI());
                docs.add(d);
                server.add(docs);
                server.commit();
            } catch (Exception ex) {
              //  Logger.getLogger(SolrLoad.class.getName()).log(Level.SEVERE, null, ex);
                Activity.log(omim, "error", item.toString() , "SolrLoad", "127.0.0.1");
            }
        }
       }

    /**
     * Launch single Resource import process.
     *
     * DEPRECATED
     */
    public void runner() {
        try {
            if (concept.equals("name")) {
                System.out.println("[Diseasecard][Solr] Processing...." + this.concept);


                /*  for (SolrObject o : list) {
                 try {
                 URL item = new URL("http://bioinformatics.ua.pt/cdn/" + this.concept + ":" + o.getOmim());
                 SolrInputDocument d = new SolrInputDocument();
                 d.addField("id", o.getOmim() + ":name:" + o.getId());
                 d.addField("title", o.getId());
                 d.addField("omim", o.getOmim());
                 // d.addField("url", item.toURI());
                 docs.add(d);
                 if (docs.size() == 100) {
                 server.add(docs);
                 server.commit();
                 docs.clear();
                 }

                 } catch (Exception ex) {
                 System.out.print(ex.getMessage());
                 }
                 }*/


            } else {
                /* System.out.println("[Diseasecard][Solr] Processing...." + this.concept);
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
                 server.commit();*/
            }
        } catch (Exception ex) {
            Logger.getLogger(SolrLoad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
