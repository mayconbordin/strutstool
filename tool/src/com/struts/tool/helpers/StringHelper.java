package com.struts.tool.helpers;

/**
 *
 * @author mayconbordin
 * @version 0.1
 */
public class StringHelper {
    public static String ucfirst(String str) {
        return str.substring(0,1).toUpperCase()
                    + str.substring(1);
    }

    public static String lcfirst(String str) {
        return str.substring(0,1).toLowerCase()
                    + str.substring(1);
    }

    public static String underlineBetweenUpCase(String str) {
        return str.replaceAll("(.)(\\p{Lu})", "$1_$2").toLowerCase();
    }
}
