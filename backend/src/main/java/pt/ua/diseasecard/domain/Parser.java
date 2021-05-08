package pt.ua.diseasecard.domain;

public class Parser {

    private String mainNode;
    private String resourceID;
    private boolean resourceInfoInAttribute;
    private String resourceInfoAttribute;
    private String resourceRegex;

    private String externalResourceNode;
    private String externalResourceID;
    private boolean externalResourceInfoInAttribute;
    private String externalResourceInfoAttribute;
    private String externalResourceRegex;

    private String filterBy;
    private String filterValue;

    private boolean methodByReplace;

    private boolean uniqueResource;
    private boolean uniqueExternalResource;

    public Parser(String resourceID) {
        this.resourceID = resourceID;
    }

    public String getMainNode() {
        return mainNode;
    }
    public void setMainNode(String mainNode) {
        this.mainNode = mainNode;
    }

    public String getResourceID() {
        return resourceID;
    }
    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    public boolean getResourceInfoInAttribute() {
        return resourceInfoInAttribute;
    }
    public void setResourceInfoInAttribute(boolean resourceInfoInAttribute) {
        this.resourceInfoInAttribute = resourceInfoInAttribute;
    }

    public String getResourceInfoAttribute() {
        return resourceInfoAttribute;
    }
    public void setResourceInfoAttribute(String resourceInfoAttribute) {
        this.resourceInfoAttribute = resourceInfoAttribute;
    }

    public String getResourceRegex() {
        return resourceRegex;
    }
    public void setResourceRegex(String resourceRegex) {
        this.resourceRegex = resourceRegex;
    }

    public String getExternalResourceNode() {
        return externalResourceNode;
    }
    public void setExternalResourceNode(String externalResourceNode) {
        this.externalResourceNode = externalResourceNode;
    }

    public String getExternalResourceID() {
        return externalResourceID;
    }
    public void setExternalResourceID(String externalResourceID) {
        this.externalResourceID = externalResourceID;
    }

    public boolean getExternalResourceInfoInAttribute() {
        return externalResourceInfoInAttribute;
    }
    public void setExternalResourceInfoInAttribute(boolean externalResourceInfoInAttribute) {
        this.externalResourceInfoInAttribute = externalResourceInfoInAttribute;
    }

    public String getExternalResourceInfoAttribute() {
        return externalResourceInfoAttribute;
    }
    public void setExternalResourceInfoAttribute(String externalResourceInfoAttribute) {
        this.externalResourceInfoAttribute = externalResourceInfoAttribute;
    }

    public String getExternalResourceRegex() {
        return externalResourceRegex;
    }
    public void setExternalResourceRegex(String externalResourceRegex) {
        this.externalResourceRegex = externalResourceRegex;
    }

    public String getFilterBy() {
        return filterBy;
    }
    public void setFilterBy(String filterBy) {
        this.filterBy = filterBy;
    }

    public String getFilterValue() {
        return filterValue;
    }
    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public boolean getUniqueResource() {
        return uniqueResource;
    }
    public void setUniqueResource(boolean uniqueResource) {
        this.uniqueResource = uniqueResource;
    }

    public boolean getUniqueExternalResource() {
        return uniqueExternalResource;
    }
    public void setUniqueExternalResource(boolean uniqueExternalResource) {
        this.uniqueExternalResource = uniqueExternalResource;
    }

    public boolean isMethodByReplace() {
        return methodByReplace;
    }
    public void setMethodByReplace(boolean methodByReplace) {
        this.methodByReplace = methodByReplace;
    }

    @Override
    public String toString() {
        return "Parser{" +
                "mainNode='" + mainNode + '\'' +
                ", resourceID='" + resourceID + '\'' +
                ", resourceInfoInAttribute=" + resourceInfoInAttribute +
                ", resourceInfoAttribute='" + resourceInfoAttribute + '\'' +
                ", resourceRegex='" + resourceRegex + '\'' +
                ", externalResourceNode='" + externalResourceNode + '\'' +
                ", externalResourceID='" + externalResourceID + '\'' +
                ", externalResourceInfoInAttribute=" + externalResourceInfoInAttribute +
                ", externalResourceInfoAttribute='" + externalResourceInfoAttribute + '\'' +
                ", externalResourceRegex='" + externalResourceRegex + '\'' +
                ", filterBy='" + filterBy + '\'' +
                ", filterValue='" + filterValue + '\'' +
                ", methodByReplace=" + methodByReplace +
                ", uniqueResource=" + uniqueResource +
                ", uniqueExternalResource=" + uniqueExternalResource +
                '}';
    }
}
