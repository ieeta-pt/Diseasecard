package pt.ua.diseasecard.domain;

import java.util.HashMap;

public class Concept {

    private String description;
    private String label;
    private String title;
    private String uri = "";
    private Resource hasResource;
    private HashMap<String, String> properties = new HashMap<String, String>();
    private Entity hasEntity;

    public Concept(String uri) {
        this.uri = uri;
        this.label = uri.substring(uri.lastIndexOf('/') + 1);
    }

    public Concept(String uri, String title, String label) {
        this.uri = uri;
        this.label = label;
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Resource getHasResource() {
        return hasResource;
    }

    public void setHasResource(Resource hasResource) {
        this.hasResource = hasResource;
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }

    public Entity getHasEntity() {
        return hasEntity;
    }

    public void setHasEntity(Entity hasEntity) {
        this.hasEntity = hasEntity;
    }
}
