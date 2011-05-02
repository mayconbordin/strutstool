package com.struts.tool.generators;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.attributes.Attribute;
import com.struts.tool.generators.controller.BeanFactory;
import com.struts.tool.generators.controller.Properties;
import com.struts.tool.generators.controller.StrutsConfig;
import com.struts.tool.helpers.DirectoryHelper;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.helpers.StringHelper;
import com.struts.tool.output.MessageOutput;
import com.struts.tool.output.MessageOutputFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author mayconbordin
 * @version 0.1
 */
public class Controller {
    private String entityName;
    private Project project;
    private List<Attribute> attributes;

    private MessageOutput out;

    public Controller(String entityName, Project project, List<Attribute> attributes) {
        this.entityName = entityName;
        this.project = project;
        this.attributes = attributes;
        this.out = MessageOutputFactory.getTerminalInstance();
    }

    public void createModelController() throws StrutsToolException {
        makeFolder();
        makeModelController();

        StrutsConfig strutsConfig = new StrutsConfig(this);
        strutsConfig.createModelConfig();

        Properties properties = new Properties(this);
        properties.create(true);

        BeanFactory beanFactory = new BeanFactory(this);
        beanFactory.createModelBean();
    }

    public void createController(List<String> actions, boolean includeView) throws StrutsToolException {
        makeFolder();
        makeController(actions);

        StrutsConfig strutsConfig = new StrutsConfig(this);
        strutsConfig.createConfig(actions, true, true);

        Properties properties = new Properties(this);
        properties.create(false);

        BeanFactory beanFactory = new BeanFactory(this);
        beanFactory.createBean();

        //View
        if (includeView) {
            View view = new View(entityName, project, attributes);
            view.create(actions);
        }
    }

    private void makeFolder() throws StrutsToolException {
        File controller = new File(Project.SRC_PATH + project.getPackages() 
                + "/" + Project.CONTROLLER_FOLDER);
        
        if (!controller.exists()) {
            if (!controller.mkdirs()) {
                throw new StrutsToolException(Messages.createControllerFolderError);
            }
        }
    }

    private void makeModelController() throws StrutsToolException {
        try {
            String refControllerPath = DirectoryHelper.getInstallationDirectory()
                    + Project.TEMPLATES_PATH
                    + Project.CONTROLLER_FOLDER
                    + "/ModelController";

            String controllerClass = FileHelper.toString(refControllerPath);

            // Replace the tags
            controllerClass = controllerClass.replace("<<packages>>", 
                    project.getPackages().replace("/", "."));
            controllerClass = controllerClass.replace("<<entityName>>", entityName);
            controllerClass = controllerClass.replace("<<entityNameLower>>",
                    StringHelper.firstToLowerCase(entityName));

            String newControllerPath = Project.SRC_PATH + project.getPackages() 
                    + "/" + Project.CONTROLLER_FOLDER
                    + "/" + entityName + "Controller.java";

            FileHelper.toFile(newControllerPath, controllerClass);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createModelControllerError, ex);
        }
    }

    private void makeController(List<String> actions) throws StrutsToolException {
        try {
            String refControllerPath = DirectoryHelper.getInstallationDirectory()
                    + Project.TEMPLATES_PATH
                    + Project.CONTROLLER_FOLDER
                    + "/Controller";

            String controllerClass = FileHelper.toString(refControllerPath);

            String actionsStr = "\t// generator:actions\n\n";
            for (String action : actions) {
                actionsStr += "\tpublic String "
                            + StringHelper.firstToUpperCase(action) +"() {\n"
                            + "\t\treturn SUCCESS;\n"
                            + "\t}\n\n";
            }

            // Replace the tags
            controllerClass = controllerClass.replace("<<packages>>",
                    project.getPackages().replace("/", "."));
            controllerClass = controllerClass.replace("<<entityName>>", entityName);
            controllerClass = controllerClass.replace("// generator:actions", actionsStr);

            String newControllerPath = Project.SRC_PATH + project.getPackages()
                    + "/" + Project.CONTROLLER_FOLDER
                    + "/" + entityName + "Controller.java";

            FileHelper.toFile(newControllerPath, controllerClass);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createModelControllerError, ex);
        }
    }

    // Getters and Setters =====================================================
 
    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
}
