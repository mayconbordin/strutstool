package com.struts.tool.generators.controller;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.generators.Controller;
import com.struts.tool.generators.Project;
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
 * @author maycon
 */
public class BeanFactory {
    private Controller controller;
    private MessageOutput out;

    private String beanFactoryRootPath;
    private String templatePath;

    public BeanFactory(Controller controller) {
        this.controller = controller;
        this.out = MessageOutputFactory.getTerminalInstance();
    }

    public void createModelBean() throws StrutsToolException {
        loadPaths();
        makeFolder();
        makeBean(true);
    }

    public void createBean() throws StrutsToolException {
        loadPaths();
        makeFolder();
        makeBean(false);
    }

    private void loadPaths() {
        beanFactoryRootPath = Project.SRC_PATH
                            + controller.getProject().getPackages() + "/"
                            + Project.CONTROLLER_CONFIG_FOLDER;

        templatePath = DirectoryHelper.getInstallationDirectory()
                     + Project.TEMPLATES_PATH
                     + Project.CONTROLLER_FOLDER;
    }

    private void makeFolder() throws StrutsToolException {
        File controllerConfig = new File(beanFactoryRootPath);

        if (!controllerConfig.exists()) {
            out.put("create  " + beanFactoryRootPath);
            if (!controllerConfig.mkdirs()) {
                throw new StrutsToolException(Messages.createControllerConfigFolderError);
            }
        } else {
            out.put("exists  " + beanFactoryRootPath);
        }
    }

    private void makeBean(boolean fromModel) throws StrutsToolException {
        try {
            String beanTemplate = "BeanFactory";
            if (fromModel) {
                beanTemplate = "ModelBeanFactory";
            }
            
            String refBeanFactoryPath = templatePath + "/" + beanTemplate;

            String beanFactory = FileHelper.toString(refBeanFactoryPath);

            // Replace the tags
            beanFactory = beanFactory.replace("<<entityName>>", controller.getEntityName());
            beanFactory = beanFactory.replace("<<entityNameLower>>",
                    StringHelper.lcfirst(controller.getEntityName()));
            beanFactory = beanFactory.replace("<<packages>>", 
                    controller.getProject().getPackages().replace("/", "."));

            String beanFactoryPath = beanFactoryRootPath + "/"
                                   + controller.getEntityName() + "BeanFactory.xml";

            out.put("create  " + beanFactoryPath);

            FileHelper.toFile(beanFactoryPath, beanFactory);

            addToApplication();
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createStrutsConfigError, ex);
        }
    }

    private void addToApplication() throws StrutsToolException {
        try {
            String applicationContext = Project.WEB_PATH + "applicationContext.xml";
            String appContextContent = FileHelper.toString(applicationContext);

            out.put("modify  " + applicationContext);

            String file = controller.getProject().getPackages() + "/"
                    + Project.CONTROLLER_FOLDER
                    + "/config/"
                    + controller.getEntityName()
                    + "BeanFactory.xml";

            String include = "<!-- generator:imports -->\n"
                           + "\t<import resource=\"classpath:" + file + "\" />\n";

            // Replace the tags
            appContextContent = appContextContent.replace("<!-- generator:imports -->", include);

            FileHelper.toFile(applicationContext, appContextContent);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.addingStrutsConfError, ex);
        }
    }
}
