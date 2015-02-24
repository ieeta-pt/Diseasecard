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
import redis.clients.jedis.Jedis;

/**
 * Entry action for accessing network links based on HGNC identifiers.
 *
 * @author pedrolopes
 */
@UrlBinding("/hgnc/{key}/{$event}")
public class HGNCEntryActionBean implements ActionBean {

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
     * Loads HTML view for genes (based on HGNC).
     *
     * @return
     */
    @DefaultHandler
    public Resolution html() {
        try {
            Activity.log(key, "entry", context.getRequest().getRequestURI(), context.getRequest().getHeader("User-Agent"), context.getRequest().getHeader("X-Forwarded-For"));
        } catch (Exception e) {
        }
        return new ForwardResolution("/final/view/entry_hgnc.jsp");
    }

    /**
     * Delivers gene network as JSON object (based on HGNC).
     *
     * @return
     */
    public Resolution js() {
        try {
            // check if content is available on Redis cache
            context.getResponse().addHeader("Access-Control-Allow-Origin", "*");
            Jedis j = Boot.getJedis();
            String output = j.get("hgnc:" + key.toUpperCase());
            Boot.getJedis_pool().returnResource(j);
            return new StreamingResolution("application/json", output); //Boot.getJedis().get("hgnc:" + key.toUpperCase()));
            //return new StreamingResolution("application/json", Boot.getJedis().get("hgnc:" + key.toUpperCase()));
        } catch (Exception ex) {
            if (Config.isDebug()) {
                System.err.println("[COEUS][HGNCEntry] Unable to load data for " + key);
               // Logger.getLogger(HGNCEntryActionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            // not on cache, load directly from triplestore using DiseaseAPI
            DiseaseAPI d = new DiseaseAPI();
            return new StreamingResolution("application/json", d.loadHGNC(this.key).toString());
        }
    }
}
