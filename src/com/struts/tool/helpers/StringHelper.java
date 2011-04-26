package com.struts.tool.helpers;

/**
 *
 * @author maycon
 */
public class StringHelper {
    public static String firstToUpperCase(String str) {
        return str.substring(0,1).toUpperCase()
                    + str.substring(1);
    }
}
