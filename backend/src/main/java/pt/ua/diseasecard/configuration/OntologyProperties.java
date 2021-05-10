package pt.ua.diseasecard.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.List;


@Configuration
@PropertySource("classpath:configuration/ontology.properties")
@ConfigurationProperties(prefix = "ontology")
public class OntologyProperties {

    @Value("#{'${ontology.pluginLabels}'.split(',')}")
    private ArrayList<String> pluginLabels;


    public OntologyProperties() { }


    public ArrayList<String> getPluginLabels() {
        return pluginLabels;
    }
    public void setPluginLabels(ArrayList<String> pluginLabels) {
        this.pluginLabels = pluginLabels;
    }
}
