package pt.ua.diseasecard.connectors.plugins;

import au.com.bytecode.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.components.data.Storage;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import pt.ua.diseasecard.domain.HGNC;
import pt.ua.diseasecard.domain.Resource;
import pt.ua.diseasecard.utils.BeanUtil;
import pt.ua.diseasecard.utils.Predicate;
import pt.ua.diseasecard.utils.PrefixFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configurable
public class HGNCPlugin {

    private final List<HGNC> hgncs;
    private final HashMap<String, com.hp.hpl.jena.rdf.model.Resource> hgncRes;
    private final HashMap<String, com.hp.hpl.jena.rdf.model.Resource> uniprotRes;
    private final Resource res;

    private final DiseasecardProperties config = BeanUtil.getBean(DiseasecardProperties.class);;
    private final SparqlAPI api = BeanUtil.getBean(SparqlAPI.class);;

    public HGNCPlugin(Resource res) {
        this.hgncs = new ArrayList<>();
        this.hgncRes = new HashMap<>();
        this.uniprotRes = new HashMap<>();
        this.res = res;
    }

    public void itemize() {
        if(this.load()) this.triplifyInformation();
    }

    private boolean load() {
        boolean sucess = false;
        File file = new File("submittedFiles/endpoints/resource_hgnc");
        BufferedReader in;
        try
        {
            in = new BufferedReader(new FileReader(file));
            CSVReader reader = new CSVReader(in, '\t');

            /*
            HGNC ID | Approved symbol | Chromosome | RefSeq IDs	| Enzyme IDs | NCBI Gene ID | Ensembl gene ID | OMIM ID	| UniProt ID | Pubmed IDs
            */

            List<String[]> rows = reader.readAll();

            // Removing rows that doesn't contains OMIM ID
            // rows.removeIf( s -> s[7].equals(""));

            List<String> oIDs;
            String[] info;

            for (int i=1; i < rows.size(); i++)
            {
                info = rows.get(i);
                oIDs = Arrays.asList(info[7].split(", "));

                HGNC hgnc = new HGNC(info[0]);
                hgnc.setApprovedSymbol(info[1]);
                hgnc.setChromosomes(info[2]);
                hgnc.setRefseqID(info[3]);
                hgnc.setEnsembl(info[4]);
                hgnc.setEnzymeID(info[5]);
                hgnc.setNcbi(info[6]);
                hgnc.setOmims(oIDs);
                hgnc.setUniprot(info[8]);
                hgnc.setPubmedIDs(Arrays.asList(info[9].split(", ")));

                this.hgncs.add(hgnc);
            }
            sucess = true;
        }
        catch (IOException ex)
        {
            if (this.config.getDebug()) System.out.println("[COEUS][HGNC] Unable to load data from HGNC");
            Logger.getLogger(HGNCPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\tLoading process finished.");
        return sucess;
    }

    private void triplifyInformation() {
        try
        {
            for (Map.Entry<String, String[]> entry : OMIMPlugin.getOmims().entrySet()) {
                String omimID = entry.getKey();
                String geneNames = entry.getValue()[0];
                String chromosomalLocation = entry.getValue()[1];

                com.hp.hpl.jena.rdf.model.Resource omim_item = api.getResource(PrefixFactory.getURIForPrefix(this.config.getKeyprefix()) + "omim_" + omimID);

                List<HGNC> relatedHGNC = this.findHGNCByOMIM(omimID);

                if (relatedHGNC.isEmpty())  relatedHGNC = this.findHGNCByApprovedSymbol(geneNames);
                if (relatedHGNC.isEmpty())  relatedHGNC = this.findHGNCByChromosomalLocation(chromosomalLocation);

                for(HGNC hgnc : relatedHGNC)
                {
                    com.hp.hpl.jena.rdf.model.Resource hgnci = null;
                    com.hp.hpl.jena.rdf.model.Resource uniproti = null;

                    if (!hgnc.getLoaded())
                    {
                        hgnci = this.itemizeResource(hgnc.getHgncID().split(":")[1], hgnc.getApprovedSymbol(), "hgnc_", "concept_HGNC");

                        // Itemize the uniprot value and all the resources that extens uniprof (Enzyme, Ensembl, etc.)
                        if(!hgnc.getUniprot().equals(""))
                        {
                            uniproti = this.itemizeResource(hgnc.getUniprot(), hgnc.getUniprot(), "uniprot_", "concept_UniProt" );
                            this.uniprotRes.put(hgnc.getUniprot(), uniproti);

                            this.itemizeUniprotExtensions(hgnc, uniproti);
                        }

                        // Avoids itemize the same resource, over and over again.
                        hgnc.setLoaded(true);
                        this.hgncRes.put(hgnc.getHgncID(), hgnci);
                    }
                    else
                    {
                        hgnci = this.hgncRes.get(hgnc.getHgncID());
                        if(!hgnc.getUniprot().equals("")) uniproti = this.uniprotRes.get(hgnc.getUniprot());
                    }

                    //Begins process of association
                    this.associateItems(hgnci, omim_item);
                    if(!hgnc.getUniprot().equals(""))  this.associateItems(uniproti, omim_item);
                }
            }
        }
        catch(Exception ex)
        {
            if (this.config.getDebug()) System.out.println("[COEUS][OMIM] Unable to triplify gene information");
            Logger.getLogger(HGNCPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void itemizeUniprotExtensions(HGNC hgnc, com.hp.hpl.jena.rdf.model.Resource uniprot) {
        if (!hgnc.getEnzymeID().equals(""))
        {
            com.hp.hpl.jena.rdf.model.Resource enzymei = this.itemizeResource(hgnc.getEnzymeID(), hgnc.getEnzymeID(), "enzyme_", "concept_ENZYME" );
            this.associateItems(enzymei, uniprot);
        }
        if (!hgnc.getEnsembl().equals(""))
        {
            com.hp.hpl.jena.rdf.model.Resource ensembli = this.itemizeResource(hgnc.getEnsembl(), hgnc.getEnsembl(), "ensembl_", "concept_Ensembl" );
            this.associateItems(ensembli, uniprot);
        }
    }

    private com.hp.hpl.jena.rdf.model.Resource itemizeResource(String ID, String name, String o, String concept) {
        com.hp.hpl.jena.rdf.model.Resource item = null;
        try
        {
            item = api.createResource(PrefixFactory.getURIForPrefix(this.config.getKeyprefix()) + o + ID);
            com.hp.hpl.jena.rdf.model.Resource obj = api.createResource(PrefixFactory.getURIForPrefix(this.config.getKeyprefix()) + "Item");

            api.addStatement(item, Predicate.get("rdf:type"), obj);

            api.addStatement(item, Predicate.get("rdfs:label"), o + name);
            api.addStatement(item, Predicate.get("dc:title"), name.toUpperCase());

            com.hp.hpl.jena.rdf.model.Resource con = api.getResource(PrefixFactory.getURIForPrefix(this.config.getKeyprefix()) + concept);

            api.addStatement(item, Predicate.get("coeus:hasConcept"), con);
            api.addStatement(con, Predicate.get("coeus:isConceptOf"), item);

        }
        catch (Exception ex)
        {
            if (this.config.getDebug()) System.out.println("[COEUS][HGNC] Unable to triplify gene information");
            Logger.getLogger(HGNCPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return item;
    }

    private void associateItems(com.hp.hpl.jena.rdf.model.Resource item, com.hp.hpl.jena.rdf.model.Resource parent) {
        try {
            api.addStatement(item, Predicate.get("coeus:isAssociatedTo"), parent);
            api.addStatement(parent, Predicate.get("coeus:isAssociatedTo"), item);
        } catch (Exception ex) {
            Logger.getLogger(HGNCPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<HGNC> findHGNCByChromosomalLocation(String chromosomalLocation) {
        List<HGNC> result = new ArrayList<>();

        for(HGNC hgnc : this.hgncs)
        {
            if(hgnc.getChromosomes().equals(chromosomalLocation))
            {
                result.add(hgnc);
            }
        }
        return result;
    }

    private List<HGNC> findHGNCByApprovedSymbol(String geneNames) {
        List<HGNC> result = new ArrayList<>();

        for(HGNC hgnc : this.hgncs)
        {
            for (String gene : geneNames.replace("[","").replace("]","").split(", "))
            {
                if(hgnc.getApprovedSymbol().equals(gene)) result.add(hgnc);
                break;
            }
        }
        return result;
    }

    private List<HGNC> findHGNCByOMIM(String omimID) {
        List<HGNC> result = new ArrayList<>();

        for(HGNC hgnc : this.hgncs)
        {
            if(hgnc.getOmims().contains(omimID))
            {
                result.add(hgnc);
            }
        }
        return result;
    }
}
