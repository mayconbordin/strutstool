package com.struts.tool.generators;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.builder.components.Attribute;
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

    private String controllerRootPath;
    private String templatePath;

    private MessageOutput out;

    public Controller(String entityName, Project project, List<Attribute> attributes) {
        this.entityName = entityName;
        this.project = project;
        this.attributes = attributes;
        this.out = MessageOutputFactory.getTerminalInstance();
    }

    public Controller(String entityName, Project project) {
        this.entityName = entityName;
        this.project = project;
        this.out = MessageOutputFactory.getTerminalInstance();
    }

    public void createModelController() throws StrutsToolException {
        loadPaths();
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
        loadPaths();
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

    private void loadPaths() {
        controllerRootPath = Project.SRC_PATH + project.getPackages()
                           + "/" + Project.CONTROLLER_FOLDER;

        templatePath = DirectoryHelper.getInstallationDirectory()
                     + Project.TEMPLATES_PATH
                     + Project.CONTROLLER_FOLDER;
    }

    private void makeFolder() throws StrutsToolException {
        File controller = new File(controllerRootPath);
        
        if (!controller.exists()) {
            out.put("create  " + controllerRootPath);
            if (!controller.mkdirs()) {
                throw new StrutsToolException(Messages.createControllerFolderError);
            }
        } else {
            out.put("exists  " + controllerRootPath);
        }
    }

    private void makeModelController() throws StrutsToolException {
        try {
            String refControllerPath = templatePath + "/ModelController";

            String controllerClass = FileHelper.toString(refControllerPath);

            // Replace the tags
            controllerClass = controllerClass.replace("<<packages>>", 
                    project.getPackages().replace("/", "."));
            controllerClass = controllerClass.replace("<<entityName>>", entityName);
            controllerClass = controllerClass.replace("<<entityNameLower>>",
                    StringHelper.lcfirst(entityName));

            String newControllerPath = controllerRootPath + "/"
                                     + entityName + "Controller.java";

            out.put("create  " + newControllerPath);

            FileHelper.toFile(newControllerPath, controllerClass);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createModelControllerError, ex);
        }
    }

    private void makeController(List<String> actions) throws StrutsToolException {
        try {
            String refControllerPath = templatePath + "/Controller";

            String controllerClass = FileHelper.toString(refControllerPath);

            String actionsStr = "\t// generator:actions\n\n";
            for (String action : actions) {
                actionsStr += "\tpublic String "
                            + StringHelper.lcfirst(action) +"() {\n"
                            + "\t\treturn SUCCESS;\n"
                            + "\t}\n\n";
            }

            // Replace the tags
            controllerClass = controllerClass.replace("<<packages>>",
                    project.getPackages().replace("/", "."));
            controllerClass = controllerClass.replace("<<entityName>>", entityName);
            controllerClass = controllerClass.replace("// generator:actions", actionsStr);

            String newControllerPath = controllerRootPath + "/"
                                     + entityName + "Controller.java";

            out.put("create  " + newControllerPath);

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
