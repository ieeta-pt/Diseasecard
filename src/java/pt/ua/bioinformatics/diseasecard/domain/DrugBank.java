/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.diseasecard.domain;

import java.util.ArrayList;

/**
 *
 * @author pedrolopes
 */
public class DrugBank {
    private String id;
    private String name;
    private String pharmacology;
    private String description;
    private UniProt uniprot;
    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String drugbank_id) {
        this.id = drugbank_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPharmacology() {
        return pharmacology;
    }

    public void setPharmacology(String pharmacology) {
        this.pharmacology = pharmacology;
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
        this.name = name;
        this.pharmacology = pharmacology;
        this.description = description;
        this.uniprot = uniprot;
    }
}
