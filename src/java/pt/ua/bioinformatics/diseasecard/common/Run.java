package pt.ua.bioinformatics.diseasecard.common;

import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.bioinformatics.coeus.api.PrefixFactory;

/**
 *
 * @author pedrolopes
 */
public class Run {

    /**
     * COEUS importer runner class.
     * <p>
     * Organized in import levels for dependency handling.
     * </p>
     * <p>
     * <strong>LEVEL 0</strong><br />Initialize system (mandatory on all runs).
     * </p>
     * <p>
     * <strong>LEVEL 1</strong><br />Loads first level.
     * </p>
     * <p>
     * <strong>LEVEL n</strong><br />Loads n level.
     * </p>
     * <p>
     * <strong>FULL</strong><br />Loads everything.
     * </p>
     * <p>
     * (This needs refactoring!)
     * </p>
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Boot.start();
            System.out.println(PrefixFactory.encode("http://bioinformatics.ua.pt/diseasecard/resource/malacards_vohwinkel_syndrome_lol"));
            
            /*
                I removed all the code here. You can go to git if you need to access it.
            */
            
        } catch (Exception ex) {
            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
