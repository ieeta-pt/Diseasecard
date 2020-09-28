package pt.ua.bioinformatics.diseasecard.services;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.coeus.api.ItemFactory;
import pt.ua.bioinformatics.diseasecard.common.Boot;
import pt.ua.bioinformatics.diseasecard.domain.DiseaseAPI;
import redis.clients.jedis.Jedis;

/**
 * Class with helper methods to preload JSON content into Redis cache.
 *
 * @author pedrolopes
 */
public class Cashier {

    
    public static void start() {

        System.out.println("Starting the process of cashing all diseases..");
        // cache disease content
        cacheDiseases();

        // cache gene content
//        cache_hgnc();
    }

    /**
     * Cache content for OMIM entries.
     *
     * <ul>
     * <li>Load OMIM entries list from knowledge base</li>
     * <li>Get network for each entry</li>
     * <li>Store network on Redis as JSON object</li>
     * </ul>
     *
     */
    private static void cacheDiseases() {
        //Boot.start();
        ResultSet rs = Boot.getAPI().selectRS("SELECT ?u WHERE { ?u coeus:hasConcept diseasecard:concept_OMIM } ORDER BY ?u", false);
        Jedis jedis = Boot.getJedis();
        while (rs.hasNext()) {
            QuerySolution row = rs.next();
            String omim = ItemFactory.getTokenFromItem(ItemFactory.getTokenFromURI(row.get("u").toString()));
            System.out.println("OMIM:" + omim);
            try {
                DiseaseAPI disease = new DiseaseAPI(omim);
                //Boot.getJedis().set("omim:" + omim, disease.load().toJSONString());
                
                jedis.set("omim:" + omim, disease.load().toJSONString());
                System.out.println("[Diseasecard][JedisLoad] cached omim:" + omim);
            } catch (Exception e) {
                Logger.getLogger(Cashier.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        jedis.save();

    }

    /**
     * Cache content for HGNC entries.
     *
     * <ul>
     * <li>Load HGNC entries list from knowledge base</li>
     * <li>Get network for each entry</li>
     * <li>Store network on Redis as JSON object</li>
     * </ul>
     *
     */
    private static void cache_hgnc() {
        Boot.start();
        ResultSet rs = Boot.getAPI().selectRS("SELECT ?u WHERE { ?u coeus:hasConcept diseasecard:concept_HGNC } ORDER BY ?u", false);
        Jedis jedis = Boot.getJedis();
        while (rs.hasNext()) {
            QuerySolution row = rs.next();
            String entry = ItemFactory.getTokenFromItem(ItemFactory.getTokenFromURI(row.get("u").toString()));
            try {
                DiseaseAPI disease = new DiseaseAPI();
                jedis.set("hgnc:" + entry.toUpperCase(), disease.loadHGNC(entry).toJSONString());
                System.out.println("[Diseasecard][JedisLoad] cached hgnc:" + entry);
            } catch (Exception e) {
                Logger.getLogger(Cashier.class.getName()).log(Level.SEVERE, null, e);
            }
            break;
        }
        jedis.save();
    }
}
