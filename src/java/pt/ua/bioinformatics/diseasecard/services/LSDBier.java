package pt.ua.bioinformatics.diseasecard.services;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.diseasecard.common.Boot;
import pt.ua.bioinformatics.diseasecard.common.Config;

/**
 * Utility class to load LSDB data into cache.
 *
 * @author pedrolopes
 */
public class LSDBier {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //*
        process();
        //*/

    }

    /**
     * Cache LSDB URLs in Redis cache for quick access
     */
    public static void process() {
        Boot.start();
        ResultSet rs = Boot.getAPI().selectRS("SELECT ?lsdb ?url { ?l coeus:hasConcept diseasecard:concept_LSDB . ?l dc:title ?lsdb . ?l dc:description ?url }", false);
        while (rs.hasNext()) {

            try {
                QuerySolution row = rs.next();
                Boot.getJedis().set("lsdb:" + row.get("lsdb").toString(), row.get("url").toString());
            } catch (Exception ex) {
                if (Config.isDebug()) {
                    System.out.println("[COEUS][Diseasecard][LSDBier] Unable to cache lsdb information.");
                    Logger.getLogger(LSDBier.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        Boot.getJedis().save();
    }
}
