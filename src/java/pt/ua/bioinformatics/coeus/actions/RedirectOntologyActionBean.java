package pt.ua.bioinformatics.coeus.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

/**
 * Redirect to COEUS Ontology file and documentation.
 *
 * @author pedrolopes
 */
@UrlBinding("/api/ontology/{$event}")
public class RedirectOntologyActionBean implements ActionBean {
    private ActionBeanContext context;

    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    public ActionBeanContext getContext() {
        return context;
    }

    /**
     * Returns the COEUS ontology OWL file.
     *
     * @return [description]
     */
    @DefaultHandler
    public Resolution handle() {
        return new ForwardResolution("/ontology/coeus_1.0.owl");
    }

    /**
     * Redirects to ontology documentation.
     *
     * @return [description]
     */
    public Resolution doc(){
        return new ForwardResolution("/ontology/doc/index.html");
    }
}
