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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    private void connect()  {
        try {
            this.store = SDBFactory.connectStore(ResourceUtils.getFile("classpath:configuration/" + this.config.getSdb()).getPath() );
            //this.store = SDBFactory.connectStore("/configuration/" + this.config.getSdb());

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
            //InputStream in = FileManager.get().open(filepath);
            //RDFReader r = this.model.getReader();
            //r.read(this.model, stream, PrefixFactory.getURIForPrefix(this.config.getKeyprefix()));

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

                if (originalEndpoint.contains("hgnc"))
                {
                    newEndpoints.put(label, originalEndpoint);
                }
                else if (originalEndpoint.contains("omim"))
                {
                    newEndpoints.put(label, originalEndpoint);
                    //newEndpoints.put("omim_morbidmap", originalEndpoint);
                }
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

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public InfModel getInfmodel() {
        return infmodel;
    }

    public void setInfmodel(InfModel infmodel) {
        this.infmodel = infmodel;
    }

    public Property getProperty(String s) {
        return this.model.getProperty(s);
    }
}
