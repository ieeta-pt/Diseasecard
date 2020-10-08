
package pt.ua.bioinformatics.diseasecard.services;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.json.JSONObject;
import org.json.JSONArray;
import pt.ua.bioinformatics.coeus.api.DB;
import pt.ua.bioinformatics.coeus.api.ItemFactory;
import pt.ua.bioinformatics.diseasecard.common.Boot;
import pt.ua.bioinformatics.diseasecard.common.Config;
import pt.ua.bioinformatics.diseasecard.domain.Disease;
import pt.ua.bioinformatics.diseasecard.domain.EntrezGene;
import pt.ua.bioinformatics.diseasecard.domain.Orphanet;
import redis.clients.jedis.Jedis;

/**
 * Main indexing controller controlling Solr indexing process.
 *
 * @author pedrolopes
 */
public class Indexer implements Runnable {

    static private HashMap<String, Disease> diseases = new HashMap<String, Disease>();
    static private HashMap<String, JSONObject> omims = new HashMap<String, JSONObject>();

    /**
     * @param args the command line arguments
     */
    public void run() {
        // 1. load OMIM network from Redis cache
        loadOMIMs();
        
        // 2. index all content into Solr
        indexer();
    }

    /**
     * Load OMIM objects from Redis server into local HashMap
     */
    void loadOMIMs() {
        Boot.start();
        // select OMIM identifiers
        System.out.println("[DC4] Indexer started, loading OMIMs");
        
        ResultSet rs = Boot.getAPI().selectRS("SELECT ?t WHERE { ?u coeus:hasConcept diseasecard:concept_OMIM . ?u diseasecard:omim ?t  }", false);
        Jedis jedis = Boot.getJedis();
        while (rs.hasNext()) {
            QuerySolution row = rs.next();
            try {
                System.out.println("OMIM: " + jedis.get("omim:" + row.get("t").toString()));
                // add to HashMap
                omims.put(row.get("t").toString(), new JSONObject(jedis.get("omim:" + row.get("t").toString())));
            } catch (Exception e) {
                Logger.getLogger(Cashier.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    /**
     * Process each OMIM object (from HashMap) into full-content indexing engine
     */
    void indexer() {
        Boot.start();
        
        System.out.println("[DC4] OMIMs loaded, starting Solr import");
//        HttpSolrServer server = new HttpSolrServer("http://container_solr:8983/solr");
        
        HttpSolrServer server = new HttpSolrServer(Config.getIndex());                      // Pode não estar inicializado!
        server.setDefaultMaxConnectionsPerHost(256);
        server.setMaxTotalConnections(256);
        
        ExecutorService pool = Executors.newFixedThreadPool(64);

        for (String omim : omims.keySet()) {
            JSONObject obj = omims.get(omim);
            try {
                JSONArray names = obj.getJSONArray("synonyms");
                for (int i = 0; i < names.length(); i++) {
                    SolrLoad load = new SolrLoad(omim, "name", server);
                    load.setValue(names.getString(i));

                    pool.execute(load);

                }
                System.out.println("[Diseasecard][Indexer] launched indexing for " + omim);
            } catch (Exception ex) {
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);

            }
            try {
                JSONArray network = obj.getJSONArray("network");
                for (int i = 0; i < network.length(); i++) {
                    SolrLoad load = new SolrLoad(omim, "link", server);
                    load.setUri(network.getString(i));
                    pool.execute(load);
                }
                System.out.println("[Diseasecard][Indexer] launched indexing for " + omim);
            } catch (Exception ex) {
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * @deprecated
     */
    static void loadNames() {
        Boot.start();
        String query = "SELECT ?omim ?name {?d coeus:hasConcept diseasecard:concept_OMIM . ?d diseasecard:omim ?omim . ?d diseasecard:name ?name}";
        ResultSet results = Boot.getAPI().selectRS(query, false);
        while (results.hasNext()) {
            QuerySolution row = results.next();
            try {
                DB db = new DB("DC4", Config.getConnectionInfo("diseasecard_root"));
                db.connect(Config.getConnectionInfo("diseasecard_root"));
                String q = "INSERT INTO DiseaseIndex(omim, info, type) VALUES(?, ? ,?);";
                PreparedStatement p = db.getConnection().prepareStatement(q);
                p.setInt(1, Integer.parseInt(row.get("omim").toString()));
                p.setString(2, row.get("name").toString());
                p.setInt(3, 1);
                p.execute();
                db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + row.get("name").toString() + "','name');");
                db.close();
            } catch (Exception ex) {
                System.out.print(ex.getMessage());
            }
        }

        query = "SELECT ?omim ?name {?d coeus:hasConcept diseasecard:concept_OMIM . ?d diseasecard:omim ?omim . ?d dc:description ?name}";
        results = Boot.getAPI().selectRS(query, false);
        while (results.hasNext()) {
            QuerySolution row = results.next();
            try {
                DB db = new DB("DC4", "jdbc:mysql://container_mysql:3306/diseasecard?user=root&password=telematica");
                db.connect("jdbc:mysql://container_mysql:3306/diseasecard?user=root&password=telematica");
                String q = "INSERT INTO DiseaseIndex(omim, info, type) VALUES(?, ? ,?);";
                PreparedStatement p = db.getConnection().prepareStatement(q);
                p.setInt(1, Integer.parseInt(row.get("omim").toString()));
                p.setString(2, row.get("name").toString());
                p.setInt(3, 1);
                p.execute();
                db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + row.get("name").toString() + "','name');");
                db.close();
            } catch (Exception ex) {
                System.out.print(ex.getMessage());
            }
        }
    }

    /**
     * @deprecated
     */
    static void load() {
        Boot.start();
        String query = "SELECT ?omim ?name {?d coeus:hasConcept diseasecard:concept_OMIM . ?d diseasecard:omim ?omim . ?d diseasecard:name ?name}";
        ResultSet results = Boot.getAPI().selectRS(query, false);
        while (results.hasNext()) {
            QuerySolution row = results.next();
            try {
                DB db = new DB("DC4", "jdbc:mysql://container_mysql:3306/diseasecard?user=root&password=telematica");
                db.connect("jdbc:mysql://container_mysql:3306/diseasecard?user=root&password=telematica");
                String q = "INSERT INTO DiseaseIndex(omim, info, type) VALUES(?, ? ,?);";
                PreparedStatement p = db.getConnection().prepareStatement(q);
                p.setInt(1, Integer.parseInt(row.get("omim").toString()));
                p.setString(2, row.get("name").toString());
                p.setInt(3, 1);
                p.execute();
                db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + row.get("name").toString() + "','name');");
                db.close();
            } catch (Exception ex) {
                System.out.print(ex.getMessage());
            }
        }

        query = "SELECT ?omim ?name {?d coeus:hasConcept diseasecard:concept_OMIM . ?d diseasecard:omim ?omim . ?d dc:description ?name}";
        results = Boot.getAPI().selectRS(query, false);
        while (results.hasNext()) {
            QuerySolution row = results.next();
            try {
                DB db = new DB("DC4", "jdbc:mysql://container_mysql:3306/diseasecard?user=root&password=telematica");
                db.connect("jdbc:mysql://container_mysql:3306/diseasecard?user=root&password=telematica");
                String q = "INSERT INTO DiseaseIndex(omim, info, type) VALUES(?, ? ,?);";
                PreparedStatement p = db.getConnection().prepareStatement(q);
                p.setInt(1, Integer.parseInt(row.get("omim").toString()));
                p.setString(2, row.get("name").toString());
                p.setInt(3, 1);
                p.execute();
                db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + row.get("name").toString() + "','name');");
                db.close();
            } catch (Exception ex) {
                System.out.print(ex.getMessage());
            }
        }

        query = "SELECT DISTINCT ?omim{?d coeus:hasConcept diseasecard:concept_OMIM . ?d diseasecard:omim ?omim}";
        results = Boot.getAPI().selectRS(query, false);
        DB db = new DB("DC4", "jdbc:mysql://container_mysql:3306/diseasecard?user=root&password=telematica");
        while (results.hasNext()) {
            QuerySolution row = results.next();
            try {
                Disease disease = new Disease(Integer.parseInt(row.get("omim").toString()));
                System.out.println("--> Indexing " + disease.getOmimId());

                // Index orphanet
                for (Orphanet o : disease.getOrphanet()) {
                    try {
                        System.out.println("\t\t" + o.getId());
                        db.connect();
                        db.insert(disease.getOmimId(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + disease.getOmimId() + ", '" + o.getId() + "','omim')");
                        db.close();
                    } catch (Exception ex) {
                        System.out.print("[Indexer][Orphanet]" + ex.getMessage());
                    }
                }

                // Index EntrezGene
                for (EntrezGene o : disease.getEntrezgene()) {
                    try {
                        System.out.println("\t\t" + o.getId());
                        db.connect();
                        db.insert(disease.getOmimId(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + disease.getOmimId() + ", '" + o.getId() + "','entrezgene')");
                        db.close();
                    } catch (Exception ex) {
                        System.out.print("[Indexer][Orphanet]" + ex.getMessage());
                    }
                }
            } catch (Exception ex) {
                System.out.print(ex.getMessage());
            }
        }
    }

    /**
     * @deprecated
     */
    static void loadGenes() {
        String query = "SELECT DISTINCT ?omim{?d coeus:hasConcept diseasecard:concept_OMIM . ?d diseasecard:omim ?omim}";
        Boot.start();
        ResultSet results = Boot.getAPI().selectRS(query, false);
        while (results.hasNext()) {
            QuerySolution row = results.next();
            try {
                String qquery = "SELECT ?o {<http://bioinformatics.ua.pt/diseasecard/resource/omim_" + row.get("omim").toString() + "> coeus:isAssociatedTo ?o}";
                ResultSet rresults = Boot.getAPI().selectRS(qquery, false);
                while (rresults.hasNext()) {
                    QuerySolution rrow = rresults.next();

                    DB db = new DB("DC4", "jdbc:mysql://container_mysql:3306/diseasecard?user=root&password=telematica");
                    db.connect("jdbc:mysql://container_mysql:3306/diseasecard?user=root&password=telematica");
                    String item = rrow.get("o").toString();
                    if (item.contains("hgnc")) {
                        item = item.replace("http://bioinformatics.ua.pt/diseasecard/resource/hgnc_", "");
                        db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + item + "','hgnc')");
                    } else if (item.contains("hpo")) {
                        item = item.replace("http://bioinformatics.ua.pt/diseasecard/resource/hpo_", "");
                        db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + item + "','hpo')");
                    }
                    db.close();
                }
            } catch (Exception ex) {
                System.out.print(ex.getMessage());
            }
        }
    }

    /**
     * Load all content from Semantic Knowledge Base into temporary database for faster processing.
     */
    static void process() {
        String query_omims = "SELECT DISTINCT ?omim {?d coeus:hasConcept diseasecard:concept_OMIM . ?d diseasecard:omim ?omim}";
        
        Boot.start();
        DB db = new DB("DC4", Config.getConnectionInfo("diseasecard_root"));
//        Boot.start();
        ResultSet results_omim = Boot.getAPI().selectRS(query_omims, false);
        while (results_omim.hasNext()) {
            QuerySolution row = results_omim.next();
            try {
                String query_omimassociations = "SELECT ?o {<http://bioinformatics.ua.pt/diseasecard/resource/omim_" + row.get("omim").toString() + "> coeus:isAssociatedTo ?o}";
                ResultSet results_associations = Boot.getAPI().selectRS(query_omimassociations, false);
                while (results_associations.hasNext()) {
                    QuerySolution association = results_associations.next();
                    db.connect();
                    db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + row.get("omim").toString() + "','omim')");
                    db.close();
                    String item = association.get("o").toString();
                    if (item.contains("hgnc")) {
                        db.connect();
                        db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(item) + "','hgnc')");
                        db.close();
                        String query_hgnc = "SELECT ?o {<" + item + "> coeus:isAssociatedTo ?o}";
                        ResultSet results_hgnc = Boot.getAPI().selectRS(query_hgnc, false);
                        while (results_hgnc.hasNext()) {
                            QuerySolution hgnc_row = results_hgnc.next();
                            String hgnc_item = hgnc_row.get("o").toString();
                            try {
                                if (hgnc_item.contains("clinicaltrials")) {
                                    db.connect();
                                    db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(hgnc_item) + "','clinicaltrials')");
                                    db.close();
                                } else if (hgnc_item.contains("gwascentral")) {
                                    db.connect();
                                    db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(hgnc_item) + "','gwascentral')");
                                    db.close();
                                } else if (hgnc_item.contains("uniprot")) {
                                    db.connect();
                                    db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(hgnc_item) + "','uniprot')");
                                    db.close();

                                    String query_uniprot = "SELECT ?o {<" + hgnc_item + "> coeus:isAssociatedTo ?o}";
                                    ResultSet results_uniprot = Boot.getAPI().selectRS(query_uniprot, false);
                                    while (results_uniprot.hasNext()) {
                                        QuerySolution up_row = results_uniprot.next();
                                        String up_item = up_row.get("o").toString();
                                        try {
                                            if (up_item.contains("interpro")) {
                                                db.connect();
                                                db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(up_item) + "','interpro')");
                                                db.close();
                                            } else if (up_item.contains("mesh")) {
                                                db.connect();
                                                db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(up_item) + "','mesh')");
                                                db.close();
                                            } else if (up_item.contains("pdb")) {
                                                db.connect();
                                                db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(up_item) + "','pdb')");
                                                db.close();
                                            } else if (up_item.contains("prosite")) {
                                                db.connect();
                                                db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(up_item) + "','prosite')");
                                                db.close();
                                            } else if (up_item.contains("drugbank")) {
                                                db.connect();
                                                db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(up_item) + "','drugbank')");
                                                db.close();
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                } else if (hgnc_item.contains("kegg")) {
                                    db.connect();
                                    db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(hgnc_item) + "','kegg')");
                                    db.close();
                                } else if (hgnc_item.contains("enzyme")) {
                                    db.connect();
                                    db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(hgnc_item) + "','enzyme')");
                                    db.close();
                                } else if (hgnc_item.contains("ensembl")) {
                                    db.connect();
                                    db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(hgnc_item) + "','ensembl')");
                                    db.close();
                                }
                            } catch (Exception e) {
                            }
                        }
                    } else if (item.contains("hpo")) {
                        db.connect();
                        db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(item) + "','hpo')");
                        db.close();

                        String query_op = "SELECT ?o {<" + item + "> coeus:isAssociatedTo ?o}";
                        ResultSet results_on = Boot.getAPI().selectRS(query_op, false);
                        while (results_on.hasNext()) {
                            QuerySolution on_row = results_on.next();
                            String on_item = on_row.get("o").toString();
                            try {
                                if (on_item.contains("umls")) {
                                    db.connect();
                                    db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(on_item) + "','umls')");
                                    db.close();
                                }
                            } catch (Exception e) {
                            }
                        }
                    } else if (item.contains("orphanet")) {
                        db.connect();
                        db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(item) + "','orphanet')");
                        db.close();
                        String query_op = "SELECT ?o {<" + item + "> coeus:isAssociatedTo ?o}";
                        ResultSet results_on = Boot.getAPI().selectRS(query_op, false);
                        while (results_on.hasNext()) {
                            QuerySolution on_row = results_on.next();
                            String on_item = on_row.get("o").toString();
                            try {
                                if (on_item.contains("icd")) {
                                    db.connect();
                                    db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(on_item) + "','icd10')");
                                    db.close();
                                }
                            } catch (Exception e) {
                            }
                        }
                    } else if (item.contains("entrez")) {
                        db.connect();
                        db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(item) + "','entrez')");
                        db.close();
                        String query_op = "SELECT ?o {<" + item + "> coeus:isAssociatedTo ?o}";
                        ResultSet results_on = Boot.getAPI().selectRS(query_op, false);
                        while (results_on.hasNext()) {
                            QuerySolution on_row = results_on.next();
                            String on_item = on_row.get("o").toString();
                            try {
                                if (on_item.contains("icd")) {
                                    db.connect();
                                    db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(on_item) + "','icd10')");
                                    db.close();
                                }
                                if (on_item.contains("pubmed")) {
                                    db.connect();
                                    db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(on_item) + "','pubmed')");
                                    db.close();
                                }
                                if (on_item.contains("pharmgkb")) {
                                    db.connect();
                                    db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(on_item) + "','pharmgkb')");
                                    db.close();
                                }
                            } catch (Exception e) {
                            }
                        }

                    } else if (item.contains("swissvar")) {
                        db.connect();
                        db.insert(row.get("omim").toString(), "INSERT INTO DiseaseIndex(omim, info, type) VALUES(" + row.get("omim").toString() + ", '" + ItemFactory.getTokenFromItem(item) + "','swissvar')");
                        db.close();
                    }
                }
            } catch (Exception ex) {
                System.out.print(ex.getMessage());
            }
        }
    }
    
    @Deprecated
    static void solrImport() {
        // instance threadables
        SolrLoad solr_omim = new SolrLoad("omim", getConcept("omim"));
        SolrLoad solr_entrez = new SolrLoad("entrez", getConcept("entrez"));
        //SolrLoad solr_pubmed = new SolrLoad("pubmed", getConcept("pubmed"));
        SolrLoad solr_uniprot = new SolrLoad("uniprot", getConcept("uniprot"));
        SolrLoad solr_ct = new SolrLoad("clinicaltrials", getConcept("clinicaltrials"));
        SolrLoad solr_hgnc = new SolrLoad("hgnc", getConcept("hgnc"));
        SolrLoad solr_orphanet = new SolrLoad("orphanet", getConcept("orphanet"));
        SolrLoad solr_pharmgkb = new SolrLoad("pharmgkb", getConcept("pharmgkb"));
        SolrLoad solr_icd = new SolrLoad("icd10", getConcept("icd10"));
        SolrLoad solr_swissvar = new SolrLoad("swissvar", getConcept("swissvar"));
        SolrLoad solr_umls = new SolrLoad("umls", getConcept("hpo"));
        SolrLoad solr_hpo = new SolrLoad("hpo", getConcept("hpo"));
        SolrLoad solr_enzyme = new SolrLoad("enzyme", getConcept("enzyme"));
        SolrLoad solr_ensembl = new SolrLoad("ensembl", getConcept("ensembl"));
        SolrLoad solr_drugbank = new SolrLoad("drugbank", getConcept("drugbank"));
        SolrLoad solr_kegg = new SolrLoad("kegg", getConcept("kegg"));
        SolrLoad solr_genecards = new SolrLoad("genecards", getConcept("genecards"));
        SolrLoad solr_gwas = new SolrLoad("gwascentral", getConcept("gwascentral"));
        SolrLoad solr_mesh = new SolrLoad("mesh", getConcept("mesh"));
        SolrLoad solr_interpro = new SolrLoad("interpro", getConcept("interpro"));
        SolrLoad solr_pdb = new SolrLoad("pdb", getConcept("pdb"));
        SolrLoad solr_prosite = new SolrLoad("prosite", getConcept("prosite"));

        // create threads
        Thread omim = new Thread(solr_omim);
        Thread entrez = new Thread(solr_entrez);
        //Thread pubmed = new Thread(solr_pubmed);
        Thread uniprot = new Thread(solr_uniprot);
        Thread ct = new Thread(solr_ct);
        Thread pharmgkb = new Thread(solr_pharmgkb);
        Thread icd = new Thread(solr_icd);
        Thread swissvar = new Thread(solr_swissvar);
        Thread umls = new Thread(solr_umls);
        Thread hpo = new Thread(solr_hpo);
        Thread enzyme = new Thread(solr_enzyme);
        Thread ensembl = new Thread(solr_ensembl);
        Thread drugbank = new Thread(solr_drugbank);
        Thread kegg = new Thread(solr_kegg);
        Thread gwas = new Thread(solr_gwas);
        Thread mesh = new Thread(solr_mesh);
        Thread genecards = new Thread(solr_genecards);
        Thread interpro = new Thread(solr_interpro);
        Thread pdb = new Thread(solr_pdb);
        Thread prosite = new Thread(solr_prosite);
        Thread hgnc = new Thread(solr_hgnc);
        Thread orphanet = new Thread(solr_orphanet);

        // start threads
        omim.start();
        entrez.start();
        // pubmed.start();
        ct.start();
        pharmgkb.start();
        icd.start();
        swissvar.start();
        hpo.start();
        enzyme.start();
        ensembl.start();
        drugbank.start();
        kegg.start();
        gwas.start();
        mesh.start();
        genecards.start();
        interpro.start();
        pdb.start();
        prosite.start();
        uniprot.start();
        hgnc.start();
        orphanet.start();
        //SolrLoad solr_name = new SolrLoad("name", getConcept("name"));
        //Thread name = new Thread(solr_name);
        //name.start();
    }

    @Deprecated
    static ArrayList<SolrObject> getConcept(String c) {
        ArrayList<SolrObject> list = new ArrayList<SolrObject>();
        try {
            String q = "SELECT DISTINCT * FROM DiseaseIndex WHERE type LIKE '" + c + "';";
            DB db = new DB("DC4", Config.getConnectionInfo("diseasecard_root"));
            db.connect();
            java.sql.ResultSet results = db.getData(q);
            while (results.next()) {
                list.add(new SolrObject(results.getString("omim"), results.getString("info")));
            }
            db.close();
        } catch (SQLException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    static void addNames() {
        try {
            String q = "SELECT DISTINCT(omim) FROM Diseases;";
            DB db = new DB("DC4", Config.getConnectionInfo("diseasecard_root"));
            DB dbx = new DB("DC4", Config.getConnectionInfo("diseasecard_root"));
            db.connect();
            java.sql.ResultSet results = db.getData(q);
            while (results.next()) {
                dbx.connect();
                dbx.insert(results.getString("omim"), "UPDATE Diseases AS D\n"
                        + "SET D.name = (SELECT DI.info AS name FROM DiseaseIndex AS DI WHERE DI.type LIKE 'name' AND DI.omim = " + results.getString("omim") + " ORDER BY DI.info ASC LIMIT 1)\n"
                        + "WHERE D.omim = " + results.getString("omim") + ";");
                dbx.close();
            }
            db.close();
        } catch (SQLException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
