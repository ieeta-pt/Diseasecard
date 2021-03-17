package pt.ua.diseasecard.utils;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SolrLoad implements Runnable{

    private String omim;
    private HttpSolrServer server;
    private String type;
    private String value;
    private String uri;
    private String concept;

    public SolrLoad(String omim, String type, HttpSolrServer server) {
        this.omim = omim;
        this.type = type;
        this.server = server;
    }

    @Override
    public void run() {
        switch (this.type) {
            case "omim":
                try {
                    Collection<SolrInputDocument> docs = new ArrayList<>();
                    SolrInputDocument d = new SolrInputDocument();
                    d.addField("id", this.omim + ":omim:" + this.value);
                    d.addField("title", this.value);
                    d.addField("omim", this.omim);
                    d.addField("content", this.value);
                    docs.add(d);
                    this.server.add(docs);
                    this.server.commit();
                } catch (Exception ex) {
                    Logger.getLogger(SolrLoad.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "name":
                try {
                    Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
                    SolrInputDocument d = new SolrInputDocument();
                    d.addField("id", this.omim + ":name:" + this.value);
                    d.addField("title", this.value);
                    d.addField("omim", this.omim);
                    d.addField("content", this.value);
                    docs.add(d);
                    this.server.add(docs);
                    this.server.commit();
                } catch (Exception ex) {
                    Logger.getLogger(SolrLoad.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "link":
                URL item = null;
                try {
                    Collection<SolrInputDocument> docs = new ArrayList<>();

                    // TODO que raio de link é este? checkar a situação

                    item = new URL("http://localhost:8080/diseasecard/services/linkout/" + this.uri.replace(" ", "%20"));
                    SolrInputDocument d = new SolrInputDocument();
                    if (this.uri.contains(":")) {
                        String[] uris = this.uri.split(":");
                        this.concept = uris[0];
                        if (uris.length == 3) {
                            this.value = uris[1] + ":" + uris[2];
                        } else {
                            this.value = uris[1];
                        }
                    }
                    d.addField("id", this.omim + ":" + this.uri);
                    d.addField("title", this.uri);
                    d.addField("omim", this.omim);
                    if (!this.uri.contains("go:") && !this.uri.contains("interpro:")) {
                        d.addField("content", item, 1);
                    }
                    d.addField("url", item.toURI());
                    docs.add(d);
                    this.server.add(docs);
                    this.server.commit();
                } catch (Exception ex) {
                    Logger.getLogger(SolrLoad.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
        }
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
