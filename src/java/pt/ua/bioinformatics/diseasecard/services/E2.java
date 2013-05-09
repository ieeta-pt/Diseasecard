/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.diseasecard.services;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.coeus.common.Boot;
import pt.ua.bioinformatics.diseasecard.engine.ICD10Processor;

/**
 *
 * @author pedrolopes
 */
public class E2 {

    static HashMap<String, String> map = new HashMap<String, String>();
    static String icd = "G10";
    static ExecutorService es = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws InterruptedException {
        load();

    }

    static void load() {
        try {
            Boot.start();
            ICD10Processor i = new ICD10Processor(map, icd, es);
            es.execute(i);
            es.shutdown();
            boolean finished = es.awaitTermination(2, TimeUnit.SECONDS);
            if (finished) {
                for (String s : map.keySet()) {
                    System.out.println(s + " -> " + map.get("s"));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(E2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
