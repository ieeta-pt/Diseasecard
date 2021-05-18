package pt.ua.diseasecard.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pt.ua.diseasecard.components.Boot;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.components.data.Storage;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import pt.ua.diseasecard.configuration.OntologyProperties;
import pt.ua.diseasecard.connectors.CSVFactory;
import pt.ua.diseasecard.connectors.PluginFactory;
import pt.ua.diseasecard.connectors.ResourceFactory;
import pt.ua.diseasecard.connectors.XMLFactory;
import pt.ua.diseasecard.domain.Concept;
import pt.ua.diseasecard.domain.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DataManagementService {

    private String uploadDir;
    private Storage storage;
    private DiseasecardProperties config;
    private OntologyProperties ontologyProperties;
    private ArrayList<Resource> resources;
    private SparqlAPI sparqlAPI;

    private AutowireCapableBeanFactory beanFactory;
    private Boot boot;

    public DataManagementService(Storage storage, DiseasecardProperties diseasecardProperties, OntologyProperties ontologyProperties, SparqlAPI sparqlAPI, AutowireCapableBeanFactory beanFactory, Boot boot) {
        this.uploadDir = "submittedFiles";
        this.storage = storage;
        this.config = diseasecardProperties;
        this.ontologyProperties = ontologyProperties;
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

            Map<String, String> newEndpoints = this.storage.loadSetup(file.getInputStream());

            return newEndpoints;
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
        //TODO: Adicionar aqui uma thread
        //this.build();
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
                        if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Already built resource " + r.getTitle());
                    }
                    else
                    {
                        if (this.config.getDebug())  Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Reading data for resource " + r.getTitle());
                        this.readData(r);
                    }
                }
                catch (Exception ex)
                {
                    if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Unable to read data for " + this.config.getName() + " in resource " + r.getTitle());
                    Logger.getLogger(DataManagementService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.boot.startInternalProcess();
        }
        catch (Exception ex)
        {
            if(this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Unable to build " + this.config.getName());
            Logger.getLogger(DataManagementService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /*
        Description
     */
    private void readResources() {
        try {
            if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Reading resources for " + this.config.getName());

            JSONArray finalR = getAllResources();

            for (Object o : finalR) {
                JSONObject info = (JSONObject) o;

                Resource r = new Resource((String) info.get("s"), (String) info.get("title"), (String) info.get("label"), (String) info.get("description"), (String) info.get("publisher"), (String) info.get("endpoint"));
                r.setExtendsConcept((String) info.get("extends"));
                r.setIsResourceOf(new Concept((String) info.get("resof")));
                r.setIdentifiers((String) info.get("identifiers"));
                r.setBuilt((Boolean) info.get("built"));
                r.loadConcept();
                r.loadParser();
                this.resources.add(r);
            }
            if (this.config.getDebug())  Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Resource information read");
        }
        catch (Exception ex)
        {
            if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Unable to read resource information");
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
                    case "xml":
                        factory = new XMLFactory(r);
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
            if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Data for " + r.getTitle() + " read");
        } catch (Exception ex) {
            if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Unable to read data for " + r.getTitle());
            Logger.getLogger(DataManagementService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /*
        Description
     */
    public JSONObject getAllEntities() {
        //if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Getting all the existing entities on " + this.config.getName());
        JSONObject finalR = new JSONObject();
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(this.sparqlAPI.select("SELECT ?s ?label ?title ?description ?isEntityOf ?isIncludedIn"
                    + " WHERE { ?s rdf:type coeus:Entity ."
                    + " ?s rdfs:label ?label ."
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
                String description = ((JSONObject) binding.get("description")).get("value").toString();
                String title = ((JSONObject) binding.get("title")).get("value").toString();
                String isIncludedIn = ((JSONObject) binding.get("isIncludedIn")).get("value").toString();

                if (!finalR.containsKey(uri)) {
                    finalR.put(uri, new JSONObject());
                    ((JSONObject) finalR.get(uri)).put("label", label);
                    ((JSONObject) finalR.get(uri)).put("uri", uri);
                    ((JSONObject) finalR.get(uri)).put("description", description);
                    ((JSONObject) finalR.get(uri)).put("title", title);
                    ((JSONObject) finalR.get(uri)).put("isIncludedIn", isIncludedIn);
                    ((JSONObject) finalR.get(uri)).put("isEntityOf", new JSONArray());
                }

                JSONObject isEntityOfJSON = (JSONObject) binding.get("isEntityOf");

                if (isEntityOfJSON != null) {
                    ((JSONArray) ((JSONObject) finalR.get(uri)).get("isEntityOf")).add(isEntityOfJSON.get("value").toString());
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
        //if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Getting all the existing concepts on " + this.config.getName());
        JSONObject finalR = new JSONObject();
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(this.sparqlAPI.select("SELECT ?s ?label ?title ?description ?hasEntity ?hasResource"
                    + " WHERE { ?s rdf:type coeus:Concept ."
                    + " ?s rdfs:label ?label ."
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
                String description = ((JSONObject) binding.get("description")).get("value").toString();
                String title = ((JSONObject) binding.get("title")).get("value").toString();
                String hasEntity = ((JSONObject) binding.get("hasEntity")).get("value").toString();

                if (!finalR.containsKey(uri)) {
                    finalR.put(uri, new JSONObject());

                    ((JSONObject) finalR.get(uri)).put("label", label);
                    ((JSONObject) finalR.get(uri)).put("uri", uri);
                    ((JSONObject) finalR.get(uri)).put("description", description);
                    ((JSONObject) finalR.get(uri)).put("title", title);
                    ((JSONObject) finalR.get(uri)).put("title", title);
                    ((JSONObject) finalR.get(uri)).put("hasEntity", hasEntity);
                    ((JSONObject) finalR.get(uri)).put("hasResource", new JSONArray());
                }

                JSONObject hasResourceJSON = (JSONObject) binding.get("hasResource");

                if (hasResourceJSON != null) {
                    ((JSONArray) ((JSONObject) finalR.get(uri)).get("hasResource")).add(hasResourceJSON.get("value").toString());
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
    private JSONArray getAllResources() {
        JSONArray finalR = new JSONArray();
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(this.sparqlAPI.select("SELECT ?s ?resof ?description ?label ?title ?built ?publisher ?extends ?extension ?order ?endpoint ?built"
                    + " WHERE { ?s rdf:type coeus:Resource ."
                    + " ?s dc:description ?description ."
                    + " ?s rdfs:label ?label ."
                    + " ?s dc:title ?title ."
                    + " ?s dc:publisher ?publisher ."
                    + " ?s coeus:isResourceOf ?resof ."
                    + " ?s coeus:extends ?extends ."
                    + " ?s coeus:endpoint ?endpoint ."
                    + " ?s coeus:order ?order . "
                    + "OPTIONAL { ?s coeus:built ?built} . "
                    + "OPTIONAL { ?s coeus:identifiers ?identifiers}} "
                    + "ORDER BY ?order", "js", false));
            JSONObject results = (JSONObject) response.get("results");
            JSONArray bindings = (JSONArray) results.get("bindings");

            for (Object o : bindings) {
                JSONObject info = new JSONObject();

                JSONObject binding = (JSONObject) o;

                info.put("s", ((JSONObject) binding.get("s")).get("value").toString());
                info.put("description", ((JSONObject) binding.get("description")).get("value").toString());
                info.put("label", ((JSONObject) binding.get("label")).get("value").toString());
                info.put("title", ((JSONObject) binding.get("title")).get("value").toString());
                info.put("resof", ((JSONObject) binding.get("resof")).get("value").toString());
                info.put("publisher", ((JSONObject) binding.get("publisher")).get("value").toString());
                info.put("extends", ((JSONObject) binding.get("extends")).get("value").toString());
                info.put("endpoint", ((JSONObject) binding.get("endpoint")).get("value").toString());
                info.put("order", ((JSONObject) binding.get("order")).get("value").toString());

                JSONObject built = (JSONObject) binding.get("built");
                JSONObject identifiers = (JSONObject) binding.get("identifiers");

                if (built != null) info.put("built", Boolean.parseBoolean(built.get("value").toString()));
                else info.put("built", false);

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
        Description
     */
    private JSONObject getResource(String resourceURI) {
        JSONObject resource = new JSONObject();
        try
        {
            JSONParser parser = new JSONParser();

            String queryString = "SELECT * "
                + " WHERE { <" + resourceURI + "> dc:title ?title ."
                + " <" + resourceURI + "> dc:description ?description ."
                + " <" + resourceURI + "> rdfs:label ?label ."
                + " <" + resourceURI + "> dc:publisher ?publisher ."
                + " <" + resourceURI + "> coeus:isResourceOf ?resof ."
                + " <" + resourceURI + "> coeus:extends ?extends ."
                + " <" + resourceURI + "> coeus:endpoint ?endpoint ."
                + " <" + resourceURI + "> coeus:order ?order . "
                + "OPTIONAL { <" + resourceURI + "> coeus:built ?built} . "
                + "OPTIONAL { <" + resourceURI + "> coeus:hasParser ?hasParser} . "
                + "OPTIONAL { <" + resourceURI + "> coeus:identifiers ?identifiers}}";

            JSONObject response = (JSONObject) parser.parse(this.sparqlAPI.select(queryString, "js", false));

            JSONObject results = (JSONObject) response.get("results");
            JSONArray bindings = (JSONArray) results.get("bindings");
            try {
                JSONObject a = (JSONObject) bindings.get(0);
                resource.put("description", ((JSONObject) a.get("description")).get("value").toString());
                resource.put("label", ((JSONObject) a.get("label")).get("value").toString());
                resource.put("title", ((JSONObject) a.get("title")).get("value").toString());
                resource.put("publisher", ((JSONObject) a.get("publisher")).get("value").toString());
                resource.put("resof", ((JSONObject) a.get("resof")).get("value").toString());
                resource.put("extends", ((JSONObject) a.get("extends")).get("value").toString());
                resource.put("endpoint", ((JSONObject) a.get("endpoint")).get("value").toString());
                resource.put("order", ((JSONObject) a.get("order")).get("value").toString());

                JSONObject built = (JSONObject) a.get("built");
                JSONObject identifiers = (JSONObject) a.get("identifiers");
                JSONObject hasParser = (JSONObject) a.get("hasParser");

                resource.put("built", built != null ? Boolean.parseBoolean(built.get("value").toString()) : false);
                resource.put("hasParser", hasParser != null ? hasParser.get("value").toString() : "");
                resource.put("identifiers", identifiers != null ? identifiers.get("value").toString() : "");

            } catch (Exception ex) {}
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return resource;
    }


    /*
        Get labels needed in forms: entities_labels, concept_labels, resources_labels, plugins_labels.
        For now, the plugins_labels are going to be hardcoded, since the current ontology is not yet ready.
     */
    public JSONObject getFormLabels(){
        if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Getting labels needed in forms of " + this.config.getName() + "Admin");
        JSONObject finalR = new JSONObject();
        try
        {
            JSONArray entities = performSimpleQuery("SELECT ?s ?label WHERE { ?s rdf:type coeus:Entity . ?s rdfs:label ?label }");
            JSONObject entitiesLabels = new JSONObject();
            for (Object o : entities) {
                JSONObject binding = (JSONObject) o;
                entitiesLabels.put(((JSONObject) binding.get("s")).get("value").toString(), ((JSONObject) binding.get("label")).get("value").toString());
            }

            JSONArray concepts = performSimpleQuery("SELECT ?s ?label WHERE { ?s rdf:type coeus:Concept . ?s rdfs:label ?label }");
            JSONObject conceptsLabels = new JSONObject();
            for (Object o : concepts) {
                JSONObject binding = (JSONObject) o;
                conceptsLabels.put(((JSONObject) binding.get("s")).get("value").toString(), ((JSONObject) binding.get("label")).get("value").toString());
            }

            JSONArray resource = performSimpleQuery("SELECT ?s ?label WHERE { ?s rdf:type coeus:Resource . ?s rdfs:label ?label }");
            JSONObject resourcesLabels = new JSONObject();
            for (Object o : resource) {
                JSONObject binding = (JSONObject) o;
                resourcesLabels.put(((JSONObject) binding.get("s")).get("value").toString(), ((JSONObject) binding.get("label")).get("value").toString());
            }

            JSONArray orders = performSimpleQuery("SELECT ?s ?order WHERE { ?s coeus:order ?order }");
            TreeSet<Integer> ordersValues = new TreeSet<>();
            for (Object o : orders) {
                JSONObject binding = (JSONObject) o;
                System.out.println(binding);
                ordersValues.add(Integer.parseInt(((JSONObject) binding.get("order")).get("value").toString()));
            }


            if ( ordersValues.size() > 0 )
                for (int i = 0 ; i < ordersValues.last() + 1; i++) {
                    if (!ordersValues.contains(i)) ordersValues.add(i);
                }
            else ordersValues.add(0);


            // Has to be changed
            JSONArray pluginsLabels = new JSONArray();
            this.ontologyProperties.getPluginLabels().forEach((element) -> pluginsLabels.add(element));

            finalR.put("entitiesLabels", entitiesLabels);
            finalR.put("conceptsLabels", conceptsLabels);
            finalR.put("resourcesLabels", resourcesLabels);
            finalR.put("pluginsLabels", pluginsLabels);
            finalR.put("ordersLabels", ordersValues);
        }
        catch (Exception e)  { e.printStackTrace(); }
        return finalR;
    }


    /*
        The idea is to organize all the entities, concepts and resources into a single object, mapping the relations between them.
        This map is then used in DiseasecardAdmin platform.
     */
    public JSONArray getOntologyStructure() {
        if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Getting Ontology Structure of " + this.config.getName() );

        JSONObject entities = this.getAllEntities();
        JSONObject concepts = this.getAllConcepts();

        for (Object key : entities.keySet()) {
            System.out.println(entities.get(key));
            ((JSONObject) entities.get(key)).put("typeOf", "Entity");
            JSONObject entityValues = (JSONObject) entities.get(key);

            JSONArray entityOf = (JSONArray) entityValues.get("isEntityOf");
            JSONArray auxE = new JSONArray();
            for (Object conceptURI : entityOf) {
                try
                {
                    JSONObject conceptValues = (JSONObject) concepts.get(conceptURI);
                    JSONArray resourcesExtending = (JSONArray) conceptValues.get("hasResource");

                    JSONArray auxR = new JSONArray();

                    for (Object resourceURI : resourcesExtending) {
                        JSONObject resource = this.getResource(resourceURI.toString());
                        resource.put("typeOf", "Resource");
                        auxR.add(resource);
                    }
                    ((JSONObject) concepts.get(conceptURI)).put("typeOf", "Concept");
                    ((JSONObject) concepts.get(conceptURI)).replace("hasResource", auxR);
                } catch (Exception ex) { }
                auxE.add(concepts.get(conceptURI));
            }
            ((JSONObject) entities.get(key)).replace("isEntityOf", auxE);
        }

        JSONArray results = new JSONArray();

        entities.forEach( (key,value) -> { results.add(value); } );

        return results;
    }


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


    public void prepareAddEntity(String title, String label, String description, String entityOf) {
        if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Add Entity to " + this.config.getName() );

        this.storage.addEntity(title, label, description, entityOf);
    }


    public void prepareAddConcept(String title, String label, String description, String hasEntity, String hasResource) {
        if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[Diseasecard][DataManagementService] Add Concept to " + this.config.getName() );

        this.storage.addConcept(title, label, description, hasEntity, hasResource);
    }


    public void prepareAddResource(String title, String label, String description, String resourceOf, String extendsResource, String order, String publisher, MultipartFile file) {
        try {
            Path copyLocation = Paths.get(uploadDir + File.separator + "endpoints" + File.separator + StringUtils.cleanPath(label));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            this.storage.addResource(title, label, description, resourceOf, extendsResource, order, publisher, copyLocation.toString());
        } catch (IOException ex) {
            Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Error while processing endpoint of resource");
        }
    }


    public void prepareAddResource(String title, String label, String description, String resourceOf, String extendsResource, String order, String publisher, String endpoint) {
        if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[Diseasecard][DataManagementService] Add Resource to " + this.config.getName() );
        this.storage.addResource(title, label, description, resourceOf, extendsResource, order, publisher, endpoint);
    }


    public void prepareAddParser(String resource, String resourceID, String regexResource, String externalResourceID, String regexExternalResource) {
        if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[Diseasecard][DataManagementService] Add Parser to " + resource );

        this.storage.addParserCore(resource, false, resourceID, regexResource, false, externalResourceID, regexExternalResource);
    }


    public void prepareAddParser(String resource, String mainNode, String isMethodByReplace, String resourceInfoInAttribute, String resourceInfo, String uniqueResource, String regexResource, String externalResourceInfoInAttribute, String externalResourceInfo, String externalResourceNode, String regexExternalResource, String uniqueExternalResource,String filterBy, String filterValue ) {
        if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[Diseasecard][DataManagementService] Add Parser to " + resource );

        this.storage.addParserCore(resource, Boolean.parseBoolean(resourceInfoInAttribute), resourceInfo, regexResource, Boolean.parseBoolean(externalResourceInfoInAttribute), externalResourceInfo, regexExternalResource);
        this.storage.addParserExtra(resource, mainNode, isMethodByReplace, uniqueResource, externalResourceNode, uniqueExternalResource, filterBy, filterValue);
    }


    public void prepareAddOMIMResource(String title, String label, String description, String resourceOf, String extendsResource, String order, String publisher, MultipartFile morbidmap, MultipartFile genemap) {
        try {
            Path copyLocationGenemap = Paths.get(uploadDir + File.separator + "endpoints" + File.separator + "omim_genemap");
            Files.copy(genemap.getInputStream(), copyLocationGenemap, StandardCopyOption.REPLACE_EXISTING);

            Path copyLocationMorbidmap = Paths.get(uploadDir + File.separator + "endpoints" + File.separator + "omim_morbidmap");
            Files.copy(morbidmap.getInputStream(), copyLocationMorbidmap, StandardCopyOption.REPLACE_EXISTING);

            Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"" + morbidmap.getInputStream());

            this.storage.addResource(title, label, description, resourceOf, extendsResource, order, publisher, "omim://full");
        } catch (IOException ex) {
            Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Error while processing endpoint of resource");
        }
    }
}