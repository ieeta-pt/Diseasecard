package pt.ua.diseasecard.components.management;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import org.springframework.stereotype.Service;
import pt.ua.diseasecard.components.Boot;
import pt.ua.diseasecard.components.data.DiseaseAPI;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.utils.ItemFactory;
import redis.clients.jedis.Jedis;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class Cashier {

    private SparqlAPI api;


    public Cashier(SparqlAPI api) {
        Objects.requireNonNull(api);
        this.api = api;
    }


    public void start() {
        Logger.getLogger(Cashier.class.getName()).log(Level.INFO,"\n[Diseasecard][Cashier] Starting process of cache");

        // cache disease content
        cacheDiseases();

        // cache gene content
        cache_hgnc();
    }


    private void cacheDiseases() {
        Jedis jedis = Boot.getJedis();
        ResultSet rs = this.api.selectRS("SELECT ?u WHERE { ?u coeus:hasConcept diseasecard:concept_OMIM } ORDER BY ?u", false);

        while (rs.hasNext()) {
            QuerySolution row = rs.next();
            String omim = ItemFactory.getTokenFromItem(ItemFactory.getTokenFromURI(row.get("u").toString()));
            DiseaseAPI disease = new DiseaseAPI(this.api, omim);
            try {
                jedis.set("omim:" + omim, disease.load().toJSONString());
            } catch (Exception e) {
                Logger.getLogger(Cashier.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        jedis.save();
        Logger.getLogger(Cashier.class.getName()).log(Level.INFO,"[Diseasecard][Cashier] Process of caching OMIMs finished");

    }


    private void cache_hgnc() {
        Jedis jedis = Boot.getJedis();
        ResultSet rs = this.api.selectRS("SELECT ?u WHERE { ?u coeus:hasConcept diseasecard:concept_HGNC } ORDER BY ?u", false);

        while (rs.hasNext()) {
            QuerySolution row = rs.next();
            String entry = ItemFactory.getTokenFromItem(ItemFactory.getTokenFromURI(row.get("u").toString()));
            try {
                DiseaseAPI disease = new DiseaseAPI(this.api);
                jedis.set("hgnc:" + entry.toUpperCase(), disease.loadHGNC(entry).toJSONString());
            } catch (Exception e) {
                Logger.getLogger(Cashier.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        jedis.save();
        Logger.getLogger(Cashier.class.getName()).log(Level.INFO,"[Diseasecard][Cashier] Process of caching HGNC finished");
    }


    public void deleteCache() {
        Logger.getLogger(Cashier.class.getName()).log(Level.INFO,"[Diseasecard][Cashier] Removing Cache");
        Jedis jedis = Boot.getJedis();
        jedis.flushDB();
    }
}
