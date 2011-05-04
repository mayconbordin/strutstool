package com.struts.tool.generators.model;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.builder.MarkUpBuilder;
import com.struts.tool.builder.components.Attribute;
import com.struts.tool.builder.components.Type;
import com.struts.tool.builder.components.MarkUpTag;
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
public class Mapping {
    private Model model;
    private MarkUpTag mapping;
    
    private String mappingRootPath;
    private String templatePath;
    private String entityPackage;

    private MessageOutput out;

    public Mapping(Model model) {
        this.model = model;
        this.out = MessageOutputFactory.getTerminalInstance();
    }
    
    public void create() throws StrutsToolException {
        loadPaths();
        makeFolder();
        makeMapping();
        buildAndSave();
    }

    private void loadPaths() {
        mappingRootPath = Project.SRC_PATH
                        + model.getProject().getPackages()
                        + "/" + Project.MODEL_MAPPING_FOLDER;

        templatePath = DirectoryHelper.getInstallationDirectory()
                     + Project.TEMPLATES_PATH
                     + Project.MODEL_FOLDER;

        entityPackage = model.getProject().getPackages().replace("/", ".")
                      + ".model.entity.";
    }

    private void makeFolder() throws StrutsToolException {
        // Create the folder
        File mappingFolder = new File(mappingRootPath);
        
        if (!mappingFolder.exists()) {
            out.put("create  " + mappingRootPath);
            if (!mappingFolder.mkdirs()) {
                throw new StrutsToolException(Messages.createModelMappingFolderError);
            }
        } else {
            out.put("exists  " + mappingRootPath);
        }
    }

    private void makeMapping() {
        mapping = new MarkUpTag("class", null);
        mapping.addAttribute("name", entityPackage + model.getEntityName());
        mapping.addAttribute("table", StringHelper.lcfirst(model.getEntityName()));
        
        for (Attribute attr : model.getAttributes()) {
            MarkUpTag property = null;
            
            // Identity mapping
            if (attr.getName().equals("id")) {
                property = mapIdentity(attr);
            }

            // Many-to-One relationship
            else if (attr.getType().getClassification().equals(Type.ENTITY)) {
                property = mapManyToOne(attr);
            }

            // One-to-Many relationship
            else if (attr.getType().getClassification().equals(Type.COLLECTION)
                    && attr.getRelatedWith() != null) {
                property = mapOneToMany(attr);
            }

            // Simple property
            else {
                property = mapProperty(attr);
            }

            // Add to the parent class
            mapping.addChilren(property);
        }
    }

    private MarkUpTag mapOneToMany(Attribute attr) {
        MarkUpTag collection = new MarkUpTag(attr.getType().getName());
        collection.addAttribute("cascade", "all-delete-orphan");
        collection.addAttribute("inverse", "true");
        collection.addAttribute("lazy", "true");
        collection.addAttribute("name", StringHelper.lcfirst(attr.getName()));
        collection.addAttribute("table", StringHelper.lcfirst(attr.getRelatedWith()));


        MarkUpTag key = new MarkUpTag("key");
        key.addAttribute("column", StringHelper.lcfirst(model.getEntityName()));

        String className = entityPackage + attr.getRelatedWith();
        MarkUpTag oneToMany = new MarkUpTag("one-to-many");
        oneToMany.addAttribute("class", className);

        collection.addChilrens(key, oneToMany);

        return collection;
    }

    private MarkUpTag mapManyToOne(Attribute attr) {
        String columnName = StringHelper.lcfirst(attr.getName());
        String className = entityPackage + attr.getType().getJavaName();
        
        MarkUpTag manyToOne = new MarkUpTag("many-to-one");
        manyToOne.addAttribute("class", className);
        manyToOne.addAttribute("column", columnName);
        manyToOne.addAttribute("name", columnName);

        return manyToOne;
    }

    private MarkUpTag mapIdentity(Attribute attr) {
        // Id Tag
        MarkUpTag id = new MarkUpTag("id");
        id.addAttribute("name", attr.getName());
        id.addAttribute("column", attr.getName());

        // Generator tag
        MarkUpTag generator = new MarkUpTag("generator", id);
        generator.addAttribute("class", "sequence");

        // Param tag
        String paramContent = model.getEntityName().toLowerCase()
                            + "_"
                            + attr.getName().toLowerCase() + "_seq";
        
        MarkUpTag param = new MarkUpTag("param", generator);
        param.setContent(paramContent);
        param.addAttribute("name", "sequence");

        // Add to respective parents
        generator.addChilren(param);
        id.addChilren(generator);

        return id;
    }

    private MarkUpTag mapProperty(Attribute attr) {
        MarkUpTag property = new MarkUpTag("property");
        property.addAttribute("name", attr.getName());
        property.addAttribute("column", attr.getName());
        property.addAttribute("not-null", "true");
        property.addAttribute("type", attr.getType().getHibernateName());

        if (attr.getType().getClassification().equals("character")) {
            String size = (attr.getSize() == null) ? "0" : attr.getSize().toString();
            property.addAttribute("length", size);
        }

        return property;
    }

    private void buildAndSave() throws StrutsToolException {
        try {
            String refMappingPath = templatePath + "/HibernateMapping";
            String mappingContent = FileHelper.toString(refMappingPath);

            String mapTag = "<!-- generator:mapping -->\n";

            MarkUpBuilder builder = new MarkUpBuilder(mapping);
            String content = mapTag + builder.build();

            // Replace the tags
            mappingContent = mappingContent.replace("<!-- generator:mapping -->", content);

            String mappingPath = mappingRootPath + "/"
                               + model.getEntityName() + ".hbm.xml";

            out.put("create  " + mappingPath);

            FileHelper.toFile(mappingPath, mappingContent);

            // Add the mapping to hibernate config file
            addMappingToConfig();
        } catch(IOException ex) {
            throw new StrutsToolException(Messages.createModelMappingError, ex);
        }
    }

    private void addMappingToConfig() throws StrutsToolException {
        try {
            out.put("modify  " + Project.HIBERNATE_CONFIG_XML);
            String configContent = FileHelper.toString(Project.HIBERNATE_CONFIG_XML);

            String resource = model.getProject().getPackages()
                            + "/" + Project.MODEL_MAPPING_FOLDER
                            + "/" + model.getEntityName() + ".hbm.xml";
            
            String mappingContent = "<!-- generator:mappings -->\n"
                           + "    <mapping resource=\""+resource+"\" />\n";

            // Replace the tags
            configContent = configContent.replace("<!-- generator:mappings -->", mappingContent);

            FileHelper.toFile(Project.HIBERNATE_CONFIG_XML, configContent);
        } catch(IOException ex) {
            throw new StrutsToolException(Messages.addingMappingToConfigError, ex);
        }
    }
}
