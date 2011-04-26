package com.struts.tool;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author maycon
 */
public class Controller {
    private String entityName;
    private String packages;
    private String path;

    public Controller(String entityName, String packages) {
        this.entityName = entityName;
        this.packages = packages;
        this.path = "src/java/" + packages + "/controller/";
    }

    public void createModelController() {
        makeFolder();
        makeModelController();
        makeStrutsConfig();
        makeProperties();
        makeValidator();
    }

    private void makeFolder() {
        new File("src/java/" + packages + "/controller").mkdirs();
        new File("src/java/" + packages + "/interceptor").mkdirs();
    }

    private void makeStrutsConfig() {
        String refStrutsConfigPath = Directory.getInstallationDirectory()
                + "/resources/files/StrutsConfigHeader.txt";

        String strutsConfig = FileUtil.toString(refStrutsConfigPath);

        // Replace the tags
        strutsConfig = strutsConfig.replaceAll("<<namespace>>", entityName.toLowerCase());
        strutsConfig = strutsConfig.replaceAll("<<controller>>",
                packages.replace("/", ".") + "." + entityName);

        String strutsConfigPath = "src/java/" + packages + "/controller/"
                + entityName + "Controller.xml";

        FileUtil.toFile(strutsConfigPath, strutsConfig);
    }

    private void makeProperties() {
        File properties = new File(path + entityName + "Controller.properties");
        try {
            properties.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void makeValidator() {
        String refValidatorPath = Directory.getInstallationDirectory()
                + "/resources/files/StrutsValidator.txt";

        String validator = FileUtil.toString(refValidatorPath);

        String validatorPath = "src/java/" + packages + "/controller/"
                + entityName + "Controller-validation.xml";

        FileUtil.toFile(validatorPath, validator);
    }

    private void makeModelController() {
        String refControllerPath = Directory.getInstallationDirectory()
                + "/resources/files/Controller.txt";

        String controllerClass = FileUtil.toString(refControllerPath);

        // Replace the tags
        controllerClass = controllerClass.replaceAll("<<packages>>", packages.replace("/", "."));
        controllerClass = controllerClass.replaceAll("<<entityName>>", entityName);
        controllerClass = controllerClass.replaceAll("<<entityNameLower>>", entityName.toLowerCase());

        String newControllerPath = "src/java/" + packages + "/controller/"
                + entityName + "Controller.java";

        FileUtil.toFile(newControllerPath, controllerClass);
    }

    /*
    private void makeModelController() {
        try {
            String filename = "src/java/" + packages + "/controller/"
                    + entityName + "Controller.java";

            FileWriter fstream = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(
                "package " + packages.replace("/", ".") + ".controller;\n" +
                "\n" +
                "import com.framework.util.struts.StrutsController;\n" +
                "import " + packages.replace("/", ".") + ".model." + entityName + ";\n" +
                "import com.opensymphony.xwork2.ModelDriven;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "\n" +
                "class " + entityName + "Controller extends StrutsController " +
                "implements ModelDriven<" + entityName + ">{\n" +
                "   private " + entityName + " " + entityName.toLowerCase() + ";\n" +
                "   private List<" + entityName + "> " + entityName.toLowerCase() + "List = new ArrayList();\n" +
                "   private int id;\n" +
                "   private " + entityName + "Service " + entityName.toLowerCase() + "Service;\n" +
                "\n" +
                "   public String index() {\n" +
                "       try {\n" +
                "           " + entityName.toLowerCase() + "List = get" + entityName + "Service().findAll(\"id\", \"asc\");\n" +
                "       } catch (RepositoryException ex) {\n" +
                "           errorHandler(ex);\n" +
                "       }\n" +
                "\n" +
                "       statusHandler();" +
                "\n" +
                "       return SUCCESS;\n" +
                "   }\n" +
                "\n" +
                "}"
            );

            //Close the output stream
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    */
}
