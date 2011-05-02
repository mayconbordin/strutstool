package com.struts.tool;

import com.struts.tool.attributes.Attribute;
import com.struts.tool.generators.Controller;
import com.struts.tool.generators.Model;
import com.struts.tool.generators.Project;
import com.struts.tool.generators.View;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.helpers.IntegerHelper;
import com.struts.tool.helpers.StringHelper;
import com.struts.tool.helpers.ZipHelper;
import com.struts.tool.output.MessageOutput;
import com.struts.tool.output.MessageOutputFactory;
import com.struts.tool.types.DataType;
import com.struts.tool.types.DataTypeCollection;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mayconbordin
 * @version 0.1
 */
public class StrutsTool {
    private Project project;
    private MessageOutput out;

    public StrutsTool() {
        this.out = MessageOutputFactory.getTerminalInstance();
    }
    
    public void newProject(String name, String packages) throws StrutsToolException {
        project = new Project(name, packages);
        project.create();
    }

    public void removeProject(String name) throws StrutsToolException {
        project = new Project(name);
        project.destroy();
    }

    public void scaffold(String[] args) throws StrutsToolException {
        project = Project.getInstance();

        if (project != null) {
            out.put(Messages.scaffoldingInProgress.replace("{entity}", args[1]));
            List<Attribute> attributes = extractParams(args, true);

            String entityName = "";
            if (args.length > 1) {
                entityName = args[1];
            }

            // Create the controllers
            Controller controller = new Controller(entityName, project, attributes);
            controller.createModelController();

            // Create the model
            Model model = new Model(entityName, project, attributes);
            model.createModel(true, true, true);

            // Create the view
            View view = new View(entityName, project, attributes);
            view.createFromModel();

            out.put(Messages.scaffoldingDone.replace("{entity}", args[1]));
        } else {
            throw new StrutsToolException(Messages.goToRootOfApp);
        }
    }

    private List<Attribute> extractParams(String[] args, boolean addId) throws StrutsToolException {
        List<Attribute> attributes = new ArrayList();
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

        return attributes;
    }
}
