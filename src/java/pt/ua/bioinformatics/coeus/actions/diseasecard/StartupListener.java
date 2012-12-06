package pt.ua.bioinformatics.coeus.actions.diseasecard;

import javax.servlet.ServletContextEvent;
import pt.ua.bioinformatics.coeus.common.Boot;

/**
 *
 * @author pedrolopes
 */
public class StartupListener implements javax.servlet.ServletContextListener {

    /**
     * Code to be executed on Tomcat server startup.
     * 
     * @param sce 
     */
    public void contextInitialized(ServletContextEvent sce) {
        Boot.start();
    }

    /**
     * Code to be executed on Tomcat server shutdown. Nothing actually.
     * 
     * @param sce 
     */
    public void contextDestroyed(ServletContextEvent sce) {
    }    
}