package pt.ua.diseasecard.components.management;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import pt.ua.diseasecard.components.Boot;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.utils.SolrLoad;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import redis.clients.jedis.Jedis;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class Indexer implements Runnable{

    private SparqlAPI api;
    private HashMap<String, JSONObject> omims;
    private String solrConnection;

    public Indexer(SparqlAPI api, DiseasecardProperties diseasecardProperties) {
        this.api = api;
        this.omims = new HashMap<>();
        this.solrConnection = diseasecardProperties.getSolr().get("host") + ":" + diseasecardProperties.getSolr().get("port") + "/" + diseasecardProperties.getSolr().get("index");
    }

    @Override
    public void run() {
        Logger.getLogger(Indexer.class.getName()).log(Level.INFO,"\n[Diseasecard][Indexer] Starting process of indexing");
        // Load OMIM network from Redis cache
        loadOMIMs();

        // Index all content into Solr
        indexer();
    }

    void loadOMIMs() {
        Logger.getLogger(Indexer.class.getName()).log(Level.INFO,"[Diseasecard][Indexer] Indexer started, loading OMIMs");

        ResultSet rs = this.api.selectRS("SELECT ?t WHERE { ?u coeus:hasConcept diseasecard:concept_OMIM . ?u diseasecard:omim ?t  }", false);
        Jedis jedis = Boot.getJedis();
        while (rs.hasNext()) {
            QuerySolution row = rs.next();
            try {
                //Logger.getLogger(Indexer.class.getName()).log(Level.INFO,"OMIM: " + jedis.get("omim:" + row.get("t").toString()));
                omims.put(row.get("t").toString(), new JSONObject(jedis.get("omim:" + row.get("t").toString())));
            } catch (Exception e) {
                Logger.getLogger(Cashier.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    void indexer() {
        Logger.getLogger(Indexer.class.getName()).log(Level.INFO,"[Diseasecard][Indexer] OMIMs loaded, starting Solr import");

        HttpSolrServer server = new HttpSolrServer(this.solrConnection);
        server.setDefaultMaxConnectionsPerHost(256);
        server.setMaxTotalConnections(256);

        ExecutorService pool = Executors.newFixedThreadPool(64);

        for (String omim : omims.keySet()) {
            JSONObject obj = omims.get(omim);
            try {
                JSONArray names = obj.getJSONArray("synonyms");
                for (int i = 0; i < names.length(); i++) {
                    SolrLoad load = new SolrLoad(omim, "name", server);
                    if (names.getString(i) != null) load.setValue(names.getString(i));

                    // TODO: VERIFICAR SE NÃO FALTA AQUI CENS
                    pool.execute(load);

                }
            } catch (Exception ex) {
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                JSONArray network = obj.getJSONArray("network");
                for (int i = 0; i < network.length(); i++) {
                    SolrLoad load = new SolrLoad(omim, "link", server);
                    load.setUri(network.getString(i));
                    pool.execute(load);
                }
            } catch (Exception ex) {
                //Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
