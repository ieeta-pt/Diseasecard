package pt.ua.diseasecard.components.data;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.*;
import org.springframework.stereotype.Component;
import pt.ua.diseasecard.utils.PrefixFactory;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Objects;


@Component
public class SparqlAPI {

    private Model model;
    private InfModel inferredModel;
    private DiseasecardProperties config;


    public SparqlAPI(DiseasecardProperties diseasecardProperties) {
        Objects.requireNonNull(diseasecardProperties);
        this.config = diseasecardProperties;
//        this.model = storage.getModel();
//        this.inferredModel = storage.getInfmodel();
    }


    public ResultSet selectRS(String query, boolean inferred) {
        ResultSet response = null;
        try {
            String sparqlQuery = PrefixFactory.allToString() + query;
            QueryExecution qe;

            if ( inferred )  qe = QueryExecutionFactory.create(sparqlQuery, inferredModel);
            else             qe = QueryExecutionFactory.create(sparqlQuery, model);

            long startTime = System.nanoTime();
            response = qe.execSelect();
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            //System.out.println("[Diseasecard][SparqlAPI][selectRS] Query execution took " + duration);

        } catch (Exception ex) {
            if (this.config.getDebug()) {
                Logger.getLogger(SparqlAPI.class.getName()).log(Level.INFO, "[COEUS][API] Unable to select ResultSet items from COEUS Data");
                Logger.getLogger(SparqlAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return response;
    }

    public String select(String query, String format, boolean inferred) {
        String response = "";
        try
        {
            String sparqlQuery = PrefixFactory.allToString() + query;

            QueryExecution qe = null;
            if (inferred)  qe = QueryExecutionFactory.create(sparqlQuery, inferredModel);
            else           qe = QueryExecutionFactory.create(sparqlQuery, model);

            response = execute(qe, format);
        }
        catch (Exception ex)
        {
            if (this.config.getDebug()) Logger.getLogger(SparqlAPI.class.getName()).log(Level.INFO, "[COEUS][API] Unable to select items from COEUS Data");
            Logger.getLogger(SparqlAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    public String getOmimName(String omim) {
        try {
            return this.getTriple("diseasecard:omim_" + omim, "diseasecard:name", "o", "csv").split("\n")[1];
        } catch (Exception e) {
            return "";
        }
    }

    private String getTriple(String sub, String pred, String obj, String format) {
        String response = "";
        String select = "SELECT ?s_sub ?s_pred ?s_obj WHERE {?w_sub ?w_pred ?w_obj}";
        if (sub.equals("s") || sub.equals("sub") || sub.equals("subject")) {
            select = select.replace("?s_sub", "?" + sub).replace("?w_sub", "?" + sub);
        } else {
            select = select.replace("?s_sub", "").replace("?w_sub", sub);
        }
        if (pred.equals("p") || pred.equals("pred") || pred.equals("predicate")) {
            select = select.replace("?s_pred", "?" + pred).replace("?w_pred", "?" + pred);
        } else {
            select = select.replace("?s_pred", "").replace("?w_pred", pred);
        }
        if (obj.equals("o") || obj.equals("obj") || obj.equals("object")) {
            select = select.replace("?s_obj", "?" + obj).replace("?w_obj", "?" + obj);
        } else {
            select = select.replace("?s_obj", "").replace("?w_obj", obj);
        }
        try {
            String sparqlQuery = PrefixFactory.allToString() + select;
            QueryExecution qe = QueryExecutionFactory.create(sparqlQuery, model);
            response = execute(qe, format);
        } catch (Exception ex) {
            if (this.config.getDebug()) {
                Logger.getLogger(SparqlAPI.class.getName()).log(Level.INFO, "[Diseasecard][SparqlAPI] Unable to get triple from COEUS Data");
                Logger.getLogger(SparqlAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return response;
    }

    private String execute(QueryExecution qe, String format) {
        String response = "";
        try {
            ResultSet rs = qe.execSelect();
            if (format.equals("txt") || format.equals("text")) {
                response = ResultSetFormatter.asText(rs);
            } else if (format.equals("json") || format.equals("js")) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ResultSetFormatter.outputAsJSON(os, rs);
                response = os.toString();
            } else if (format.equals("xml")) {
                response = ResultSetFormatter.asXMLString(rs);
            } else if (format.equals("rdf")) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ResultSetFormatter.outputAsRDF(os, "RDF/XML", rs);
                response = os.toString();
            } else if (format.equals("csv")) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ResultSetFormatter.outputAsCSV(os, rs);
                response = os.toString();
            }
        } catch (Exception ex) {
            if (this.config.getDebug()) {
                Logger.getLogger(SparqlAPI.class.getName()).log(Level.INFO, "[Diseasecard][SparqlAPI] Unable to get triple from COEUS Data");
                Logger.getLogger(SparqlAPI.class.getName()).log(Level.INFO,"[Diseasecard][SparqlAPI] Unable to execute SPARQL select");
                Logger.getLogger(SparqlAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return response;
    }

    public void addStatement(Statement statement) {
        try
        {
            this.model.add(statement);
        } catch (Exception ex)
        {
            if (this.config.getDebug())
            {
                String stat = statement.getSubject() + " " + statement.getPredicate() + " " + statement.getObject();
                Logger.getLogger(SparqlAPI.class.getName()).log(Level.INFO,"[COEUS][API] Unable add statement in the model: " + stat);
                Logger.getLogger(SparqlAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void addStatement(Resource subject, Property predicate, boolean object) throws Exception {
        try
        {
            this.model.addLiteral(subject, predicate, object);
        }
        catch (Exception ex)
        {
            if (this.config.getDebug()) Logger.getLogger(SparqlAPI.class.getName()).log(Level.INFO,"[COEUS][API] Unable to add triple to database");
            Logger.getLogger(SparqlAPI.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    public void addStatement(Resource subject, Property predicate, Resource object) throws Exception {
        try
        {
            this.model.add(subject, predicate, object);
        }
        catch (Exception ex)
        {
            if (this.config.getDebug()) Logger.getLogger(SparqlAPI.class.getName()).log(Level.INFO,"[COEUS][API] Unable to add triple to database");
            Logger.getLogger(SparqlAPI.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    public void addStatement(Resource subject, Property predicate, String object) throws Exception {
        try
        {
            this.model.add(subject, predicate, object);
        }
        catch (Exception ex)
        {
            if (this.config.getDebug()) Logger.getLogger(SparqlAPI.class.getName()).log(Level.INFO,"[COEUS][API] Unable to add triple to database. (" + subject + "| " + predicate + "| " + object + ")");
            Logger.getLogger(SparqlAPI.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    public Resource createResource(String uri) {
        Resource resource = null;
        try
        {
            resource = this.model.createResource(uri);
        }
        catch (Exception ex)
        {
            if (this.config.getDebug()) Logger.getLogger(SparqlAPI.class.getName()).log(Level.INFO,"[COEUS][API] Unable to create new Resource in model");
            Logger.getLogger(SparqlAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resource;
    }

    public void removeStatement(Statement statement) {
        try
        {
            this.model.remove(statement);
        } catch (Exception ex)
        {
            if (this.config.getDebug()) Logger.getLogger(SparqlAPI.class.getName()).log(Level.INFO,"[COEUS][API] Unable to remove statement from the model");
            Logger.getLogger(SparqlAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Resource getResource(String uri) {
        Resource resource = null;
        try
        {
            resource = this.model.getResource(uri);
        } catch (Exception ex)
        {
            if (this.config.getDebug()) Logger.getLogger(SparqlAPI.class.getName()).log(Level.INFO,"[COEUS][API] Unable to obtain new Resource from Model");
            Logger.getLogger(SparqlAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resource;
    }

    public Model getModel() {
        return this.model;
    }
    public void setModel(Model model) {
        this.model = model;
    }

    public InfModel getInferredModel() {
        return inferredModel;
    }
    public void setInferredModel(InfModel inferredModel) {
        this.inferredModel = inferredModel;
    }
}
