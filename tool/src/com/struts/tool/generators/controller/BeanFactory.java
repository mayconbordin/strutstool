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
public class BeanFactory {
    private Controller controller;

    public BeanFactory(Controller controller) {
        this.controller = controller;
    }

    public void createModelBean() throws StrutsToolException {
        makeFolder();
        makeBean(true);
    }

    public void createBean() throws StrutsToolException {
        makeFolder();
        makeBean(false);
    }

    private void makeFolder() throws StrutsToolException {
        File controllerConfig = new File(Project.SRC_PATH
                + controller.getProject().getPackages() + "/"
                + Project.CONTROLLER_CONFIG_FOLDER);

        if (!controllerConfig.exists()) {
            if (!controllerConfig.mkdirs()) {
                throw new StrutsToolException(Messages.createControllerConfigFolderError);
            }
        }
    }

    private void makeBean(boolean fromModel) throws StrutsToolException {
        try {
            String beanTemplate = "BeanFactory";
            if (fromModel) {
                beanTemplate = "ModelBeanFactory";
            }
            
            String refBeanFactoryPath = DirectoryHelper.getInstallationDirectory()
                    + Project.TEMPLATES_PATH
                    + Project.CONTROLLER_FOLDER
                    + "/" + beanTemplate;

            String beanFactory = FileHelper.toString(refBeanFactoryPath);

            // Replace the tags
            beanFactory = beanFactory.replace("<<entityName>>", controller.getEntityName());
            beanFactory = beanFactory.replace("<<entityNameLower>>",
                    StringHelper.firstToLowerCase(controller.getEntityName()));
            beanFactory = beanFactory.replace("<<packages>>", 
                    controller.getProject().getPackages().replace("/", "."));

            String beanFactoryPath = Project.SRC_PATH
                    + controller.getProject().getPackages() + "/"
                    + Project.CONTROLLER_FOLDER
                    + "/config/"
                    + controller.getEntityName() + "BeanFactory.xml";

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
