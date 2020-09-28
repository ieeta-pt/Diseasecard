package pt.ua.bioinformatics.diseasecard.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.diseasecard.ext.COEUSActionBeanContext;
import pt.ua.bioinformatics.diseasecard.domain.DiseaseAPI;

/**
 *
 * Returns JSON object with disease network.
 *
 * @author pedrolopes
 */
@UrlBinding("/services/disease/{key}.{$event}")
public class ServicesDiseaseActionBean implements ActionBean {

    private COEUSActionBeanContext context;
    private String key;

    public void setContext(ActionBeanContext context) {
        this.context = (COEUSActionBeanContext) context;
    }

    public COEUSActionBeanContext getContext() {
        return context;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @DefaultHandler
    public Resolution js() {
        DiseaseAPI d = new DiseaseAPI(key);
        return new StreamingResolution("application/json", d.load().toString());
    }
}
