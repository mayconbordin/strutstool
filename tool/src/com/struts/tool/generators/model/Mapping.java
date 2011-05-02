package com.struts.tool.generators.model;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.attributes.Attribute;
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
public class Mapping {
    private Model model;

    private String mappingRootPath;
    private String templatePath;

    public Mapping(Model model) {
        this.model = model;
    }
    
    public void create() throws StrutsToolException {
        loadPaths();
        makeFolder();
        makeMapping();
    }

    private void loadPaths() {
        mappingRootPath = Project.SRC_PATH
                        + model.getProject().getPackages()
                        + "/" + Project.MODEL_MAPPING_FOLDER;

        templatePath = DirectoryHelper.getInstallationDirectory()
                     + Project.TEMPLATES_PATH
                     + Project.MODEL_FOLDER;
    }

    private void makeFolder() throws StrutsToolException {
        // Create the folder
        File mapping = new File(mappingRootPath);
        
        if (!mapping.exists()) {
            if (!mapping.mkdirs()) {
                throw new StrutsToolException(Messages.createModelMappingFolderError);
            }
        }
    }

    private void makeMapping() throws StrutsToolException {
        try {
            String refMappingPath = templatePath + "/HibernateMapping";

            String mappingContent = FileHelper.toString(refMappingPath);

            String properties = "\t<!-- generator:properties -->\n";
            for (Attribute attr : model.getAttributes()) {
                if (!attr.getName().equals("id")) {
                    properties += "\t\t<property name=\""+attr+"\" column=\""
                                + ""+attr+"\" ";

                    if (attr.getType().getClassification().equals("character")) {
                        properties += "length=\""+attr.getSize()+"\" ";
                    }

                    properties += "not-null=\"true\" type=\""
                                + attr.getType().getHibernate()+"\" />\n";
                }
            }

            // Replace the tags
            mappingContent = mappingContent.replace("<<packages>>", model.getProject().getPackages().replace("/", "."));
            mappingContent = mappingContent.replace("<<entityName>>", model.getEntityName());
            mappingContent = mappingContent.replace("<<entityNameLower>>", StringHelper.firstToLowerCase(model.getEntityName()));
            mappingContent = mappingContent.replace("<!-- generator:properties -->", properties);

            String mappingPath = mappingRootPath + "/"
                               + model.getEntityName() + ".hbm.xml";

            FileHelper.toFile(mappingPath, mappingContent);

            // Add the mapping to hibernate config file
            addMappingToConfig();
        } catch(IOException ex) {
            throw new StrutsToolException(Messages.createModelMappingError, ex);
        }
    }

    private void addMappingToConfig() throws StrutsToolException {
        try {
            String configContent = FileHelper.toString(Project.HIBERNATE_CONFIG_XML);

            String resource = model.getProject().getPackages()
                            + "/" + Project.MODEL_MAPPING_FOLDER
                            + "/" + model.getEntityName() + ".hbm.xml";
            
            String mapping = "<!-- generator:mappings -->\n"
                           + "    <mapping resource=\""+resource+"\" />\n";

            // Replace the tags
            configContent = configContent.replace("<!-- generator:mappings -->", mapping);

            FileHelper.toFile(Project.HIBERNATE_CONFIG_XML, configContent);
        } catch(IOException ex) {
            throw new StrutsToolException(Messages.addingMappingToConfigError, ex);
        }
    }
}
