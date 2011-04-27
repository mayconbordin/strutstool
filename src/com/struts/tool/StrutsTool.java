package com.struts.tool;

import com.struts.tool.generators.Controller;
import com.struts.tool.generators.Model;
import com.struts.tool.generators.View;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.helpers.StringHelper;
import com.struts.tool.helpers.ZipHelper;
import com.struts.tool.output.MessageOutput;
import com.struts.tool.types.DataTypeCollection;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author maycon
 */
public class StrutsTool {
    private Map<String, String> params;
    private MessageOutput out;

    public StrutsTool(MessageOutput out) { this.out= out; }
    
    public void newProject(String name) throws StrutsToolException {
        out.put(Messages.creatingProjectFolder);
        File projectDir = new File(name);
        if (!projectDir.mkdir()) {
            throw new StrutsToolException(Messages.newProjectDirError);
        }

        out.put(Messages.unzipProjectFiles);
        File projectFiles = new File("resources/project.zip");
        ZipHelper zip = new ZipHelper();
       
        try {
            zip.unzip(projectFiles, projectDir);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.unzipProjectError, ex);
        }

        out.put(Messages.configuringNetBeans);
        changeNetbeansProjectName(name);
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
        Controller controller = new Controller(entityName, packages, params, out);
        controller.createModelController();

        // Create the model
        out.put(Messages.creatingModelFiles);
        Model model = new Model(entityName, packages, params, out);
        model.createModel();

        // Create the view
        out.put(Messages.creatingViewFiles);
        View view = new View(entityName, packages, params, out);
        view.makeView();
    }

    private void extractParams(String[] args, boolean addId) throws StrutsToolException {
        params = new LinkedHashMap<String, String>();
        if (addId) params.put("id", "Integer");

        String[] temp;

        try {
            for (int i = 2; i < args.length; i++) {
                temp = args[i].split(":");
                if (!DataTypeCollection.isValid(temp[1])) {
                    throw new StrutsToolException(Messages.invalidDataType.replace("{type}", temp[1]));
                }
                params.put(temp[0], temp[1]);
            }
        } catch(ArrayIndexOutOfBoundsException ex) {
            throw new StrutsToolException(Messages.extractParamsError, ex);
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
