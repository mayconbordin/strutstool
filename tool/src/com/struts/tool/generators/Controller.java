package com.struts.tool.generators;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.attributes.Attribute;
import com.struts.tool.helpers.DirectoryHelper;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.output.MessageOutput;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author mayconbordin
 * @version 0.1bordin
 */
public class Controller {
    private String entityName;
    private String packages;
    private String path;
    private List<Attribute> attributes;

    private MessageOutput out;

    public Controller(String entityName, String packages, List<Attribute> attributes, MessageOutput out) {
        this.entityName = entityName;
        this.packages = packages;
        this.path = "src/java/" + packages + "/controller/";
        this.attributes = attributes;
        this.out = out;
    }

    public void createModelController() throws StrutsToolException {
        makeFolder();
        makeModelController();
        makeStrutsConfig();
        makeProperties();
        //makeValidator();
    }

    private void makeFolder() throws StrutsToolException {
        File controller = new File("src/java/" + packages + "/controller");
        if (!controller.exists()) {
            if (!controller.mkdirs()) {
                throw new StrutsToolException(Messages.createControllerFolderError);
            }
        }
        
        File interceptor = new File("src/java/" + packages + "/interceptor");
        if (!interceptor.exists()) {
            if (!interceptor.mkdirs()) {
                throw new StrutsToolException(Messages.createControllerFolderError);
            }
        }
    }

    private void makeStrutsConfig() throws StrutsToolException {
        try {
            String refStrutsConfigPath = DirectoryHelper.getInstallationDirectory()
                    + "/resources/files/StrutsConfig";
            String strutsConfig = FileHelper.toString(refStrutsConfigPath);

            // Replace the tags
            strutsConfig = strutsConfig.replaceAll("<<namespace>>", entityName.toLowerCase());
            strutsConfig = strutsConfig.replaceAll("<<controller>>", packages.replace("/", ".")
                    + ".controller." + entityName + "Controller");
            
            String strutsConfigPath = path + entityName + "Controller.xml";

            FileHelper.toFile(strutsConfigPath, strutsConfig);
            addConfigToStrutsConf();
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createStrutsConfigError, ex);
        }
    }

    private void addConfigToStrutsConf() throws StrutsToolException {
        try {
            String strutsConfig = "src/java/struts.xml";
            String configContent = FileHelper.toString(strutsConfig);

            //Load StrutsConfigInclude

            String file = packages + "/controller/" + entityName + "Controller.xml";
            String include = "<!-- generator:includes -->\n"
                           + "    <include file=\"" + file + "\"/>\n";

            // Replace the tags
            configContent = configContent.replaceAll("<!-- generator:includes -->", include);
            
            FileHelper.toFile(strutsConfig, configContent);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.addingStrutsConfError, ex);
        }
    }

    private void makeProperties() throws StrutsToolException {
        try {
            String labels = "label.save=Save\n";
            String invalids = "";
            String required = "";

            for (Attribute attr : attributes) {
                labels += "label." + attr + "="
                        + attr.getNameFirstUpper() + "\n";

                invalids += "invalid.fieldvalue." + attr + "=Invalid data type,"
                          + " should be an "+attr.getType()+"\n";
                
                required += attr + ".required=" + attr.getNameFirstUpper()
                          + " field is required\n";
            }

            String status = "status.success="+entityName+" successfully saved!\n"
                          + "status.error=An error occurred attempting "
                          + "to save the "+entityName.toLowerCase()+"\n"
                          + "\n"
                          + "status.notFound="+entityName+" does not exist!";

            String properties = labels + "\n" + invalids + "\n" + required + "\n"
                              + status;

            String propertiesPath = path + entityName + "Controller.properties";

            FileHelper.toFile(propertiesPath, properties);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createControllerPropError, ex);
        }
    }

    private void makeValidator() throws StrutsToolException {
        try {
            String refValidatorPath = DirectoryHelper.getInstallationDirectory()
                    + "/resources/files/StrutsValidator";

            String validator = FileHelper.toString(refValidatorPath);

            String validators = "";
            for (Attribute attr : attributes) {
                validators += "    <field name=\""+attr+"\">\n\n"
                           + "    </field>\n";
            }

            // Fill the validators
            validator = validator.replaceAll("<<validators>>", validators);

            String validatorPath = path + entityName + "Controller-validation.xml";

            FileHelper.toFile(validatorPath, validator);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createControllerValidatorError, ex);
        }
    }

    private void makeModelController() throws StrutsToolException {
        try {
            String refControllerPath = DirectoryHelper.getInstallationDirectory()
                    + "/resources/files/Controller";

            String controllerClass = FileHelper.toString(refControllerPath);

            // Replace the tags
            controllerClass = controllerClass.replaceAll("<<packages>>", packages.replace("/", "."));
            controllerClass = controllerClass.replaceAll("<<entityName>>", entityName);
            controllerClass = controllerClass.replaceAll("<<entityNameLower>>", entityName.toLowerCase());

            String newControllerPath = path + entityName + "Controller.java";

            FileHelper.toFile(newControllerPath, controllerClass);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createModelControllerError, ex);
        }
    }
}