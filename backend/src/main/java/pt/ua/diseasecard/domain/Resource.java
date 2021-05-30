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
    private String publisher;
    private String extendsConcept;
    private String endpoint;
    private String identifiers;
    private Concept isResourceOf;
    private Parser hasParser;

    private final SparqlAPI sparqlAPI = BeanUtil.getBean(SparqlAPI.class);;

    private final DiseasecardProperties config = BeanUtil.getBean(DiseasecardProperties.class);;

    private boolean built = false;

    public Resource(String uri, String title, String label, String description, String publisher, String endpoint) {
        this.uri = uri;
        this.description = description;
        this.label = label;
        this.title = title;
        this.publisher = publisher;
        this.endpoint = endpoint;
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

    public Parser getHasParser() {
        return hasParser;
    }
    public void setHasParser(Parser hasParser) {
        this.hasParser = hasParser;
    }


    public HashMap<String, String> getExtended() {
        HashMap<String, String> extensions = new HashMap<>();
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


    public void loadParser() {
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(this.sparqlAPI.select("SELECT *" +
                    " WHERE { " + PrefixFactory.encode(this.uri) + " coeus:hasParser ?s ."
                    + " ?s coeus:resourceID ?resourceID . "
                    + " OPTIONAL { ?s coeus:externalResourceID ?externalResourceID} . "
                    + " OPTIONAL { ?s coeus:mainNode ?mainNode} . "
                    + " OPTIONAL { ?s coeus:methodByReplace ?methodByReplace} . "
                    + " OPTIONAL { ?s coeus:resourceInfoInAttribute ?resourceInfoInAttribute} . "
                    + " OPTIONAL { ?s coeus:resourceInfoAttribute ?resourceInfoAttribute} . "
                    + " OPTIONAL { ?s coeus:resourceRegex ?resourceRegex} . "
                    + " OPTIONAL { ?s coeus:externalResourceNode ?externalResourceNode} . "
                    + " OPTIONAL { ?s coeus:externalResourceID ?externalResourceID} . "
                    + " OPTIONAL { ?s coeus:externalResourceInfoInAttribute ?externalResourceInfoInAttribute} . "
                    + " OPTIONAL { ?s coeus:externalResourceInfoAttribute ?externalResourceInfoAttribute} . "
                    + " OPTIONAL { ?s coeus:externalResourceRegex ?externalResourceRegex} . "
                    + " OPTIONAL { ?s coeus:filterBy ?filterBy} . "
                    + " OPTIONAL { ?s coeus:filterValue ?filterValue} . "
                    + " OPTIONAL { ?s coeus:uniqueResource ?uniqueResource} . "
                    + " OPTIONAL { ?s coeus:uniqueExternalResource ?uniqueExternalResource}}", "js", false));

            JSONObject results = (JSONObject) response.get("results");
            JSONArray bindings = (JSONArray) results.get("bindings");

            for (Object obj : bindings)
            {
                JSONObject binding = (JSONObject) obj;
                JSONObject mainNode = (JSONObject) binding.get("mainNode");
                JSONObject methodByReplace = (JSONObject) binding.get("methodByReplace");
                JSONObject resourceID = (JSONObject) binding.get("resourceID");
                JSONObject resourceInfoInAttribute = (JSONObject) binding.get("resourceInfoInAttribute");
                JSONObject resourceInfoAttribute = (JSONObject) binding.get("resourceInfoAttribute");
                JSONObject resourceRegex = (JSONObject) binding.get("resourceRegex");

                JSONObject externalResourceNode = (JSONObject) binding.get("externalResourceNode");
                JSONObject externalResourceID = (JSONObject) binding.get("externalResourceID");
                JSONObject externalResourceInfoInAttribute = (JSONObject) binding.get("externalResourceInfoInAttribute");
                JSONObject externalResourceInfoAttribute = (JSONObject) binding.get("externalResourceInfoAttribute");
                JSONObject externalResourceRegex = (JSONObject) binding.get("externalResourceRegex");
                JSONObject filterBy = (JSONObject) binding.get("filterBy");
                JSONObject filterValue = (JSONObject) binding.get("filterValue");
                JSONObject uniqueResource = (JSONObject) binding.get("uniqueResource");
                JSONObject uniqueExternalResource = (JSONObject) binding.get("uniqueExternalResource");

                Parser resourceParser = new Parser(resourceID.get("value").toString());
                resourceParser.setMainNode(!(mainNode == null) ? mainNode.get("value").toString() : "");
                resourceParser.setMethodByReplace(!(methodByReplace == null) ? Boolean.parseBoolean(methodByReplace.get("value").toString()) : false);
                resourceParser.setResourceInfoInAttribute(!(resourceInfoInAttribute == null) ? Boolean.parseBoolean(resourceInfoInAttribute.get("value").toString()) : false);
                resourceParser.setResourceInfoAttribute(!(resourceInfoAttribute == null) ? resourceInfoAttribute.get("value").toString() : "");
                resourceParser.setResourceRegex(!(resourceRegex == null) ? resourceRegex.get("value").toString() : "");

                resourceParser.setExternalResourceID(!(externalResourceID == null) ? externalResourceID.get("value").toString() : "");
                resourceParser.setExternalResourceNode(!(externalResourceNode == null) ? externalResourceNode.get("value").toString() : "");
                resourceParser.setExternalResourceInfoInAttribute(!(externalResourceInfoInAttribute == null) ? Boolean.parseBoolean(externalResourceInfoInAttribute.get("value").toString()) : false);
                resourceParser.setExternalResourceInfoAttribute(!(externalResourceInfoAttribute == null) ? externalResourceInfoAttribute.get("value").toString() : "");
                resourceParser.setExternalResourceRegex(!(externalResourceRegex == null) ? externalResourceRegex.get("value").toString() : "");
                resourceParser.setFilterBy(!(filterBy == null) ? filterBy.get("value").toString() : "");
                resourceParser.setFilterValue(!(filterValue == null) ? filterValue.get("value").toString() : "");
                resourceParser.setUniqueResource(!(uniqueResource == null) ? Boolean.parseBoolean(uniqueResource.get("value").toString()) : true);
                resourceParser.setUniqueExternalResource(!(uniqueExternalResource == null) ? Boolean.parseBoolean(uniqueExternalResource.get("value").toString()) : false);

                this.hasParser = resourceParser;
            }
        }
        catch (Exception ex)
        {
            if (this.config.getDebug()) System.out.println("[COEUS][Resource] Unable to load resource parser information");
            Logger.getLogger(Resource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void loadOMIMParser() {
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(this.sparqlAPI.select("SELECT *" +
                    " WHERE { " + PrefixFactory.encode(this.uri) + " coeus:hasParser ?s ."
                    + " ?s coeus:resourceID ?resourceID . "
                    + " ?s coeus:genemap_cName ?genemap_cName . "
                    + " ?s coeus:genemap_cOMIM ?genemap_cOMIM . "
                    + " ?s coeus:genemap_cLocation ?genemap_cLocation . "
                    + " ?s coeus:genemap_cGenes ?genemap_cGenes . "
                    + " ?s coeus:morbidmap_cName ?morbidmap_cName . "
                    + " ?s coeus:morbidmap_cOMIM ?morbidmap_cOMIM . "
                    + " ?s coeus:morbidmap_cLocation ?morbidmap_cLocation . "
                    + " ?s coeus:morbidmap_cGenes ?morbidmap_cGenes}", "js", false));

            JSONObject results = (JSONObject) response.get("results");
            JSONArray bindings = (JSONArray) results.get("bindings");

            for (Object obj : bindings)
            {
                JSONObject binding = (JSONObject) obj;
                JSONObject resourceID = (JSONObject) binding.get("resourceID");

                JSONObject genemap_cName = (JSONObject) binding.get("genemap_cName");
                JSONObject genemap_cOMIM = (JSONObject) binding.get("genemap_cOMIM");
                JSONObject genemap_cLocation = (JSONObject) binding.get("genemap_cLocation");
                JSONObject genemap_cGenes = (JSONObject) binding.get("genemap_cGenes");
                JSONObject morbidmap_cName = (JSONObject) binding.get("morbidmap_cName");
                JSONObject morbidmap_cOMIM = (JSONObject) binding.get("morbidmap_cOMIM");
                JSONObject morbidmap_cLocation = (JSONObject) binding.get("morbidmap_cLocation");
                JSONObject morbidmap_cGenes = (JSONObject) binding.get("morbidmap_cGenes");

                Parser resourceParser = new Parser(resourceID.get("value").toString());

                resourceParser.setGenecardName(Integer.parseInt(genemap_cName.get("value").toString()));
                resourceParser.setGenecardOMIM(Integer.parseInt(genemap_cOMIM.get("value").toString()));
                resourceParser.setGenecardLocation(Integer.parseInt(genemap_cLocation.get("value").toString()));
                resourceParser.setGenecardGenes(Integer.parseInt(genemap_cGenes.get("value").toString()));
                resourceParser.setMorbidmapName(Integer.parseInt(morbidmap_cName.get("value").toString()));
                resourceParser.setMorbidmapOMIM(Integer.parseInt(morbidmap_cOMIM.get("value").toString()));
                resourceParser.setMorbidmapLocation(Integer.parseInt(morbidmap_cLocation.get("value").toString()));
                resourceParser.setMorbidmapGene(Integer.parseInt(morbidmap_cGenes.get("value").toString()));

                this.hasParser = resourceParser;
            }
        }
        catch (Exception ex)
        {
            if (this.config.getDebug()) System.out.println("[COEUS][Resource] Unable to load resource parser information");
            Logger.getLogger(Resource.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String toString() {
        return "Resource{" +
                "description='" + description + '\'' +
                ", label='" + label + '\'' +
                ", title='" + title + '\'' +
                ", uri='" + uri + '\'' +
                ", publisher='" + publisher + '\'' +
                ", extendsConcept='" + extendsConcept + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", identifiers='" + identifiers + '\'' +
                ", isResourceOf=" + isResourceOf +
                ", hasParser=" + hasParser +
                ", sparqlAPI=" + sparqlAPI +
                ", config=" + config +
                ", built=" + built +
                '}';
    }
}
