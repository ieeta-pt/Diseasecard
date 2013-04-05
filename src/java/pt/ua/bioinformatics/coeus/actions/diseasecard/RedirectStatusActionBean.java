package pt.ua.bioinformatics.coeus.actions.diseasecard;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.coeus.common.Config;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/status")
public class RedirectStatusActionBean implements ActionBean {
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
        version = Config.getVersion();
        return new ForwardResolution("/final/view/status.jsp");
    }
    
    
}
