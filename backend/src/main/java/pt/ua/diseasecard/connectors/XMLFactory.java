package pt.ua.diseasecard.connectors;

import com.hp.hpl.jena.rdf.model.Statement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.components.data.Triplify;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import pt.ua.diseasecard.domain.Parser;
import pt.ua.diseasecard.domain.Resource;
import pt.ua.diseasecard.utils.BeanUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import org.w3c.dom.Document;
import pt.ua.diseasecard.utils.ItemFactory;
import pt.ua.diseasecard.utils.Predicate;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URLConnection;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLFactory implements ResourceFactory {

    private Resource res;
    private Triplify rdfizer;
    private boolean hasError;
    private Map<String, Set<String>> info;

    private final SparqlAPI api = BeanUtil.getBean(SparqlAPI.class);
    private DiseasecardProperties config = BeanUtil.getBean(DiseasecardProperties.class);

    public XMLFactory(Resource res) {
        this.res = res;
        this.rdfizer = null;
        this.hasError = false;
        this.info = new HashMap<>();
    }


    private void readEndpoint() throws IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try
        {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc;

            if (!this.res.getEndpoint().contains("http"))
            {
                doc =  db.parse(new File("submittedFiles/endpoints/" + res.getLabel()));
            }
            else
            {
                URLConnection conn = new URL(res.getEndpoint()).openConnection();
                InputStream is = conn.getInputStream();
                doc =  db.parse(is);
            }
            this.getResourceInfo(doc);
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }


    private void readSpecificEndpoint(String item) {

        this.info = new HashMap<>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        String uri = this.res.getEndpoint().replace("#REPLACE", item);
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            InputStream is = new URL(uri).openConnection().getInputStream();
            this.getResourceInfo(db.parse(is));
            is.close();
        }
        catch(ParserConfigurationException | SAXException e) {
            Logger.getLogger(XMLFactory.class.getName()).log(Level.INFO,"[Diseasecard][XMLFactory] Unable to read endpoint " + uri);
        }
        catch (Exception e) {
            Logger.getLogger(XMLFactory.class.getName()).log(Level.INFO,"[Diseasecard][XMLFactory] Unable to read endpoint " + uri);
            //e.printStackTrace();
        }
    }


    private void getResourceInfo(Document doc) {
        Parser parser = res.getHasParser();

        String resourceID = parser.getResourceID();
        boolean resourceInfoInAttribute = parser.getResourceInfoInAttribute();
        String resourceInfoAttribute = parser.getResourceInfoAttribute();
        boolean uniqueR = parser.getUniqueResource();

        NodeList nodeList = doc.getElementsByTagName(parser.getMainNode());

        for (int i = 0 ; i < nodeList.getLength() ; i++)
        {
            Node node = nodeList.item(i);
            Element element = (Element) node;
            String id = null;

            // TODO: ter em conta o filter by
            if (uniqueR)
            {
                id = getID(element, resourceInfoInAttribute, resourceInfoAttribute, resourceID, parser.getResourceRegex());
                this.getExternalResourceInfo(id, element);
            }
            else
            {
                NodeList resourcesNodes = element.getElementsByTagName(resourceID);
                for (int j = 0 ; j < resourcesNodes.getLength() ; j++) {
                    Node externalReference = resourcesNodes.item(j);
                    Element e = (Element) externalReference;

                    id = getID(e, resourceInfoInAttribute, resourceInfoAttribute, resourceID, parser.getResourceRegex());
                    this.getExternalResourceInfo(id, e);
                }
            }
        }
    }


    private void getExternalResourceInfo(String id, Element element) {
        Parser parser = res.getHasParser();

        String externalResourceNode = parser.getExternalResourceNode();
        String externalResourceID = parser.getExternalResourceID();
        boolean externalResourceInfoInAttribute = parser.getExternalResourceInfoInAttribute();
        String externalResourceAttribute = parser.getExternalResourceInfoAttribute();
        String filterBy = parser.getFilterBy();
        String filterValue = parser.getFilterValue();
        boolean uniqueExtR = parser.getUniqueExternalResource();

        NodeList externalResourcesNodes = element.getElementsByTagName(externalResourceNode);

        if (!uniqueExtR)
        {
            for (int j = 0 ; j < externalResourcesNodes.getLength() ; j++) {
                Node externalReference = externalResourcesNodes.item(j);
                Element e = (Element) externalReference;
                String extRID = null;


                if (!filterBy.equals(""))
                {
                    String type;
                    if (externalResourceInfoInAttribute)
                    {
                        type = e.getAttribute(filterBy);
                        if (type.equals(filterValue)) extRID = e.getAttribute(externalResourceAttribute);
                    }
                    else
                    {
                        type = e.getElementsByTagName(filterBy).item(0).getTextContent();
                        if (type.equals(filterValue)) extRID = e.getElementsByTagName(externalResourceID).item(0).getTextContent();
                    }
                }
                else extRID = getID(e, externalResourceInfoInAttribute, externalResourceAttribute, externalResourceID, parser.getExternalResourceRegex());


                if (extRID != null)
                {
                    // System.out.println(id + " " + extRID);
                    if (!this.info.containsKey(extRID)) {
                        this.info.put(extRID, new HashSet<String>());
                    }
                    this.info.get(extRID).add(id);
                }
            }
        }
        else
        {
//            String type = e.getElementsByTagName(filterBy).item(0).getTextContent();
//            if (type.equals(filterValue))
//            {
//                // If External Resource's ID is defined through attribute
//                if (externalResourceInfoInTag) extRID = e.getElementsByTagName(externalResourceID).item(0).getAttributes().getNamedItem(externalResourceTag).getNodeValue();
//                // If External Resource's ID is defined in tag context
//                else extRID = e.getElementsByTagName(externalResourceID).item(0).getTextContent();
//            }
        }
    }


    private String getID(Element element, boolean resourceInAttribute, String resourceAttribute, String resourceID, String regex) {
        String id;
        // If Resource's ID is defined through attribute
        if (resourceInAttribute) id = ((Element) element.getElementsByTagName(resourceID).item(0)).getAttribute(resourceAttribute);
            // If Resource's ID is defined in tag context
        else id = element.getElementsByTagName(resourceID).item(0).getTextContent();

        if (!regex.equals(""))
        {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(id);
            if (m.find()) id = m.group();
        }

        return id;
    }


    @Override
    public void read() {
        try {
            if (!res.getHasParser().isMethodByReplace()) this.readEndpoint();

            HashMap<String, String> extensions = res.getExtended();

            for (String itemRaw : extensions.keySet() )
            {
                String item = ItemFactory.getTokenFromItem(itemRaw);

                if (res.getHasParser().isMethodByReplace()) this.readSpecificEndpoint(item);

                if ( this.info.containsKey(item) )
                {
                    rdfizer = new Triplify(res, extensions.get(itemRaw));

                    for (String r : this.info.get(item))
                    {
                        if (!res.getIdentifiers().equals(""))
                        {
                            String[] tmp = res.getIdentifiers().split("\\|");
                            for (String inside : tmp ) rdfizer.add(inside, r);
                        }
                        rdfizer.itemize(r);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                Logger.getLogger(XMLFactory.class.getName()).log(Level.INFO,"[COEUS][API] Saved resource " + res.getUri());
            }
        } catch (Exception ex) {
            if (this.config.getDebug())
            {
                saveError(ex);
                Logger.getLogger(XMLFactory.class.getName()).log(Level.INFO,"[COEUS][API] Unable to save resource " + res.getUri());
                Logger.getLogger(XMLFactory.class.getName()).log(Level.SEVERE, null, ex);
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

            if (this.config.getDebug()) Logger.getLogger(XMLFactory.class.getName()).log(Level.INFO,"[COEUS][API] Saved error on resource " + res.getUri());
        } catch (Exception e) {
            if (this.config.getDebug()) {
                Logger.getLogger(XMLFactory.class.getName()).log(Level.INFO,"[COEUS][API] Unable to save error on resource " + res.getUri());
                Logger.getLogger(XMLFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
