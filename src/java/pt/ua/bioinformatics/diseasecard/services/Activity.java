package pt.ua.bioinformatics.diseasecard.services;

import java.sql.PreparedStatement;
import pt.ua.bioinformatics.coeus.api.DB;

/**
 *
 * @author pedrolopes
 */
public class Activity {

    private static DB db = new DB("DC4", "jdbc:mysql://localhost:3306/diseasecard?user=root&password=telematica");

    public static void log(String query, String action, String url, String useragent, String ip) {
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
