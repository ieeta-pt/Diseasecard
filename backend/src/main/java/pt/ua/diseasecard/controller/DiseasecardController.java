package pt.ua.diseasecard.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pt.ua.diseasecard.components.Boot;
import pt.ua.diseasecard.components.data.DiseaseAPI;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import pt.ua.diseasecard.service.FileService;
import pt.ua.diseasecard.utils.Finder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/diseasecard")
public class DiseasecardController {

    private SparqlAPI api;
    private String solrIndex;
    private String connectionString;
    private Map<String, String> sources;
    private List<String> protectedSources;
    private Jedis jedis;
    private Boot boot;

    @Autowired
    FileService fileService;

    public DiseasecardController(DiseasecardProperties diseasecardProperties, SparqlAPI sparqlAPI, Boot boot) {
        Objects.requireNonNull(diseasecardProperties);
        Objects.requireNonNull(sparqlAPI);
        this.api = sparqlAPI;
        this.solrIndex = diseasecardProperties.getSolr().get("host") + ":" + diseasecardProperties.getSolr().get("port") + "/" + diseasecardProperties.getSolr().get("index");
        this.sources = diseasecardProperties.getSources();
        this.protectedSources = diseasecardProperties.getProtectedSources();
        this.connectionString = diseasecardProperties.getDatabase().get("url") + "?user=" + diseasecardProperties.getDatabase().get("username") + "&password=" + diseasecardProperties.getDatabase().get("password");
        this.jedis = boot.getJedis();
        this.boot = boot;
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


    // TODO: Em vez de estar sempre a fazer load, tentar ir ver se não tem no jedis ------ done
    @GetMapping("/services/disease")
    @ResponseBody
    public String getDiseaseByOMIM(
            @RequestParam(name = "omim", required = true) String omim) {

        try {
            return jedis.get("omim:" + omim);
        } catch (Exception ex) {
            DiseaseAPI d = new DiseaseAPI(this.api, omim);
            return d.load().toString();
        }

    }


    // TODO: Tornar mais bonito o código
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


    @GetMapping("/services/browse")
    @ResponseBody
    public String getBrowserResultsByLetter(
            @RequestParam(name = "letter", required = true) String letter) {

        try {
            return jedis.get("browse:" + letter);
        } catch (Exception ex) {
            Finder finder = new Finder(this.connectionString);
            return finder.browse(letter);
        }
    }


    @PostMapping("/startup")
    public void startInternalProcess() {
        System.out.println("[Diseasecard][Controller] Receive alert to start my internal processing");
        this.boot.startInternalProcess();
    }


    @PostMapping("/dcadmin/uploadOntology")
    public String uploadOntology(@RequestParam("file") MultipartFile file) {
        fileService.uploadFile(file);
        //redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/";
    }
}
