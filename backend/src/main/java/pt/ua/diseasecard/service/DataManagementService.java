package pt.ua.diseasecard.service;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
import pt.ua.diseasecard.utils.PrefixFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    // TODO: COMPOR A CENA DO PREFIXES PORUQE AGR SÓ TEM MESMO O PREFIXOS.. FALTA O À FRENTE
    /*
    public void validateSetup(MultipartFile file) {

        Map<String, String> prefixes = this.config.getPrefixes();

        try
        {
            Path copyLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            // TODO: Get endpoints
            OntModel model = ModelFactory.createOntologyModel();
            RDFReader r = model.getReader();
            r.read(model, file.getInputStream(), PrefixFactory.getURIForPrefix(this.config.getKeyprefix()));

            Property endpoint = model.getProperty(prefixes.get("coeus") + "endpoint");
            Property name = model.getProperty(prefixes.get("rdfs") + "label");

            ResIterator iter = model.listSubjectsWithProperty(endpoint);
            while (iter.hasNext())
            {
                com.hp.hpl.jena.rdf.model.Resource res = iter.nextResource();
                String originalEndpoint = res.getProperty(endpoint).getString();

                if (!(originalEndpoint.contains("hgnc") || originalEndpoint.contains("omim") || originalEndpoint.contains("http")))
                {
                    res.removeAll(endpoint);
                    res.addProperty(endpoint, "submittedFiles/endpoints/" + res.getProperty(name).getString());
                    System.out.println(res.getProperty(endpoint).getString());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    */

    public Map<String, String> uploadSetup(MultipartFile file) {
        try
        {
            Path copyLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            // TODO: Get endpoints
            Map<String, String> newEndpoints = this.storage.loadSetup(file.getInputStream());
            for (Map.Entry<String, String> entry : newEndpoints.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue().toString());
            }

            return newEndpoints;
            //this.build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void uploadEndpoints(List<MultipartFile> endpoints) throws IOException {
        for (MultipartFile file : endpoints) {
            Path copyLocation = Paths.get(uploadDir + File.separator + "endpoints" + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        }
        this.build();
    }

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

    private void readResources() {
        try {
            if (this.config.getDebug()) System.out.println("[COEUS][Builder] Reading resources for " + this.config.getName());

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
                System.out.println(o);
                JSONObject binding = (JSONObject) o;
                JSONObject s = (JSONObject) binding.get("s");
                JSONObject d = (JSONObject) binding.get("comment");
                JSONObject l = (JSONObject) binding.get("label");
                JSONObject t = (JSONObject) binding.get("title");
                JSONObject resof = (JSONObject) binding.get("resof");
                JSONObject publisher = (JSONObject) binding.get("publisher");
                JSONObject ext = (JSONObject) binding.get("extends");
                JSONObject endpoint = (JSONObject) binding.get("endpoint");
                JSONObject method = (JSONObject) binding.get("method");
                JSONObject extension = (JSONObject) binding.get("extension");
                JSONObject built = (JSONObject) binding.get("built");
                JSONObject query = (JSONObject) binding.get("query");
                JSONObject regex = (JSONObject) binding.get("regex");
                JSONObject identifiers = (JSONObject) binding.get("identifiers");

                Resource r = new Resource(s.get("value").toString(), t.get("value").toString(), l.get("value").toString(), d.get("value").toString(), publisher.get("value").toString(), endpoint.get("value").toString(), method.get("value").toString());
                r.setExtendsConcept(ext.get("value").toString());
                r.setIsResourceOf(new Concept((resof.get("value").toString())));
                r.setRegex(!(regex == null) ? regex.get("value").toString() : "");
                r.setIdentifiers(!(identifiers == null) ? identifiers.get("value").toString() : "");
                r.setExtension(!(extension == null) ? extension.get("value").toString() : "");
                r.setQuery(!(query == null) ? query.get("value").toString() : "");
                r.setBuilt(!(built == null) && Boolean.parseBoolean(built.get("value").toString()));
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

}