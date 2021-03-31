package pt.ua.diseasecard.service;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pt.ua.diseasecard.components.Boot;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.components.data.Storage;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import pt.ua.diseasecard.connectors.CSVFactory;
import pt.ua.diseasecard.connectors.PluginFactory;
import pt.ua.diseasecard.domain.Concept;
import pt.ua.diseasecard.domain.Resource;
import pt.ua.diseasecard.connectors.ResourceFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DataManagementService {

    private String uploadDir;
    private Storage storage;
    private DiseasecardProperties config;
    private ArrayList<Resource> resources;
    private SparqlAPI sparqlAPI;

    private AutowireCapableBeanFactory beanFactory;
    private Boot boot;

    public DataManagementService(Storage storage, DiseasecardProperties diseasecardProperties, SparqlAPI sparqlAPI, AutowireCapableBeanFactory beanFactory, Boot boot) {
        this.uploadDir = "submittedFiles";
        this.storage = storage;
        this.config = diseasecardProperties;
        this.resources = new ArrayList<>();
        this.sparqlAPI = sparqlAPI;
        this.beanFactory = beanFactory;
        this.boot = boot;
    }

    /*
        Description
     */
    public Map<String, String> uploadSetup(MultipartFile file) {
        try
        {
            Path copyLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            // TODO: Get endpoints
            Map<String, String> newEndpoints = this.storage.loadSetup(file.getInputStream());
            /*for (Map.Entry<String, String> entry : newEndpoints.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue().toString());
            }*/

            return newEndpoints;
            //this.build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    /*
        Description
     */
    public void uploadEndpoints(List<MultipartFile> endpoints) throws IOException {
        for (MultipartFile file : endpoints) {
            Path copyLocation = Paths.get(uploadDir + File.separator + "endpoints" + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        }
        this.build();
    }


    /*
        Description
     */
    private void build() {
        try {
            this.readResources();
            for(Resource r : this.resources)
            {
                try
                {
                    if (r.isBuilt())
                    {
                        if (this.config.getDebug()) System.out.println("[COEUS][Builder] Already built resource " + r.getTitle());
                    }
                    else
                    {
                        if (this.config.getDebug())  System.out.println("[COEUS][Builder] Reading data for resource " + r.getTitle());
                        this.readData(r);
                    }
                }
                catch (Exception ex)
                {
                    if (this.config.getDebug()) System.out.println("[COEUS][Builder] Unable to read data for " + this.config.getName() + " in resource " + r.getTitle());
                    Logger.getLogger(DataManagementService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.boot.startInternalProcess();
        }
        catch (Exception ex)
        {
            if(this.config.getDebug()) System.out.println("[COEUS][DataManagementService] Unable to build " + this.config.getName());
            Logger.getLogger(DataManagementService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /*
        Description
     */
    private void readResources() {
        try {
            if (this.config.getDebug()) System.out.println("[COEUS][Builder] Reading resources for " + this.config.getName());

            JSONArray finalR = getAllResources();

            for (Object o : finalR) {
                JSONObject info = (JSONObject) o;

                Resource r = new Resource((String) info.get("s"), (String) info.get("title"), (String) info.get("label"), (String) info.get("comment"), (String) info.get("publisher"), (String) info.get("endpoint"), (String) info.get("method"));
                r.setExtendsConcept((String) info.get("extends"));
                r.setIsResourceOf(new Concept((String) info.get("resof")));
                r.setRegex((String) info.get("regex"));
                r.setIdentifiers((String) info.get("identifiers"));
                r.setExtension((String) info.get("extension"));
                r.setQuery((String) info.get("query"));
                r.setBuilt((Boolean) info.get("built"));
                r.loadConcept();
                this.resources.add(r);
            }
            if (this.config.getDebug())  System.out.println("[COEUS][Builder] Resource information read");
        }
        catch (Exception ex)
        {
            if (this.config.getDebug()) System.out.println("[COEUS][Builder] Unable to read resource information");
            Logger.getLogger(DataManagementService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /*
        Description
     */
    private void readData(Resource r) {
        ResourceFactory factory;
        try
        {
            if (!r.isBuilt())
            {
                switch (r.getPublisher().toLowerCase())
                {
                    case "csv":
                        factory = new CSVFactory(r);
                        break;
                    case "plugin":
                        factory = new PluginFactory(r);
                        //this.beanFactory.autowireBean(factory);
                        break;
                    default:
                        factory = null;
                }

                if (factory != null)
                {
                    factory.read();
                    factory.save();
                }
            }
            if (this.config.getDebug()) System.out.println("[COEUS][Builder] Data for " + r.getTitle() + " read");
        } catch (Exception ex) {
            if (this.config.getDebug()) System.out.println("[COEUS][Builder] Unable to read data for " + r.getTitle());
            Logger.getLogger(DataManagementService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /*
        Description
     */
    public JSONObject getAllEntities() {
        if (this.config.getDebug()) System.out.println("[COEUS][Builder] Getting all the existing entities on " + this.config.getName());
        JSONObject finalR = new JSONObject();
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(this.sparqlAPI.select("SELECT ?s ?label ?comment ?title ?description ?isEntityOf ?isIncludedIn"
                    + " WHERE { ?s rdf:type coeus:Entity ."
                    + " ?s rdfs:label ?label ."
                    + " ?s rdfs:comment ?comment ."
                    + " ?s dc:description ?description ."
                    + " ?s dc:title ?title ."
                    + "OPTIONAL { ?s coeus:isIncludedIn ?isIncludedIn} . "
                    + "OPTIONAL { ?s coeus:isEntityOf ?isEntityOf}} " , "js", false));

            JSONObject results = (JSONObject) response.get("results");
            JSONArray bindings = (JSONArray) results.get("bindings");

            for (Object o : bindings) {

                JSONObject binding = (JSONObject) o;
                String uri = ((JSONObject) binding.get("s")).get("value").toString();
                String label = ((JSONObject) binding.get("label")).get("value").toString();
                String comment = ((JSONObject) binding.get("comment")).get("value").toString();
                String description = ((JSONObject) binding.get("description")).get("value").toString();
                String title = ((JSONObject) binding.get("title")).get("value").toString();

                if (!finalR.containsKey(label)) finalR.put(label, new JSONObject());

                ((JSONObject) finalR.get(label)).put("label", label);
                ((JSONObject) finalR.get(label)).put("uri", uri);
                ((JSONObject) finalR.get(label)).put("comment", comment);
                ((JSONObject) finalR.get(label)).put("description", description);
                ((JSONObject) finalR.get(label)).put("title", title);
                ((JSONObject) finalR.get(label)).put("isIncludedIn", new JSONArray());
                ((JSONObject) finalR.get(label)).put("isEntityOf", new JSONArray());



                JSONObject isIncludedInJSON = (JSONObject) binding.get("isIncludedIn");
                JSONObject isEntityOfJSON = (JSONObject) binding.get("isEntityOf");

                if (isIncludedInJSON != null) {
                    String x = isIncludedInJSON.get("value").toString();
                    ((JSONArray) ((JSONObject) finalR.get(label)).get("isIncludedIn")).add(new String[]{ x.substring( x.lastIndexOf("/") + 1 ), x });
                }

                if (isEntityOfJSON != null) {
                    String x = isEntityOfJSON.get("value").toString();
                    ((JSONArray) ((JSONObject) finalR.get(label)).get("isEntityOf")).add(new String[]{ x.substring( x.lastIndexOf("/") + 1 ), x });
                }
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return finalR;
    }


    /*
        Description
     */
    public JSONObject getAllConcepts() {
        if (this.config.getDebug()) System.out.println("[COEUS][Builder] Getting all the existing concepts on " + this.config.getName());
        JSONObject info = new JSONObject();
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(this.sparqlAPI.select("SELECT ?s ?label ?comment ?title ?description ?hasEntity ?hasResource"
                    + " WHERE { ?s rdf:type coeus:Entity ."
                    + " ?s rdfs:label ?label ."
                    + " ?s rdfs:comment ?comment ."
                    + " ?s dc:description ?description ."
                    + " ?s dc:title ?title ."
                    + "OPTIONAL { ?s coeus:hasEntity ?hasEntity} . "
                    + "OPTIONAL { ?s coeus:hasResource ?hasResource}} " , "js", false));

            JSONObject results = (JSONObject) response.get("results");
            JSONArray bindings = (JSONArray) results.get("bindings");


            for (Object o : bindings) {
                JSONObject binding = (JSONObject) o;
                String uri = ((JSONObject) binding.get("s")).get("value").toString();
                String label = ((JSONObject) binding.get("label")).get("value").toString();
                String comment = ((JSONObject) binding.get("comment")).get("value").toString();
                String description = ((JSONObject) binding.get("description")).get("value").toString();
                String title = ((JSONObject) binding.get("title")).get("value").toString();

                if (!info.containsKey("label")) {
                    info.put("label", label);
                    info.put("uri", uri);
                    info.put("comment", comment);
                    info.put("description", description);
                    info.put("title", title);
                    info.put("hasEntity", "");
                    info.put("hasResource", "");
                }

                JSONObject hasResourceJSON = (JSONObject) binding.get("hasResource");
                JSONObject hasEntityJSON = (JSONObject) binding.get("hasEntity");

                if (hasResourceJSON != null) {
                    String x = hasResourceJSON.get("value").toString();
                    ((JSONArray) info.get("hasResource")).add(new String[]{ x.substring( x.lastIndexOf("/") + 1 ), x });
                }

                if (hasEntityJSON != null) {
                    String x = hasEntityJSON.get("value").toString();
                    ((JSONArray) info.get("hasEntity")).add(new String[]{ x.substring( x.lastIndexOf("/") + 1 ), x });
                }
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return info;
    }


    /*
        Description
     */
    private JSONArray getAllResources() {
        JSONArray finalR = new JSONArray();
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(this.sparqlAPI.select("SELECT ?s ?resof ?method ?comment ?label ?title ?built ?publisher ?extends ?extension ?order ?endpoint ?built ?query ?regex ?identifiers ?line WHERE { ?s rdf:type coeus:Resource ."
                    + " ?s rdfs:comment ?comment ."
                    + " ?s rdfs:label ?label ."
                    + " ?s dc:title ?title ."
                    + " ?s dc:publisher ?publisher ."
                    + " ?s coeus:isResourceOf ?resof ."
                    + " ?s coeus:extends ?extends ."
                    + " ?s coeus:method ?method ."
                    + " ?s coeus:endpoint ?endpoint ."
                    + " ?s coeus:order ?order . "
                    + "OPTIONAL { ?s coeus:built ?built} . "
                    + "OPTIONAL { ?s coeus:line ?line} . "
                    + "OPTIONAL { ?s coeus:identifiers ?identifiers} . "
                    + "OPTIONAL { ?s coeus:regex ?regex} . "
                    + "OPTIONAL { ?s coeus:extension ?extension} . "
                    + "OPTIONAL {?s coeus:query ?query}} "
                    + "ORDER BY ?order", "js", false));
            JSONObject results = (JSONObject) response.get("results");
            JSONArray bindings = (JSONArray) results.get("bindings");

            for (Object o : bindings) {
                JSONObject info = new JSONObject();

                JSONObject binding = (JSONObject) o;

                info.put("s", ((JSONObject) binding.get("s")).get("value").toString());
                info.put("comment", ((JSONObject) binding.get("comment")).get("value").toString());
                info.put("label", ((JSONObject) binding.get("label")).get("value").toString());
                info.put("title", ((JSONObject) binding.get("title")).get("value").toString());
                info.put("resof", ((JSONObject) binding.get("resof")).get("value").toString());
                info.put("publisher", ((JSONObject) binding.get("publisher")).get("value").toString());
                info.put("extends", ((JSONObject) binding.get("extends")).get("value").toString());
                info.put("endpoint", ((JSONObject) binding.get("endpoint")).get("value").toString());
                info.put("order", ((JSONObject) binding.get("order")).get("value").toString());
                info.put("method", ((JSONObject) binding.get("method")).get("value").toString());

                JSONObject extension = (JSONObject) binding.get("extension");
                JSONObject built = (JSONObject) binding.get("built");
                JSONObject query = (JSONObject) binding.get("query");
                JSONObject regex = (JSONObject) binding.get("regex");
                JSONObject identifiers = (JSONObject) binding.get("identifiers");

                if (extension != null) info.put("extension", extension.get("value").toString());
                else info.put("extension", "");

                if (built != null) info.put("built", Boolean.parseBoolean(built.get("value").toString()));
                else info.put("built", false);

                if (query != null) info.put("query", query.get("value").toString());
                else info.put("query", "");

                if (regex != null) info.put("regex", regex.get("value").toString());
                else info.put("regex", "");

                if (identifiers != null) info.put("identifiers", identifiers.get("value").toString());
                else info.put("identifiers", "");

                finalR.add(info);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return finalR;
    }


    /*
        Get labels needed in forms: entities_labels, concept_labels, resources_labels, plugins_labels.
        For now, the plugins_labels are going to be hardcoded, since the current ontology is not yet ready.
     */
    public JSONObject getFormLabels(){
        if (this.config.getDebug()) System.out.println("[COEUS][Builder] Getting labels needed in forms of " + this.config.getName() + "Admin");
        JSONObject finalR = new JSONObject();
        try
        {
            JSONArray entities = performSimpleQuery("SELECT ?s ?label WHERE { ?s rdf:type coeus:Entity . ?s rdfs:label ?label }");
            JSONArray entitiesLabels = new JSONArray();
            for (Object o : entities) {
                JSONObject binding = (JSONObject) o;
                entitiesLabels.add(((JSONObject) binding.get("label")).get("value").toString());
            }

            JSONArray concepts = performSimpleQuery("SELECT ?s ?label WHERE { ?s rdf:type coeus:Concept . ?s rdfs:label ?label }");
            JSONArray conceptsLabels = new JSONArray();
            for (Object o : concepts) {
                JSONObject binding = (JSONObject) o;
                conceptsLabels.add(((JSONObject) binding.get("label")).get("value").toString());
            }

            JSONArray resource = performSimpleQuery("SELECT ?s ?label WHERE { ?s rdf:type coeus:Resource . ?s rdfs:label ?label }");
            JSONArray resourcesLabels = new JSONArray();
            for (Object o : resource) {
                JSONObject binding = (JSONObject) o;
                resourcesLabels.add(((JSONObject) binding.get("label")).get("value").toString());
            }

            // Has to be changed
            JSONArray pluginsLabels = new JSONArray();
            pluginsLabels.add("OMIM");
            pluginsLabels.add("CSV");

            finalR.put("entitiesLabels", entitiesLabels);
            finalR.put("conceptsLabels", conceptsLabels);
            finalR.put("resourcesLabels", resourcesLabels);
            finalR.put("pluginsLabels", pluginsLabels);
        }
        catch (Exception e)  { e.printStackTrace(); }
        return finalR;
    }


    /*
        The idea is to organize all the entities, concepts and resources into a single object, mapping the relations between them.
        This map is then used in DiseasecardAdmin platform.
     */
    public void getOntologyStructure() {
        JSONObject entities = this.getAllEntities();
        JSONObject concepts = this.getAllConcepts();
        JSONArray resources = this.getAllResources();


    }



    /*
        Description
     */
    private JSONArray performSimpleQuery(String query) {
        JSONArray bindings = new JSONArray();
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(this.sparqlAPI.select( query , "js", false));

            JSONObject results = (JSONObject) response.get("results");
            bindings = (JSONArray) results.get("bindings");
        }
        catch (Exception e)  { e.printStackTrace(); }
        return bindings;
    }

    /*
        Description
     */
    private void addEntity() {

    }


    /*
        Description
     */
    private void addConcept() {

    }


    /*
        Description
     */
    private void addResource() {

    }

}