package pt.ua.bioinformatics.diseasecard.services;

import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.coeus.api.DB;
import pt.ua.bioinformatics.coeus.common.Config;

/**
 * Utility class to store feedback messages on database.
 * 
 * @author pedrolopes
 */
public class Feedback {

    private String email;
    private String message;
    private DB db;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Feedback(String email, String message) {
        this.email = email;
        this.message = message;
        this.db = new DB("DC4");
    }

    /**
     * Save message on database.
     * 
     * @return success of the operation.
     */
    public boolean save() {
        boolean success = false;
        try {
            db.connect(DC4.getIndexString());
            String q = "INSERT INTO feedback(email, message) values(?, ?);";
            java.sql.PreparedStatement p = db.getConnection().prepareStatement(q);
            p.setString(1, this.email);
            p.setString(2, this.message);
            p.execute();
            success = true;
        } catch (Exception ex) {
            if (Config.isDebug()) {
                System.out.println("[COEUS][Diseasecard][Finder] Unable to save feedback");
                Logger.getLogger(Feedback.class.getName()).log(Level.SEVERE, null, ex);
            }
            db.close();
        } finally {
            db.close();
        }
        return success;
    }
}
