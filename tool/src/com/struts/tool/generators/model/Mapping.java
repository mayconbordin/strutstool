package com.struts.tool.generators.model;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.builder.XmlBuilder;
import com.struts.tool.builder.components.Attribute;
import com.struts.tool.builder.components.Type;
import com.struts.tool.builder.components.XmlAttribute;
import com.struts.tool.builder.components.XmlTag;
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
    private XmlTag mapping;
    
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
        mapping = new XmlTag("class", null);
        mapping.addAttribute("name", entityPackage + model.getEntityName());
        mapping.addAttribute("table", StringHelper.lcfirst(model.getEntityName()));
        
        for (Attribute attr : model.getAttributes()) {
            XmlTag property = null;
            
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

    private XmlTag mapOneToMany(Attribute attr) {
        XmlTag collection = new XmlTag(attr.getType().getName());
        collection.addAttribute("cascade", "all-delete-orphan");
        collection.addAttribute("inverse", "true");
        collection.addAttribute("lazy", "true");
        collection.addAttribute("name", StringHelper.lcfirst(attr.getName()));
        collection.addAttribute("table", StringHelper.lcfirst(model.getEntityName()));


        XmlTag key = new XmlTag("key");
        key.addAttribute("column", "id");

        String className = entityPackage + attr.getRelatedWith();
        XmlTag oneToMany = new XmlTag("one-to-many");
        oneToMany.addAttribute("class", className);

        collection.addChilrens(key, oneToMany);

        return collection;
    }

    private XmlTag mapManyToOne(Attribute attr) {
        String columnName = StringHelper.lcfirst(attr.getName());
        String className = entityPackage + attr.getType().getJavaName();
        
        XmlTag manyToOne = new XmlTag("many-to-one");
        manyToOne.addAttribute("class", className);
        manyToOne.addAttribute("column", columnName);
        manyToOne.addAttribute("name", columnName);

        return manyToOne;
    }

    private XmlTag mapIdentity(Attribute attr) {
        // Id Tag
        XmlTag id = new XmlTag("id");
        id.addAttribute("name", attr.getName());
        id.addAttribute("column", attr.getName());

        // Generator tag
        XmlTag generator = new XmlTag("generator", id);
        generator.addAttribute("class", "sequence");

        // Param tag
        String paramContent = model.getEntityName().toLowerCase()
                            + "_"
                            + attr.getName().toLowerCase() + "_seq";
        
        XmlTag param = new XmlTag("param", generator);
        param.setContent(paramContent);
        param.addAttribute("name", "sequence");

        // Add to respective parents
        generator.addChilren(param);
        id.addChilren(generator);

        return id;
    }

    private XmlTag mapProperty(Attribute attr) {
        XmlTag property = new XmlTag("property");
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

            XmlBuilder builder = new XmlBuilder(mapping);
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
