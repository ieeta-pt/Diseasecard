package pt.ua.diseasecard.components.data;

import au.com.bytecode.opencsv.CSVReader;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import pt.ua.diseasecard.utils.Predicate;
import pt.ua.diseasecard.utils.PrefixFactory;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


@Component
public class Storage {

    private Store store;
    private Model model;
    private final Reasoner reasoner;
    private InfModel infmodel;
    private ResourceLoader resourceLoader;

    private DiseasecardProperties config;

    public Storage(DiseasecardProperties diseasecardProperties, ResourceLoader resourceLoader) {
        Objects.requireNonNull(diseasecardProperties);
        this.config = diseasecardProperties;
        this.reasoner = ReasonerRegistry.getTransitiveReasoner();
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct()
    public void init() throws IOException {
        connect();
        loadPredicates();
    }


    /*
        Description
     */
    private void connect()  {
        try {
            //this.store = SDBFactory.connectStore(ResourceUtils.getFile("classpath:configuration/" + this.config.getSdb()).getPath() );
            this.store = SDBFactory.connectStore("/configuration/" + this.config.getSdb());

            this.model = SDBFactory.connectDefaultModel(store);
            this.infmodel = ModelFactory.createInfModel(reasoner, model);

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


    /*
        Description
     */
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


    /*
        Description
     */
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

            if (this.config.getDebug()) {
                System.out.println("[COEUS][Storage] " + this.config.getName() + " setup loaded");
            }
        } catch (Exception ex) {
            if (this.config.getDebug()) {
                System.out.println("[COEUS][Storage] Unable to load " + this.config.getName() + " setup");
                Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
            File file = resourceLoader.getResource ("classpath:configuration/default_setup.rdf").getFile();
            InputStream stream = new FileInputStream(file);

            RDFReader r = this.model.getReader();
            r.read(this.model, stream, PrefixFactory.getURIForPrefix(this.config.getKeyprefix()));

            if (this.config.getDebug()) System.out.println("[COEUS][Storage] " + this.config.getName() + " default setup loaded");
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

        if (this.config.getDebug()) System.out.println("[COEUS][Storage] Successfully extracted11 " + this.config.getName() + " default setup loaded");

        return null;
    }


    /*
        Description
        TODO: O entity of pode ser um array!! A entity pode ter multiplos "entity of" logo no ínicio..
     */
    public void addEntity(String title, String label, String description, String comment, String entityOf)  {
        try {
            this.prepareModel();

            Resource newEntity = this.model.createResource( this.config.getPrefixes().get("diseasecard") + label );
            this.addCore(newEntity, title, label, description, comment, "Entity");

            Property isIncludedInProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "isIncludedIn");
            Resource seed = this.model.getResource(this.getSeedURI());
            System.out.println("URI of the seed: " + this.getSeedURI());

            newEntity.addProperty(isIncludedInProperty, seed);

            if ( !entityOf.equals("") ) {
                System.out.println("URI of the concept: " + this.config.getPrefixes().get("diseasecard") + entityOf);

                Property entityOfProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "isEntityOf");
                Property hasEntityProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "hasEntity");

                Resource concept = this.model.getResource(this.config.getPrefixes().get("diseasecard") + entityOf);

                concept.addProperty(hasEntityProperty, newEntity);
                newEntity.addProperty(entityOfProperty, concept);
            }

            System.out.println(newEntity);
        } catch (IOException ex) {
            if (this.config.getDebug()) {
                System.out.println("[COEUS][Storage] Error while preparing model");
                Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    /*
        Description
     */
    public void addConcept(String title, String label, String description, String comment, String relatedEntity, String relatedResource)  {
        try {
            this.prepareModel();

            Resource newConcept = this.model.createResource( this.config.getPrefixes().get("diseasecard") + label );
            this.addCore(newConcept, title, label, description, comment, "Concept");

            Property hasEntity = this.model.getProperty(this.config.getPrefixes().get("coeus") + "hasEntity");
            Property entityOfProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "isEntityOf");

            Resource entity = this.model.getResource(this.config.getPrefixes().get("diseasecard") + relatedEntity);

            entity.addProperty(entityOfProperty, newConcept);
            newConcept.addProperty(hasEntity, entity);

            if ( !relatedResource.equals("") ) {
                Property hasResource = this.model.getProperty(this.config.getPrefixes().get("coeus") + "hasResource");
                Property extendsProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "extends");

                Resource resource = this.model.getResource(this.config.getPrefixes().get("diseasecard") + relatedResource);

                resource.addProperty(extendsProperty, newConcept);
                newConcept.addProperty(hasResource, resource);
            }

        } catch (IOException ex) {
            if (this.config.getDebug()) {
                System.out.println("[COEUS][Storage] Error while preparing model");
                Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    /*
        Description
     */
    public void addResource(String title, String label, String description, String comment, String resourceOf, String extendsResource, String order, String publisher, String regex, String query, String location) {
        try {
            this.prepareModel();
            System.out.println("URI of the new resource: " + this.config.getPrefixes().get("diseasecard") + label);

            Resource newResource = this.model.createResource(this.config.getPrefixes().get("diseasecard") + label);
            this.addCore(newResource, title, label, description, comment, "Resource");

            Property orderProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "order");
            Property publisherProperty = this.model.getProperty(this.config.getPrefixes().get("dc") + "publisher");
            Property regexProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "regex");
            Property queryProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "query");
            Property endpointProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "endpoint");

            newResource.addProperty(orderProperty, order);
            newResource.addProperty(publisherProperty, publisher);
            newResource.addProperty(regexProperty, regex);
            newResource.addProperty(queryProperty, query);

            newResource.addProperty(endpointProperty, location);

            Property resourceOfProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "isResourceOf");
            Property extendsProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "extends");
            Property hasResourceProperty = this.model.getProperty(this.config.getPrefixes().get("coeus") + "hasResource");

            Resource conceptResourceOf = this.model.getResource(this.config.getPrefixes().get("diseasecard") + resourceOf);
            Resource conceptExtends = this.model.getResource(this.config.getPrefixes().get("diseasecard") + extendsResource);

            newResource.addProperty(resourceOfProperty, conceptResourceOf);
            newResource.addProperty(extendsProperty, conceptExtends);
            conceptResourceOf.addProperty(hasResourceProperty, newResource);

        } catch (IOException ex) {
            if (this.config.getDebug()) {
                System.out.println("[COEUS][Storage] Error while preparing model");
                Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    /*
        Description
     */
    private void addCore(Resource resource, String title, String label, String description, String comment, String type) {
        Property labelProperty = this.model.getProperty(this.config.getPrefixes().get("rdfs") + "label");
        Property typeProperty = this.model.getProperty(this.config.getPrefixes().get("rdf") + "type");
        Property commentProperty = this.model.getProperty(this.config.getPrefixes().get("rdfs") + "comment");
        Property titleProperty = this.model.getProperty(this.config.getPrefixes().get("dc") + "title");
        Property descriptionProperty = this.model.getProperty(this.config.getPrefixes().get("dc") + "description");

        Resource typeR = this.model.getResource(this.config.getPrefixes().get("coeus") + type);

        resource.addProperty(labelProperty, label);
        resource.addProperty(commentProperty, comment);
        resource.addProperty(titleProperty, title);
        resource.addProperty(descriptionProperty, description);
        resource.addProperty(typeProperty, typeR);
    }


    /*
        Description
     */
    public Model getModel() {
        return model;
    }
    public void setModel(Model model) {
        this.model = model;
    }


    /*
        Description
     */
    public InfModel getInfmodel() {
        return infmodel;
    }
    public void setInfmodel(InfModel infmodel) {
        this.infmodel = infmodel;
    }


}
