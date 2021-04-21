package pt.ua.diseasecard.utils;

import com.hp.hpl.jena.rdf.model.Property;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Predicate {
    public static HashMap<String, Property> predicates = new HashMap<String, Property>();

    public static HashMap<String, Property> getPredicates() {
        return predicates;
    }

    public static void setPredicates(HashMap<String, Property> predicates) {
        Predicate.predicates = predicates;
    }

    public static Property get(String what) {
        Property p = predicates.get(what);
        return p;
    }

    /**
     * Add new Predicate (Property) to in-memory predicate HashMap.
     *
     * @param shortname the Predicate shortname with <prefix>:<property>.
     * @param predicate the predicate Property (in Jena's structure).
     */
    public static void add(String shortname, Property predicate) {
        predicates.put(shortname, predicate);
    }

    /**
     * Helper method to list all predicates in COEUS SDB.
     *
     */
    public static void list() {
        Logger.getLogger(Predicate.class.getName()).log(Level.INFO,"[COEUS][Predicate] Listing all known predicates");
        for (String s : predicates.keySet()) {
            Logger.getLogger(Predicate.class.getName()).log(Level.INFO,"\t" + s + " - " + predicates.get(s));
        }
    }
}
