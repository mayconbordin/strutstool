package com.struts.tool.generators.model;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.generators.Model;
import com.struts.tool.generators.Project;
import com.struts.tool.helpers.DirectoryHelper;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.output.MessageOutput;
import com.struts.tool.output.MessageOutputFactory;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author maycon
 */
public class Repository {
    private Model model;

    private String repositoryRootPath;
    private String templatePath;

    private MessageOutput out;

    public Repository(Model model) {
        this.model = model;
        this.out = MessageOutputFactory.getTerminalInstance();
    }

    public void create() throws StrutsToolException {
        loadPaths();
        makeFolder();
        makeRepository();
    }

    private void loadPaths() {
        repositoryRootPath = Project.SRC_PATH
                           + model.getProject().getPackages()
                           + "/" + Project.MODEL_REPOSITORY_FOLDER;

        templatePath = DirectoryHelper.getInstallationDirectory()
                               + Project.TEMPLATES_PATH
                               + Project.MODEL_FOLDER;
    }

    private void makeFolder() throws StrutsToolException {
        // Create the folder
        File repository = new File(repositoryRootPath);
        
        if (!repository.exists()) {
            out.put("create  " + repositoryRootPath);
            if (!repository.mkdirs()) {
                throw new StrutsToolException(Messages.createModelRepositoryFolderError);
            }
        } else {
            out.put("exists  " + repositoryRootPath);
        }
    }

    private void makeRepository() throws StrutsToolException {
        try {
            // Repository Interface
            String refRepositoryPath = templatePath + "/Repository";

            String repositoryContent = FileHelper.toString(refRepositoryPath);

            // Replace the tags
            repositoryContent = repositoryContent.replace("<<packages>>", 
                    model.getProject().getPackages().replace("/", "."));
            repositoryContent = repositoryContent.replace("<<entityName>>",
                    model.getEntityName());

            String repositoryPath = repositoryRootPath + "/"
                    + model.getEntityName() + "Repository.java";

            out.put("create  " + repositoryPath);

            FileHelper.toFile(repositoryPath, repositoryContent);

            // Hibernate Repository Implementation
            String refRepositoryHibernatePath = templatePath + "/RepositoryHibernate";

            String repositoryHibernateContent = FileHelper.toString(refRepositoryHibernatePath);

            // Replace the tags
            repositoryHibernateContent = repositoryHibernateContent.replace(
                    "<<packages>>", model.getProject().getPackages().replace("/", "."));
            repositoryHibernateContent = repositoryHibernateContent.replace(
                    "<<entityName>>", model.getEntityName());

            String repositoryHibernatePath = repositoryRootPath + "/"
                    + model.getEntityName() + "RepositoryHibernate.java";

            out.put("create  " + repositoryHibernatePath);

            FileHelper.toFile(repositoryHibernatePath, repositoryHibernateContent);
        } catch(IOException ex) {
            throw new StrutsToolException(Messages.createModelRepositoryError, ex);
        }
    }
}
