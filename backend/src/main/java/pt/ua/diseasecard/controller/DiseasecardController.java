package pt.ua.diseasecard.controller;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;
import pt.ua.diseasecard.components.data.DiseaseAPI;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import pt.ua.diseasecard.utils.Finder;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/diseasecard")
public class DiseasecardController {

    private SparqlAPI api;
    private String solrIndex;
    private Map<String, String> sources;
    private List<String> protectedSources;

    public DiseasecardController(DiseasecardProperties diseasecardProperties, SparqlAPI sparqlAPI) {
        Objects.requireNonNull(diseasecardProperties);
        Objects.requireNonNull(sparqlAPI);
        this.api = sparqlAPI;
        this.solrIndex = diseasecardProperties.getSolr().get("host") + ":" + diseasecardProperties.getSolr().get("port") + "/" + diseasecardProperties.getSolr().get("index");
        this.sources = diseasecardProperties.getSources();
        this.protectedSources = diseasecardProperties.getProtectedSources();
    }

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot! ";
    }

    @GetMapping("/services/results/{searchType}")
    public String searchResults(
            @PathVariable String searchType,
            @RequestParam(name = "query", required = true) String query) {

        Finder finder = new Finder(this.api, this.solrIndex, query);
        if (searchType.equals("id"))
            return finder.find("id");
        else
            return finder.find("full");
    }

    @GetMapping("/services/autocomplete")
    @ResponseBody
    public String autocomplete(
            @RequestParam(name = "query", required = true) String query) {

        Finder finder = new Finder(this.api, this.solrIndex, query);
        return finder.get("id");
    }

    @GetMapping("/services/disease")
    @ResponseBody
    public JSONObject getDiseaseByOMIM(
            @RequestParam(name = "omim", required = true) String omim) {

        DiseaseAPI d = new DiseaseAPI(this.api, omim);

        return d.load();
    }

    @GetMapping("/services/linkout/{key}:{value}")
    @ResponseBody
    public JSONObject getSourceURL(
            @PathVariable String key,
            @PathVariable String value) {

        JSONObject res = new JSONObject();

        try {
            key = key.toLowerCase();

            if (this.protectedSources.contains(key)) res.put("protection", true);
            else res.put("protection", false);

            res.put("url", this.sources.get(key.toLowerCase()).replace("#replace#", value));

            return res;
        } catch (Exception ex) {
            System.out.println("[Diseasecard][API][getSourceURL] Source not found");
            return null;
        }
    }
}
