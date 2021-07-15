package pt.ua.diseasecard.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pt.ua.diseasecard.components.Boot;
import pt.ua.diseasecard.components.data.DiseaseAPI;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import pt.ua.diseasecard.service.DataManagementService;
import pt.ua.diseasecard.utils.Finder;
import redis.clients.jedis.Jedis;
import java.io.IOException;
import java.util.*;
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
    private SimpMessagingTemplate template;

    public DiseasecardController(DiseasecardProperties diseasecardProperties, SparqlAPI sparqlAPI, Boot boot, DataManagementService dataManagementService, SimpMessagingTemplate template) {
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
        this.template = template;
    }


    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot! ";
    }


    @GetMapping("/services/results/{searchType}")
    public String searchResults(@PathVariable String searchType, @RequestParam(name = "query", required = true) String query) {

        Finder finder = new Finder(this.api, this.solrIndex, query);
        if (searchType.equals("id"))
            return finder.find("id");
        else
            return finder.find("full");
    }


    @GetMapping("/services/autocomplete")
    @ResponseBody
    public String autocomplete(@RequestParam(name = "query", required = true) String query) {

        Finder finder = new Finder(this.api, this.solrIndex, query);
        return finder.get("id");
    }


    @GetMapping("/services/disease")
    @ResponseBody
    public String getDiseaseByOMIM(@RequestParam(name = "omim", required = true) String omim) {
        String disease;

        try {
            disease = jedis.get("omim:" + omim);
        } catch (Exception ex) {
            DiseaseAPI d = new DiseaseAPI(this.api, omim);
            disease = d.load().toString();
        }

        // TODO:
        //  - Please note that this operation can take to much time. It would be better without having to perform this validation here...
        // return this.dataManagementService.validateDiseaseEndpoints(disease);
        return disease;
    }


    @GetMapping("/services/linkout/{key}:{value}")
    @ResponseBody
    public JSONObject getSourceURL(@PathVariable String key, @PathVariable String value) {

        JSONObject res = new JSONObject();

        try {
            key = key.toLowerCase();

            if (this.protectedSources.contains(key)) res.put("protection", true);
            else res.put("protection", false);

            res.put("url", this.sources.get(key.toLowerCase()).replace("#replace#", value));

            return res;
        } catch (Exception ex) {
            Logger.getLogger(DiseasecardController.class.getName()).log(Level.INFO, "[Diseasecard][API][getSourceURL] Source not found");
            return null;
        }
    }


    @GetMapping("/services/browse")
    @ResponseBody
    public String getBrowserResultsByLetter(@RequestParam(name = "letter", required = true) String letter) {

        try {
            return jedis.get("browse:" + letter);
        } catch (Exception ex) {
            Finder finder = new Finder(this.connectionString);
            return finder.browse(letter);
        }
    }


    @GetMapping("/services/treeStructure")
    @ResponseBody
    public JSONArray getTreeStructure() {
        return this.dataManagementService.getTreeStructure();
    }


    @PostMapping("/startup")
    public void startInternalProcess() {
        Logger.getLogger(DiseasecardController.class.getName()).log(Level.INFO, "[Diseasecard][Controller] Receive alert to start my internal processing");
        this.boot.startInternalProcess();
    }


    @PostMapping("/dcadmin/uploadOntology")
    public Map<String, String> uploadOntology(@RequestParam("file") MultipartFile file) {
        Map<String, String> endpoints = dataManagementService.uploadSetup(file);
        //redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
        return endpoints;
    }


    @PostMapping(value = "/dcadmin/uploadEndpoints", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void uploadEndpoints(@RequestParam("information[]") List<MultipartFile> information) throws IOException {
        dataManagementService.uploadEndpoints(information);
    }


    @PostMapping(value = "/dcadmin/operations/addEntity")
    public ResponseEntity<String> addEntity(@RequestParam("titleEntity") String title,
                                            @RequestParam("labelEntity") String label,
                                            @RequestParam("descriptionEntity") String description,
                                            @RequestParam(name = "isEntityOfEntity", required = false) String entityOf) throws IOException {

        dataManagementService.prepareAddEntity(title, label, description, entityOf);
        return new ResponseEntity<>("Add Entity", HttpStatus.OK);
    }


    @PostMapping(value = "/dcadmin/operations/addConcept")
    public ResponseEntity<String> addConcept(@RequestParam("titleConcept") String title,
                                             @RequestParam("labelConcept") String label,
                                             @RequestParam("descriptionConcept") String description,
                                             @RequestParam("hasEntityConcept") String hasEntity,
                                             @RequestParam(name = "hasResourceConcept", required = false) String hasResource) throws IOException {

        dataManagementService.prepareAddConcept(title, label, description, hasEntity, hasResource);
        return new ResponseEntity<>("Add Concept", HttpStatus.OK);
    }


    @PostMapping(value = "/dcadmin/operations/addResource")
    public void addResource(@RequestParam("titleResource") String title,
                            @RequestParam("labelResource") String label,
                            @RequestParam("descriptionResource") String description,
                            @RequestParam("resourceOf") String resourceOf,
                            @RequestParam("extendsResource") String extendsResource,
                            @RequestParam("orderResource") String order,
                            @RequestParam("files") MultipartFile files,
                            @RequestParam("publisherEndpoint") String publisher) throws IOException {

        dataManagementService.prepareAddResource(title, label, description, resourceOf, extendsResource, order, publisher, files);
    }


    @PostMapping(value = "/dcadmin/operations/addResourceWithURLEndpoint")
    public void addResourceWithURLEndpoint(@RequestParam("titleResource") String title,
                                           @RequestParam("labelResource") String label,
                                           @RequestParam("descriptionResource") String description,
                                           @RequestParam("resourceOf") String resourceOf,
                                           @RequestParam("extendsResource") String extendsResource,
                                           @RequestParam("orderResource") String order,
                                           @RequestParam("publisherEndpoint") String publisher,
                                           @RequestParam("endpointResource") String endpoint) {

        dataManagementService.prepareAddResource(title, label, description, resourceOf, extendsResource, order, publisher, endpoint);
    }


    @PostMapping(value = "/dcadmin/operations/addOMIMResource")
    public void addOMIMResource(@RequestParam("titleResource") String title,
                                @RequestParam("labelResource") String label,
                                @RequestParam("descriptionResource") String description,
                                @RequestParam("resourceOf") String resourceOf,
                                @RequestParam("extendsResource") String extendsResource,
                                @RequestParam("orderResource") String order,
                                @RequestParam("publisherEndpoint") String publisher,
                                @RequestParam("morbidmap") MultipartFile morbidmap,
                                @RequestParam("genemap") MultipartFile genemap) {

        dataManagementService.prepareAddOMIMResource(title, label, description, resourceOf, extendsResource, order, publisher, morbidmap, genemap);
    }


    // TODO: Adicionar o isMethodBYReplace
    @PostMapping(value = "/dcadmin/operations/addCSVParser")
    public void addCSVParser(@RequestParam("resource") String resource,
                             @RequestParam("resourceID") String resourceID,
                             @RequestParam(name = "resourceRegex", required = false, defaultValue = "") String regexResource,
                             @RequestParam("externalResourceID") String externalResourceID,
                             @RequestParam(name = "externalResourceRegex", required = false, defaultValue = "") String regexExternalResource) {

        dataManagementService.prepareAddParser(resource, resourceID, regexResource, externalResourceID, regexExternalResource);
    }


    @PostMapping(value = "/dcadmin/operations/addXMLParser")
    public void addXMLParser(@RequestParam("resource") String resource,
                             @RequestParam("mainNode") String mainNode,
                             @RequestParam("isMethodByReplace") String isMethodByReplace,
                             @RequestParam("resourceInfoInAttribute") String resourceInfoInAttribute,
                             @RequestParam("resourceID") String resourceInfo,
                             @RequestParam("uniqueResource") String uniqueResource,
                             @RequestParam(name = "regexResource", required = false, defaultValue = "") String regexResource,
                             @RequestParam("externalResourceInfoInAttribute") String externalResourceInfoInAttribute,
                             @RequestParam(name = "externalResourceID", required = false, defaultValue = "") String externalResourceInfo,
                             @RequestParam("externalResourceNode") String externalResourceNode,
                             @RequestParam("uniqueExternalResource") String uniqueExternalResource,
                             @RequestParam(name = "filterBy", required = false, defaultValue = "") String filterBy,
                             @RequestParam(name = "filterValue", required = false, defaultValue = "") String filterValue,
                             @RequestParam(name = "regexExternalResource", required = false, defaultValue = "") String regexExternalResource) {

        dataManagementService.prepareAddParser(resource, mainNode, isMethodByReplace,
                resourceInfoInAttribute, resourceInfo, uniqueResource, regexResource, externalResourceInfoInAttribute,
                externalResourceInfo, externalResourceNode, regexExternalResource, uniqueExternalResource, filterBy, filterValue);
    }


    @PostMapping(value = "/dcadmin/operations/addOMIMParser")
    public void addOMIMParser(@RequestParam("resource") String resource,
                              @RequestParam("genecardName") String genecardName,
                              @RequestParam("genecardOMIM") String genecardOMIM,
                              @RequestParam("genecardLocation") String genecardLocation,
                              @RequestParam("genecardGenes") String genecardGenes,
                              @RequestParam("morbidmapName") String morbidmapName,
                              @RequestParam("morbidmapGene") String morbidmapGene,
                              @RequestParam("morbidmapOMIM") String morbidmapOMIM,
                              @RequestParam("morbidmapLocation") String morbidmapLocation) {
        dataManagementService.prepareAddOMIMParser(resource, genecardName, genecardOMIM, genecardLocation, genecardGenes, morbidmapName, morbidmapGene, morbidmapOMIM, morbidmapLocation);
    }


    @PostMapping(value = "/dcadmin/operations/editInstance")
    @ResponseBody
    public void editInstance(@RequestParam Map<String, String> allParams) {
        dataManagementService.prepareEditInstance(allParams);
    }


    @PostMapping(value = "/dcadmin/operations/editResourceSingleEndpoint")
    @ResponseBody
    public void editResourceSingleEndpoint(@RequestParam Map<String, String> allParams,
                                           @RequestParam("files") MultipartFile file) {
        dataManagementService.prepareEditResourceSingleEndpoint(allParams, file);
    }


    @PostMapping(value = "/dcadmin/operations/editResourceOMIMEndpoint")
    @ResponseBody
    public void editResourceOMIM(@RequestParam Map<String, String> allParams,
                                 @RequestParam("morbidmap") MultipartFile morbidmap,
                                 @RequestParam("genemap") MultipartFile genemap) {
        dataManagementService.prepareEditResourceOMIM(allParams, genemap, morbidmap);
    }


    @PostMapping(value = "/dcadmin/operations/removeInstance")
    @ResponseBody
    public void removeInstance(@RequestParam("uri") String uri, @RequestParam("typeOf") String typeOf) {
        dataManagementService.removeInstance(typeOf, uri);
    }


    @GetMapping(value = "/dcadmin/operations/buildSystem")
    public void buildSystem() {
        dataManagementService.build();
    }


    @GetMapping(value = "/dcadmin/operations/unbuildSystem")
    public void unbuildSystem() {
        dataManagementService.unbuild();
    }


    @GetMapping(value = "/dcadmin/operations/validateAllEndpoints")
    public ResponseEntity<Object> validateAllEndpoints() throws InterruptedException {
        Logger.getLogger(DiseasecardController.class.getName()).log(Level.INFO, "[Diseasecard][Controller] Forcing validation");
        new Thread(() -> dataManagementService.validateAllEndpoints()).start();
        Thread.sleep(1000);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


    @GetMapping(value = "/dcadmin/operations/validateEndpoints")
    public ResponseEntity<Object> validateEndpoints() throws InterruptedException {
        Logger.getLogger(DiseasecardController.class.getName()).log(Level.INFO, "[Diseasecard][Controller] Forcing validation");
        new Thread(() -> dataManagementService.validateEndpoints()).start();
        Thread.sleep(1000);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


    @GetMapping("/dcadmin/status/labels")
    public JSONObject getFormLabelsInfo() {
        return dataManagementService.getFormLabels();
    }


    @GetMapping("/dcadmin/status/parserFields")
    public JSONObject getParserFields(@PathVariable String parserType) {
        return null;
    }


    @GetMapping("/dcadmin/status/allEntities")
    public JSONObject getAllEntitiesInfo() {
        return dataManagementService.getAllEntities();
    }


    @GetMapping("/dcadmin/status/allConcepts")
    public JSONObject getAllConceptsInfo() {
        return dataManagementService.getAllConcepts();
    }


    @GetMapping("/dcadmin/status/allResources")
    public JSONArray getAllResources() {
        return dataManagementService.getAllResources();
    }


    @GetMapping("/dcadmin/status/ontologyStructure")
    public JSONArray getOntologyStructureInfo() {
        return dataManagementService.getOntologyStructure();
    }


    @GetMapping("/dcadmin/status/systemBuild")
    public String getSystemBuildStatus() {
        return dataManagementService.getSystemStatus();
    }


    @PostMapping("/send")
    public String sendMessage(@RequestParam("text") String text) {
        template.convertAndSend("/topic/message", text);
        return text;
    }


    @PostMapping("/dcadmin/utils/queryJenaModel")
    @ResponseBody
    public ResponseEntity<Object> queryJenaModel(@RequestParam(name = "query", required = true) String query) {
        try {
            return  ResponseEntity.ok(dataManagementService.queryJenaModel(query));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }


    @GetMapping("/dcadmin/utils/getPrefixes")
    @ResponseBody
    public JSONArray getPrefixes() {
        return dataManagementService.getPrefixes();
    }


    @GetMapping("/dcadmin/utils/systemStats")
    @ResponseBody
    public JSONObject getSystemStats() {
        return this.dataManagementService.getInstancesCount();
    }


    @GetMapping("/dcadmin/utils/ontology")
    @ResponseBody
    public JSONObject getOntology() {
        return this.dataManagementService.getSimplifiedOntologyStructure();
    }


    @GetMapping("/dcadmin/endpointManagement/getSourcesURLS")
    @ResponseBody
    public JSONArray getSourcesURLS() {
        return dataManagementService.getSources();
    }


    @GetMapping("/dcadmin/endpointManagement/getResourcesWithoutBaseURL")
    @ResponseBody
    public JSONArray getResourcesWithoutBaseURL() {
        return dataManagementService.getResourcesWithoutBaseURL();
    }


    @GetMapping("/dcadmin/endpointManagement/getAlertBoxResults")
    @ResponseBody
    public JSONObject getAlertBoxResults() {
        return dataManagementService.getAlertBoxResults();
    }


    @PostMapping("/dcadmin/endpointManagement/addSourceBaseURL")
    @ResponseBody
    public void addSourceBaseURL(@RequestParam("resourceLabel") String resourceLabel,
                                      @RequestParam("baseURL") String baseURL) {
        dataManagementService.prepareAddSourceBaseURL(resourceLabel, baseURL);
    }


    @PostMapping("/dcadmin/endpointManagement/editSourceBaseURL")
    @ResponseBody
    public void editSourceBaseURL(@RequestParam("resourceLabel") String resourceLabel,
                                  @RequestParam("baseURL") String baseURL) {
        dataManagementService.prepareEditSourceBaseURL(resourceLabel, baseURL);
    }


    @PostMapping("/dcadmin/endpointManagement/removeSourceBaseURL")
    @ResponseBody
    public void removeSourceBaseURL(@RequestParam("resourceLabel") String resourceLabel) {
        dataManagementService.prepareRemoveSourceBaseURL(resourceLabel);
    }

}
