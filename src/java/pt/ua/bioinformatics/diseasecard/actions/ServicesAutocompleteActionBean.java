package pt.ua.bioinformatics.diseasecard.actions;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.diseasecard.common.Config;
import pt.ua.bioinformatics.diseasecard.ext.COEUSActionBeanContext;
import pt.ua.bioinformatics.diseasecard.services.Activity;
import pt.ua.bioinformatics.diseasecard.services.Finder;

/**
 *
 * Autocomplete handler, searches Solr and returns JSON object with results.
 *
 * @author pedrolopes
 */
@UrlBinding("/services/autocomplete/{$event}")
public class ServicesAutocompleteActionBean implements ActionBean {

    private ActionBeanContext context;
    private String term;
    private Finder finder;
    private String auto = "[]";

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
     *
     * @return
     */
    @DefaultHandler
    public Resolution id() {
        try {
            finder = new Finder(term.replace("%20", " "));
            auto = finder.get("id");
        } catch (Exception e) {
            if (Config.isDebug()) {
                System.out.println("[AutocompleteActionBean] Unable to find matches for " + term + "\n\t" + e.toString());
                Logger.getLogger(ServicesAutocompleteActionBean.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        try {
            Activity.log(term, "autocompleteid", context.getRequest().getRequestURI(), context.getRequest().getHeader("User-Agent"), context.getRequest().getHeader("X-Forwarded-For"));
        } catch (Exception e) {
        }
        return new StreamingResolution("application/json", auto);
    }
}
