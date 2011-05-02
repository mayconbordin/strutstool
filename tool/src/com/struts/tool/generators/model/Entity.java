package com.struts.tool.generators.model;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.attributes.Attribute;
import com.struts.tool.generators.Model;
import com.struts.tool.generators.Project;
import com.struts.tool.helpers.DirectoryHelper;
import com.struts.tool.helpers.FileHelper;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author maycon
 */
public class Entity {
    private Model model;

    private String entityRootPath;
    private String templatePath;

    public Entity(Model model) {
        this.model = model;
    }

    public void create(boolean validate, boolean search) throws StrutsToolException {
        loadPaths();
        makeFolder();
        makeEntity(validate, search);
    }

    private void loadPaths() {
        entityRootPath = Project.SRC_PATH
                           + model.getProject().getPackages()
                           + "/" + Project.MODEL_ENTITY_FOLDER;

        templatePath = DirectoryHelper.getInstallationDirectory()
                               + Project.TEMPLATES_PATH
                               + Project.MODEL_FOLDER;
    }

    private void makeFolder() throws StrutsToolException {
        // Create the folder
        File repository = new File(entityRootPath);

        if (!repository.exists()) {
            if (!repository.mkdirs()) {
                throw new StrutsToolException(Messages.createModelRepositoryFolderError);
            }
        }
    }

    private void makeEntity(boolean validate, boolean search) throws StrutsToolException {
        try {
            String refEntityPath = templatePath + "/Entity";
            String entityContent = FileHelper.toString(refEntityPath);

            // Create the attributes, getters and setters
            String attribs = "// generator:attributes\n";
            String accessors = "// generator:accessors\n";
            String imports = "// generator:imports\n";

            // Validation imports
            if (validate) {
                imports += "import com.framework.util.validator.constraints.NotNull;\n"
                        + "import com.framework.util.validator.constraints.XSSFilter;\n";
            }

            // Hibernate search imports
            if (search) {
                imports += "import org.hibernate.search.annotations.DateBridge;\n"
                         + "import org.hibernate.search.annotations.DocumentId;\n"
                         + "import org.hibernate.search.annotations.Indexed;\n"
                         + "import org.hibernate.search.annotations.Field;\n"
                         + "import org.hibernate.search.annotations.Index;\n"
                         + "import org.hibernate.search.annotations.Resolution;\n"
                         + "import org.hibernate.search.annotations.Store;\n";
            }
            
            for (Attribute attr : model.getAttributes()) {
                if (attr.getType().getJavaImport() != null) {
                    imports += "import " + attr.getType().getJavaImport() + ";\n";
                }

                if (search) {
                    if (attr.getName().equals("id")) {
                        attribs += "\t@DocumentId\n";
                    } else if (attr.getType().getClassification().equals("date")) {
                        attribs += "\t@Field(index=Index.UN_TOKENIZED, store=Store.YES)\n"
                                 + "\t@DateBridge(resolution=Resolution.SECOND)\n";
                    } else {
                        attribs += "\t@Field(index=Index.TOKENIZED,store=Store.YES)\n";
                    }
                }
                attribs += "\tprivate "+attr.getType()+" "+attr+";\n";

                if (validate && !attr.getName().equals("id")) {
                    accessors += "\t@NotNull\n"
                               + "\t@XSSFilter\n";
                }

                accessors += "\tpublic "+attr.getType()+" get"+attr.getNameFirstUpper()+"() {\n"
                           + "\t\treturn "+attr.getNameLower()+";\n"
                           + "\t}\n"
                           + "\n"
                           + "\tpublic void set"+attr.getNameFirstUpper()+"("+attr.getType()+" "
                           + attr.getNameLower()+") {\n"
                           + "\t\tthis."+attr.getNameLower()+" = "+attr.getNameLower()+";\n"
                           + "\t}\n\n";
            }

            // Replace the tags
            entityContent = entityContent.replace("<<packages>>",
                    model.getProject().getPackages().replace("/", "."));
            entityContent = entityContent.replace("<<entityName>>", model.getEntityName());
            entityContent = entityContent.replace("// generator:attributes", attribs);
            entityContent = entityContent.replace("// generator:accessors", accessors);
            entityContent = entityContent.replace("// generator:imports", imports);
            if (validate) {
                entityContent = entityContent.replace("public class", "@Indexed\npublic class");
            }

            String entityPath = entityRootPath + "/"
                              + model.getEntityName() + ".java";

            FileHelper.toFile(entityPath, entityContent);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createModelEntityError, ex);
        }
    }
}
