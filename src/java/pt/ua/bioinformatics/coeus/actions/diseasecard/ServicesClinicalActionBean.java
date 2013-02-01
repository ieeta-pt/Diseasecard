package pt.ua.bioinformatics.coeus.actions.diseasecard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/services/clinical/{key}.{$event}")
public class ServicesClinicalActionBean implements ActionBean {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    private COEUSActionBeanContext context;

    public void setContext(ActionBeanContext context) {
        this.context = (COEUSActionBeanContext) context;
    }

    public COEUSActionBeanContext getContext() {
        return context;
    }
    
@DefaultHandler
    public Resolution js() {
        String response = "";
        try {
            URL omim = new URL("http://api.europe.omim.org/api/clinicalSynopsis?mimNumber=" + key + "&include=externalLinks&format=json&apiKey=F713C7BF1074F7A0AA3C11549ABC7BE5F28320E7");
            BufferedReader in = new BufferedReader(new InputStreamReader(omim.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response += inputLine;
            }
            in.close();
        } catch (MalformedURLException e) {
            Logger.getLogger(ServicesClinicalActionBean.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(ServicesClinicalActionBean.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception e) {
            Logger.getLogger(ServicesClinicalActionBean.class.getName()).log(Level.SEVERE, null, e);
        }
        return new StreamingResolution("application/json", response);
    }

    
    public Resolution html() {
        String response = "";
        try {
            URL omim = new URL("http://api.europe.omim.org/api/clinicalSynopsis?mimNumber=" + key + "&include=externalLinks&format=html&apiKey=F713C7BF1074F7A0AA3C11549ABC7BE5F28320E7");
            BufferedReader in = new BufferedReader(new InputStreamReader(omim.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response += inputLine;
            }
            in.close();
        } catch (MalformedURLException e) {
            Logger.getLogger(ServicesClinicalActionBean.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(ServicesClinicalActionBean.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception e) {
            Logger.getLogger(ServicesClinicalActionBean.class.getName()).log(Level.SEVERE, null, e);
        }
        return new StreamingResolution("text/html", response);
    }
}
