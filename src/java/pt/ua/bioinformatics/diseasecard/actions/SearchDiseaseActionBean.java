package pt.ua.bioinformatics.diseasecard.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.diseasecard.ext.COEUSActionBeanContext;

/**
 *
 * Helper URL to cover Diseasecard 3 links.
 *
 * @author pedrolopes
 */
@UrlBinding("/searchDisease.do")
public class SearchDiseaseActionBean implements ActionBean {

    private COEUSActionBeanContext context;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setContext(ActionBeanContext context) {
        this.context = (COEUSActionBeanContext) context;
    }

    public COEUSActionBeanContext getContext() {
        return context;
    }

    @DefaultHandler
    public Resolution html() {
        return new RedirectResolution("/browse#" + key);
    }
}
