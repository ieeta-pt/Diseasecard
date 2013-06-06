package pt.ua.bioinformatics.coeus.actions.diseasecard;

import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import pt.ua.bioinformatics.coeus.common.Boot;

/**
 *
 * @author pedrolopes
 */
public class StartupListener implements HttpSessionListener {

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

    public void sessionCreated(HttpSessionEvent hse) {
        
    }

    public void sessionDestroyed(HttpSessionEvent hse) {
        
    }
}