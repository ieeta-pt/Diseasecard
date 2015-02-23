package pt.ua.bioinformatics.coeus.actions.diseasecard;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.coeus.common.Boot;
import pt.ua.bioinformatics.coeus.common.Config;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;
import pt.ua.bioinformatics.diseasecard.domain.DiseaseAPI;
import pt.ua.bioinformatics.diseasecard.services.Activity;

/**
 * Main entry point for diseases (by OMIM).
 *
 * <p>Duplicate from DiseaseActionBean.java</p>
 *
 * @author pedrolopes
 */
@UrlBinding("/entry/{key}.{$event}")
public class EntryActionBean implements ActionBean {

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

    /**
     * Loads HTML view for diseases (based on OMIM).
     *
     * @return
     */
    @DefaultHandler
    public Resolution html() {
        try {
            Activity.log(key, "entry", context.getRequest().getRequestURI(), context.getRequest().getHeader("User-Agent"), context.getRequest().getHeader("X-Forwarded-For"));
        } catch (Exception e) {
            Logger.getLogger(EntryActionBean.class.getName()).log(Level.WARNING, null, e);
        }
        return new ForwardResolution("/final/view/entry.jsp");
    }

    /**
     * Delivers disease network as JSON object (based on OMIM).
     *
     * @return
     */
    public Resolution js() {
       try {
            // check if content is available on Redis cache
           context.getResponse().addHeader("Access-Control-Allow-Origin", "*");
            return new StreamingResolution("application/json", Boot.getJedis().get("omim:" + key));
        } catch (Exception ex) {
            if (Config.isDebug()) {
                System.err.println("[COEUS][Entry] Unable to load data for " + key);
                Logger.getLogger(EntryActionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            // not on cache, load directly from triplestore using DiseaseAPI
            DiseaseAPI d = new DiseaseAPI(key);
            return new StreamingResolution("application/json", d.load().toString());
        }
    }
}
