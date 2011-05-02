package com.struts.tool.generators.controller;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.attributes.Attribute;
import com.struts.tool.generators.Controller;
import com.struts.tool.generators.Project;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.helpers.StringHelper;
import java.io.IOException;

/**
 *
 * @author maycon
 */
public class Properties {
    private Controller controller;
    
    public Properties(Controller controller) {
        this.controller = controller;
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
                          + StringHelper.firstToLowerCase(controller.getEntityName())+"\n"
                          + "\n"
                          + "status.notFound="+controller.getEntityName()+" does not exist!"+"\n"
                          + "\n"
                          + "# generator:properties";

            String properties = modelStr + status;

            String propertiesPath = Project.SRC_PATH
                    + controller.getProject().getPackages() + "/"
                    + Project.CONTROLLER_FOLDER
                    + "/" + controller.getEntityName() + "Controller.properties";

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
                    + attr.getNameFirstUpper() + "\n";

            invalids += "invalid.fieldvalue." + attr + "=Invalid data type,"
                      + " should be an "+attr.getType()+"\n";

            required += attr + ".required=" + attr.getNameFirstUpper()
                      + " field is required\n";
        }

        return labels + "\n" + invalids + "\n" + required + "\n";
    }
}
