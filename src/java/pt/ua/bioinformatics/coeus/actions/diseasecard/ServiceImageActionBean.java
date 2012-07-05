package pt.ua.bioinformatics.coeus.actions.diseasecard;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;
import java.util.Random;
import pt.ua.bioinformatics.coeus.common.Config;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/services/image/{key}.{$event}")
public class ServiceImageActionBean implements ActionBean {

    private COEUSActionBeanContext context;
    private String key = "random";

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
    public Resolution background() {
        String address = "";
        try {
            if (key.equals("random")) {
                Random rnd = new Random();
                address = "/beta/assets/image/background" + rnd.nextInt(6) + ".jpg";
            } else {
                address = "/beta/assets/image/background" + key + ".jpg";
            }
        } catch (Exception ex) {
            if (Config.isDebug()) {
                Logger.getLogger(ServiceImageActionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new ForwardResolution(address);
    }
}
