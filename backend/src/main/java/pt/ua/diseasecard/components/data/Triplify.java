package pt.ua.diseasecard.components.data;

import pt.ua.diseasecard.configuration.DiseasecardProperties;
import pt.ua.diseasecard.domain.Resource;
import pt.ua.diseasecard.utils.BeanUtil;
import pt.ua.diseasecard.utils.ConceptFactory;
import pt.ua.diseasecard.utils.Predicate;
import pt.ua.diseasecard.utils.PrefixFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Triplify {

    private HashMap<String, ArrayList<String>> properties;
    private Resource resource;
    private String extension;

    private final SparqlAPI api = BeanUtil.getBean(SparqlAPI.class);;
    private final DiseasecardProperties config = BeanUtil.getBean(DiseasecardProperties.class);


    public Triplify(Resource resource, String extension) {
        this.resource = resource;
        this.extension = extension;
        this.properties = new HashMap<>();
    }

    public void itemize(String i) {
        try {
            // create initial Item triple with <concept>_<key> structure
            String[] itemTmp = resource.getIsResourceOf().getUri().split("_");
            com.hp.hpl.jena.rdf.model.Resource item = api.createResource(PrefixFactory.getURIForPrefix(this.config.getKeyprefix()) + itemTmp[1] + "_" + i);
            com.hp.hpl.jena.rdf.model.Resource obj = api.createResource(PrefixFactory.getURIForPrefix(this.config.getKeyprefix()) + "Item");
            api.addStatement(item, Predicate.get("rdf:type"), obj);

            // set Item label and creator
            api.addStatement(item, Predicate.get("rdfs:label"), ConceptFactory.getTokenFromConcept(this.resource.getIsResourceOf().getLabel()) + i);
            api.addStatement(item, Predicate.get("dc:creator"), this.config.getName());

            // associate Item with Concept
            com.hp.hpl.jena.rdf.model.Resource con = api.getResource(resource.getIsResourceOf().getUri());
            api.addStatement(item, Predicate.get("coeus:hasConcept"), con);
            api.addStatement(con, Predicate.get("coeus:isConceptOf"), item);

            // associate with other Item
            if (!extension.equals("")) {
                com.hp.hpl.jena.rdf.model.Resource it = api.getResource(extension);
                api.addStatement(item, Predicate.get("coeus:isAssociatedTo"), it);
                api.addStatement(it, Predicate.get("coeus:isAssociatedTo"), item);
            }

            // set Resource-specific properties (from HashMap)
            for (String key : properties.keySet()) {
                for (String object : properties.get(key)) {
                    api.addStatement(item, Predicate.get(key), object);
                }
            }
        }
        catch (Exception ex)
        {
            if (this.config.getDebug()) Logger.getLogger(Triplify.class.getName()).log(Level.INFO,"[COEUS][Triplify] Unable to add item to " + resource.getTitle());
            Logger.getLogger(Triplify.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void add(String pred, String obj) {
        if (!properties.containsKey(pred)) {
            properties.put(pred, new ArrayList<String>());
        }
        properties.get(pred).add(obj);
    }
}
