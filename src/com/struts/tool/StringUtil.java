package com.struts.tool;

/**
 *
 * @author maycon
 */
public class StringUtil {
    public static String firstToUpperCase(String str) {
        return str.substring(0,1).toUpperCase()
                    + str.substring(1);
    }
}
