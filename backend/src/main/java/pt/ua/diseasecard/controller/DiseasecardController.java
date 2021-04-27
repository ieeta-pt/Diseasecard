package pt.ua.diseasecard.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import pt.ua.diseasecard.components.Boot;
import pt.ua.diseasecard.components.data.DiseaseAPI;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.components.management.Browsier;
import pt.ua.diseasecard.components.management.Cashier;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import pt.ua.diseasecard.service.DataManagementService;
import pt.ua.diseasecard.utils.Finder;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping("/diseasecard/api")
public class DiseasecardController {

    private SparqlAPI api;
    private DataManagementService dataManagementService;
    private String solrIndex;
    private String connectionString;
    private Map<String, String> sources;
    private List<String> protectedSources;
    private Jedis jedis;
    private Boot boot;

    public DiseasecardController(DiseasecardProperties diseasecardProperties, SparqlAPI sparqlAPI, Boot boot, DataManagementService dataManagementService) {
        Objects.requireNonNull(diseasecardProperties);
        Objects.requireNonNull(sparqlAPI);
        this.api = sparqlAPI;
        this.solrIndex = diseasecardProperties.getSolr().get("host") + ":" + diseasecardProperties.getSolr().get("port") + "/" + diseasecardProperties.getSolr().get("index");
        this.sources = diseasecardProperties.getSources();
        this.protectedSources = diseasecardProperties.getProtectedSources();
        this.connectionString = diseasecardProperties.getDatabase().get("url") + "?user=" + diseasecardProperties.getDatabase().get("username") + "&password=" + diseasecardProperties.getDatabase().get("password");
        this.jedis = boot.getJedis();
        this.boot = boot;
        this.dataManagementService = dataManagementService;
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
    public String getDiseaseByOMIM(
            @RequestParam(name = "omim", required = true) String omim) {

//        DiseaseAPI d = new DiseaseAPI(this.api, omim);
//        return d.load().toString();

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
            Logger.getLogger(DiseasecardController.class.getName()).log(Level.INFO,"[Diseasecard][API][getSourceURL] Source not found");
            return null;
        }
    }


    @GetMapping("/services/browse")
    @ResponseBody
    public String getBrowserResultsByLetter( @RequestParam(name = "letter", required = true) String letter) {

        try {
            return jedis.get("browse:" + letter);
        } catch (Exception ex) {
            Finder finder = new Finder(this.connectionString);
            return finder.browse(letter);
        }
    }


    @PostMapping("/startup")
    public void startInternalProcess() {
        Logger.getLogger(DiseasecardController.class.getName()).log(Level.INFO,"[Diseasecard][Controller] Receive alert to start my internal processing");
        this.boot.startInternalProcess();
    }


    @PostMapping("/dcadmin/uploadOntology")
    public Map<String, String> uploadOntology(@RequestParam("file") MultipartFile file) {
        Map<String, String> endpoints = dataManagementService.uploadSetup(file);
        //redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
        return endpoints;
    }


    @PostMapping(value = "/dcadmin/uploadEndpoints", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public void uploadEndpoints(@RequestParam("information[]") List<MultipartFile> information) throws IOException {
        dataManagementService.uploadEndpoints(information);
    }


    @PostMapping(value = "/dcadmin/operations/addEntity")
    public void addEntity(@RequestParam("titleEntity") String title,
                          @RequestParam("labelEntity") String label,
                          @RequestParam("descriptionEntity") String description,
                          @RequestParam(name= "isEntityOfEntity", required = false) String entityOf ) throws IOException {

        dataManagementService.prepareAddEntity(title, label, description, entityOf);
    }

    @PostMapping(value = "/dcadmin/operations/addConcept")
    public void addConcept(@RequestParam("titleConcept") String title,
                          @RequestParam("labelConcept") String label,
                          @RequestParam("descriptionConcept") String description,
                          @RequestParam("hasEntityConcept") String hasEntity,
                          @RequestParam(name= "hasResourceConcept", required = false) String hasResource ) throws IOException {

        dataManagementService.prepareAddConcept(title, label, description, hasEntity, hasResource);
    }


    @PostMapping(value = "/dcadmin/operations/addResource")
    public void addResource(@RequestParam("titleResource") String title,
                          @RequestParam("labelResource") String label,
                          @RequestParam("descriptionResource") String description,
                          @RequestParam("resourceOf") String resourceOf,
                          @RequestParam("extendsResource") String extendsResource,
                          @RequestParam("orderResource") String order,
                          @RequestParam("publisherEndpoint") String publisher,
                          @RequestParam("regexResource") String regex,
                          @RequestParam("files") MultipartFile files,
                          @RequestParam("queryResource") String query ) throws IOException {

        dataManagementService.prepareAddResource(title, label, description, resourceOf, extendsResource, order, publisher, regex, query, files);
    }

    @PostMapping(value = "/dcadmin/operations/addResource")
    public void addResourceWithURLEndpoint(@RequestParam("titleResource") String title,
                          @RequestParam("labelResource") String label,
                          @RequestParam("descriptionResource") String description,
                          @RequestParam("resourceOf") String resourceOf,
                          @RequestParam("extendsResource") String extendsResource,
                          @RequestParam("orderResource") String order,
                          @RequestParam("publisherEndpoint") String publisher,
                          @RequestParam("regexResource") String regex,
                          @RequestParam("files") String endpoint,
                          @RequestParam("queryResource") String query ) throws IOException {

        dataManagementService.prepareAddResource(title, label, description, resourceOf, extendsResource, order, publisher, regex, query, endpoint);
    }

    @PostMapping(value = "/dcadmin/operations/addOMIMResource")
    public void addOMIMResource(@RequestParam("titleResource") String title,
                            @RequestParam("labelResource") String label,
                            @RequestParam("descriptionResource") String description,
                            @RequestParam("resourceOf") String resourceOf,
                            @RequestParam("extendsResource") String extendsResource,
                            @RequestParam("orderResource") String order,
                            @RequestParam("publisherEndpoint") String publisher,
                            @RequestParam("regexResource") String regex,
                            @RequestParam("morbidmap") MultipartFile morbidmap,
                            @RequestParam("genemap") MultipartFile genemap,
                            @RequestParam("queryResource") String query ) throws IOException {

        dataManagementService.prepareAddOMIMResource(title, label, description, resourceOf, extendsResource, order, publisher, regex, query, morbidmap, genemap);
    }


    @GetMapping("/dcadmin/status/labels")
    public JSONObject getFormLabelsInfo() {
        return dataManagementService.getFormLabels();
    }


    @GetMapping("/dcadmin/status/allEntities")
    public JSONObject getAllEntitiesInfo() {
        return dataManagementService.getAllEntities();
    }

    @GetMapping("/dcadmin/status/allConcepts")
    public JSONObject getAllConceptsInfo() {
        return dataManagementService.getAllConcepts();
    }


    @GetMapping("/dcadmin/status/ontologyStructure")
    public JSONObject getOntologyStructureInfo() {
        return dataManagementService.getOntologyStructure();
    }


}
