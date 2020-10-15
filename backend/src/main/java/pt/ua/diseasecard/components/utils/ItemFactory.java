package pt.ua.diseasecard.components.utils;


public class ItemFactory {

    public static String getTokenFromItem(String item) {
        String token = "";
        String check = "";
        if (item.contains("_")) {
            String[] full = item.split("\\_");
            if (full.length == 2) {
                check = full[1];
            } else if (full.length > 2) {
                check = full[1];
                for(int i =2; i <= full.length; i++ ) {
                    check += "_" + full[i];
                }
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


    public static String getTokenFromURI(String uri) {
        String token = "";
        if(uri.contains("/")) {
            String[] full = uri.split("/");
            token = full[full.length - 1];
        }
        return token;
    }


}
