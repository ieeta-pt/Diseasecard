{
    "config": {
        "name": "Diseasecard",
        "description": "Diseasecard: rare genetic diseases research portal",
        "keyprefix":"diseasecard",
        "version": "4.5",
        "ontology": "http://bioinformatics.ua.pt/diseasecard/diseasecard.owl",
        "setup": "dc4_setup.rdf",
        "sdb":"dc4_sdb.ttl",
        "predicates":"dc4_predicates.csv",
        "built": false,
        "load": false,
        "debug": true,
        "environment": "testing",
        "index": "http://container_solr:8983/solr"
    },
    "prefixes" : {
        "coeus": "http://bioinformatics.ua.pt/coeus/",
        "owl2xml":"http://www.w3.org/2006/12/owl2-xml#",
        "xsd": "http://www.w3.org/2001/XMLSchema#",
        "rdfs": "http://www.w3.org/2000/01/rdf-schema#",
        "owl": "http://www.w3.org/2002/07/owl#",
        "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
        "dc": "http://purl.org/dc/elements/1.1/",
        "d2r":"http://sites.wiwiss.fu-berlin.de/suhl/bizer/d2r-server/config.rdf#",
        "diseasecard":"http://bioinformatics.ua.pt/diseasecard/resource/"
    },
    "connections": {
        "diseasecard_root": "jdbc:mysql://container_mysql:3306/diseasecard?user=root&password=telematica",
        "diseasecard_diseasecard": "jdbc:mysql://container_mysql:3306/diseasecard?user=diseasecard&password=diseasecard",
        "hummer_root": "jdbc:mysql://container_mysql:3306/hummer?user=root&amp;password=telematica"
    },
    "resources": {
        "level0": "resource_uniprot",
        "level1": "resource_uniprot,resource_orphanet",
        "level2": "resource_icd10,resource_mesh,resource_interpro,resource_pdb,resource_enzyme,resource_kegg,resource_genecards",
        "level3": "resource_gwascentral, resource_clinicaltrials,resource_lsdb,resource_pharmgkb,resource_prosite,resource_go",
        "level4": "resource_drugbank,resource_ipi,resource_ensembl"
    }
}
