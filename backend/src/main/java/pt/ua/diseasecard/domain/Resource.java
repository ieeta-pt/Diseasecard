package pt.ua.diseasecard.domain;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import pt.ua.diseasecard.utils.BeanUtil;
import pt.ua.diseasecard.utils.PrefixFactory;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Resource {
    private String description;
    private String label;
    private String title;
    private String uri;
    private String source;
    private String subject;
    private String method;
    private String publisher;
    private Concept isResourceOf;
    private String extendsConcept;
    private String endpoint;
    private String extension;
    private String query;
    private String regex;
    private String identifiers;

    private final SparqlAPI sparqlAPI = BeanUtil.getBean(SparqlAPI.class);;

    private final DiseasecardProperties config = BeanUtil.getBean(DiseasecardProperties.class);;

    private boolean built = false;

    public Resource(String uri, String title, String label, String description, String publisher, String endpoint, String method) {
        this.uri = uri;
        this.description = description;
        this.label = label;
        this.title = title;
        this.publisher = publisher;
        this.endpoint = endpoint;
        this.method = method;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }

    public Concept getIsResourceOf() {
        return isResourceOf;
    }
    public void setIsResourceOf(Concept isResourceOf) {
        this.isResourceOf = isResourceOf;
    }

    public String getExtendsConcept() {
        return extendsConcept;
    }
    public void setExtendsConcept(String extendsConcept) {
        this.extendsConcept = extendsConcept;
    }

    public String getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }

    public String getRegex() {
        return regex;
    }
    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getIdentifiers() {
        return identifiers;
    }
    public void setIdentifiers(String identifiers) {
        this.identifiers = identifiers;
    }

    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public boolean isBuilt() {
        return built;
    }
    public void setBuilt(boolean built) {
        this.built = built;
    }


    public HashMap<String, String> getExtended() {
        HashMap<String, String> extensions = new HashMap<String, String>();
        try
        {
            if (this.config.getDebug()) System.out.println("[COEUS][Resource] " + title + " loading extension data");

            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(this.sparqlAPI.select("SELECT ?title WHERE { ?title coeus:hasConcept " + PrefixFactory.encode(extendsConcept) + " } ORDER BY ?title", "json", false));
            JSONObject results = (JSONObject) response.get("results");
            JSONArray bindings = (JSONArray) results.get("bindings");

            for (Object obj : bindings)
            {
                JSONObject binding = (JSONObject) obj;
                JSONObject tit = (JSONObject) binding.get("title");
                extensions.put(tit.get("value").toString(), tit.get("value").toString());
            }

        }
        catch (Exception ex)
        {
            if (this.config.getDebug()) System.out.println("[COEUS][Resource] Unable to load extension concept information");
            Logger.getLogger(Resource.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (this.config.getDebug()) System.out.println("\tExtension data loaded");

        return extensions;
    }

    public HashMap<String, String> getExtended(String uri) {
        HashMap<String, String> extensions = new HashMap<String, String>();

        try
        {
            if (this.config.getDebug()) System.out.println("[COEUS][Resource] " + title + " loading extension data");

            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(this.sparqlAPI.select("SELECT ?t ?title WHERE { ?t coeus:hasConcept " + PrefixFactory.encode(extendsConcept) + " . ?t " + uri + " ?title} ORDER BY ?title", "json", false));
            JSONObject results = (JSONObject) response.get("results");
            JSONArray bindings = (JSONArray) results.get("bindings");

            for (Object obj : bindings)
            {
                JSONObject binding = (JSONObject) obj;
                JSONObject tit = (JSONObject) binding.get("title");
                JSONObject element = (JSONObject) binding.get("t");
                extensions.put(tit.get("value").toString(), element.get("value").toString());
            }
        } catch (Exception ex)
        {
            if (this.config.getDebug()) System.out.println("[COEUS][Resource] Unable to load extension concept information");
            Logger.getLogger(Resource.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (this.config.getDebug()) {
            System.out.println("\tExtension data loaded");
        }
        return extensions;
    }

    public void loadConcept() {
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(this.sparqlAPI.select("SELECT ?c ?title ?label WHERE { "
                    + PrefixFactory.encode(uri) + " coeus:isResourceOf ?c ."
                    + " ?c dc:title ?title ."
                    + " ?c rdfs:label ?label}", "js", false));
            JSONObject results = (JSONObject) response.get("results");
            JSONArray bindings = (JSONArray) results.get("bindings");

            for (Object obj : bindings)
            {
                JSONObject binding = (JSONObject) obj;
                JSONObject con_label = (JSONObject) binding.get("label");
                JSONObject con_title = (JSONObject) binding.get("title");
                JSONObject con = (JSONObject) binding.get("c");
                this.isResourceOf = new Concept(con.get("value").toString(), con_title.get("value").toString(), con_label.get("value").toString());
            }
        }
        catch (Exception ex)
        {
            if (this.config.getDebug()) System.out.println("[COEUS][Resource] Unable to load resource concept information");
            Logger.getLogger(Resource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
