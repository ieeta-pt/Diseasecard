package pt.ua.diseasecard.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import pt.ua.diseasecard.utils.PrefixFactory;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@PropertySource("classpath:configuration/diseasecard.properties")
@ConfigurationProperties(prefix = "diseasecard")
public class DiseasecardProperties {

    private String name;
    private String description;
    private String keyprefix;
    private String version;
    private String ontology;
    private String setup;
    private boolean built;
    private boolean load;
    private boolean debug;
    private String sdb;
    private String predicates;

    private Map<String, String> solr;
    private Map<String, String> redis;
    private Map<String, String> database;
    private Map<String, String> prefixes;
    private Map<String, String> sources;

    public DiseasecardProperties() {
    }

    private List<String> protectedSources;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeyprefix() {
        return keyprefix;
    }

    public void setKeyprefix(String keyprefix) {
        this.keyprefix = keyprefix;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOntology() {
        return ontology;
    }

    public void setOntology(String ontology) {
        this.ontology = ontology;
    }

    public String getSetup() {
        return setup;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public boolean getBuilt() {
        return built;
    }

    public void setBuilt(boolean built) {
        this.built = built;
    }

    public boolean getLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }

    public boolean getDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public Map<String, String> getSolr() {
        return solr;
    }

    public void setSolr(Map<String, String> solr) {
        this.solr = solr;
    }

    public Map<String, String> getRedis() {
        return redis;
    }

    public void setRedis(Map<String, String> redis) {
        this.redis = redis;
    }

    public Map<String, String> getDatabase() {
        return database;
    }

    public void setDatabase(Map<String, String> database) {
        this.database = database;
    }

    public Map<String, String> getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(Map<String, String> prefixes) {
        this.prefixes = prefixes;
    }

    public String getSdb() {
        return sdb;
    }

    public void setSdb(String sdb) {
        this.sdb = sdb;
    }

    public String getPredicates() {
        return predicates;
    }

    public List<String> getProtectedSources() {
        return protectedSources;
    }

    public void setProtectedSources(List<String> protectedSources) {
        this.protectedSources = protectedSources;
    }

    public void setPredicates(String predicates) {
        this.predicates = predicates;
    }

    public Map<String, String> getSources() {
        return sources;
    }

    public void setSources(Map<String, String> sources) {
        this.sources = sources;
    }

    @PostConstruct
    public void addPrefix() {
        PrefixFactory.setPrefixes((HashMap<String, String>) this.getPrefixes());
    }
}
