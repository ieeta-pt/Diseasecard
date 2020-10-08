/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.diseasecard.services;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.diseasecard.common.Config;

/**
 * This class is an upgrade of part of the old Run.java 
 * 
 * @author mfs98
 */
public class LoadResources {

    
    public static void load(String level)
    {
        System.out.println("[DISEASECARD][LOAD] Start loading process.");
        if (level.equals("FULL"))
        {
            for (Map.Entry<String, String[]> entry : Config.getResources().entrySet())
                startImport(entry.getKey(), entry.getValue());
        } 
        else
        {
            int l = Integer.parseInt(level.replace("level", ""));
            for (int i = 0 ; i <= l ; i++){
                startImport(level, Config.getResourcesByLevel("level" + i));
            }
                
        }   
    }
    
    private static void startImport(String l, String[] resourcesToLoad)
    {
        ArrayList<Thread> threads = new ArrayList();
        
        for (String resource : resourcesToLoad)
        {
//            SingleImport single = new SingleImport(resource);
//            Thread t = new Thread(single);
//            threads.add(t);
//            t.start();
        }
        
        
        try 
        {
            for (Thread thread : threads) 
                thread.join();
            System.out.println("[DISEASECARD][LOAD] Level " + l + "loaded");
        } 
        catch (InterruptedException ex) 
        {
            System.out.println("[DISEASECARD][LOAD] Not possible to load " + l);
            Logger.getLogger(LoadResources.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
