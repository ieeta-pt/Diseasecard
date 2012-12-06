package pt.ua.bioinformatics.coeus.actions.diseasecard;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import pt.ua.bioinformatics.coeus.common.Config;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;
import pt.ua.bioinformatics.diseasecard.services.Finder;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/autocomplete")
public class AutocompleteActionBean implements ActionBean {

    private ActionBeanContext context;
    private String term;
    private Finder finder;
    private String auto;

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Finder getFinder() {
        return finder;
    }

    public void setFinder(Finder finder) {
        this.finder = finder;
    }

    public void setContext(ActionBeanContext context) {
        this.context = (COEUSActionBeanContext) context;
    }

    public COEUSActionBeanContext getContext() {
        return (COEUSActionBeanContext) context;
    }

    /**
     * Main resolution handler.
     * @return
     */
    @DefaultHandler
    public Resolution get() {
        try {
            auto = (String) getContext().getAutocompleteResults("autocomplete:" + term);
            if (auto == null) {
                finder = new Finder(term);
                auto = finder.get();
                getContext().setAutocompleteResults("autocomplete:" + term, auto);
            }
        } catch (Exception e) {
            if (Config.isDebug()) {
                System.out.println("[AutocompleteActionBean] Unable to find matches for " + term + "\n\t" + e.toString());
                Logger.getLogger(AutocompleteActionBean.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return new StreamingResolution("application/json", auto);
    }
}
