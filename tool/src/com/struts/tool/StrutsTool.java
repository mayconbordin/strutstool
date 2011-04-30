package com.struts.tool;

import com.struts.tool.attributes.Attribute;
import com.struts.tool.generators.Controller;
import com.struts.tool.generators.Model;
import com.struts.tool.generators.View;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.helpers.IntegerHelper;
import com.struts.tool.helpers.StringHelper;
import com.struts.tool.helpers.ZipHelper;
import com.struts.tool.output.MessageOutput;
import com.struts.tool.types.DataType;
import com.struts.tool.types.DataTypeCollection;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mayconbordin
 * @version 0.1
 */
public class StrutsTool {
    private List<Attribute> attributes;
    private MessageOutput out;

    public StrutsTool(MessageOutput out) { this.out= out; }
    
    public void newProject(String name) throws StrutsToolException {
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
        changeNetbeansProjectName(name);

        out.put(Messages.projectCreated.replace("{name}", name));
    }

    public void removeProject(String name) throws StrutsToolException {
        out.put(Messages.removingProject);
        File projectDir = new File(name);
        if (projectDir.exists()) {
            if (!FileHelper.deleteDir(projectDir)) {
                throw new StrutsToolException(Messages.removeProjectError);
            }
        }
        out.put(Messages.projectRemoved);
    }

    public boolean buildXmlExists() {
        File buildXml = new File("build.xml");

        if (buildXml.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public void scaffold(String[] args) throws StrutsToolException {
        out.put(Messages.scaffoldingInProgress.replace("{entity}", args[1]));
        out.put(Messages.extractingParams);
        extractParams(args, true);

        out.put(Messages.configPackAndEntityNames);
        String packages = "";
        String entityName = "";
        if (args.length > 1) {
            //Get the package name
            int lastDot = args[1].lastIndexOf(".");
            packages = args[1].substring(0, lastDot).replace(".", "/");

            //Get the entity name
            String[] temp = args[1].split("\\.");
            entityName = StringHelper.firstToUpperCase(temp[temp.length - 1]);
        }

        // Create the controllers
        out.put(Messages.creatingControllerFiles);
        Controller controller = new Controller(entityName, packages, attributes, out);
        controller.createModelController();

        // Create the model
        out.put(Messages.creatingModelFiles);
        Model model = new Model(entityName, packages, attributes, out);
        model.createModel();

        // Create the view
        out.put(Messages.creatingViewFiles);
        View view = new View(entityName, packages, attributes, out);
        view.makeView();

        out.put(Messages.scaffoldingDone.replace("{entity}", args[1]));
    }

    private void extractParams(String[] args, boolean addId) throws StrutsToolException {
        attributes = new ArrayList();
        if (addId) {
            attributes.add(new Attribute("id", DataTypeCollection.get("int")));
        }

        String[] temp;

        for (int i = 2; i < args.length; i++) {
            temp = args[i].split(":");
            Attribute attr = new Attribute();

            // Wrong attribute declaration
            if (temp.length < 2) {
                throw new StrutsToolException(Messages.wrongAttrFormat
                        .replace("{attr}", temp[0]));
            }

            if (temp.length > 1) {
                attr.setName(temp[0]);

                DataType dt = DataTypeCollection.get(temp[1]);
                if (dt == null) {
                    throw new StrutsToolException(Messages.invalidDataType
                            .replace("{type}", temp[1]));
                }
                attr.setType(dt);
            }
                
            if (temp.length > 2) {
                if (IntegerHelper.isInteger(temp[2])) {
                    attr.setSize(Integer.parseInt(temp[2]));
                } else {
                    attr.setRelatedWith(temp[2]);
                }
            }

            if (temp.length > 3) {
                attr.setRelatedWith(temp[2]);
                attr.setSize(Integer.parseInt(temp[3]));
            }

            attributes.add(attr);
        }
    }

    private void changeNetbeansProjectName(String name) throws StrutsToolException {
        try {
            String buildXml = name + "/build.xml";
            String projectXml = name + "/nbproject/project.xml";
            String contextXml = name + "/web/META-INF/context.xml";

            String buildContent = FileHelper.toString(buildXml);
            String projectContent = FileHelper.toString(projectXml);
            String contextContent = FileHelper.toString(contextXml);

            // Replace the tags
            buildContent = buildContent.replaceAll("<<StandardApplication>>", name);
            projectContent = projectContent.replaceAll("<<StandardApplication>>", name);
            contextContent = contextContent.replaceAll("<<StandardApplication>>", name);
            
            FileHelper.toFile(buildXml, buildContent);
            FileHelper.toFile(projectXml, projectContent);
            FileHelper.toFile(contextXml, contextContent);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.renameNetbeansProjectError, ex);
        }
    }
}
