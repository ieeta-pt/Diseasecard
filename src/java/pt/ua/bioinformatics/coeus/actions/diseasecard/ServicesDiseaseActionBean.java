package pt.ua.bioinformatics.coeus.actions.diseasecard;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;
import pt.ua.bioinformatics.diseasecard.domain.DiseaseAPI;
import redis.clients.jedis.Jedis;

/**
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
    public Resolution html() {
        return new ForwardResolution("/final/view/disease.jsp");
    }

    public Resolution js() {
        DiseaseAPI d = new DiseaseAPI(key);
        return new StreamingResolution("application/json", d.load().toString());
    }
}