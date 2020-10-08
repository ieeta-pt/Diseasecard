package pt.ua.bioinformatics.diseasecard.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.diseasecard.common.Boot;
import pt.ua.bioinformatics.diseasecard.ext.COEUSActionBeanContext;

/**
 *
 * Handler for internal locus-specific database redirects.
 * 
 * @author pedrolopes
 */
@UrlBinding("/services/lsdb/{key}")
public class ServicesLSDBActionBean implements ActionBean {

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
        Boot.start();
        return new RedirectResolution(Boot.getJedis().get("lsdb:" + key), false);
    }
}
