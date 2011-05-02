package com.struts.tool.generators.controller;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.generators.Controller;
import com.struts.tool.generators.Project;
import com.struts.tool.helpers.DirectoryHelper;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.helpers.StringHelper;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author maycon
 */
public class StrutsConfig {
    private Controller controller;

    private String strutsConfRootPath;
    private String templatePath;

    public StrutsConfig(Controller controller) {
        this.controller = controller;
    }

    public void createModelConfig() throws StrutsToolException {
        loadPaths();
        makeFolder();
        makeModelConfig(true);
    }

    public void createConfig(List<String> actions, boolean fromBeanFactory, 
            boolean tilesTemplate) throws StrutsToolException {
        loadPaths();
        makeFolder();
        makeConfig(actions, fromBeanFactory, tilesTemplate);
    }

    private void loadPaths() {
        strutsConfRootPath = Project.SRC_PATH
                           + controller.getProject().getPackages()
                           + "/" + Project.CONTROLLER_CONFIG_FOLDER;

        templatePath = DirectoryHelper.getInstallationDirectory()
                               + Project.TEMPLATES_PATH
                               + Project.CONTROLLER_FOLDER;
    }

    private void makeFolder() throws StrutsToolException {
        File controllerConfig = new File(strutsConfRootPath);
        
        if (!controllerConfig.exists()) {
            if (!controllerConfig.mkdirs()) {
                throw new StrutsToolException(Messages.createControllerConfigFolderError);
            }
        }
    }

    private void makeModelConfig(boolean fromBeanFactory) throws StrutsToolException {
        try {
            String refStrutsConfigPath = templatePath + "/StrutsModelConfig";
            
            String strutsConfig = FileHelper.toString(refStrutsConfigPath);

            // Create the controller reference
            String controllerStr = "";
            if (fromBeanFactory) {
                controllerStr = StringHelper.firstToLowerCase(controller.getEntityName())
                              + "Controller";
            } else {
                controllerStr = controller.getProject().getPackages().replace("/", ".") + "."
                        + Project.CONTROLLER_FOLDER + "."
                        + controller.getEntityName() + "Controller";
            }

            // Replace the tags
            strutsConfig = strutsConfig.replace("<<namespace>>",
                    StringHelper.firstToLowerCase(controller.getEntityName()));
            strutsConfig = strutsConfig.replace("<<controller>>", controllerStr);

            String strutsConfigPath = strutsConfRootPath + "/"
                                    + controller.getEntityName()
                                    + "Controller.xml";

            FileHelper.toFile(strutsConfigPath, strutsConfig);
            
            addToApplication();
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createStrutsConfigError, ex);
        }
    }

    private void makeConfig(List<String> actions, boolean fromBeanFactory, boolean tilesTemplate)
            throws StrutsToolException {
        try {
            String refStrutsConfigPath = templatePath + "/StrutsModelConfig";

            String strutsConfig = FileHelper.toString(refStrutsConfigPath);

            // Create the controller reference
            String controllerStr = "";
            if (fromBeanFactory) {
                controllerStr = StringHelper.firstToLowerCase(controller.getEntityName())
                              + "Controller";
            } else {
                controllerStr = controller.getProject().getPackages().replace("/", ".") + "."
                        + Project.CONTROLLER_FOLDER + "."
                        + controller.getEntityName() + "Controller";
            }

            // Tiles templating
            String tileAttr = "";
            if (tilesTemplate) {
                tileAttr += " type=\"tiles\" ";
            }

            String actionsStr = "";
            for (String action : actions) {
                String resultPage = "/WEB-INF/"
                        + StringHelper.firstToLowerCase(controller.getEntityName());
                if (tilesTemplate) {
                    resultPage = controller.getEntityName()
                            + StringHelper.firstToUpperCase(action);
                }
                
                actionsStr += "\t\t<!-- " + StringHelper.firstToUpperCase(action) + " -->\n"
                            + "\t\t<action name=\""
                            + StringHelper.firstToLowerCase(action)+"\" method=\""
                            + StringHelper.firstToLowerCase(action)+"\" "
                            + "class=\""+controllerStr+"\">\n"
                            + "\t\t\t<result name=\"success\""+tileAttr+">\n"
                            + resultPage + "</result>\n"
                            + "\t\t\t<result name=\"error\""+tileAttr+">"
                            + resultPage+"</result>\n"
                            + "\t\t\t<result name=\"input\""+tileAttr+">"
                            + resultPage+"</result>\n"
                            + "\t\t</action>\n\n";
            }

            // Replace the tags
            strutsConfig = strutsConfig.replace("<<namespace>>",
                    StringHelper.firstToLowerCase(controller.getEntityName()));
            strutsConfig = strutsConfig.replace("<!-- generator:actions -->",
                    actionsStr);

            String strutsConfigPath = strutsConfRootPath + "/"
                                    + controller.getEntityName()
                                    + "Controller.xml";

            FileHelper.toFile(strutsConfigPath, strutsConfig);

            addToApplication();
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createStrutsConfigError, ex);
        }
    }

    private void addToApplication() throws StrutsToolException {
        try {
            String configContent = FileHelper.toString(Project.STRUTS_CONFIG_XML);

            String file = controller.getProject().getPackages() + "/"
                    + Project.CONTROLLER_FOLDER
                    + "/config/"
                    + controller.getEntityName()
                    + "Controller.xml";
            
            String include = "<!-- generator:includes -->\n"
                           + "\t<include file=\"" + file + "\" />\n";

            // Replace the tags
            configContent = configContent.replace("<!-- generator:includes -->", include);

            FileHelper.toFile(Project.STRUTS_CONFIG_XML, configContent);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.addingStrutsConfError, ex);
        }
    }
}
