package pt.ua.bioinformatics.coeus.actions.diseasecard;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.coeus.common.Boot;
import pt.ua.bioinformatics.coeus.common.Config;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;
import pt.ua.bioinformatics.diseasecard.services.Indexer;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/services/indexer")
public class ServicesIndexerActionBean implements ActionBean {

    private COEUSActionBeanContext context;
   
    public void setContext(ActionBeanContext context) {
        this.context = (COEUSActionBeanContext) context;
    }

    public COEUSActionBeanContext getContext() {
        return context;
    }

    @DefaultHandler
    public Resolution html() {
        
        try {
            Boot.start();
        Indexer index = new Indexer();
        Thread t = new Thread(index);
        t.start();
        } catch (Exception ex) {
            if (Config.isDebug()) {
                Logger.getLogger(ServicesIndexerActionBean.class.getName()).log(Level.SEVERE, null, ex);
            }           
        }
         return new StreamingResolution("txt", "[DC4] Indexer launched...");
        
    }
}
