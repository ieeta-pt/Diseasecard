package pt.ua.bioinformatics.coeus.api;

/**
 * Utility class for Item transformation tasks.
 *
 * @author pedrolopes
 */
public class ItemFactory {

    /**
     * Converts a COEUS-formatted item name into a single token, usable on any
     * other method.
     *
     * @param item the Item to be converted.
     * @return the converted token.
     */
    public static String getTokenFromItem(String item) {
        String token = "";
        String check = "";
        if (item.contains("_")) {
            String[] full = item.split("\\_");
            if (full.length == 2) {
                check = full[1];
            } else if (full.length > 2) {
                check = full[1] + full[2];
            }
        } else {
            check = item;
        }

        if (check.contains("~")) {
            String[] tmp = check.split("~");
            token = tmp[0];
        } else {
            token = check;
        }
        return token;
    }
    
    /**
     * Gets the toke string in the <key>_<value> format from the given URI.
     * 
     * @param uri
     * @return 
     */
    public static String getTokenFromURI(String uri) {
        String token = "";
        if(uri.contains("/")) {
            String[] full = uri.split("/");
            token = full[full.length - 1];        
        }
        return token;
    }
}
