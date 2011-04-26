package com.struts.tool.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author maycon
 */
public class FileHelper {
    public static String toString(String filename) {
        try {
            File file = new File(filename);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = "", string = "";
            while((line = reader.readLine()) != null) {
                string += line + "\r\n";
            }
            reader.close();
            return string;
         } catch (IOException ex) {
             ex.printStackTrace();
         }

        return null;
    }

    public static boolean toFile(String filename, String content) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(content);
            writer.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
