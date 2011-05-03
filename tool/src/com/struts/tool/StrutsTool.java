package com.struts.tool;

import com.struts.tool.builder.components.Attribute;
import com.struts.tool.builder.components.Type;
import com.struts.tool.builder.components.TypeCollection;
import com.struts.tool.generators.Controller;
import com.struts.tool.generators.Model;
import com.struts.tool.generators.Project;
import com.struts.tool.generators.View;
import com.struts.tool.generators.model.Entity;
import com.struts.tool.helpers.IntegerHelper;
import com.struts.tool.helpers.ListHelper;
import com.struts.tool.output.MessageOutput;
import com.struts.tool.output.MessageOutputFactory;
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

    public static final String VERSION = "0.2";

    public StrutsTool() {
        this.out = MessageOutputFactory.getTerminalInstance();
    }

    // Project =================================================================
    public void newProject(String name, String packages) throws StrutsToolException {
        project = new Project(name, packages);
        project.create();
    }

    public void removeProject(String name) throws StrutsToolException {
        project = new Project(name);
        project.destroy();
    }

    // Scaffold ================================================================
    public void scaffold(String[] args) throws StrutsToolException {
        project = Project.getInstance();

        if (project != null) {
            List<Attribute> attributes = extractAttributes(args, true);

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
        } else {
            throw new StrutsToolException(Messages.goToRootOfApp);
        }
    }

    private List<Attribute> extractAttributes(String[] args, boolean addId) throws StrutsToolException {
        List<Attribute> attributes = new ArrayList();
        if (addId) {
            attributes.add(new Attribute("id", TypeCollection.get("int")));
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

                Type dt = TypeCollection.get(temp[1]);
                if (dt == null) {
                    if (Entity.exists(temp[1], project.getPackages())) {
                        dt = new Type(temp[1].toLowerCase(), temp[1], null, null, "entity");
                    } else {
                        throw new StrutsToolException(Messages.invalidDataType
                            .replace("{type}", temp[1]));
                    }
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

    // Controller ==============================================================
    public void newController(String[] args) throws StrutsToolException {
        project = Project.getInstance();
        Controller controller = new Controller(args[2], project);
        controller.createController(extractActions(args), true);
    }

    private List<String> extractActions(String[] args) {
        List<String> actions = new ArrayList();
        
        for (int i = 3; i < args.length; i++) {
            actions.add( args[i] );
        }

        ListHelper.removeDuplicate(actions);
        return actions;
    }
}
