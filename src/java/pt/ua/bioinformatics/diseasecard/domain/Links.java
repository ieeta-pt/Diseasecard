package pt.ua.bioinformatics.diseasecard.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.coeus.common.Boot;
import pt.ua.bioinformatics.coeus.common.Config;

/**
 *
 * @author pedrolopes
 */
public class Links {

    private final static HashMap<String, String> links = new HashMap<String, String>();
    private static boolean loaded = false;

    static private void load() {
        // Disease
        links.put("omim", "http://bioinformatics.ua.pt/omim/#replace#");
        links.put("orphanet", "http://www.orpha.net/consor/cgi-bin/Disease_Search.php?lng=EN&data_id=#replace#");
        links.put("nord", "http://www.rarediseases.org/rare-disease-information/rare-diseases/viewSearchResults?term=#replace#");
        // Protein
        links.put("uniprot", "http://www.uniprot.org/uniprot/#replace#");
        links.put("pdb", "http://www.pdb.org/pdb/explore/explore.do?structureId=#replace#");
        links.put("prosite", "http://prosite.expasy.org/cgi-bin/prosite/prosite-search-ac?#replace#");
        links.put("interpro", "http://wwwdev.ebi.ac.uk/interpro/ISearch?query=#replace#");
        //Literature
        links.put("pubmedsearch", "http://www.ncbi.nlm.nih.gov/pubmed?term=#replace#");
        links.put("pubmed", "http://www.ncbi.nlm.nih.gov/pubmed/#replace#");
        // Locus
        links.put("hgnc", "http://www.genenames.org/data/hgnc_data.php?match=#replace#");
        links.put("genecards", "http://www.genecards.org/cgi-bin/carddisp.pl?gene=#replace#");
        links.put("ensembl", "http://www.ensembl.org/Homo_sapiens/Gene/Summary?db=core;g=#replace#");
        links.put("entrez", "http://www.ncbi.nlm.nih.gov/sites/entrez?db=gene&term=#replace#");
        // Studies
        links.put("gwascentral","https://www.gwascentral.org/study/#replace#");
        links.put("clinicaltrials", "http://www.clinicaltrials.gov/ct2/show/#replace#");
        
        // Pathway
        links.put("kegg", "http://www.genome.jp/dbget-bin/www_bget?#replace#");
        links.put("enzyme", "http://enzyme.expasy.org/EC/#replace#");
        
        // Drug
        links.put("pharmgkb", "http://www.pharmgkb.org/do/serve?objId=#replace#");
        links.put("drugbank", "http://www.drugbank.ca/drugs/#replace#");
        // Ontology
        links.put("mesh", "http://www.nlm.nih.gov/cgi/mesh/2011/MB_cgi?term=#replace#&field=uid&exact=Find%20Exact%20Term");
        links.put("hp", "http://www.berkeleybop.org/obo/#replace#.pro");
        links.put("icd10","http://apps.who.int/classifications/icd10/browse/2010/en#/#replace#");
        links.put("umls", "http://ncim.nci.nih.gov/ncimbrowser/ConceptReport.jsp?code=#replace#");
        // Variome
        links.put("wave", "http://bioinformatics.ua.pt/WAVe/gene/#replace#");
        links.put("swissvar","http://web.expasy.org/cgi-bin/variant_pages/get-sprot-variant.pl?#replace#");
        loaded = true;
    }

    static public String get(String type) {
        if (!loaded) {
            load();
        }
        return links.get(type);
    }

    /**
     * Circumvent OMIM's frame denial header. Load URL InputStream, update
     * links, send to String.
     *
     * @param key OMIM identifier
     * @return String from OMIM Stream with replaced content
     */
    static public String hijackOmim(String key) {
        String response = "";
        try {
            URL omim = new URL("http://omim.org/entry/" + key);
            BufferedReader in = new BufferedReader(new InputStreamReader(omim.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response += inputLine.replace("href=\"", "href=\"http://omim.org");
            }
            in.close();
        } catch (MalformedURLException e) {
            if (Config.isDebug()) {
                System.out.println("[COEUS][Links] Unable to hijack OMIM, bad URL");
            }
            Logger.getLogger(Boot.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            if (Config.isDebug()) {
                System.out.println("[COEUS][Links] Unable to hijack OMIM, cannot load stream");
            }
            Logger.getLogger(Boot.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception e) {
            if (Config.isDebug()) {
                System.out.println("[COEUS][Boot] Unable to hijack OMIM");
            }
            Logger.getLogger(Boot.class.getName()).log(Level.SEVERE, null, e);
        }

        return response;
    }
}
