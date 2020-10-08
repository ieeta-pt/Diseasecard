package pt.ua.bioinformatics.diseasecard.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.diseasecard.common.Boot;
import pt.ua.bioinformatics.diseasecard.ext.COEUSActionBeanContext;
import pt.ua.bioinformatics.diseasecard.services.Activity;
import pt.ua.bioinformatics.diseasecard.services.Finder;

/**
 *
 * Returns JSON object with disease list for browsing page.
 *
 * @author pedrolopes
 */
@UrlBinding("/services/browse/{key}.{$event}")
public class ServicesBrowseActionBean implements ActionBean {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    private COEUSActionBeanContext context;

    public void setContext(ActionBeanContext context) {
        this.context = (COEUSActionBeanContext) context;
    }

    public COEUSActionBeanContext getContext() {
        return context;
    }

    @DefaultHandler
    public Resolution js() {
        try {
            try {
                Activity.log(key, "browse", context.getRequest().getRequestURI(), context.getRequest().getHeader("User-Agent"), context.getRequest().getHeader("X-Forwarded-For"));
            } catch (Exception e) {
            }
            return new StreamingResolution("application/json", Boot.getJedis().get("browse:" + key));

        } catch (Exception ex) {
            
            Finder f = new Finder();
            try {
                Activity.log(key, "browse", context.getRequest().getRequestURI(), context.getRequest().getHeader("User-Agent"), context.getRequest().getHeader("X-Forwarded-For"));
            } catch (Exception e) {
            }
            return new StreamingResolution("application/json", f.browse(key));
            
        }

    }
}
