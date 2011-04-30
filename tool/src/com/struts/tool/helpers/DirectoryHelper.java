package com.struts.tool.helpers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 * @author mayconbordin
 * @version 0.1
 */
public class DirectoryHelper {
    public static String getInstallationDirectory() {
        String path = "";
        try {
            path = DirectoryHelper.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException ex) {
            return null;
        }

        int pos = path.lastIndexOf("/");
        path = path.substring(0, pos);

        return path;
    }

    public static String getCurrentDirectory() {
        File directory = new File (".");
        String current = "";
        
        try {
            current = directory.getCanonicalPath();
        } catch (IOException ex) {
            return null;
        }

        //return current;

        return System.getProperty("user.dir");
    }

    public static String getParentDirectory() {
        File directory = new File ("..");

        String parent = "";

        try {
            parent = directory.getCanonicalPath();
        } catch (IOException ex) {
            return null;
        }

        return parent;
    }
}
