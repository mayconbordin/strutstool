package com.struts.tool.generators;

import com.struts.tool.helpers.DirectoryHelper;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.helpers.StringHelper;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author maycon
 */
public class Model {
    private String packages;
    private String entityName;
    private Map<String, String> params;
    private Map<String, String> dataTypes;

    public Model(String entityName, String packages, Map<String, String> params) {
        this.packages = packages;
        this.entityName = entityName;
        this.params = params;
    }

    public void createModel() {
        loadDataTypes();
        makeEntity();
        makeMapping();
        makeRepository();
    }

    private void makeService() {
        // Create the folder
        new File("src/java/" + packages + "/model/repository").mkdirs();

        // Service Interface
        String refRepositoryPath = DirectoryHelper.getInstallationDirectory()
                + "/resources/files/Repository.txt";

        String repositoryContent = FileHelper.toString(refRepositoryPath);

        // Replace the tags
        repositoryContent = repositoryContent.replaceAll("<<packages>>", packages.replace("/", "."));
        repositoryContent = repositoryContent.replaceAll("<<entityName>>", entityName);

        String repositoryPath = "src/java/" + packages + "/model/repository/"
                + entityName + "Repository.java";

        FileHelper.toFile(repositoryPath, repositoryContent);

        // Service Implementation
        String refRepositoryHibernatePath = DirectoryHelper.getInstallationDirectory()
                + "/resources/files/Repository.txt";

        String repositoryHibernateContent = FileHelper.toString(refRepositoryHibernatePath);

        // Replace the tags
        repositoryHibernateContent = repositoryHibernateContent.replaceAll("<<packages>>", packages.replace("/", "."));
        repositoryHibernateContent = repositoryHibernateContent.replaceAll("<<entityName>>", entityName);

        String repositoryHibernatePath = "src/java/" + packages + "/model/repository/"
                + entityName + "RepositoryHibernate.java";

        FileHelper.toFile(repositoryHibernatePath, repositoryHibernateContent);
    }

    private void makeRepository() {
        // Create the folder
        new File("src/java/" + packages + "/model/repository").mkdirs();

        // Repository Interface
        String refRepositoryPath = DirectoryHelper.getInstallationDirectory()
                + "/resources/files/Repository.txt";

        String repositoryContent = FileHelper.toString(refRepositoryPath);

        // Replace the tags
        repositoryContent = repositoryContent.replaceAll("<<packages>>", packages.replace("/", "."));
        repositoryContent = repositoryContent.replaceAll("<<entityName>>", entityName);

        String repositoryPath = "src/java/" + packages + "/model/repository/"
                + entityName + "Repository.java";

        FileHelper.toFile(repositoryPath, repositoryContent);

        // Hibernate Repository Implementation
        String refRepositoryHibernatePath = DirectoryHelper.getInstallationDirectory()
                + "/resources/files/Repository.txt";

        String repositoryHibernateContent = FileHelper.toString(refRepositoryHibernatePath);

        // Replace the tags
        repositoryHibernateContent = repositoryHibernateContent.replaceAll("<<packages>>", packages.replace("/", "."));
        repositoryHibernateContent = repositoryHibernateContent.replaceAll("<<entityName>>", entityName);

        String repositoryHibernatePath = "src/java/" + packages + "/model/repository/"
                + entityName + "RepositoryHibernate.java";

        FileHelper.toFile(repositoryHibernatePath, repositoryHibernateContent);
    }

    private void makeMapping() {
        // Create the folder
        new File("src/java/" + packages + "/model/mapping").mkdirs();

        String refMappingPath = DirectoryHelper.getInstallationDirectory()
                + "/resources/files/HibernateMapping.txt";

        String mappingContent = FileHelper.toString(refMappingPath);

        String properties = "";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String attr = entry.getKey();
            String type = entry.getValue();

            properties += "<property name=\""+attr+"\" column=\""+attr+"\" ";

            if (type.toLowerCase().equals("character") || type.toLowerCase().equals("string")) {
                properties += "length=\"\" ";
            }
            
            properties += "not-null=\"true\" type=\""+type.toLowerCase()+"\"/>\n";
        }

        // Replace the tags
        mappingContent = mappingContent.replaceAll("<<packages>>", packages.replace("/", "."));
        mappingContent = mappingContent.replaceAll("<<entityName>>", entityName);
        mappingContent = mappingContent.replaceAll("<<entityNameLower>>", entityName.toLowerCase());
        mappingContent = mappingContent.replaceAll("<<properties>>", properties);

        String mappingPath = "src/java/" + packages + "/model/mapping/"
                + entityName + ".hbm.xml";

        FileHelper.toFile(mappingPath, mappingContent);
    }

    private void makeEntity() {
        // Create the folder
        new File("src/java/" + packages + "/model/entity").mkdirs();
        
        String refEntityPath = DirectoryHelper.getInstallationDirectory()
                + "/resources/files/Entity.txt";

        String entityContent = FileHelper.toString(refEntityPath);

        // Create the attributes, getters and setters
        String attributes = "";
        String accessors = "";
        String imports = "";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String attr = entry.getKey();
            String type = entry.getValue();

            if (dataTypes.containsKey(type.toLowerCase())) {
                imports += "import " + dataTypes.get(type.toLowerCase()) + ";\n";
            }

            attributes += "   private "+type+" "+attr+";\n";
            accessors += "    @NotNull\n"
                       + "    @XSSFilter\n"
                       + "    public "+type+" get"+StringHelper.firstToUpperCase(attr)+"() {\n"
                       + "        return "+attr.toLowerCase()+";\n"
                       + "    }\n"
                       + "\n"
                       + "    public void set"+StringHelper.firstToUpperCase(attr)+"("+type+" "
                       + attr.toLowerCase()+") {\n"
                       + "        this."+attr.toLowerCase()+" = "+attr.toLowerCase()+";\n"
                       + "    }\n";
        }

        // Replace the tags
        entityContent = entityContent.replaceAll("<<packages>>", packages.replace("/", "."));
        entityContent = entityContent.replaceAll("<<entityName>>", entityName);
        entityContent = entityContent.replaceAll("<<attributes>>", attributes);
        entityContent = entityContent.replaceAll("<<accessors>>", accessors);
        entityContent = entityContent.replaceAll("<<imports>>", imports);

        String entityPath = "src/java/" + packages + "/model/entity/"
                + entityName + ".java";

        FileHelper.toFile(entityPath, entityContent);
    }

    private void loadDataTypes() {
        dataTypes = new HashMap<String, String>();

        dataTypes.put("list", "java.util.List");
        dataTypes.put("date", "java.util.Date");
        dataTypes.put("currency", "java.util.Currency");
        dataTypes.put("set", "java.util.Set");
        dataTypes.put("map", "java.util.Map");
    }

    private void makeFolder() {
        new File("src/java/" + packages + "/model/entity").mkdirs();
        new File("src/java/" + packages + "/model/mapping").mkdirs();
        new File("src/java/" + packages + "/model/repository").mkdirs();
        new File("src/java/" + packages + "/model/service").mkdirs();
        new File("src/java/" + packages + "/model/validator").mkdirs();
    }
}