package com.struts.tool.generators;

import com.struts.tool.helpers.DirectoryHelper;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.helpers.StringHelper;
import java.io.File;
import java.util.Map;

/**
 *
 * @author maycon
 */
public class Controller {
    private String entityName;
    private String packages;
    private String path;
    private Map<String, String> params;

    public Controller(String entityName, String packages, Map<String, String> params) {
        this.entityName = entityName;
        this.packages = packages;
        this.path = "src/java/" + packages + "/controller/";
        this.params = params;
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
        String refStrutsConfigPath = DirectoryHelper.getInstallationDirectory()
                + "/resources/files/StrutsConfig.txt";

        String strutsConfig = FileHelper.toString(refStrutsConfigPath);

        // Replace the tags
        strutsConfig = strutsConfig.replaceAll("<<namespace>>", entityName.toLowerCase());
        strutsConfig = strutsConfig.replaceAll("<<controller>>",
                packages.replace("/", ".") + ".controller." + entityName + "Controller");

        String strutsConfigPath = path + entityName + "Controller.xml";

        FileHelper.toFile(strutsConfigPath, strutsConfig);

        addConfigToStrutsConf();
    }

    private void addConfigToStrutsConf() {
        String strutsConfig = "src/java/struts.xml";

        String configContent = FileHelper.toString(strutsConfig);

        String file = packages + "/controller/" + entityName + "Controller.xml";
        String include = "<!-- generator:includes -->\n"
                       + "    <include file=\""+file+"\"/>\n";

        // Replace the tags
        configContent = configContent.replaceAll("<!-- generator:includes -->", include);

        FileHelper.toFile(strutsConfig, configContent);
    }

    private void makeProperties() {
        String labels = "label.save=Save\n";
        String invalids = "";
        String required = "";

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String attr = entry.getKey();
            labels += "label." + attr + "=" + StringHelper.firstToUpperCase(attr) + "\n";
            invalids += "invalid.fieldvalue." + attr + "=Invalid data type\n";
            required += attr + ".required=" + StringHelper.firstToUpperCase(attr) + " field is required\n";
        }

        String status = "status.success="+entityName+" successfully saved!\n"
                      + "status.error=An error occurred attempting "
                      + "to save the "+entityName.toLowerCase()+"\n";

        String properties = labels + "\n" + invalids + "\n" + required + "\n" + status;

        String propertiesPath = path + entityName + "Controller.properties";

        FileHelper.toFile(propertiesPath, properties);
    }

    private void makeValidator() {
        String refValidatorPath = DirectoryHelper.getInstallationDirectory()
                + "/resources/files/StrutsValidator.txt";

        String validator = FileHelper.toString(refValidatorPath);

        String validators = "";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String attr = entry.getKey();
            validators += "    <field name=\""+attr+"\">\n\n"
                       + "    </field>\n";
        }

        // Fill the validators
        validator = validator.replaceAll("<<validators>>", validators);

        String validatorPath = path + entityName + "Controller-validation.xml";

        FileHelper.toFile(validatorPath, validator);
    }

    private void makeModelController() {
        String refControllerPath = DirectoryHelper.getInstallationDirectory()
                + "/resources/files/Controller.txt";

        String controllerClass = FileHelper.toString(refControllerPath);

        // Replace the tags
        controllerClass = controllerClass.replaceAll("<<packages>>", packages.replace("/", "."));
        controllerClass = controllerClass.replaceAll("<<entityName>>", entityName);
        controllerClass = controllerClass.replaceAll("<<entityNameLower>>", entityName.toLowerCase());

        String newControllerPath = path + entityName + "Controller.java";

        FileHelper.toFile(newControllerPath, controllerClass);
    }
}
