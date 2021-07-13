package pt.ua.diseasecard.connectors;

import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.components.data.Storage;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import pt.ua.diseasecard.connectors.plugins.HGNCPlugin;
import pt.ua.diseasecard.connectors.plugins.OMIMPlugin;
import pt.ua.diseasecard.domain.Resource;
import pt.ua.diseasecard.utils.BeanUtil;
import pt.ua.diseasecard.utils.Predicate;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    Need to rethink about this class, because now we don't have HGNC plugin, only OMIM
 */
public class PluginFactory implements ResourceFactory{

    private Resource res;

    private final DiseasecardProperties config = BeanUtil.getBean(DiseasecardProperties.class);;

    private final SparqlAPI api = BeanUtil.getBean(SparqlAPI.class);

    private final Storage storage = BeanUtil.getBean(Storage.class);

    public PluginFactory(Resource res) {
        this.res = res;
    }

    @Override
    public void read() {
        String[] divide = res.getEndpoint().split("://");
        try {
            switch (divide[0].trim())
            {
                case "omim":
                    OMIMPlugin omim = new OMIMPlugin(res);
                    omim.itemize();
                    break;
                case "hgnc":
                    HGNCPlugin hgnc = new HGNCPlugin(res);
                    hgnc.itemize();
                    break;
            }
        }
        catch (Exception ex)
        {
            if (this.config.getDebug()) {
                Logger.getLogger(PluginFactory.class.getName()).log(Level.INFO,"[COEUS][SPARQLFactory] unable to load data for " + res.getUri());
                Logger.getLogger(PluginFactory.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
    }

    @Override
    public void save()
    {
        Map<String, String> prefixes = this.config.getPrefixes();
        try
        {
            com.hp.hpl.jena.rdf.model.Resource resource = api.getResource(this.res.getUri());
            api.addStatement(resource, Predicate.get("coeus:built"), true);

            if (this.config.getDebug()) Logger.getLogger(PluginFactory.class.getName()).log(Level.INFO,"[COEUS][API] Saved resource " + res.getUri());
        }
        catch (Exception ex)
        {
            if (this.config.getDebug())
            {
                Logger.getLogger(PluginFactory.class.getName()).log(Level.INFO,"[COEUS][API] Unable to save resource " + res.getUri());
                Logger.getLogger(PluginFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Resource getRes() {
        return res;
    }

    public void setRes(Resource res) {
        this.res = res;
    }
}
