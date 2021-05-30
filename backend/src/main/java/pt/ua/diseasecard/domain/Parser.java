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


    // OMIM's Properties
    private int genecardName;
    private int genecardOMIM;
    private int genecardLocation;
    private int genecardGenes;
    private int morbidmapName;
    private int morbidmapOMIM;
    private int morbidmapLocation;
    private int morbidmapGene;

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

    public int getGenecardName() {
        return genecardName;
    }
    public void setGenecardName(int genecardName) {
        this.genecardName = genecardName;
    }

    public int getGenecardOMIM() {
        return genecardOMIM;
    }
    public void setGenecardOMIM(int genecardOMIM) {
        this.genecardOMIM = genecardOMIM;
    }

    public int getGenecardLocation() {
        return genecardLocation;
    }
    public void setGenecardLocation(int genecardLocation) {
        this.genecardLocation = genecardLocation;
    }

    public int getGenecardGenes() {
        return genecardGenes;
    }
    public void setGenecardGenes(int genecardGenes) {
        this.genecardGenes = genecardGenes;
    }

    public int getMorbidmapName() {
        return morbidmapName;
    }
    public void setMorbidmapName(int morbidmapName) {
        this.morbidmapName = morbidmapName;
    }

    public int getMorbidmapOMIM() {
        return morbidmapOMIM;
    }
    public void setMorbidmapOMIM(int morbidmapOMIM) {
        this.morbidmapOMIM = morbidmapOMIM;
    }

    public int getMorbidmapLocation() {
        return morbidmapLocation;
    }
    public void setMorbidmapLocation(int morbidmapLocation) {
        this.morbidmapLocation = morbidmapLocation;
    }

    public int getMorbidmapGene() {
        return morbidmapGene;
    }
    public void setMorbidmapGene(int morbidmapGene) {
        this.morbidmapGene = morbidmapGene;
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

    public String parserOMIMToString() {
        return "Parser{" +
                "genecardName=" + genecardName +
                ", genecardOMIM=" + genecardOMIM +
                ", genecardLocation=" + genecardLocation +
                ", genecardGenes=" + genecardGenes +
                ", morbidmapName=" + morbidmapName +
                ", morbidmapOMIM=" + morbidmapOMIM +
                ", morbidmapLocation=" + morbidmapLocation +
                ", morbidmapGene=" + morbidmapGene +
                '}';
    }
}
