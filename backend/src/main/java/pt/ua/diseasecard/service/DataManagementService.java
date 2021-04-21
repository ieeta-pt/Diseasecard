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
import pt.ua.diseasecard.connectors.CSVFactory;
import pt.ua.diseasecard.connectors.PluginFactory;
import pt.ua.diseasecard.connectors.ResourceFactory;
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

                Resource r = new Resource((String) info.get("s"), (String) info.get("title"), (String) info.get("label"), (String) info.get("comment"), (String) info.get("publisher"), (String) info.get("endpoint"), (String) info.get("method"));
                r.setExtendsConcept((String) info.get("extends"));
                r.setIsResourceOf(new Concept((String) info.get("resof")));
                r.setRegex((String) info.get("regex"));
                r.setExtendsIdentifier((String) info.get("extendsIdentifier"));
                r.setExtendsIdentifierRegex((String) info.get("extendsIdentifierRegex"));
                r.setIdentifiers((String) info.get("identifiers"));
                r.setExtension((String) info.get("extension"));
                r.setQuery((String) info.get("query"));
                r.setBuilt((Boolean) info.get("built"));
                r.loadConcept();
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
                String isIncludedIn = ((JSONObject) binding.get("isIncludedIn")).get("value").toString();

                if (!finalR.containsKey(uri)) {
                    finalR.put(uri, new JSONObject());
                    ((JSONObject) finalR.get(uri)).put("label", label);
                    ((JSONObject) finalR.get(uri)).put("uri", uri);
                    ((JSONObject) finalR.get(uri)).put("comment", comment);
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
            JSONObject response = (JSONObject) parser.parse(this.sparqlAPI.select("SELECT ?s ?label ?comment ?title ?description ?hasEntity ?hasResource"
                    + " WHERE { ?s rdf:type coeus:Concept ."
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
                String hasEntity = ((JSONObject) binding.get("hasEntity")).get("value").toString();

                if (!finalR.containsKey(uri)) {
                    finalR.put(uri, new JSONObject());

                    ((JSONObject) finalR.get(uri)).put("label", label);
                    ((JSONObject) finalR.get(uri)).put("uri", uri);
                    ((JSONObject) finalR.get(uri)).put("comment", comment);
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
            JSONObject response = (JSONObject) parser.parse(this.sparqlAPI.select("SELECT ?s ?resof ?method ?comment ?label ?title ?built ?publisher ?extends ?extension ?order ?endpoint ?built ?query ?regex ?identifiers ?line ?extendsIdentifier ?extendsIdentifierRegex"
                    + " WHERE { ?s rdf:type coeus:Resource ."
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
                    + "OPTIONAL { ?s coeus:extendsIdentifier ?extendsIdentifier} . "
                    + "OPTIONAL { ?s coeus:extendsIdentifierRegex ?extendsIdentifierRegex} . "
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
                JSONObject extendsIdentifier = (JSONObject) binding.get("extendsIdentifier");
                JSONObject extendsIdentifierRegex = (JSONObject) binding.get("extendsIdentifierRegex");
                JSONObject identifiers = (JSONObject) binding.get("identifiers");

                if (extension != null) info.put("extension", extension.get("value").toString());
                else info.put("extension", "");

                if (built != null) info.put("built", Boolean.parseBoolean(built.get("value").toString()));
                else info.put("built", false);

                if (query != null) info.put("query", query.get("value").toString());
                else info.put("query", "");

                if (regex != null) info.put("regex", regex.get("value").toString());
                else info.put("regex", "");

                if (extendsIdentifier != null) info.put("extendsIdentifier", extendsIdentifier.get("value").toString());
                else info.put("extendsIdentifier", "");

                if (extendsIdentifierRegex != null) info.put("extendsIdentifierRegex", extendsIdentifierRegex.get("value").toString());
                else info.put("extendsIdentifierRegex", "");

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
                + " <" + resourceURI + "> rdfs:comment ?comment ."
                + " <" + resourceURI + "> dc:description ?description ."
                + " <" + resourceURI + "> rdfs:label ?label ."
                + " <" + resourceURI + "> dc:publisher ?publisher ."
                + " <" + resourceURI + "> coeus:isResourceOf ?resof ."
                + " <" + resourceURI + "> coeus:extends ?extends ."
                + " <" + resourceURI + "> coeus:endpoint ?endpoint ."
                + " <" + resourceURI + "> coeus:order ?order . "
                + "OPTIONAL { <" + resourceURI + "> coeus:method ?method} . "
                + "OPTIONAL { <" + resourceURI + "> coeus:built ?built} . "
                + "OPTIONAL { <" + resourceURI + "> coeus:identifiers ?identifiers} . "
                + "OPTIONAL { <" + resourceURI + "> coeus:regex ?regex} . "
                + "OPTIONAL { <" + resourceURI + "> coeus:query ?query}} ";

            JSONObject response = (JSONObject) parser.parse(this.sparqlAPI.select(queryString, "js", false));

            JSONObject results = (JSONObject) response.get("results");
            JSONArray bindings = (JSONArray) results.get("bindings");

            try {
                JSONObject a = (JSONObject) bindings.get(0);
                resource.put("comment", ((JSONObject) a.get("comment")).get("value").toString());
                resource.put("label", ((JSONObject) a.get("label")).get("value").toString());
                resource.put("title", ((JSONObject) a.get("title")).get("value").toString());
                resource.put("publisher", ((JSONObject) a.get("publisher")).get("value").toString());
                resource.put("resof", ((JSONObject) a.get("resof")).get("value").toString());
                resource.put("extends", ((JSONObject) a.get("extends")).get("value").toString());
                resource.put("method", ((JSONObject) a.get("method")).get("value").toString());
                resource.put("endpoint", ((JSONObject) a.get("endpoint")).get("value").toString());
                resource.put("order", ((JSONObject) a.get("order")).get("value").toString());

                JSONObject built = (JSONObject) a.get("built");
                JSONObject query = (JSONObject) a.get("query");
                JSONObject regex = (JSONObject) a.get("regex");
                JSONObject identifiers = (JSONObject) a.get("identifiers");
                JSONObject method = (JSONObject) a.get("method");

                if (built != null) resource.put("built", Boolean.parseBoolean(built.get("value").toString()));
                else resource.put("built", false);

                if (method != null) resource.put("method", method.get("value").toString());
                else resource.put("method", "");

                if (query != null) resource.put("query", query.get("value").toString());
                else resource.put("query", "");

                if (regex != null) resource.put("regex", regex.get("value").toString());
                else resource.put("regex", "");

                if (identifiers != null) resource.put("identifiers", identifiers.get("value").toString());
                else resource.put("identifiers", "");
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
        //if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Getting labels needed in forms of " + this.config.getName() + "Admin");
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
    public JSONObject getOntologyStructure() {
        //if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Getting Ontology Structure of " + this.config.getName() );

        JSONObject entities = this.getAllEntities();
        JSONObject concepts = this.getAllConcepts();

        for (Object key : entities.keySet()) {
            JSONObject entityValues = (JSONObject) entities.get(key);

            JSONArray entityOf = (JSONArray) entityValues.get("isEntityOf");
            JSONObject auxE = new JSONObject();
            for (Object conceptURI : entityOf) {
                try
                {
                    JSONObject conceptValues = (JSONObject) concepts.get(conceptURI);
                    JSONArray resourcesExtending = (JSONArray) conceptValues.get("hasResource");
                    JSONObject auxR = new JSONObject();

                    for (Object resourceURI : resourcesExtending) {
                        JSONObject resource = this.getResource(resourceURI.toString());
                        auxR.put(resourceURI, resource);
                    }

                    ((JSONObject) concepts.get(conceptURI)).replace("hasResource", auxR);
                } catch (Exception ex) { }
                auxE.put(conceptURI, concepts.get(conceptURI));
            }
            ((JSONObject) entities.get(key)).replace("isEntityOf", auxE);
        }

        return entities;
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


    public void prepareAddEntity(String title, String label, String description, String comment, String entityOf) {
        if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Add Entity to " + this.config.getName() );

        this.storage.addEntity(title, label, description, comment, entityOf);
    }


    public void prepareAddConcept(String title, String label, String description, String comment, String hasEntity, String hasResource) {
        if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Add Concept to " + this.config.getName() );

        this.storage.addConcept(title, label, description, comment, hasEntity, hasResource);
    }


    public void prepareAddResource(String title, String label, String description, String comment, String resourceOf, String extendsResource, String order, String publisher, String regex, String query, MultipartFile file) {
        try {
            Path copyLocation = Paths.get(uploadDir + File.separator + "endpoints" + File.separator + StringUtils.cleanPath(label));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            this.storage.addResource(title, label, description, comment, resourceOf, extendsResource, order, publisher, regex, query, copyLocation.toString());
        } catch (IOException ex) {
            Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Error while processing endpoint of resource");
        }
    }


    public void prepareAddOMIMResource(String title, String label, String description, String comment, String resourceOf, String extendsResource, String order, String publisher, String regex, String query, MultipartFile morbidmap, MultipartFile genemap) {
        try {
            Path copyLocationGenemap = Paths.get(uploadDir + File.separator + "endpoints" + File.separator + "omim_genemap");
            Files.copy(genemap.getInputStream(), copyLocationGenemap, StandardCopyOption.REPLACE_EXISTING);

            Path copyLocationMorbidmap = Paths.get(uploadDir + File.separator + "endpoints" + File.separator + "omim_morbidmap");
            Files.copy(morbidmap.getInputStream(), copyLocationMorbidmap, StandardCopyOption.REPLACE_EXISTING);

            this.storage.addResource(title, label, description, comment, resourceOf, extendsResource, order, publisher, regex, query, "omim://full");
        } catch (IOException ex) {
            Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Error while processing endpoint of resource");
        }
    }
}