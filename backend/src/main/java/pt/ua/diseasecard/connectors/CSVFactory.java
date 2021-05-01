package pt.ua.diseasecard.connectors;

import com.hp.hpl.jena.rdf.model.Statement;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.components.data.Triplify;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import pt.ua.diseasecard.domain.Parser;
import pt.ua.diseasecard.domain.Resource;
import pt.ua.diseasecard.utils.BeanUtil;
import pt.ua.diseasecard.utils.ItemFactory;
import pt.ua.diseasecard.utils.Predicate;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVFactory implements ResourceFactory {

    private Resource res;
    private Triplify rdfizer;
    private boolean hasError;

    private final SparqlAPI api = BeanUtil.getBean(SparqlAPI.class);
    private DiseasecardProperties config = BeanUtil.getBean(DiseasecardProperties.class);

    public CSVFactory(Resource res) {
        this.res = res;
        this.rdfizer = null;
        this.hasError = false;
    }

    private List<String[]> readEndpoint() throws IOException {
        CsvParserSettings settings = new CsvParserSettings();
        settings.setNullValue("");
        settings.setEmptyValue("");
        settings.detectFormatAutomatically();
        CsvParser parser = new CsvParser(settings);
        try {
            // Read Local File
            if (!this.res.getEndpoint().contains("http"))
            {
                return parser.parseAll(new File("submittedFiles/endpoints/" + res.getLabel()));
            }
            // Read Online File
            else
            {
                URL u = new URL(res.getEndpoint());
                return parser.parseAll(new InputStreamReader(u.openStream()));
            }
        } catch (MalformedURLException ex) {
            //saveError(ex);
            Logger.getLogger(CSVFactory.class.getName()).log(Level.INFO,"[COEUS][CSVFactory] Impossible to read the file.");
            Logger.getLogger(CSVFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void read() {
        try
        {
            List<String[]> rows = readEndpoint();

            HashMap<String, String> extensions = res.getExtended();

            for ( String itemRaw : extensions.keySet() )
            {
                String item = ItemFactory.getTokenFromItem(itemRaw);
                for (String[] r : rows)
                {
                    Parser parser = res.getHasParser();
                    List<String> extendIdentifiers = this.getIDs(parser.getExternalResourceID(), parser.getExternalResourceRegex(), r);
                    for (String extendIdentifier : extendIdentifiers)
                    {
                        if ( extendIdentifier.equals(item) )
                        {
                            List<String> resIDs = this.getIDs(parser.getResourceID(), parser.getResourceRegex(), r);
                            for (String resID : resIDs)
                            {
                                rdfizer = new Triplify(res, extensions.get(itemRaw));

                                if (!res.getIdentifiers().equals(""))
                                {
                                    String[] tmp = res.getIdentifiers().split("\\|");
                                    for (String inside : tmp) rdfizer.add(inside, resID);
                                }
                                rdfizer.itemize(resID);
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            //saveError(ex);
            Logger.getLogger(CSVFactory.class.getName()).log(Level.INFO,"[COEUS][CSVFactory] Impossible to read the information.");
            Logger.getLogger(CSVFactory.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    @Override
    public void save() {
        try {
            //only change built property if there are no errors
            if (!hasError) {
                com.hp.hpl.jena.rdf.model.Resource resource = api.getResource(this.res.getUri());
                Statement statementToRemove = api.getModel().createLiteralStatement(resource, Predicate.get("coeus:built"), false);
                api.removeStatement(statementToRemove);
                api.addStatement(resource, Predicate.get("coeus:built"), true);
            }
            if (this.config.getDebug()) {
                Logger.getLogger(CSVFactory.class.getName()).log(Level.INFO,"[COEUS][API] Saved resource " + res.getUri());
            }
        } catch (Exception ex) {
            if (this.config.getDebug())
            {
                saveError(ex);
                Logger.getLogger(CSVFactory.class.getName()).log(Level.INFO,"[COEUS][API] Unable to save resource " + res.getUri());
                Logger.getLogger(CSVFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void saveError(Exception ex) {
        try
        {
            com.hp.hpl.jena.rdf.model.Resource resource = this.api.getResource(this.res.getUri());
            Statement statement = api.getModel().createLiteralStatement(resource, Predicate.get("dc:coverage"), "ERROR: "+ex.getMessage()+". For more information, please see the application server log.");
            api.addStatement(statement);
            hasError = true;

            if (this.config.getDebug()) Logger.getLogger(CSVFactory.class.getName()).log(Level.INFO,"[COEUS][API] Saved error on resource " + res.getUri());
        } catch (Exception e) {
            if (this.config.getDebug()) {
                Logger.getLogger(CSVFactory.class.getName()).log(Level.INFO,"[COEUS][API] Unable to save error on resource " + res.getUri());
                Logger.getLogger(CSVFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private List<String> getIDs(String identifier, String regex, String[] row) {
        List<String> ids = new ArrayList<>();

        if (!regex.isEmpty())
        {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(row[Integer.parseInt(identifier)]);
            while (m.find()) ids.add(m.group());
        }
        else
        {
            ids.add(row[Integer.parseInt(identifier)]);
        }

        return ids;
    }

}
