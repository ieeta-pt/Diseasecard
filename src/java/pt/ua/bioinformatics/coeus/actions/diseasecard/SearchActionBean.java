package pt.ua.bioinformatics.coeus.actions.diseasecard;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;
import pt.ua.bioinformatics.diseasecard.services.Activity;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/search/{query}.{$event}")
public class SearchActionBean implements ActionBean {

    private COEUSActionBeanContext context;
    private String query;

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

    @DefaultHandler
    public Resolution html() {
        try {
            Activity.log(query, "search", context.getRequest().getRequestURI(), context.getRequest().getHeader("User-Agent"), context.getRequest().getRemoteAddr());
        } catch (Exception e) {
        }
        return new ForwardResolution("/final/view/search.jsp");
    }
}
