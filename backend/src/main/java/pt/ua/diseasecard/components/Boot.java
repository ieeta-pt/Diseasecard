package pt.ua.diseasecard.components;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import pt.ua.diseasecard.components.data.Storage;
import pt.ua.diseasecard.components.management.Browsier;
import pt.ua.diseasecard.components.management.Cashier;
import pt.ua.diseasecard.components.management.Indexer;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class Boot {

    private final DiseasecardProperties config;
    private final Cashier cashier;
    private final Browsier browsier;
    private final Indexer indexer;
    private final Storage storage;
    private static JedisPool jedis_pool;
    private SimpMessagingTemplate template;

    public Boot(DiseasecardProperties diseasecardProperties, Cashier cashier, Browsier browsier, Indexer indexer, SimpMessagingTemplate template, Storage storage) {
        Objects.requireNonNull(diseasecardProperties);
        Objects.requireNonNull(cashier);
        Objects.requireNonNull(browsier);
        Objects.requireNonNull(indexer);
        this.config = diseasecardProperties;
        this.cashier = cashier;
        this.browsier = browsier;
        this.indexer = indexer;
        this.template = template;
        this.storage = storage;
        jedis_pool = new JedisPool(new JedisPoolConfig(), this.config.getRedis().get("host"), Integer.parseInt(this.config.getRedis().get("port")), 10000 );
    }

    /**
     *  Begins the process behind Diseasecard Setup.
     */
    @PostConstruct
    private void init() {

        // TODO: if diseasecard is not built!  (?)
        if (this.config.getLoad()) {
            this.startInternalProcess();
        }
    }


    public static Jedis getJedis() {
        return jedis_pool.getResource();
    }


    public void startInternalProcess() {
        // TODO: garantir que primeiro são eliminados os dados que já lá estão

        this.config.setLoad(false);
        this.storage.setBuildPhase("Building_Cashier");
        this.cashier.start();
        this.browsier.start();

        template.convertAndSend("/topic/message", "StartingIndexer");

        Thread t = new Thread(this.indexer);
        t.start();
    }
}
