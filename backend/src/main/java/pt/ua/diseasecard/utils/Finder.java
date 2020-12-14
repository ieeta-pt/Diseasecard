package pt.ua.diseasecard.utils;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.json.JSONArray;
import org.json.JSONObject;
import pt.ua.diseasecard.components.data.DB;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.domain.SearchResult;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Finder {

    private DB db;

    private LinkedHashMap<String, SearchResult> network;
    private JSONObject result;
    private String query;
    private String solrIndex;
    private SparqlAPI api;


    public Finder(String connectionString) {
        this.db = new DB("DC4", connectionString);
    }

    public Finder(SparqlAPI api, String solrIndex, String query) {
        this.api = api;
        this.solrIndex = solrIndex;
        this.query = query;
        this.network = new LinkedHashMap<>();
        this.result = new JSONObject();
    }

    // TODO: Mudar o raça do Browsier para que ele retorne algo de jeito
    public String browse(String key) {
        JSONObject alldiseases = new JSONObject();
        JSONArray list = new JSONArray();
        PreparedStatement p;
        try {
            db.connect();
            p = db.getConnection().prepareStatement("SELECT * FROM Diseases WHERE name REGEXP ? ORDER BY name ASC;");
            if (key.equals("0")) {
                p.setString(1, "^[0-9\\[\\{].*");
            } else {
                p.setString(1, "^[" + key + "].*");
            }
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                JSONArray o = new JSONArray();
                o.put("<a rel=\"tooltip\" title=\"View " + rs.getString("name") + "\" href=\"./entry/" + rs.getInt("omim") + "\">" + rs.getInt("omim") + "</a>");
                o.put("<i class=\"icon-angle-right\"></i> <a rel=\"tooltip\" title=\"View " + rs.getString("name") + "\" href=\"./entry/" + rs.getInt("omim") + "\">" + rs.getString("name") + "</a><a href=\"http://omim.org/entry/" + rs.getInt("omim") + "\" class=\"pull-right\" target=\"_blank\"><i class=\"icon-external-link\"></i></a>");
                double progress = (rs.getInt("c") / 1127.0) * 100;
                String type;
                if (progress > 30.0) {
                    type = "";
                } else if (progress > 20.0) {
                    type = " progress-bar-info";
                } else if (progress > 15.0) {
                    type = " progress-bar-success";
                } else if (progress > 10.0) {
                    type = " progress-bar-warning";
                } else {
                    progress = 10.0;
                    type = " progress-bar-danger";
                }
                o.put("<div class=\"progress\"><div rel=\"tooltip\" title=\"" + rs.getInt("c") + " connections\" class=\"progress-bar " + type + "\" role=\"progressbar\" style=\"width: " + progress + "%;\">" + rs.getInt("c") + "</div></div>");
                list.put(o);
            }
            db.close();
            alldiseases.put("aaData", list);
        } catch (Exception ex) {
            System.out.println("[COEUS][Diseasecard][Finder] Unable to get diseases info");
            Logger.getLogger(Finder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return alldiseases.toString();
    }

    public String find(String type) {
        if (this.query.length() > 3) {
            ModifiableSolrParams params = new ModifiableSolrParams();
            if (type.equals("id")) {
                //params.set("q", "id:*\"" + query.toLowerCase() + "\"* OR id:*\"" + WordUtils.capitalize(query.toLowerCase()) + "\"*");
                params.set("q", "title:*" + this.query + "*");
                params.set("rows", 512);
            } else if (type.equals("full")) {
                params.set("q", this.query + "*");
                params.set("rows", 1024);
            }
            try {
                SolrServer server = new HttpSolrServer(this.solrIndex);
                QueryResponse response = server.query(params);
                SolrDocumentList docs = response.getResults();
                if (docs.isEmpty()) {
                    this.result.put("status", 110);
                } else if (docs.size() == 1) {
                    this.result.put("status", 121);
                } else {
                    this.result.put("status", 122);
                }
                JSONArray mtp = new JSONArray();

                for (SolrDocument sol : docs) {
                    if (!this.network.containsKey(sol.get("omim").toString())) {
                        SearchResult sr = new SearchResult(sol.get("omim").toString());
                        sr.setName(this.api.getOmimName(sol.get("omim").toString()));
                        if (sol.get("id").toString().contains("name")) {
                            sr.getAlias().add(sol.get("id").toString());
                        } else {
                            sr.getNetwork().add(sol.get("id").toString());
                        }
                        this.network.put(sol.get("omim").toString(), sr);

                    } else {
                        this.network.get(sol.get("omim").toString()).getNetwork().add(sol.get("id").toString());
                    }
                }

                int size = 0;
                for (SearchResult s : this.network.values()) {
                    JSONObject o = new JSONObject();
                    if (!s.getName().equals("")) {
                        o.put("name", s.getName());
                        o.put("omim", Integer.parseInt(s.getOmim()));
                        JSONArray alias = new JSONArray();
                        for (String link : s.getAlias()) {
                            alias.put(link);
                            size++;
                        }
                        JSONArray links = new JSONArray();
                        for (String link : s.getNetwork()) {
                            if (!link.contains("hpo")) {
                                links.put(link);
                                size++;
                            }
                        }
                        o.put("alias", alias);
                        o.put("links", links);
                        mtp.put(o);
                    }
                }
                this.result.put("size", size);
                this.result.put("results", mtp);
            } catch (Exception ex) {
                System.out.println("[Diseasecard][Finder] Unable to find disease");
                Logger.getLogger(Finder.class.getName()).log(Level.SEVERE, null, ex);

            }
        } else {
            try {
                result.put("status", 140);
            } catch (Exception ex) {
                System.out.println("[Diseasecard][Finder] Unable to find disease");
                Logger.getLogger(Finder.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
        //return (ArrayList<Disease>) new ArrayList(map.values());
        return result.toString();
    }

    public String get(String type) {
        JSONArray list = new JSONArray();
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("q", "title:*" + query + "*");
        params.set("rows", 254);

        try {
            SolrServer server = new HttpSolrServer(this.solrIndex);
            QueryResponse response = server.query(params);
            SolrDocumentList docs = response.getResults();
            for (SolrDocument sol : docs) {
                JSONObject obj = new JSONObject();
                obj.put("info", sol.get("title").toString());
                obj.put("omim", Integer.parseInt(sol.get("omim").toString()));
                list.put(obj);
            }
            result.put("results", list);
        } catch (Exception ex) {
            System.out.println("[COEUS][Diseasecard][Finder] Unable to get disease");
            Logger.getLogger(Finder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list.toString();
    }
}
