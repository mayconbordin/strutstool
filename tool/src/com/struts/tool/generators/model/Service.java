package com.struts.tool.generators.model;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.generators.Model;
import com.struts.tool.generators.Project;
import com.struts.tool.helpers.DirectoryHelper;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.helpers.StringHelper;
import com.struts.tool.output.MessageOutput;
import com.struts.tool.output.MessageOutputFactory;
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

    private MessageOutput out;

    public Service(Model model) {
        this.model = model;
        this.out = MessageOutputFactory.getTerminalInstance();
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
            out.put("create  " + serviceRootPath);
            if (!service.mkdirs()) {
                throw new StrutsToolException(Messages.createModelServiceFolderError);
            }
        } else {
            out.put("exists  " + serviceRootPath);
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
            serviceContent = serviceContent.replace("<<entityNameLower>>", StringHelper.lcfirst(model.getEntityName()));

            String servicePath = serviceRootPath 
                    + "/" + model.getEntityName()
                    + "Service.java";

            out.put("create  " + servicePath);

            FileHelper.toFile(servicePath, serviceContent);

            // Service Implementation
            String refServiceImplPath = templatePath + "/ServiceImpl";

            String serviceImplContent = FileHelper.toString(refServiceImplPath);

            // Replace the tags
            serviceImplContent = serviceImplContent.replace("<<packages>>", model.getProject().getPackages().replace("/", "."));
            serviceImplContent = serviceImplContent.replace("<<entityName>>", model.getEntityName());
            serviceImplContent = serviceImplContent.replace("<<entityNameLower>>", StringHelper.lcfirst(model.getEntityName()));

            String serviceImplPath = serviceRootPath
                    + "/" + model.getEntityName()
                    + "ServiceImpl.java";

            out.put("create  " + serviceImplPath);

            FileHelper.toFile(serviceImplPath, serviceImplContent);
        } catch(IOException ex) {
            throw new StrutsToolException(Messages.createModelServiceError, ex);
        }
    }
}
