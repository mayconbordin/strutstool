package com.struts.tool.helpers;

/**
 *
 * @author mayconbordin
 * @version 0.1
 */
public class StringHelper {
    public static String firstToUpperCase(String str) {
        return str.substring(0,1).toUpperCase()
                    + str.substring(1);
    }

    public static String firstToLowerCase(String str) {
        return str.substring(0,1).toLowerCase()
                    + str.substring(1);
    }
}
