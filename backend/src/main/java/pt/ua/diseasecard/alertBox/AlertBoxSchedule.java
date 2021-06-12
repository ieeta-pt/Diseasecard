package pt.ua.diseasecard.alertBox;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.components.data.Storage;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import pt.ua.diseasecard.utils.Predicate;
import pt.ua.diseasecard.utils.PrefixFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;


@Component
public class AlertBoxSchedule {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private SparqlAPI sparqlAPI;
    private Storage storage;
    private HashMap<String, String> sourceBaseURLs;
    private Model model;
    private DiseasecardProperties config;

    public AlertBoxSchedule(SparqlAPI sparqlAPI, Storage storage, DiseasecardProperties config) {
        this.sparqlAPI = sparqlAPI;
        this.storage = storage;
        this.model = storage.getModel();
        this.config = config;
    }

    /*
        With the specified cron we are telling the program to execute this method at 0am of the 15º and last day of each month.
        The cron annotation follows this structure:
            - second, minute, hour, day, month, weekday
     */

    //@Scheduled(cron = "0 0 0 1,15 * ?" )
    @Scheduled(fixedRate = 500000000 )

    public void searchInvalidItems()  {
        if (this.config.getDebug()) java.util.logging.Logger.getLogger(AlertBoxSchedule.class.getName()).log(Level.INFO,"[Diseasecard][AlertBoxSchedule] Searching Invalid Items at " + dateFormat.format(new Date()) );

        this.storage.removeSourceBaseURLsErrors();
        this.storage.updateDateOfBeginValidation();
        this.getSourcesBaseURLs();

        Resource itemCategory = this.model.getResource(PrefixFactory.getURIForPrefix(this.config.getKeyprefix()) + "Item");
        StmtIterator iter = this.model.listStatements(null, Predicate.get("rdf:type"), itemCategory);

        while(iter.hasNext()) {
            String itemUri = iter.nextStatement().getSubject().toString();

            String[] info = itemUri.substring(itemUri.lastIndexOf("/")).replace("/", "").split("_", 2);

            //if (info[0].equalsIgnoreCase("omim")) continue;

            String finalURL = this.sourceBaseURLs.get(info[0].toLowerCase()).replace("#replace#", info[1]);

            try
            {
                System.out.println("URL: " + finalURL);
                URL url = new URL(finalURL);

                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setRequestProperty("User-Agent", "Mozilla/5.0 AppleWebKit/537.36 Chrome/65.0.3325.181 Safari/537.36");
                huc.setRequestMethod("HEAD");
                huc.setRequestProperty("Accept", "*/*");
                int responseCode = huc.getResponseCode();

                if (responseCode != 200) {
                    System.out.println("Bad: " + url + " | Error: " + responseCode);
                    this.storage.saveSourceBaseURLsError(info[0], info[1], finalURL, responseCode+"");
                }
            }
            catch (Exception e)
            {
                System.out.println("Error with URL " + finalURL);
                this.storage.saveSourceBaseURLsError(info[0], info[1], finalURL, e.getClass().getSimpleName());
                e.printStackTrace();
            }
        }

        if (this.config.getDebug()) java.util.logging.Logger.getLogger(AlertBoxSchedule.class.getName()).log(Level.INFO,"[Diseasecard][AlertBoxSchedule] Finished Items Validation Process at " + dateFormat.format(new Date()) );

        this.storage.updateDateOfLastValidation();
    }


    private void getSourcesBaseURLs() {
        this.sourceBaseURLs = new HashMap<>();

        try
        {
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(this.sparqlAPI.select("SELECT *"
                    + " WHERE { ?s rdf:type coeus:SourceBaseURL ."
                    + " ?s rdfs:label ?source ."
                    + " ?s coeus:baseURL ?url } ", "js", false));

            JSONObject results = (JSONObject) response.get("results");
            JSONArray bindings = (JSONArray) results.get("bindings");

            for (Object o : bindings) {
                JSONObject binding = (JSONObject) o;
                sourceBaseURLs.put(((JSONObject) binding.get("source")).get("value").toString(), ((JSONObject) binding.get("url")).get("value").toString());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

//        sourceBaseURLs.entrySet().forEach(entry -> {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        });

    }
}
