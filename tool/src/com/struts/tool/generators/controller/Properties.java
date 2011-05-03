package com.struts.tool.generators.controller;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.builder.components.Attribute;
import com.struts.tool.generators.Controller;
import com.struts.tool.generators.Project;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.helpers.StringHelper;
import com.struts.tool.output.MessageOutput;
import com.struts.tool.output.MessageOutputFactory;
import java.io.IOException;

/**
 *
 * @author maycon
 */
public class Properties {
    private Controller controller;
    private MessageOutput out;
    
    public Properties(Controller controller) {
        this.controller = controller;
        this.out = MessageOutputFactory.getTerminalInstance();
    }

    public void create(boolean fromModel) throws StrutsToolException {
        try {
            String modelStr = "";
            if (fromModel) {
                modelStr = getModelPropertiesString();
            }

            String status = "status.success="+controller.getEntityName()+" successfully saved!\n"
                          + "status.error=An error occurred attempting "
                          + "to save the "
                          + StringHelper.lcfirst(controller.getEntityName())+"\n"
                          + "\n"
                          + "status.notFound="+controller.getEntityName()+" does not exist!"+"\n"
                          + "\n"
                          + "# generator:properties";

            String properties = modelStr + status;

            String propertiesPath = Project.SRC_PATH
                    + controller.getProject().getPackages() + "/"
                    + Project.CONTROLLER_FOLDER
                    + "/" + controller.getEntityName() + "Controller.properties";

            out.put("create  " + propertiesPath);

            FileHelper.toFile(propertiesPath, properties);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createControllerPropError, ex);
        }
    }

    private String getModelPropertiesString() {
        String labels = "label.save=Save\n";
        String invalids = "";
        String required = "";

        for (Attribute attr : controller.getAttributes()) {
            labels += "label." + attr + "="
                    + StringHelper.ucfirst(attr.getName()) + "\n";

            invalids += "invalid.fieldvalue." + attr + "=Invalid data type,"
                      + " should be an "+attr.getType()+"\n";

            required += attr + ".required=" + StringHelper.ucfirst(attr.getName())
                      + " field is required\n";
        }

        return labels + "\n" + invalids + "\n" + required + "\n";
    }
}
