package pt.ua.bioinformatics.coeus.actions.diseasecard;

import java.util.ArrayList;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;
import pt.ua.bioinformatics.diseasecard.domain.Disease;
import pt.ua.bioinformatics.diseasecard.services.Finder;

/**
 *
 * Handler for search results. Returns JSON object with search results for
 * full-text search and identifier-only search.
 *
 * @author pedrolopes
 */
@UrlBinding("/services/results/{$event}/{query}")
public class ServicesSearchResultsActionBean implements ActionBean {

    private COEUSActionBeanContext context;
    private String query;
    private ArrayList<Disease> diseases;
    private Disease disease;
    private Finder finder;

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public void setContext(ActionBeanContext context) {
        this.context = (COEUSActionBeanContext) context;
    }

    public COEUSActionBeanContext getContext() {
        return context;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String key) {
        this.query = key;
    }

    public ArrayList<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(ArrayList<Disease> diseases) {
        this.diseases = diseases;
    }

    // TODO
    // - add activity tracking
    @DefaultHandler
    public Resolution id() {
        finder = new Finder(query.replace("%20", " "));
        return new StreamingResolution("application/js", finder.find("id"));
    }

    public Resolution full() {
        finder = new Finder(query.replace("%20", " "));
        return new StreamingResolution("application/js", finder.find("full"));
    }
}
