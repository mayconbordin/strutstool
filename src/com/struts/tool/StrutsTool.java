package com.struts.tool;

import com.struts.tool.generators.Controller;
import com.struts.tool.generators.Model;
import com.struts.tool.helpers.StringHelper;
import com.struts.tool.helpers.ZipHelper;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author maycon
 */
public class StrutsTool {
    private Map<String, String> params;
    
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
        extractParams(args, true);
        
        //File dir = new File(".");

        String packages = "";
        String entityName = "";

        if (args.length > 1) {
            //Get the package name
            int lastDot = args[1].lastIndexOf(".");
            packages = args[1].substring(0, lastDot).replace(".", "/");

            //Get the entity name
            String[] temp = args[1].split("\\.");
            entityName = StringHelper.firstToUpperCase(temp[temp.length - 1]);
        }

        // Create the controllers
        Controller controller = new Controller(entityName, packages, params);
        controller.createModelController();

        // Create the model
        Model model = new Model(entityName, packages, params);
        model.createModel();

        // Create the view
        makeView(entityName);
    }

    private void makeView(String entityName) {
        new File("web/WEB-INF/" + entityName.toLowerCase()).mkdirs();
    }

    private void extractParams(String[] args, boolean addId) {
        params = new HashMap<String, String>();
        if (addId) params.put("id", "Integer");

        String[] temp;

        for (int i = 2; i < args.length; i++) {
            temp = args[i].split(":");
            params.put(temp[0], temp[1]);
        }
    }
}
