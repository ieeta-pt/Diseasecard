package pt.ua.bioinformatics.diseasecard.domain;

import java.util.HashMap;

/**
 *
 * @author pedrolopes
 */
public class Links {

    private final static HashMap<String, String> links = new HashMap<String, String>();
    private static boolean loaded = false;

    static private void load() {
        // Disease
        links.put("omim", "http://omim.org/entry/#replace#");
        links.put("orphanet", "http://www.pdb.org/pdb/explore/explore.do?structureId=");
        links.put("nord", "http://www.rarediseases.org/rare-disease-information/rare-diseases/viewSearchResults?term=#replace#");
        // Protein
        links.put("uniprot", "http://www.uniprot.org/uniprot/#replace#");
        links.put("pdb", "http://www.pdb.org/pdb/explore/explore.do?structureId=#replace#");
        links.put("prosite", "http://prosite.expasy.org/cgi-bin/prosite/prosite-search-ac?#replace#");
        links.put("interpro", "http://wwwdev.ebi.ac.uk/interpro/ISearch?query=#replace#");
        //Literature
        links.put("pubmedsearch","http://www.ncbi.nlm.nih.gov/pubmed?term=#replace#");
        links.put("pubmed", "http://www.ncbi.nlm.nih.gov/pubmed/#replace#");
        links.put("omimref","http://omim.org/entry/#replace##references");
        // Locus
        links.put("hgnc", "http://www.genenames.org/data/hgnc_data.php?match=#replace#");
        links.put("genecards", "http://www.genecards.org/cgi-bin/carddisp.pl?gene=#replace#");
        links.put("ensembl", "http://www.ensembl.org/Homo_sapiens/Gene/Summary?db=core;g=#replace#");
        links.put("entrez", "http://www.ncbi.nlm.nih.gov/sites/entrez?db=gene&term=#replace#");
        // Drug
        links.put("pharmgkb", "http://www.pharmgkb.org/do/serve?objId=#replace#");
        // Ontology
        links.put("mesh", "http://www.nlm.nih.gov/cgi/mesh/2011/MB_cgi?term=#replace#&field=uid&exact=Find%20Exact%20Term");
        links.put("hp", "http://www.berkeleybop.org/obo/#replace#.pro");
        // Variome
        links.put("wave", "http://bioinformatics.ua.pt/WAVe/gene/#replace#");
        loaded = true;
    }

    static public String get(String type) {
        if (!loaded) {
            load();
        }
        return links.get(type);
    }
}
