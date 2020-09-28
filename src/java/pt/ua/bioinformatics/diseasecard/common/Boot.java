package pt.ua.bioinformatics.diseasecard.common;

import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.coeus.api.API;
import pt.ua.bioinformatics.coeus.data.Storage;
import pt.ua.bioinformatics.diseasecard.services.Browsier;
import pt.ua.bioinformatics.diseasecard.services.Cashier;
import pt.ua.bioinformatics.diseasecard.services.DC4;
import pt.ua.bioinformatics.diseasecard.services.Indexer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Seed launcher.
 *
 * @author pedrolopes
 */
public class Boot {

    private static boolean started = false;
    private static API api = null;
    //private static Jedis jedis = null;
    private static JedisPool jedis_pool = null;

    public static Jedis getJedis() {
        if (jedis_pool == null) {
            DC4.load();
            jedis_pool = new JedisPool(new JedisPoolConfig(),DC4.getRedis_host().get("host").toString(), Integer.parseInt(DC4.getRedis_host().get("port").toString()), 10000 );
        }
        Jedis jedis = jedis_pool.getResource();
        
        return jedis;
    }
/*
    public static void setJedis(Jedis jedis) {
        Boot.jedis = jedis;
    }
*/
    public static boolean isStarted() {
        return started;
    }

    public static void setStarted(boolean started) {
        Boot.started = started;
    }

    public static API getAPI() {
        if (api.getModel() == null) {
            api.setModel(Storage.getModel());
        }
        return api;
    }

    public static void setAPI(API api) {
        Boot.api = api;
    }

    public static JedisPool getJedis_pool() {
        return jedis_pool;
    }

    public static void setJedis_pool(JedisPool jedis_pool) {
        Boot.jedis_pool = jedis_pool;
    }

    /**
     * Starts the configured COEUS instance.
     * <p>
     * <b>Built workflow</b><ol>
     * <li>Connect to COEUS Data SDB</li>
     * <li>Load Predicate information for further usage</li>
     * </ol></p>
     * <p>
     * <b>Unbuilt workflow</b><ol>
     * <li>Load Storage information (connect, ontology, setup, predicates)</li>
     * <li>Build instance</li>
     * <li>Save and restart</li>
     * </ol></p>
     */
    public static void start() {
        if (!started) {
            try {
                try {
                    Config.load();
                    DC4.load();
                } catch (Exception ex) {
                    if (Config.isDebug()) {
                        System.out.println("[COEUS][Boot] Unable to load configuration");
                    }
                    Logger.getLogger(Boot.class.getName()).log(Level.SEVERE, null, ex);
                }
//                if (!Config.isBuilt()) {
//                    Storage.load();
//                    api = new API();
//                    Storage.loadPredicates();
//                    Builder.build();
//                    Builder.save();
//                    //remove
//                    Config.setBuilt(true);
//                    start();
//                } else {

                Storage.connect();
                api = new API();
                Storage.loadPredicates();
                System.out.println("\n\t[COEUS] " + Config.getName() + " Online\n");
                
                
                if (!Config.isInfoLoaded()) {
                    Config.setLoad(true);
                    /*
                        This loop is necessary to REALLY setup the system. 
                        Here we're going to: 
                            1) Load disease list into browsing cache;
                            2) Load knowledge network into cache for each disease (OMIM based) and each gene;
                            3) Index full-text content for Solr-based search;
                    
                        Sadly, this is a sequencial process. 
                        Prepare your self, this is going to take some time :) 
                    */
                    System.out.println("\n\nTIME TO CASHIER");
                    Cashier.start();                    
                    System.out.println("\n\nTIME TO BROWSER");
                    Browsier.start();
                    
                                        
                    /*
                        This is the last step and takes a lot of time to finish. 
                        Maybe we can leave it like this? 
                    
                        PS: By doing this process here, we no longer need to go to "/services/indexer" 
                    */
                    
                    System.out.println("\n\nTIME TO INDEX");
                    Indexer index = new Indexer();
                    Thread t = new Thread(index);
                    t.start();
                    
                    
                    
                }

                started = true;
            } catch (Exception ex) {
                if (Config.isDebug()) {
                    System.out.println("[COEUS][Boot] Unable to start COEUS instance");
                }
                Logger.getLogger(Boot.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
        }
    }

    /**
     * Starts Seed for importing data from a single Resource.
     *
     * @param resource
     */
    public static void singleImport(String resource) {
        try {
            Config.load();
            Storage.connectX();
            api = new API();
            Storage.loadPredicates();
//            Builder.build(resource);
        } catch (Exception ex) {
            Logger.getLogger(Boot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
