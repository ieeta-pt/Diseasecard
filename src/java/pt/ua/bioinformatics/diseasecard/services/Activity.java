package pt.ua.bioinformatics.diseasecard.services;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Activity logger for Diseasecard actions.
 * <p>Used to track relevant UI actions (click-through, links...) for future auditing.</p> 
 * 
 * @author pedrolopes
 */
public class Activity {

    /**
     * Log atomic activity entry.
     * 
     * @param query     Activity query.
     * @param action    Performed action.
     * @param url       Tracked URL.
     * @param useragent Browser User Agent.
     * @param ip        Origin IP address.
     */
    public static void log(String query, String action, String url, String useragent, String ip) {
        try {
            ActivityTracking at = new ActivityTracking(query, action, url, useragent, ip);
            Thread thread_at = new Thread(at);
            thread_at.start();
        } catch (Exception e) {
             Logger.getLogger(Activity.class.getName()).log(Level.SEVERE, null, e);
        }

    }
}
