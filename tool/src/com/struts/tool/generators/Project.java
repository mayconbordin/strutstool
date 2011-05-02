package com.struts.tool.generators;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.helpers.ZipHelper;
import com.struts.tool.output.MessageOutput;
import com.struts.tool.output.MessageOutputFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author maycon
 */
public class Project {
    private String name;
    private String packages;
    private Properties properties;

    private MessageOutput out;

    public static final String SRC_PATH = "src/java/";
    public static final String WEB_PATH = "web/WEB-INF/";
    public static final String TEMPLATES_PATH = "/resources/templates/";

    public static final String CONTROLLER_FOLDER = "controller";
    public static final String CONTROLLER_CONFIG_FOLDER = "controller/config";

    public static final String INTERCEPTOR_FOLDER = "interceptor";

    public static final String MODEL_FOLDER = "model";
    public static final String MODEL_VALIDATOR_FOLDER = "model/validator";
    public static final String MODEL_SERVICE_FOLDER = "model/service";
    public static final String MODEL_REPOSITORY_FOLDER = "model/repository";
    public static final String MODEL_MAPPING_FOLDER = "model/mapping";
    public static final String MODEL_ENTITY_FOLDER = "model/entity";
    public static final String MODEL_SEARCH_FOLDER = "model/search";

    public static final String VIEW_FOLDER = "view";

    public static final String HIBERNATE_CONFIG_XML = "src/java/hibernate.cfg.xml";
    public static final String STRUTS_CONFIG_XML = "src/java/struts.xml";

    public Project(String name, String packages) {
        this.name = name;
        this.packages = packages.replace(".", "/");
        this.out = MessageOutputFactory.getTerminalInstance();
    }

    public Project(String name) {
        this.name = name;
        this.out = MessageOutputFactory.getTerminalInstance();
    }

    public void create() throws StrutsToolException {
        out.put(Messages.buildingProject);
        out.put(Messages.creatingProjectFolder);

        File projectDir = new File(name);
        if (!projectDir.mkdir()) {
            throw new StrutsToolException(Messages.newProjectDirError);
        }

        ZipHelper zip = new ZipHelper();

        out.put(Messages.unzipProjectFiles);

        File projectFiles = new File("resources/project.zip");
        try {
            zip.unzip(projectFiles, projectDir);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.unzipProjectError, ex);
        }

        out.put(Messages.unzipLibFiles);

        File libFiles = new File("resources/lib.zip");
        try {
            zip.unzip(libFiles, projectDir);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.unzipLibError, ex);
        }

        out.put(Messages.configuringNetBeans);
        configure();
        createPropertiesFile();
        createPackageFolders();

        out.put(Messages.projectCreated.replace("{name}", name));
    }

    public void destroy() throws StrutsToolException {
        out.put(Messages.removingProject);
        File projectDir = new File(name);
        if (projectDir.exists()) {
            if (!FileHelper.deleteDir(projectDir)) {
                throw new StrutsToolException(Messages.removeProjectError);
            }
        }
        out.put(Messages.projectRemoved);
    }

    private void createPackageFolders() throws StrutsToolException {
        String path = name + "/" + SRC_PATH + packages.replace(".", "/");
        File packageDirs = new File(path);
        if (!packageDirs.exists()) {
            if (!packageDirs.mkdirs()) {
                throw new StrutsToolException(Messages.newProjectDirError);
            }
        }

    }

    private void configure() throws StrutsToolException {
        try {
            String buildXml = name + "/build.xml";
            String buildImplXml = name + "/nbproject/build-impl.xml";
            String projectXml = name + "/nbproject/project.xml";
            String contextXml = name + "/web/META-INF/context.xml";

            String buildContent = FileHelper.toString(buildXml);
            String buildImplContent = FileHelper.toString(buildImplXml);
            String projectContent = FileHelper.toString(projectXml);
            String contextContent = FileHelper.toString(contextXml);

            // Replace the tags
            buildContent = buildContent.replace("<<StandardApplication>>", name);
            buildImplContent = buildImplContent.replace("<<StandardApplication>>", name);
            projectContent = projectContent.replace("<<StandardApplication>>", name);
            contextContent = contextContent.replace("<<StandardApplication>>", name);

            FileHelper.toFile(buildXml, buildContent);
            FileHelper.toFile(buildImplXml, buildImplContent);
            FileHelper.toFile(projectXml, projectContent);
            FileHelper.toFile(contextXml, contextContent);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.renameNetbeansProjectError, ex);
        }
    }

    private void createPropertiesFile() {
        try {
            File file = new File(name + "/project.properties");

            // Create file if it does not exist
            boolean success = file.createNewFile();
            if (success) {
                // File did not exist and was created
            } else {
                // File already exists
            }

            properties = new Properties();
            properties.load(new FileInputStream(name + "/project.properties"));
            properties.setProperty("project.name", name);
            properties.setProperty("project.packages", packages);
            properties.store(new FileOutputStream(name + "/project.properties"), null);
        } catch (IOException e) {
            
        }
    }

    private void loadProperties() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("project.properties"));
        } catch (IOException ex) {}
            
        name = properties.getProperty("project.name");
        packages = properties.getProperty("project.packages");
    }

    private void saveProperties() {
        try {
            properties.store(new FileOutputStream("project.properties"), null);
        } catch (IOException ex) {}
    }

    public static Project getInstance() {
        boolean loaded = false;
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("project.properties"));
            loaded =  true;
        } catch (IOException ex) {

        }

        Project project = null;
        if (loaded) {
            project = new Project(
                    properties.getProperty("project.name"),
                    properties.getProperty("project.packages"));
        }

        return project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackages() {
        return packages;
    }

    
    public void setPackages(String packages) {
        this.packages = packages;
    }

    public Properties getProperties() {
        if (properties == null) {
            loadProperties();
        }
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}