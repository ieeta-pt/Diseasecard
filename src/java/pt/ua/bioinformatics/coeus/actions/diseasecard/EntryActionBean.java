package pt.ua.bioinformatics.coeus.actions.diseasecard;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.coeus.common.Config;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;
import pt.ua.bioinformatics.diseasecard.domain.Disease;
import pt.ua.bioinformatics.diseasecard.services.Activity;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/entry/{key}.{$event}")
public class EntryActionBean implements ActionBean {

    private COEUSActionBeanContext context;
    private String key;
    private Disease disease;
    private String item;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

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

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    @DefaultHandler
    public Resolution html() {
        try {
            disease = (Disease) getContext().getDisease("omim:" + key);
            if (disease == null) {
                disease = new Disease(Integer.parseInt(key));
                getContext().setDisease("omim:" + key, disease);
            }
        } catch (Exception ex) {
            if (Config.isDebug()) {
                Logger.getLogger(EntryActionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            Activity.log(key, "entry", context.getRequest().getRequestURI(), context.getRequest().getHeader("User-Agent"), context.getRequest().getHeader("X-Forwarded-For"));
        } catch (Exception e) {
        }
        return new ForwardResolution("/final/view/disease.jsp");
    }
}
