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
 * @version 0.1
 */
public class Model {
    private String packages;
    private String entityName;
    private List<Attribute> attributes;

    private MessageOutput out;

    public Model(String entityName, String packages, List<Attribute> attributes, MessageOutput out) {
        this.packages = packages;
        this.entityName = entityName;
        this.attributes = attributes;
        this.out = out;
    }

    public void createModel() throws StrutsToolException {
        makeEntity();
        makeMapping();
        makeRepository();
        makeService();
        makeValidator();
    }

    private void makeValidator() throws StrutsToolException {
        // Create the folder
        File validator = new File("src/java/" + packages + "/model/validator");
        if (!validator.exists()) {
            if (!validator.mkdirs()) {
                throw new StrutsToolException(Messages.createModelValidatorFolderError);
            }
        }
    }

    private void makeService() throws StrutsToolException {
        // Create the folder
        File service = new File("src/java/" + packages + "/model/service");
        if (!service.exists()) {
            if (!service.mkdirs()) {
                throw new StrutsToolException(Messages.createModelServiceFolderError);
            }
        }

        try {
            // Service Interface
            String refServicePath = DirectoryHelper.getInstallationDirectory()
                    + "/resources/files/Service";

            String serviceContent = FileHelper.toString(refServicePath);

            // Replace the tags
            serviceContent = serviceContent.replaceAll("<<packages>>", packages.replace("/", "."));
            serviceContent = serviceContent.replaceAll("<<entityName>>", entityName);
            serviceContent = serviceContent.replaceAll("<<entityNameLower>>", entityName.toLowerCase());

            String servicePath = "src/java/" + packages + "/model/service/"
                    + entityName + "Service.java";

            FileHelper.toFile(servicePath, serviceContent);

            // Service Implementation
            String refServiceImplPath = DirectoryHelper.getInstallationDirectory()
                    + "/resources/files/ServiceImpl";

            String serviceImplContent = FileHelper.toString(refServiceImplPath);

            // Replace the tags
            serviceImplContent = serviceImplContent.replaceAll("<<packages>>", packages.replace("/", "."));
            serviceImplContent = serviceImplContent.replaceAll("<<entityName>>", entityName);
            serviceImplContent = serviceImplContent.replaceAll("<<entityNameLower>>", entityName.toLowerCase());

            String serviceImplPath = "src/java/" + packages + "/model/service/"
                    + entityName + "ServiceImpl.java";

            FileHelper.toFile(serviceImplPath, serviceImplContent);
        } catch(IOException ex) {
            throw new StrutsToolException(Messages.createModelServiceError, ex);
        }
    }

    private void makeRepository() throws StrutsToolException {
        // Create the folder
        File repository = new File("src/java/" + packages + "/model/repository");
        if (!repository.exists()) {
            if (!repository.mkdirs()) {
                throw new StrutsToolException(Messages.createModelRepositoryFolderError);
            }
        }
        
        try {
            // Repository Interface
            String refRepositoryPath = DirectoryHelper.getInstallationDirectory()
                    + "/resources/files/Repository";

            String repositoryContent = FileHelper.toString(refRepositoryPath);

            // Replace the tags
            repositoryContent = repositoryContent.replaceAll("<<packages>>", packages.replace("/", "."));
            repositoryContent = repositoryContent.replaceAll("<<entityName>>", entityName);

            String repositoryPath = "src/java/" + packages + "/model/repository/"
                    + entityName + "Repository.java";

            FileHelper.toFile(repositoryPath, repositoryContent);

            // Hibernate Repository Implementation
            String refRepositoryHibernatePath = DirectoryHelper.getInstallationDirectory()
                    + "/resources/files/RepositoryHibernate";

            String repositoryHibernateContent = FileHelper.toString(refRepositoryHibernatePath);

            // Replace the tags
            repositoryHibernateContent = repositoryHibernateContent.replaceAll("<<packages>>", packages.replace("/", "."));
            repositoryHibernateContent = repositoryHibernateContent.replaceAll("<<entityName>>", entityName);

            String repositoryHibernatePath = "src/java/" + packages + "/model/repository/"
                    + entityName + "RepositoryHibernate.java";

            FileHelper.toFile(repositoryHibernatePath, repositoryHibernateContent);
        } catch(IOException ex) {
            throw new StrutsToolException(Messages.createModelRepositoryError, ex);
        }
    }

    private void makeMapping() throws StrutsToolException {
        // Create the folder
        File mapping = new File("src/java/" + packages + "/model/mapping");
        if (!mapping.exists()) {
            if (!mapping.mkdirs()) {
                throw new StrutsToolException(Messages.createModelMappingFolderError);
            }
        }

        try {
            String refMappingPath = DirectoryHelper.getInstallationDirectory()
                    + "/resources/files/HibernateMapping";

            String mappingContent = FileHelper.toString(refMappingPath);

            //Load HibernateMappingProperty

            String properties = "";
            for (Attribute attr : attributes) {
                if (!attr.getName().equals("id")) {
                    properties += "        <property name=\""+attr+"\" column=\""
                                + ""+attr+"\" ";

                    if (attr.getType().getRawType().equals("char") 
                            || attr.getType().getRawType().equals("string")
                            || attr.getType().getRawType().equals("character")) {
                        properties += "length=\"\" ";
                    }

                    properties += "not-null=\"true\" type=\""
                                + attr.getType().getHibernateType()+"\"/>\n";
                }
            }

            // Replace the tags
            mappingContent = mappingContent.replaceAll("<<packages>>", packages.replace("/", "."));
            mappingContent = mappingContent.replaceAll("<<entityName>>", entityName);
            mappingContent = mappingContent.replaceAll("<<entityNameLower>>", entityName.toLowerCase());
            mappingContent = mappingContent.replaceAll("<<properties>>", properties);

            String mappingPath = "src/java/" + packages + "/model/mapping/"
                    + entityName + ".hbm.xml";

            FileHelper.toFile(mappingPath, mappingContent);

            // Add the mapping to hibernate config file
            addMappingToConfig();
        } catch(IOException ex) {
            throw new StrutsToolException(Messages.createModelMappingError, ex);
        }
    }

    private void addMappingToConfig() throws StrutsToolException {
        try {
            String hibernateConfig = "src/java/hibernate.cfg.xml";

            String configContent = FileHelper.toString(hibernateConfig);

            //Load HibernateMappingResource

            String resource = packages + "/model/mapping/" + entityName + ".hbm.xml";
            String mapping = "<!-- generator:mappings -->\n"
                           + "    <mapping resource=\""+resource+"\" />\n";

            // Replace the tags
            configContent = configContent.replaceAll("<!-- generator:mappings -->", mapping);

            FileHelper.toFile(hibernateConfig, configContent);
        } catch(IOException ex) {
            throw new StrutsToolException(Messages.addingMappingToConfigError, ex);
        }
    }

    private void makeEntity() throws StrutsToolException {
        // Create the folder
        File entity = new File("src/java/" + packages + "/model/entity");
        if (!entity.exists()) {
            if (!entity.mkdirs()) {
                throw new StrutsToolException(Messages.createModelEntityFolderError);
            }
        }

        try {
            String refEntityPath = DirectoryHelper.getInstallationDirectory()
                    + "/resources/files/Entity";

            String entityContent = FileHelper.toString(refEntityPath);

            // Create the attributes, getters and setters
            String attribs = "";
            String accessors = "";
            String imports = "";
            for (Attribute attr : attributes) {
                if (attr.getType().getJavaTypeImport() != null) {
                    imports += "import " + attr.getType().getJavaTypeImport() + ";\n";
                }

                attribs += "    private "+attr.getType()+" "+attr+";\n";

                if (!attr.getName().equals("id")) {
                    accessors += "    @NotNull\n"
                               + "    @XSSFilter\n";
                }

                accessors += "    public "+attr.getType()+" get"+attr.getNameFirstUpper()+"() {\n"
                           + "        return "+attr.getNameLower()+";\n"
                           + "    }\n"
                           + "\n"
                           + "    public void set"+attr.getNameFirstUpper()+"("+attr.getType()+" "
                           + attr.getNameLower()+") {\n"
                           + "        this."+attr.getNameLower()+" = "+attr.getNameLower()+";\n"
                           + "    }\n";
            }

            // Replace the tags
            entityContent = entityContent.replaceAll("<<packages>>", packages.replace("/", "."));
            entityContent = entityContent.replaceAll("<<entityName>>", entityName);
            entityContent = entityContent.replaceAll("<<attributes>>", attribs);
            entityContent = entityContent.replaceAll("<<accessors>>", accessors);
            entityContent = entityContent.replaceAll("<<imports>>", imports);

            String entityPath = "src/java/" + packages + "/model/entity/"
                    + entityName + ".java";

            FileHelper.toFile(entityPath, entityContent);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createModelEntityError, ex);
        }
    }
}