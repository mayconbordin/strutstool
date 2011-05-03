package com.struts.tool.generators.controller;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.builder.components.Attribute;
import com.struts.tool.generators.Controller;
import com.struts.tool.generators.Project;
import com.struts.tool.helpers.DirectoryHelper;
import com.struts.tool.helpers.FileHelper;
import java.io.IOException;

/**
 *
 * @author maycon
 */
public class Validator {
    private Controller controller;

    public Validator(Controller controller) {
        this.controller = controller;
    }

    private void makeValidator() throws StrutsToolException {
        try {
            String refValidatorPath = DirectoryHelper.getInstallationDirectory()
                    + "/resources/files/StrutsValidator";

            String validator = FileHelper.toString(refValidatorPath);

            String validators = "";
            for (Attribute attr : controller.getAttributes()) {
                validators += "    <field name=\""+attr+"\">\n\n"
                           + "    </field>\n";
            }

            // Fill the validators
            validator = validator.replace("<<validators>>", validators);

            String validatorPath = Project.SRC_PATH
                    + controller.getProject().getPackages() + "/"
                    + Project.CONTROLLER_FOLDER
                    + "/" + controller.getEntityName() + "Controller-validation.xml";

            FileHelper.toFile(validatorPath, validator);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createControllerValidatorError, ex);
        }
    }
}
