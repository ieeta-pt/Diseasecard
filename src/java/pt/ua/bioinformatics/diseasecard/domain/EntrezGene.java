package pt.ua.bioinformatics.diseasecard.domain;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.coeus.api.ItemFactory;
import pt.ua.bioinformatics.coeus.api.PrefixFactory;
import pt.ua.bioinformatics.coeus.common.Boot;
import pt.ua.bioinformatics.coeus.common.Config;

/**
 * Entrez Gene mirror class.
 * 
 * @author pedrolopes
 * @deprecated
 */
public class EntrezGene {

    private String id;
    private String uri;
    private ArrayList<PharmGKB> pharmgkb = new ArrayList<PharmGKB>();
    private OMIM omim;
    private String label;
    private Disease disease;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public OMIM getOmim() {
        return omim;
    }

    public void setOmim(OMIM omim) {
        this.omim = omim;
    }

    public ArrayList<PharmGKB> getPharmgkb() {
        return pharmgkb;
    }

    public void setPharmgkb(ArrayList<PharmGKB> pharmgkb) {
        this.pharmgkb = pharmgkb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public EntrezGene() {
    }

    public EntrezGene(OMIM om, String uri) {
        this.uri = uri;
        this.omim = om;
        this.disease = om.getDisease();
        this.id = ItemFactory.getTokenFromItem(uri);
    }

    public boolean load() {
        boolean success = false;

        try {
            ResultSet results = Boot.getAPI().selectRS("SELECT ?p ?o {<" + uri + "> ?p ?o }", false);
            while (results.hasNext()) {
                QuerySolution row = results.next();
                if (PrefixFactory.encode(row.get("p").toString()).equals("rdfs:label")) {
                    this.label = row.get("o").toString();
                } else if (PrefixFactory.encode(row.get("p").toString()).equals("dc:title")) {
                    this.id = row.get("o").toString();
                } else if (PrefixFactory.encode(row.get("p").toString()).equals("coeus:isAssociatedTo")) {
                    if (row.get("o").toString().contains("pharmgkb")) {
                        String code = ItemFactory.getTokenFromItem(row.get("o").toString());
                        if (!this.disease.getDrug().getPharmgkb().containsKey(code)) {
                            PharmGKB pm = new PharmGKB(row.get("o").toString(), this);
                            this.pharmgkb.add(pm);
                            this.disease.getDrug().getPharmgkb().put(code, pm);
                        } else {
                            this.pharmgkb.add(this.disease.getDrug().getPharmgkb().get(code));
                        }
                    }
                }
            }
            success = true;
        } catch (Exception ex) {
            if (Config.isDebug()) {
                System.out.println("[COEUS][API] Unable to execute SPARQL select");
                Logger.getLogger(EntrezGene.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return success;
    }
}
