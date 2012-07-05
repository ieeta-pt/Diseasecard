package pt.ua.bioinformatics.coeus.actions.diseasecard;

import javax.servlet.ServletContextEvent;
import pt.ua.bioinformatics.coeus.common.Boot;
import pt.ua.bioinformatics.diseasecard.domain.Disease;

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
        Disease d = new Disease(114480);
    }

    /**
     * Code to be executed on Tomcat server shutdown. Nothing actually.
     * 
     * @param sce 
     */
    public void contextDestroyed(ServletContextEvent sce) {
    }    
}