/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.diseasecard.domain;

import pt.ua.bioinformatics.coeus.api.ItemFactory;

/**
 *
 * @author pedrolopes
 */
public class DrugBank {
    private String id;
    private UniProt uniprot;
    private String uri;    
    private Disease disease;

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String drugbank_id) {
        this.id = drugbank_id;
    }

    public UniProt getUniprot() {
        return uniprot;
    }

    public void setUniprot(UniProt uniprot) {
        this.uniprot = uniprot;
    }

    public DrugBank() {
    }
    
    public DrugBank(UniProt uniprot) {
        this.uniprot = uniprot;
    }

    public DrugBank(String drugbank_id, String name, String pharmacology, String description, UniProt uniprot) {
        this.id = drugbank_id;
        this.uniprot = uniprot;
    }

    public DrugBank(String uri, Disease disease) {
        this.uri = uri;
        this.disease = disease;
        this.id = ItemFactory.getTokenFromItem(uri);
    }
}
