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
import pt.ua.bioinformatics.diseasecard.domain.Links;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/services/frame/{key}:{value}")
public class ServiceFrameActionBean implements ActionBean {

    private COEUSActionBeanContext context;
    private String key;
    private String value;
    private Disease disease;
    private String url;
    private String level;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setContext(ActionBeanContext context) {
        this.context = (COEUSActionBeanContext) context;
    }

    public COEUSActionBeanContext getContext() {
        return context;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
            if (!key.equals("0")) {
                url = Links.get(key).replace("#replace#", value);
            } else {
                url = "";
            }
        } catch (Exception ex) {
            if (Config.isDebug()) {
                Logger.getLogger(ServiceFrameActionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            url = getContext().getRequest().getContextPath() + "/final/view/empty_frame.jsp";
        }
        return new ForwardResolution("/final/view/frame.jsp");
    }
}
