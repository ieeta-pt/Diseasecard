package pt.ua.diseasecard.connectors.plugins;

import au.com.bytecode.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Configurable;
import pt.ua.diseasecard.components.data.SparqlAPI;
import pt.ua.diseasecard.configuration.DiseasecardProperties;
import pt.ua.diseasecard.domain.Disease;
import pt.ua.diseasecard.domain.Resource;
import pt.ua.diseasecard.utils.BeanUtil;
import pt.ua.diseasecard.utils.Predicate;
import pt.ua.diseasecard.utils.PrefixFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configurable
public class OMIMPlugin {

    private Resource res;
    private HashMap<String, Disease> diseases;
    private HashMap<String, Disease> genotypes;
    private static HashMap<String, String[]> omims;

    private final SparqlAPI api = BeanUtil.getBean(SparqlAPI.class);;

    private final DiseasecardProperties config = BeanUtil.getBean(DiseasecardProperties.class);;

    public OMIMPlugin(Resource res) {
        this.res = res;
        this.diseases = new HashMap<>();
        this.genotypes = new HashMap<>();
        omims = new HashMap<>();
    }

    public HashMap<String, String[]> itemize() {
        if (loadGenotype() && loadPhenotype()) triplify();
        return omims;
    }

    private boolean loadGenotype() {
        boolean success = false;
        try
        {
            File file = new File("submittedFiles/endpoints/omim_genemap");
            BufferedReader in = new BufferedReader(new FileReader(file));

            CSVReader reader = new CSVReader(in, '|');
            List<String[]> genemap = reader.readAll();
            for (String[] genes : genemap)
            {
                Disease d = new Disease(genes[7], genes[9]);
                d.setLocation(genes[4]);
                genotypes.put(d.getOmimId(), d);

                omims.put(genes[9], new String[]{genes[5], genes[4]});

                String[] genelist = genes[5].split(", ");
                d.getGenes().addAll(Arrays.asList(genelist));
            }
            success = true;
        }
        catch (Exception ex)
        {
            Logger.getLogger(OMIMPlugin.class.getName()).log(Level.INFO,"[COEUS][OMIM] Unable to load genotype information from OMIM");
            Logger.getLogger(OMIMPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;

    }

    private boolean loadPhenotype() {
        boolean success = false;
        try
        {
            File file = new File("submittedFiles/endpoints/omim_morbidmap");
            BufferedReader in = new BufferedReader(new FileReader(file));

            CSVReader reader = new CSVReader(in, '|');
            List<String[]> morbidmap = reader.readAll();
            Pattern p = Pattern.compile("[0-9]{6}");

            for (String[] disease : morbidmap) {
                Disease d;
                Matcher m = p.matcher(disease[0]);
                String pheno_omim = "";
                String dis_name = "";

                try {
                    if (m.find())
                    {
                        pheno_omim = m.group(0);
                        dis_name = disease[0].substring(0, disease[0].length() - 12);

                        if (diseases.containsKey(pheno_omim))
                        {
                            d = diseases.get(pheno_omim);
                            d.getNames().add(dis_name);
                        }
                        else
                        {
                            d = new Disease(dis_name, pheno_omim);
                            d.setLocation(disease[3]);
                            d.getNames().add(dis_name);
                            diseases.put(pheno_omim, d);
                        }

                        Disease genotype = genotypes.get(disease[2]);
                        if (!d.getGenotypes().contains(genotype))      d.getGenotypes().add(genotype);
                        if (!genotype.getPhenotypes().contains(d))     genotype.getPhenotypes().add(d);

                        String[] genelist = disease[1].split(", ");
                        d.getGenes().addAll(Arrays.asList(genelist));
                        omims.put(pheno_omim, new String[] {d.getGenes().toString(), d.getLocation()});
                    }
                    else
                    {
                        Disease genotype = genotypes.get(disease[2]);
                        d = new Disease(disease[0], genotype.getOmimId());
                        d.getNames().add(disease[0]);
                        d.setLocation(disease[3]);
                        if (!d.getGenotypes().contains(genotype))     d.getGenotypes().add(genotype);
                        if (!genotype.getPhenotypes().contains(d))    genotype.getPhenotypes().add(d);

                        diseases.put(d.getOmimId(), d);
                    }
                }
                catch (Exception ex)
                {
                    Logger.getLogger(OMIMPlugin.class.getName()).log(Level.INFO,"[COEUS][OMIM] Unable to load phenotype information from OMIM for " + disease[0]);
                    Logger.getLogger(OMIMPlugin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            success = true;
        }
        catch (Exception ex)
        {
            Logger.getLogger(OMIMPlugin.class.getName()).log(Level.INFO,"[COEUS][OMIM] Unable to load phenotype information from OMIM");
            Logger.getLogger(OMIMPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }

    private void triplify() {
        //Map<String, String> prefixes = this.config.getPrefixes();
        for (Disease genotype : genotypes.values()) {
            if (!genotype.getOmimId().equals("")) {
                try {
                    String[] itemTmp = res.getIsResourceOf().getLabel().split("_");
                    com.hp.hpl.jena.rdf.model.Resource geno_item = api.createResource(PrefixFactory.getURIForPrefix(this.config.getKeyprefix()) + itemTmp[1] + "_" + genotype.getOmimId());
                    com.hp.hpl.jena.rdf.model.Resource geno_obj = api.createResource(PrefixFactory.getURIForPrefix(this.config.getKeyprefix()) + "Item");
                    api.addStatement(geno_item, Predicate.get("rdf:type"), geno_obj);

                    // set Item label
                    api.addStatement(geno_item, Predicate.get("rdfs:label"), "omim_" + genotype.getOmimId());

                    // associate Item with Concept
                    com.hp.hpl.jena.rdf.model.Resource con = api.getResource(res.getIsResourceOf().getUri());
                    api.addStatement(geno_item, Predicate.get("coeus:hasConcept"), con);
                    api.addStatement(con, Predicate.get("coeus:isConceptOf"), geno_item);

                    // add name/comment
                    api.addStatement(geno_item, Predicate.get("rdfs:comment"), genotype.getName());
                    api.addStatement(geno_item, Predicate.get("dc:description"), genotype.getName());
                    for (String name : genotype.getNames()) {
                        api.addStatement(geno_item, Predicate.get("diseasecard:name"), name);
                    }

                    api.addStatement(geno_item, Predicate.get("diseasecard:omim"), genotype.getOmimId());
                    api.addStatement(geno_item, Predicate.get("diseasecard:chromosomalLocation"), genotype.getLocation());

                    //triplifyGenes(genotype.getGenes(), geno_item);

                    for (Disease phenotype : genotype.getPhenotypes()) {
                        com.hp.hpl.jena.rdf.model.Resource pheno_item = api.createResource(PrefixFactory.getURIForPrefix(this.config.getKeyprefix()) + itemTmp[1] + "_" + phenotype.getOmimId());
                        com.hp.hpl.jena.rdf.model.Resource pheno_obj = api.createResource(PrefixFactory.getURIForPrefix(this.config.getKeyprefix()) + "Item");
                        api.addStatement(pheno_item, Predicate.get("rdf:type"), pheno_obj);

                        // set Item label
                        api.addStatement(pheno_item, Predicate.get("rdfs:label"), "omim_" + phenotype.getOmimId());

                        // associate Item with Concept
                        com.hp.hpl.jena.rdf.model.Resource pheno_concept = api.getResource(res.getIsResourceOf().getUri());
                        api.addStatement(pheno_item, Predicate.get("coeus:hasConcept"), pheno_concept);
                        api.addStatement(pheno_concept, Predicate.get("coeus:isConceptOf"), pheno_item);

                        // add name/comment
                        api.addStatement(pheno_item, Predicate.get("rdfs:comment"), phenotype.getName());
                        api.addStatement(pheno_item, Predicate.get("dc:description"), phenotype.getName());
                        for (String name : phenotype.getNames()) {
                            api.addStatement(pheno_item, Predicate.get("diseasecard:name"), name);
                        }
                        api.addStatement(pheno_item, Predicate.get("diseasecard:omim"), phenotype.getOmimId());
                        api.addStatement(pheno_item, Predicate.get("diseasecard:chromosomalLocation"), phenotype.getLocation());

                        //add diseasecard-specific info
                        api.addStatement(pheno_item, Predicate.get("diseasecard:phenotype"), "true");
                        api.addStatement(pheno_item, Predicate.get("diseasecard:hasGenotype"), geno_item);
                        api.addStatement(geno_item, Predicate.get("diseasecard:hasPhenotype"), pheno_item);

                        //triplifyGenes(phenotype.getGenes(), pheno_item);
                    }

                    api.addStatement(geno_item, Predicate.get("diseasecard:genotype"), "true");
                }
                catch (Exception ex)
                {
                    if (this.config.getDebug()) Logger.getLogger(OMIMPlugin.class.getName()).log(Level.INFO,"[COEUS][OMIM] Unable to triplify inmemory data");
                    Logger.getLogger(OMIMPlugin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static HashMap<String, String[]> getOmims() {
        return omims;
    }

    public static void setOmims(HashMap<String, String[]> omims) {
        OMIMPlugin.omims = omims;
    }
}
