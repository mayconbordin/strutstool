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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author maycon
 */
public class SearchMap {
    private Model model;

    private String searchRootPath;
    private String templatePath;

    private List<String> fieldTypes;

    public SearchMap(Model model) {
        this.model = model;
    }

    public void create() throws StrutsToolException {
        loadPaths();
        loadFieldTypes();
        makeFolder();
        makeSearchMap();
    }

    private void loadPaths() {
        searchRootPath = Project.SRC_PATH
                           + model.getProject().getPackages()
                           + "/" + Project.MODEL_SEARCH_FOLDER;

        templatePath = DirectoryHelper.getInstallationDirectory()
                               + Project.TEMPLATES_PATH
                               + Project.MODEL_FOLDER;
    }

    private void loadFieldTypes() {
        fieldTypes = new ArrayList();
        fieldTypes.add("string");
        fieldTypes.add("int");
        fieldTypes.add("long");
        fieldTypes.add("short");
        fieldTypes.add("float");
        fieldTypes.add("double");
        fieldTypes.add("bigdecimal");
        fieldTypes.add("boolean");
        fieldTypes.add("date");
    }

    private void makeFolder() throws StrutsToolException {
        // Create the folder
        File repository = new File(searchRootPath);

        if (!repository.exists()) {
            if (!repository.mkdirs()) {
                throw new StrutsToolException(Messages.createModelRepositoryFolderError);
            }
        }
    }

    private void makeSearchMap() throws StrutsToolException {
        try {
            String refSearchMapPath = templatePath + "/SearchMap";
            String searchMapContent = FileHelper.toString(refSearchMapPath);

            // Create the attributes, getters and setters
            String fields = "// generator:fields\n";
            
            for (Attribute attr : model.getAttributes()) {
                if (fieldTypes.contains(attr.getType().getRaw())) {
                    fields += "\t\tfields.put(\""+attr+"\", new EntityField(\""
                            + attr.getNameFirstUpper()+"\", \""+attr+"\","
                            + " EntityFieldType."
                            + attr.getType().getRaw().toUpperCase()+"));\n";
                }
            }

            // Replace the tags
            searchMapContent = searchMapContent.replace("<<packages>>",
                    model.getProject().getPackages().replace("/", "."));
            searchMapContent = searchMapContent.replace("<<entityName>>", model.getEntityName());
            searchMapContent = searchMapContent.replace("// generator:fields", fields);

            String searchMapPath = searchRootPath + "/"
                              + model.getEntityName() + "SearchMap.java";

            FileHelper.toFile(searchMapPath, searchMapContent);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createModelEntityError, ex);
        }
    }
}
