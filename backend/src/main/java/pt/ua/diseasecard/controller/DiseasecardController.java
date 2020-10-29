package pt.ua.diseasecard.controller;

import org.springframework.web.bind.annotation.*;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import pt.ua.diseasecard.utils.Finder;

import javax.websocket.server.PathParam;
import java.util.Objects;


@RestController
@RequestMapping("/diseasecard")
public class DiseasecardController {

    private SparqlAPI api;
    private String solrIndex;

    public DiseasecardController(DiseasecardProperties diseasecardProperties, SparqlAPI sparqlAPI) {
        Objects.requireNonNull(diseasecardProperties);
        Objects.requireNonNull(sparqlAPI);
        this.api = sparqlAPI;
        this.solrIndex = diseasecardProperties.getSolr().get("host") + ":" + diseasecardProperties.getSolr().get("port") + "/" + diseasecardProperties.getSolr().get("index");
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
}
