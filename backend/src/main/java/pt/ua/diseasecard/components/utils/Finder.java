package pt.ua.diseasecard.components.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import pt.ua.diseasecard.components.data.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Finder {

    private DB db;

    public Finder(String connectionString) {
        this.db = new DB("DC4", connectionString);
    }

    public String browse(String key) {
        JSONObject alldiseases = new JSONObject();
        JSONArray list = new JSONArray();
        PreparedStatement p;
        try {
            db.connect();
            p = db.getConnection().prepareStatement("SELECT * FROM Diseases WHERE name REGEXP ? ORDER BY name ASC;");
            if (key.equals("0")) {
                p.setString(1, "^[0-9\\[\\{].*");
            } else {
                p.setString(1, "^[" + key + "].*");
            }
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                JSONArray o = new JSONArray();
                o.put("<a rel=\"tooltip\" title=\"View " + rs.getString("name") + "\" href=\"./entry/" + rs.getInt("omim") + "\">" + rs.getInt("omim") + "</a>");
                o.put("<i class=\"icon-angle-right\"></i> <a rel=\"tooltip\" title=\"View " + rs.getString("name") + "\" href=\"./entry/" + rs.getInt("omim") + "\">" + rs.getString("name") + "</a><a href=\"http://omim.org/entry/" + rs.getInt("omim") + "\" class=\"pull-right\" target=\"_blank\"><i class=\"icon-external-link\"></i></a>");
                double progress = (rs.getInt("c") / 1127.0) * 100;
                String type;
                if (progress > 30.0) {
                    type = "";
                } else if (progress > 20.0) {
                    type = " progress-bar-info";
                } else if (progress > 15.0) {
                    type = " progress-bar-success";
                } else if (progress > 10.0) {
                    type = " progress-bar-warning";
                } else {
                    progress = 10.0;
                    type = " progress-bar-danger";
                }
                o.put("<div class=\"progress\"><div rel=\"tooltip\" title=\"" + rs.getInt("c") + " connections\" class=\"progress-bar " + type + "\" role=\"progressbar\" style=\"width: " + progress + "%;\">" + rs.getInt("c") + "</div></div>");
                list.put(o);
            }
            db.close();
            alldiseases.put("aaData", list);
        } catch (Exception ex) {
            System.out.println("[COEUS][Diseasecard][Finder] Unable to get diseases info");
            Logger.getLogger(Finder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return alldiseases.toString();
    }
}
