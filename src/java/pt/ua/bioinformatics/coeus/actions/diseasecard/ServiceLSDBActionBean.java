package pt.ua.bioinformatics.coeus.actions.diseasecard;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.coeus.common.Boot;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/services/lsdb/{key}")
public class ServiceLSDBActionBean implements ActionBean {

    private COEUSActionBeanContext context;
    private String key;
    private String url;

    public void setContext(ActionBeanContext context) {
        this.context = (COEUSActionBeanContext) context;
    }

    public COEUSActionBeanContext getContext() {
        return context;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @DefaultHandler
    public Resolution html() {        
        return new RedirectResolution(Boot.getAPI().getTriple("diseasecard:lsdb_" + key, "dc:description", "o", "csv").split("\n")[1], false);
    }
}
