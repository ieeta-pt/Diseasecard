package pt.ua.diseasecard.utils;

public class ConceptFactory {

    public static String getTokenFromConcept(String item) {
        String check = "";
        if (item.contains("_"))
        {
            String[] full = item.split("\\_");
            check = full[1];
        }
        else check = item;

        return check.toLowerCase() + "_";
    }
}
