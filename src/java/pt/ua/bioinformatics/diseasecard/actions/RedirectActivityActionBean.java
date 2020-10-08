package pt.ua.bioinformatics.diseasecard.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.diseasecard.ext.COEUSActionBeanContext;

/**
 * UI handler for showing latest tracked activities.
 *
 * @author pedrolopes
 */
@UrlBinding("/activity")
public class RedirectActivityActionBean implements ActionBean {

    private COEUSActionBeanContext context;

    public void setContext(ActionBeanContext context) {
        this.context = (COEUSActionBeanContext) context;
    }

    public COEUSActionBeanContext getContext() {
        return context;
    }

    @DefaultHandler
    public Resolution get() {
        return new ForwardResolution("/final/view/activity.jsp");
    }


}
