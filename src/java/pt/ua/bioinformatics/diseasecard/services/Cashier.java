/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.diseasecard.services;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.coeus.api.ItemFactory;
import pt.ua.bioinformatics.coeus.common.Boot;
import redis.clients.jedis.Jedis;

/**
 *
 * @author pedrolopes
 */
public class Cashier {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Boot.start();
        Jedis jedis = new Jedis("localhost");
        ResultSet rs = Boot.getAPI().selectRS("SELECT ?u WHERE { ?u coeus:hasConcept diseasecard:concept_OMIM }", false);
        while (rs.hasNext()) {
            QuerySolution row = rs.next();
            String omim = ItemFactory.getTokenFromItem(ItemFactory.getTokenFromURI(row.get("u").toString()));
            try {
                URL dcard = new URL("http://localhost:8084/diseasecard/services/disease/" + omim + ".js");
                BufferedReader in = new BufferedReader(new InputStreamReader(dcard.openStream()));
                String inputLine = "";
                String response = "";
                while ((inputLine = in.readLine()) != null) {
                    response += inputLine;
                }
                in.close();
                jedis.set(omim, response);

                System.out.println("[Diseasecard][Cashier] cached " + omim);
            } catch (MalformedURLException e) {
                Logger.getLogger(Cashier.class.getName()).log(Level.SEVERE, null, e);
            } catch (IOException e) {
                Logger.getLogger(Cashier.class.getName()).log(Level.SEVERE, null, e);
            } catch (Exception e) {
                Logger.getLogger(Cashier.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        jedis.save();

    }
}
