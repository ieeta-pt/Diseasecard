package pt.ua.bioinformatics.coeus.actions.diseasecard;

import java.util.ArrayList;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;
import pt.ua.bioinformatics.diseasecard.domain.Disease;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/browse/{query}.{$event}")
public class BrowseActionBean implements ActionBean {

    private COEUSActionBeanContext context;
    private String query;
    private ArrayList<Disease> diseases;

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

    @DefaultHandler
    public Resolution html() {
        return new ForwardResolution("/beta/view/search.jsp");
    }
}
