package com.struts.tool;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author maycon
 */
public class StrutsTool {
    public boolean newProject(String name) {
       File projectDir = new File(name);
       boolean status = projectDir.mkdir();
       
       File projectFiles = new File("resources/project.zip");
       ZipHelper zip = new ZipHelper();
       
       try {
           zip.unzip(projectFiles, projectDir);
       } catch (IOException ex) {
           System.out.println("Erro: " + ex.getMessage());
           status = false;
       }

       return status;
    }

    public boolean buildXmlExists() {
        File buildXml = new File("build.xml");

        if (buildXml.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public void scaffold(String[] args) {
        File dir = new File(".");

        String packages = "";
        String entityName = "";

        if (args.length > 1) {
            //Get the package name
            int lastDot = args[1].lastIndexOf(".");
            packages = args[1].substring(0, lastDot).replace(".", "/");

            //Get the entity name
            String[] temp = args[1].split("\\.");
            entityName = StringUtil.firstToUpperCase(temp[temp.length - 1]);
        }

        // Create the controllers
        Controller controller = new Controller(entityName, packages);
        controller.createModelController();

        // Create the model
        Model model = new Model(entityName, packages);
        model.createModel(args);

        // Create the view
        makeView(entityName);
    }

    private void makeView(String entityName) {
        new File("web/WEB-INF/" + entityName.toLowerCase()).mkdirs();
    }
}
