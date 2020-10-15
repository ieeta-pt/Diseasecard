package pt.ua.diseasecard.components.data;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;
import pt.ua.diseasecard.components.utils.PrefixFactory;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Objects;


@Component
public class SparqlAPI {

    private Model model;
    private InfModel inferredModel;
    private JSONParser jsonParser;
    private DiseasecardProperties config;


    public SparqlAPI(Storage storage, DiseasecardProperties diseasecardProperties) {
        Objects.requireNonNull(storage);
        Objects.requireNonNull(diseasecardProperties);
        this.config = diseasecardProperties;
        this.model = storage.getModel();
        this.inferredModel = storage.getInfmodel();
        this.jsonParser = new JSONParser();
    }


    public ResultSet selectRS(String query, boolean inferred) {
        ResultSet response = null;
        try {
            String sparqlQuery = PrefixFactory.allToString() + query;
            QueryExecution qe = null;

            if (inferred)    qe = QueryExecutionFactory.create(sparqlQuery, inferredModel);
            else             qe = QueryExecutionFactory.create(sparqlQuery, model);

            response = qe.execSelect();

        } catch (Exception ex) {
            if (this.config.getDebug()) {
                System.out.println("[COEUS][API] Unable to select ResultSet items from COEUS Data");
                Logger.getLogger(SparqlAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return response;
    }
}
