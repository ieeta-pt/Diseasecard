package pt.ua.bioinformatics.diseasecard.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.diseasecard.ext.COEUSActionBeanContext;
import pt.ua.bioinformatics.diseasecard.services.Finder;

/**
 *
 * Returns JSON object with short disease information.
 * 
 * @author pedrolopes
 */
@UrlBinding("/services/info/{key}.{$event}")
public class ServicesInfoActionBean implements ActionBean {

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
    public Resolution get() {
        Finder f = new Finder();
        return new StreamingResolution("application/json", f.status(Integer.parseInt(key)));
    }
}
