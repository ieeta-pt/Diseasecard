package pt.ua.bioinformatics.coeus.actions.diseasecard;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import org.json.JSONObject;
import pt.ua.bioinformatics.coeus.common.Config;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;
import pt.ua.bioinformatics.diseasecard.services.Feedback;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/feedback")
public class FeedbackActionBean implements ActionBean {

    private COEUSActionBeanContext context;
    private String email = "";
    private String message = "";

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setContext(ActionBeanContext context) {
        this.context = (COEUSActionBeanContext) context;
    }

    public COEUSActionBeanContext getContext() {
        return context;
    }

    @DefaultHandler
    public Resolution html() {
        JSONObject obj = null;
        try {
            obj = new JSONObject();
            Feedback f = new Feedback(email, message);
            if (f.save()) {
                obj.put("status", 100);
            } else {
                obj.put("status", 200);
            }
        } catch (Exception ex) {
            if (Config.isDebug()) {
                Logger.getLogger(FeedbackActionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new StreamingResolution("application/json", obj.toString());
    }
}
