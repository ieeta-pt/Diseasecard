package pt.ua.diseasecard.components.utils;

import org.springframework.stereotype.Component;
import pt.ua.diseasecard.configuration.DiseasecardProperties;

import java.util.HashMap;
import java.util.Objects;


public class PrefixFactory {

    private static HashMap<String, String> prefixes;

    public static HashMap<String, String> getPrefixes() {
        return prefixes;
    }

    public static void setPrefixes(HashMap<String, String> prefixes) {
        PrefixFactory.prefixes = prefixes;
    }

    public static String encode(String uri) {
        String prefix = getPrefixForURI(uri);
        return uri.replace(prefixes.get(prefix), prefix + ":");
    }

    public static String decode(String what) {
        String[] tmp = what.split(":");
        return prefixes.get(tmp[0]) + tmp[1];
    }

    public static String getURIForPrefix(String what) {
        return prefixes.get(what);
    }

    private static String getPrefixForURI(String uri) {
        String prefix = "";
        for (String s : prefixes.values()) {
            if (uri.contains(s)) {
                prefix = getKeyForValue(prefixes, s);
            }
        }
        return prefix;
    }

    private static String getKeyForValue(HashMap<String, String> hm, String value) {
        String list = "";
        for (String o : hm.keySet()) {
            if (hm.get(o).equals(value)) list = o;
        }
        return list;
    }

    public static String allToString() {
        StringBuilder p = new StringBuilder();
        for (String o : prefixes.keySet()) {
            p.append("PREFIX ").append(o).append(": <").append(prefixes.get(o)).append(">\n");
        }
        return p.toString();
    }
}
