package pt.ua.bioinformatics.coeus.actions.diseasecard;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;
import pt.ua.bioinformatics.diseasecard.services.Finder;

/**
 *
 * Handler for activity data. Returns JSON object with activity details (Why is called status?).
 * 
 * @author pedrolopes
 */
@UrlBinding("/services/status/{key}.{$event}")
public class ServicesStatusActionBean implements ActionBean {

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
