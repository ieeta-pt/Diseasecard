package pt.ua.bioinformatics.diseasecard.services;

/**
 *
 * @author pedrolopes
 */
public class Activity {

    public static void log(String query, String action, String url, String useragent, String ip) {
        try {
        ActivityTracking at = new ActivityTracking(query, action, url, useragent, ip);
        Thread thread_at = new Thread(at);
        thread_at.start();
        } catch(Exception e){
            
        }

    }
}
