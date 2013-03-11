package pt.ua.bioinformatics.coeus.actions.diseasecard;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.coeus.common.Config;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;
import pt.ua.bioinformatics.diseasecard.domain.Disease;
import pt.ua.bioinformatics.diseasecard.services.Finder;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/services/query/{query}")
public class ServicesQueryActionBean implements ActionBean {

    private COEUSActionBeanContext context;
    private String query;
    private ArrayList<Disease> diseases;
    private Disease disease;
    private Finder finder;

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public void setContext(ActionBeanContext context) {
        this.context = (COEUSActionBeanContext) context;
    }

    public COEUSActionBeanContext getContext() {
        return context;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String key) {
        this.query = key;
    }

    public ArrayList<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(ArrayList<Disease> diseases) {
        this.diseases = diseases;
    }

    @DefaultHandler
    public Resolution js() throws IOException {
        return new StreamingResolution("application/js",new Scanner(new URL(Config.getIndex() + "/diseasecard/select?q=" + query + "&rows=1000&wt=json").openStream(), "UTF-8").useDelimiter("\\A").next());

    }
}
