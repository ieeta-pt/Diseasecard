package pt.ua.diseasecard.connectors;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Statement;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.components.data.Storage;
import pt.ua.diseasecard.components.data.Triplify;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import pt.ua.diseasecard.connectors.plugins.OMIMPlugin;
import pt.ua.diseasecard.domain.Resource;
import pt.ua.diseasecard.utils.ItemFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configurable
public class CSVFactory implements ResourceFactory {

    private Resource res;
    private Triplify rdfizer;
    private boolean hasError;

    @Autowired
    private SparqlAPI api;

    @Autowired
    private DiseasecardProperties config;

    @Autowired
    private Storage storage;

    public CSVFactory(Resource res) {
        this.res = res;
        this.rdfizer = null;
        this.hasError = false;
    }

    private List<String[]> readEndpoint() throws IOException {
        CsvParserSettings settings = new CsvParserSettings();
        settings.detectFormatAutomatically();
        CsvParser parser = new CsvParser(settings);
        try {
            // Read Local File
            if (this.res.getEndpoint().contains("sourceFilesLocation"))
            {
                return parser.parseAll(new File("/usr/local/tomcat/datasets/malacards_diseasecards_dump.csv"));
            }
            // Read Online File
            else
            {
                URL u = new URL(res.getEndpoint());
                return parser.parseAll(new InputStreamReader(u.openStream()));
            }
        } catch (MalformedURLException ex) {
            //saveError(ex);
            System.out.println("[COEUS][CSVFactory] Impossible to read the file.");
            Logger.getLogger(CSVFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void read() {
        try
        {
            List<String[]> rows = readEndpoint();

            HashMap<String, String> extensions;
            if (res.getExtension().equals("")) extensions = res.getExtended();
            else                               extensions = res.getExtended(res.getExtension());

            for ( String itemRaw : extensions.keySet() )
            {
                String item = ItemFactory.getTokenFromItem(itemRaw);
                for (String[] r : rows)
                {
                    if ( r[Integer.parseInt(res.getRegex())].equals(item) )
                    {
                        rdfizer = new Triplify(res, extensions.get(itemRaw));
                        String[] tmp = res.getDescription().split("\\|");

                        for (String inside : tmp)
                        {
                            int col = Integer.parseInt(res.getQuery());
                            rdfizer.add(inside, r[col]);
                        }

                        rdfizer.itemize(r[Integer.parseInt(res.getQuery())]);
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            //saveError(ex);
            System.out.println("[COEUS][CSVFactory] Impossible to read the information.");
            Logger.getLogger(CSVFactory.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    @Override
    public void save() {
        try {
            //only change built property if there are no errors
            if (!hasError) {
                com.hp.hpl.jena.rdf.model.Resource resource = api.getResource(this.res.getUri());
                Map<String, String> prefixes = this.config.getPrefixes();
                Statement statementToRemove = api.getModel().createLiteralStatement(resource, this.storage.getProperty(prefixes.get("coeus:built")), false);
                api.removeStatement(statementToRemove);
                api.addStatement(resource, this.storage.getProperty(prefixes.get("coeus:built")), true);
            }
            if (this.config.getDebug()) {
                System.out.println("[COEUS][API] Saved resource " + res.getUri());
            }
        } catch (Exception ex) {
            if (this.config.getDebug())
            {
                saveError(ex);
                System.out.println("[COEUS][API] Unable to save resource " + res.getUri());
                Logger.getLogger(CSVFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void saveError(Exception ex) {
        try
        {
            com.hp.hpl.jena.rdf.model.Resource resource = this.api.getResource(this.res.getUri());
            Map<String, String> prefixes = this.config.getPrefixes();
            Statement statement = api.getModel().createLiteralStatement(resource, this.storage.getProperty(prefixes.get("dc:coverage")), "ERROR: "+ex.getMessage()+". For more information, please see the application server log.");
            api.addStatement(statement);
            hasError = true;

            if (this.config.getDebug()) System.out.println("[COEUS][API] Saved error on resource " + res.getUri());
        } catch (Exception e) {
            if (this.config.getDebug()) {
                System.out.println("[COEUS][API] Unable to save error on resource " + res.getUri());
                Logger.getLogger(CSVFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
