package pt.ua.bioinformatics.coeus.actions.diseasecard;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import org.json.JSONArray;
import org.json.JSONObject;
import pt.ua.bioinformatics.coeus.common.Config;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;
import pt.ua.bioinformatics.diseasecard.domain.Disease;
import pt.ua.bioinformatics.diseasecard.services.Finder;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/view/results/{$event}/{query}")
public class ViewResultsActionBean implements ActionBean {

    private COEUSActionBeanContext context;
    private String query;
    private ArrayList<Disease> diseases;
    private Disease disease;
    private JSONObject response = new JSONObject();
    private Finder finder;
    private String auto;

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
    public Resolution search() {
        finder = new Finder(query);
      /*  if (query.length() > 2) {
            try {
                diseases = (ArrayList<Disease>) getContext().getSearchResults("search:" + query);
                if (diseases == null) {
                    finder = new Finder(query);
                 //   diseases = finder.find();
                    out = finder.find();
                }
                process();
            } catch (Exception ex) {
                if (Config.isDebug()) {
                    Logger.getLogger(ViewResultsActionBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            try {
                response.put("status", 140);
            } catch (Exception ex) {
            }
        }*/
        //return new StreamingResolution("application/js", response.toString());
        
        return new StreamingResolution("application/js", finder.find());
    }

    void process() {
        try {
            if (diseases.isEmpty()) {
                response.put("status", "110");
            } else if (diseases.size() == 1) {
                response.put("status", "121");
                response.put("size", diseases.size());
                disease = diseases.get(0);
                response.put("id", disease.getOmim().getId());
                getContext().setDisease("omim:" + disease.getOmimId(), disease);
            } else {
                response.put("status", "122");
                response.put("size", diseases.size());
                JSONArray list = new JSONArray();
                for (Disease d : diseases) {
                    getContext().setDisease("omim:" + d.getOmimId(), d);
                    JSONObject job = new JSONObject();
                    job.put("id", d.getOmimId());
                    job.put("name", d.getOmim().getDescription());
                    list.put(job);
                }
                getContext().setSearchResults("browse:" + query, diseases);
                response.put("results", list);
            }
        } catch (Exception ex) {
            if (Config.isDebug()) {
                Logger.getLogger(ViewResultsActionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
