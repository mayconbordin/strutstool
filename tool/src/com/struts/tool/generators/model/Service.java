package com.struts.tool.generators.model;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.generators.Model;
import com.struts.tool.generators.Project;
import com.struts.tool.helpers.DirectoryHelper;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.helpers.StringHelper;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author maycon
 */
public class Service {
    private Model model;

    private String serviceRootPath;
    private String templatePath;

    public Service(Model model) {
        this.model = model;
    }

    public void create() throws StrutsToolException {
        loadPaths();
        makeFolder();
        makeService();
    }

    private void loadPaths() {
        serviceRootPath = Project.SRC_PATH
                        + model.getProject().getPackages()
                        + "/" + Project.MODEL_SERVICE_FOLDER;

        templatePath = DirectoryHelper.getInstallationDirectory()
                            + Project.TEMPLATES_PATH
                            + Project.MODEL_FOLDER;
    }

    private void makeFolder() throws StrutsToolException {
        // Create the folder
        File service = new File(serviceRootPath);
        
        if (!service.exists()) {
            if (!service.mkdirs()) {
                throw new StrutsToolException(Messages.createModelServiceFolderError);
            }
        }
    }

    private void makeService() throws StrutsToolException {
        try {
            // Service Interface
            String refServicePath = templatePath + "/Service";

            String serviceContent = FileHelper.toString(refServicePath);

            // Replace the tags
            serviceContent = serviceContent.replace("<<packages>>", model.getProject().getPackages().replace("/", "."));
            serviceContent = serviceContent.replace("<<entityName>>", model.getEntityName());
            serviceContent = serviceContent.replace("<<entityNameLower>>", StringHelper.firstToLowerCase(model.getEntityName()));

            String servicePath = serviceRootPath 
                    + "/" + model.getEntityName()
                    + "Service.java";

            FileHelper.toFile(servicePath, serviceContent);

            // Service Implementation
            String refServiceImplPath = templatePath + "/ServiceImpl";

            String serviceImplContent = FileHelper.toString(refServiceImplPath);

            // Replace the tags
            serviceImplContent = serviceImplContent.replace("<<packages>>", model.getProject().getPackages().replace("/", "."));
            serviceImplContent = serviceImplContent.replace("<<entityName>>", model.getEntityName());
            serviceImplContent = serviceImplContent.replace("<<entityNameLower>>", StringHelper.firstToLowerCase(model.getEntityName()));

            String serviceImplPath = serviceRootPath
                    + "/" + model.getEntityName()
                    + "ServiceImpl.java";

            FileHelper.toFile(serviceImplPath, serviceImplContent);
        } catch(IOException ex) {
            throw new StrutsToolException(Messages.createModelServiceError, ex);
        }
    }
}
