package pt.ua.bioinformatics.coeus.actions.diseasecard;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;

/**
 *
 * @author pedrolopes
 */
//@UrlBinding("/entry/{diseaseid}.{$event}")
@UrlBinding("/evaluateCard.do")
public class EvaluateCardActionBean implements ActionBean {

    private COEUSActionBeanContext context;
    private String diseaseid;

    public void setContext(ActionBeanContext context) {
        this.context = (COEUSActionBeanContext) context;
    }

    public COEUSActionBeanContext getContext() {
        return context;
    }

    public String getDiseaseid() {
        return diseaseid;
    }

    public void setDiseaseid(String key) {
        this.diseaseid = key;
    }

    @DefaultHandler
    public Resolution html() { 
        return new RedirectResolution("/entry/" + diseaseid);
    }
}
