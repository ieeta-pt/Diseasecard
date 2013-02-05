package pt.ua.bioinformatics.diseasecard.services;

import java.sql.PreparedStatement;
import pt.ua.bioinformatics.coeus.api.DB;

/**
 * Single thread handler for importing single resources.
 *
 * @author pedrolopes
 */
public class ActivityTracking implements Runnable {

    private static DB db = new DB("DC4", "jdbc:mysql://localhost:3306/diseasecard?user=root&password=telematica");
    private String query;
    private String action;
    private String url;
    private String useragent;
    private String ip;

    public static DB getDb() {
        return db;
    }

    public static void setDb(DB db) {
        ActivityTracking.db = db;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUseragent() {
        return useragent;
    }

    public void setUseragent(String useragent) {
        this.useragent = useragent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public ActivityTracking(String query, String action, String url, String useragent, String ip) {
        this.query = query;
        this.action = action;
        this.url = url;
        this.useragent = useragent;
        this.ip = ip;
    }

    /**
     * Launch single Resource import process.
     */
    public void run() {
        try {
            db.connect();
            String q = "INSERT INTO Activity(query, action, url, useragent,ip) VALUES(?, ? ,?, ?, ?);";
            PreparedStatement p = db.getConnection().prepareStatement(q);
            p.setString(1, query);
            p.setString(2, action);
            p.setString(3, url);
            p.setString(4, useragent);
            p.setString(5, ip);
            p.execute();
            db.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
