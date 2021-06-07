package pt.ua.diseasecard.components.data;

import com.hp.hpl.jena.rdf.model.Statement;
import au.com.bytecode.opencsv.CSVReader;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import org.springframework.core.io.ResourceLoader;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import pt.ua.diseasecard.service.DataManagementService;
import pt.ua.diseasecard.utils.Predicate;
import pt.ua.diseasecard.utils.PrefixFactory;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import javax.annotation.PostConstruct;
import javax.swing.plaf.nimbus.State;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


@Component
public class Storage {

    private Store store;
    private Model model;
    private final Reasoner reasoner;
    private InfModel infmodel;
    private ResourceLoader resourceLoader;
    private OntModel ontModel;

    private DiseasecardProperties config;
    private SimpMessagingTemplate template;
    private SparqlAPI api;

    public Storage(DiseasecardProperties diseasecardProperties, ResourceLoader resourceLoader, SimpMessagingTemplate template, SparqlAPI api) {
        Objects.requireNonNull(diseasecardProperties);
        this.config = diseasecardProperties;
        this.reasoner = ReasonerRegistry.getTransitiveReasoner();
        this.resourceLoader = resourceLoader;
        this.template = template;
        this.api = api;
    }

    @PostConstruct()
    public void init() throws IOException {
        connect();
        loadPredicates();
    }


    private void connect()  {
        try {
            //this.store = SDBFactory.connectStore(ResourceUtils.getFile("classpath:configuration/" + this.config.getSdb()).getPath() );
            this.store = SDBFactory.connectStore("/configuration/" + this.config.getSdb());

            this.model = SDBFactory.connectDefaultModel(store);
            this.infmodel = ModelFactory.createInfModel(reasoner, model);
            this.ontModel = ModelFactory.createOntologyModel();

            this.api.setInferredModel(infmodel);
            this.api.setModel(model);

            if (this.config.getDebug()) {
                System.out.println("[Diseasecard][Storage] Successfully connected to Diseasecard SDB");
            }
        } catch (Exception ex) {
            if (this.config.getDebug()) {
                System.out.println("[Diseasecard][Storage] Unable to connect to Diseasecard SDB");
                Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    private void loadPredicates() {
        try {
            CSVReader predicatesFile = new CSVReader(new InputStreamReader(resourceLoader.getResource ("classpath:configuration/" + this.config.getPredicates()).getInputStream()));
            String[] nextLine;
            while ((nextLine = predicatesFile.readNext()) != null) {
                if (!(nextLine[0].indexOf("#") == 0)) {
                    Predicate.add(PrefixFactory.encode(nextLine[0]), this.model.getProperty(nextLine[0]));
                }
                //this.predicates.put(PrefixFactory.encode(nextLine[0]), this.model.getProperty(nextLine[0]));
            }
            if (this.config.getDebug()) System.out.println("[Diseasecard][Storage] Successfully loaded predicates");

        } catch (Exception ex) {
            if (this.config.getDebug()) {
                System.out.println("[Diseasecard][API] Unable to read Predicates File select");
                Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    public Map<String, String> loadSetup(InputStream stream) {
        Map<String, String> newEndpoints = new HashMap<>();

        try {
            RDFReader r = this.model.getReader();
            r.read(this.model, stream, PrefixFactory.getURIForPrefix(this.config.getKeyprefix()));

            Property endpoint = this.model.getProperty(this.config.getPrefixes().get("coeus") + "endpoint");
            Property name = this.model.getProperty(this.config.getPrefixes().get("rdfs") + "label");

            ResIterator iter = this.model.listSubjectsWithProperty(endpoint);
            while (iter.hasNext())
            {
                com.hp.hpl.jena.rdf.model.Resource res = iter.nextResource();
                String originalEndpoint = res.getProperty(endpoint).getString();
                String label = res.getProperty(name).getString();

                if (originalEndpoint.contains("hgnc")) { newEndpoints.put(label, originalEndpoint); }
                else if (originalEndpoint.contains("omim")) { newEndpoints.put(label, originalEndpoint); }
                else if (!originalEndpoint.contains("http"))
                {
                    res.removeAll(endpoint);
                    res.addProperty(endpoint, "submittedFiles/endpoints/" + label);
                    newEndpoints.put(label, originalEndpoint);
                }
            }

            this.createBuiltStatus();

            if (this.config.getDebug()) {
                Logger.getLogger(Storage.class.getName()).log(Level.INFO,"[COEUS][Storage] " + this.config.getName() + " setup loaded");
            }
        } catch (Exception ex) {
            if (this.config.getDebug()) {
                Logger.getLogger(Storage.class.getName()).log(Level.INFO,"[COEUS][Storage] Unable to load " + this.config.getName() + " setup");
                Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.setBuildPhase("BuildReady");
        return newEndpoints;
    }


    /*
        This function allows to verify if the model is empty.
        It's used to assure that when the user decides to add new statements (entities, concepts or resources) the model has the
        necessary properties.
        If the model is empty a default ontology is uploaded.
     */
    private void prepareModel() throws IOException {
        if (this.model.isEmpty()) {
            // Upload Default Ontology
            InputStreamReader stream = new InputStreamReader(resourceLoader.getResource("classpath:configuration/default_setup.rdf").getInputStream());

            RDFReader r = this.model.getReader();
            r.read(this.model, stream, PrefixFactory.getURIForPrefix(this.config.getKeyprefix()));

            this.createBuiltStatus();

            if (this.config.getDebug()) Logger.getLogger(Storage.class.getName()).log(Level.INFO,"[COEUS][Storage] " + this.config.getName() + " default setup loaded");
        }
    }


    private void createBuiltStatus() {
        Resource resource = this.model.getResource(this.config.getPrefixes().get("diseasecard") + "builtStatus");

        System.out.println("RESOURCE: " + resource);

        if (!this.model.containsResource(resource))
        {
            resource = this.model.createResource(this.config.getPrefixes().get("diseasecard") + "builtStatus");
            this.setBuildPhase("BuildReady");
            System.out.println("NEW BUILT STATUS: " + resource);
        }
    }


    /*
        Pretty sure that exists a easier way to get this information
     */
    public String getSeedURI() {
        Property type = this.model.getProperty(this.config.getPrefixes().get("rdf") + "type");
        Property seed = this.model.getProperty(this.config.getPrefixes().get("coeus") + "Seed");

        StmtIterator iter = model.listStatements( new SimpleSelector(null, type, seed) { public boolean selects(Statement s) { return true; }});

        if (iter.hasNext()) {
            return iter.nextStatement().getSubject().toString();
        }

        if (this.config.getDebug()) Logger.getLogger(Storage.class.getName()).log(Level.INFO,"[COEUS][Storage] Successfully extracted11 " + this.config.getName() + " default setup loaded");

        return null;
    }


    /*
        TODO: O entity of pode ser um array!! A entity pode ter multiplos "entity of" logo no ínicio..
     */
    public void addEntity(String title, String label, String description, String entityOf)  {
        try {
            this.prepareModel();

            Resource newEntity = this.model.createResource( this.config.getPrefixes().get("diseasecard") + label );
            this.addCore(newEntity, title, label, description, "Entity");

            Property isIncludedInProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "isIncludedIn");
            Resource seed = this.model.getResource(this.getSeedURI());

            newEntity.addProperty(isIncludedInProperty, seed);

            if ( !entityOf.equals("") ) {
                Property entityOfProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "isEntityOf");
                Property hasEntityProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "hasEntity");

                Resource concept = this.model.getResource(entityOf);

                concept.addProperty(hasEntityProperty, newEntity);
                newEntity.addProperty(entityOfProperty, concept);
            }

            System.out.println(newEntity);
        } catch (IOException ex) {
            if (this.config.getDebug()) {
                Logger.getLogger(Storage.class.getName()).log(Level.INFO,"[COEUS][Storage] Error while preparing model");
                Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    public void addConcept(String title, String label, String description, String relatedEntity, String relatedResource)  {
        Resource newConcept = this.model.createResource( this.config.getPrefixes().get("diseasecard") + label );
        this.addCore(newConcept, title, label, description, "Concept");

        Property hasEntity = this.model.getProperty(this.config.getPrefixes().get("coeus") + "hasEntity");
        Property entityOfProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "isEntityOf");

        Resource entity = this.model.getResource(relatedEntity);

        entity.addProperty(entityOfProperty, newConcept);
        newConcept.addProperty(hasEntity, entity);

        if ( !relatedResource.equals("") ) {
            Property hasResource = this.model.getProperty(this.config.getPrefixes().get("coeus") + "hasResource");
            Property extendsProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "extends");

            Resource resource = this.model.getResource(relatedResource);

            resource.addProperty(extendsProperty, newConcept);
            newConcept.addProperty(hasResource, resource);
        }
    }


    public void addResource(String title, String label, String description, String resourceOf, String extendsResource, String order, String publisher, String location) {

        Logger.getLogger(Storage.class.getName()).log(Level.INFO,"URI of the new resource: " + this.config.getPrefixes().get("diseasecard") + label);

        Resource newResource = this.model.createResource(this.config.getPrefixes().get("diseasecard") + label);
        this.addCore(newResource, title, label, description, "Resource");

        Property orderProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "order");
        Property publisherProperty = this.model.getProperty(this.config.getPrefixes().get("dc") + "publisher");
        Property endpointProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "endpoint");

        newResource.addProperty(orderProperty, order);
        newResource.addProperty(publisherProperty, publisher);
        newResource.addProperty(endpointProperty, location);

        Property resourceOfProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "isResourceOf");
        Property extendsProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "extends");
        Property hasResourceProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "hasResource");

        Resource conceptResourceOf = this.model.getResource(resourceOf);
        Resource conceptExtends = this.model.getResource(extendsResource);

        newResource.addProperty(resourceOfProperty, conceptResourceOf);
        newResource.addProperty(extendsProperty, conceptExtends);
        conceptResourceOf.addProperty(hasResourceProperty, newResource);

        try {
            api.addStatement(newResource, Predicate.get("coeus:built"), false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.checkBuildConsistence();
    }


    public void addParserCore(String resourceLabel, boolean resourceInfoInAttribute, String resourceInfo, String regexResource, boolean externalResourceInfoInAttribute, String externalResourceInfo, String regexExternalResource){

        Resource resource = this.model.getResource(this.config.getPrefixes().get("diseasecard") + resourceLabel);
        Resource parser = this.model.createResource(this.config.getPrefixes().get("diseasecard") + "parser_" + resourceLabel);

        Property hasParserProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "hasParser");
        Property isParserOfProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "isParserOf");

        resource.addProperty(hasParserProperty, parser);
        parser.addProperty(isParserOfProperty, resource);

        Property resourceInfoProperty;
        if (!resourceInfoInAttribute)   resourceInfoProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "resourceID");
        else                            resourceInfoProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "resourceInfoAttribute");
        parser.addProperty(resourceInfoProperty, resourceInfo);
        System.out.println("ResourceInfoProperty: " + resourceInfoProperty);

        Property externalResourceInfoProperty;
        if (!externalResourceInfoInAttribute)   externalResourceInfoProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "externalResourceID");
        else                                    externalResourceInfoProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "externalResourceInfoAttribute");
        parser.addProperty(externalResourceInfoProperty, externalResourceInfo);


        Property resourceRegexProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "resourceRegex");
        parser.addProperty(resourceRegexProperty, regexResource);


        Property externalResourceRegexProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "externalResourceRegex");
        parser.addProperty(externalResourceRegexProperty, regexExternalResource);

    }


    public void addParserExtra(String resourceLabel, String mainNode, String isMethodByReplace, String uniqueResource, String externalResourceNode, String uniqueExternalResource, String filterBy, String filterValue) {

        Resource parser = this.model.getResource(this.config.getPrefixes().get("diseasecard") + "parser_" + resourceLabel);

        Property mainProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "mainNode");
        Property isMethodByReplaceProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "methodByReplace");
        Property uniqueResourceProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "uniqueResource");
        Property externalResourceNodeProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "externalResourceNode");
        Property uniqueExternalResourceProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "uniqueExternalResource");
        Property filterByProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "filterBy");
        Property filterValueProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "filterValue");

        parser.addProperty(mainProperty, mainNode);
        parser.addProperty(isMethodByReplaceProperty, isMethodByReplace);
        parser.addProperty(uniqueResourceProperty, uniqueResource);
        parser.addProperty(externalResourceNodeProperty, externalResourceNode);
        parser.addProperty(uniqueExternalResourceProperty, uniqueExternalResource);
        parser.addProperty(filterByProperty, filterBy);
        parser.addProperty(filterValueProperty, filterValue);
    }


    public void addOMIMParser(String resourceLabel, String genecardName, String genecardOMIM, String genecardLocation, String genecardGenes, String morbidmapName, String morbidmapGene, String morbidmapOMIM, String morbidmapLocation) {
        Resource resource = this.model.getResource(this.config.getPrefixes().get("diseasecard") + resourceLabel);
        Resource parser = this.model.createResource(this.config.getPrefixes().get("diseasecard") + "parser_" + resourceLabel);

        resource.addProperty(Predicate.get("coeus:hasParser"), parser);
        parser.addProperty(Predicate.get("coeus:isParserOf"), resource);

        parser.addProperty(Predicate.get("coeus:genemap_cName"), genecardName);
        parser.addProperty(Predicate.get("coeus:genemap_cOMIM"), genecardOMIM);
        parser.addProperty(Predicate.get("coeus:genemap_cLocation"), genecardLocation);
        parser.addProperty(Predicate.get("coeus:genemap_cGenes"), genecardGenes);
        parser.addProperty(Predicate.get("coeus:morbidmap_cName"), morbidmapName);
        parser.addProperty(Predicate.get("coeus:morbidmap_cOMIM"), morbidmapOMIM);
        parser.addProperty(Predicate.get("coeus:morbidmap_cLocation"), morbidmapLocation);
        parser.addProperty(Predicate.get("coeus:morbidmap_cGenes"), morbidmapGene);
    }


    public void addSourceBaseURL(String s, String baseURL) {
        if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[Diseasecard][DataManagementService] Add Source Base URL with " + s );

        Resource newSourceBaseURL = this.model.createResource(this.config.getPrefixes().get("diseasecard") + s);
        Resource type = this.model.getResource(this.config.getPrefixes().get("coeus") + "SourceBaseURL");
        newSourceBaseURL.addProperty(Predicate.get("rdf:type"), type);
        newSourceBaseURL.addProperty(Predicate.get("coeus:baseURL"), baseURL);
        newSourceBaseURL.addProperty(Predicate.get("rdfs:label"), s);
    }


    public void editEntity(String uri, Map<String, String> propertiesToUpdate) {
        Resource entity = this.model.getResource(uri);

        for(Map.Entry<String,String> entry : propertiesToUpdate.entrySet()) {
            String propertyLabel = entry.getKey();

            if ( propertyLabel.contains("_replace") ) {
                Property propertyIsEntityOf  = this.model.getProperty(this.config.getPrefixes().get("coeus") + "isEntityOf");
                Property propertyHasEntity = this.model.getProperty(this.config.getPrefixes().get("coeus") + "hasEntity");

                this.editingCrossProperties(entity, propertyIsEntityOf, entry.getValue(), propertyHasEntity);
            }
            else {
                Property property = this.model.getProperty(this.config.getPrefixes().get(this.getPropertyPrefix(propertyLabel)) + propertyLabel);
                entity.removeAll(property);
                entity.addProperty(property, entry.getValue());
            }
        }
    }


    public void editConcept(String uri, Map<String, String> propertiesToUpdate) {
        Resource concept = this.model.getResource(uri);

        System.out.println(propertiesToUpdate);

        for(Map.Entry<String,String> entry : propertiesToUpdate.entrySet()) {
            String propertyLabel = entry.getKey();

            if (propertyLabel.equals("extendedEntityLabel")) {
                Property hasEntityProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "hasEntity");
                Property isEntityOfProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "isEntityOf");

                Resource oldEntity = concept.getPropertyResourceValue(hasEntityProperty);
                this.model.remove(oldEntity, isEntityOfProperty, concept);

                StmtIterator l = oldEntity.listProperties();
                System.out.println("\nOLD ENTITY PROPERTIES: ");
                while (l.hasNext()) {
                    System.out.println("- " + l.nextStatement().toString());
                }


                Resource entity = this.model.getResource(this.config.getPrefixes().get("diseasecard") + entry.getValue());
                entity.addProperty(isEntityOfProperty, concept);

                // Note: In this case we're removing all properties of this type, but it's irrelevant since the concept can only have one entity.
                concept.removeAll(hasEntityProperty);
                concept.addProperty(hasEntityProperty, entity);

                l = concept.listProperties();
                System.out.println("\nCONCEPT: ");
                while (l.hasNext()) {
                    System.out.println("- " + l.nextStatement().toString());
                }
            }
            else if (propertyLabel.equals("relatedResourceLabel")) {
                Property hasResourceProperty = this.model.getProperty(this.config.getPrefixes().get(this.getPropertyPrefix(propertyLabel)) + "hasResource");
                Property isResourceOfProperty = this.model.getProperty(this.config.getPrefixes().get(this.getPropertyPrefix(propertyLabel)) + "isResourceOf");

                this.editingCrossProperties(concept, hasResourceProperty, entry.getValue(), isResourceOfProperty);
            }
            else {
                Property property = this.model.getProperty(this.config.getPrefixes().get(this.getPropertyPrefix(propertyLabel)) + propertyLabel);
                concept.removeAll(property);
                concept.addProperty(property, entry.getValue());
            }
        }
    }


    public void editResource(String uri, Map<String, String> propertiesToUpdate) {
        Resource resource = this.model.getResource(uri);

        for(Map.Entry<String,String> entry : propertiesToUpdate.entrySet()) {
            String propertyLabel = entry.getKey();

            if (propertyLabel.equals("isResourceOf")) {
                Property isResourceOfProperty = this.model.getProperty(this.config.getPrefixes().get(this.getPropertyPrefix(propertyLabel)) + "isResourceOf");
                Property hasResourceProperty = this.model.getProperty(this.config.getPrefixes().get(this.getPropertyPrefix(propertyLabel)) + "hasResource");

                this.editingCrossProperties(resource, isResourceOfProperty, entry.getValue().replace("http://bioinformatics.ua.pt/diseasecard/resource/",""), hasResourceProperty);
            }
            else {
                Property property = this.model.getProperty(this.config.getPrefixes().get(this.getPropertyPrefix(propertyLabel)) + propertyLabel);
                resource.removeAll(property);
                resource.addProperty(property, entry.getValue());
            }
        }
        this.setBuildPhase("Inconsistent");
    }


    public void editSourceBaseURL(String s, String baseURL) {
        Resource sourceBaseURL = this.model.getResource(this.config.getPrefixes().get("diseasecard") + s);
        sourceBaseURL.removeAll(Predicate.get("coeus:baseURL"));
        sourceBaseURL.addProperty(Predicate.get("coeus:baseURL"), baseURL);
    }


    public void removeEntity(String uri) {
        if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Remove Entity with " + uri );

        Resource entity = this.model.getResource(uri);
        Property isEntityOfProperty = this.model.getProperty(this.config.getPrefixes().get(this.getPropertyPrefix("coeus")) + "isEntityOf");

        StmtIterator concepts = entity.listProperties(isEntityOfProperty);
        while (concepts.hasNext()) this.removeConcept(concepts.nextStatement().getResource().toString());

        this.model.removeAll(entity, null, (RDFNode) null);
        this.model.removeAll(null, null, entity);
    }


    public void removeConcept(String uri) {
        if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[COEUS][DataManagementService] Remove Concept with " + uri );

        Resource concept = this.model.getResource(uri);
        Property hasResourceProperty = this.model.getProperty(this.config.getPrefixes().get(this.getPropertyPrefix("coeus")) + "hasResource");

        StmtIterator resources = concept.listProperties(hasResourceProperty);
        while (resources.hasNext()) this.removeResource(resources.nextStatement().getResource().toString());

        this.model.removeAll(concept, null, (RDFNode) null);
        this.model.removeAll(null, null, concept);
    }


    public void removeResource(String uri) {
        if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[Diseasecard][DataManagementService] Remove Resource with " + uri );

        Resource resource = this.model.getResource(uri);

        this.model.removeAll(resource, null, (RDFNode) null);
        this.model.removeAll(null, null, resource);

        this.checkBuildConsistence();
        //this.setBuildPhase("Inconsistent");
    }


    private void removeItem(String uri) {
        Resource resource = this.model.getResource(uri);

        this.model.removeAll(resource, null, (RDFNode) null);
        this.model.removeAll(null, null, resource);
    }


    public void removeBuild() {
        if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[Diseasecard][DataManagementService] Starting system unbuild " );

        Resource item = this.model.getResource(PrefixFactory.getURIForPrefix(this.config.getKeyprefix()) + "Item");

        StmtIterator iter = this.model.listStatements(null, Predicate.get("rdf:type"), item);
        while (iter.hasNext()) {
            String resource = iter.nextStatement().getSubject().toString();
            try { this.removeItem(resource); }
            catch (Exception e) { System.out.println(e); }
        }

        this.removeBuiltFlag();
    }


    public void setBuildPhase(String phase) {
        Resource type = this.model.getResource(this.config.getPrefixes().get("diseasecard") + "builtStatus");

        StmtIterator iter = type.listProperties(Predicate.get("coeus:systemBuiltPhase"));
        String value = "";
        while (iter.hasNext()) {
            Statement statement = iter.nextStatement();
            value = statement.getObject().toString();
        }

        if (!value.equals(phase)) {
            type.removeAll(Predicate.get("coeus:systemBuiltPhase"));
            type.addProperty(Predicate.get("coeus:systemBuiltPhase"), phase);

            template.convertAndSend("/topic/message", phase);
        }
    }


    private void removeBuiltFlag()  {
        if (this.config.getDebug()) Logger.getLogger(DataManagementService.class.getName()).log(Level.INFO,"[Diseasecard][DataManagementService] Updating Build property " );

        Resource resourceType = this.model.getResource(this.config.getPrefixes().get("coeus") + "Resource");
        StmtIterator iter = this.model.listStatements(null, Predicate.get("rdf:type"), resourceType);

        while (iter.hasNext()) {
            Statement statement = iter.nextStatement();

            try {
                Resource resource = this.model.getResource(statement.getSubject().toString());
                Statement statementToRemove = api.getModel().createLiteralStatement(resource, Predicate.get("coeus:built"), true);
                api.removeStatement(statementToRemove);
                api.addStatement(resource, Predicate.get("coeus:built"), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void addCore(Resource resource, String title, String label, String description, String type) {
        Property labelProperty = this.model.getProperty(this.config.getPrefixes().get("rdfs") + "label");
        Property typeProperty = this.model.getProperty(this.config.getPrefixes().get("rdf") + "type");
        Property titleProperty = this.model.getProperty(this.config.getPrefixes().get("dc") + "title");
        Property descriptionProperty = this.model.getProperty(this.config.getPrefixes().get("dc") + "description");

        Resource typeR = this.model.getResource(this.config.getPrefixes().get("coeus") + type);

        resource.addProperty(labelProperty, label);
        resource.addProperty(titleProperty, title);
        resource.addProperty(descriptionProperty, description);
        resource.addProperty(typeProperty, typeR);
    }


    private void editingCrossProperties(Resource mainInstance, Property mainInstanceProperty, String values, Property supportInstanceProperty) {
        // Remove from concept the 'hasEntity' values
        StmtIterator list = mainInstance.listProperties(mainInstanceProperty);
        while (list.hasNext()) {
            Resource r = this.model.getResource(list.nextStatement().getResource().toString());
            this.model.remove(r, supportInstanceProperty, mainInstance);
        }

        // Remove from resource all 'isEntityOf' values
        mainInstance.removeAll(mainInstanceProperty);

        // Add to resource and concept the proper properties
        for (String label : values.split(",")) {
            Resource instance = this.model.getResource(this.config.getPrefixes().get("diseasecard") + label);
            instance.addProperty(supportInstanceProperty, mainInstance);
            mainInstance.addProperty(mainInstanceProperty, instance);
        }
    }


    private String getPropertyPrefix(String propertyLabel) {
        for (Map.Entry<String, ArrayList<String>> entry : this.config.getProperties().entrySet())
        {
            for (String label : entry.getValue()) if (label.equals(propertyLabel)) return entry.getKey();
        }
        return "coeus";
    }


    private void checkBuildConsistence() {
        Resource resourceType = this.model.getResource(this.config.getPrefixes().get("coeus") + "Resource");
        StmtIterator iter = this.model.listStatements(null, Predicate.get("rdf:type"), resourceType);
        Property builtProperty = Predicate.get("coeus:built");

        Set<Boolean> flags = new HashSet<>();

        while (iter.hasNext()) {
            Statement statement = iter.nextStatement();
            Resource resource = statement.getSubject();
            flags.add(resource.getProperty(builtProperty).getObject().asLiteral().getBoolean());
        }
        System.out.println("flags: " + flags);
        if (flags.size() > 1) this.setBuildPhase("Inconsistent");
    }


    public Model getModel() {
        return model;
    }
    public void setModel(Model model) {
        this.model = model;
    }
}
