package com.struts.tool.generators.model;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.generators.Model;
import com.struts.tool.generators.Project;
import java.io.File;

/**
 *
 * @author maycon
 */
public class Validator {
    private Model model;

    private String validatorRootPath;

    public Validator(Model model) {
        this.model = model;
    }

    public void create() throws StrutsToolException {
        loadPaths();
        makeFolder();
    }

    private void loadPaths() {
        validatorRootPath = Project.SRC_PATH
                          + model.getProject().getPackages()
                          + "/" + Project.MODEL_VALIDATOR_FOLDER;
    }

    private void makeFolder() throws StrutsToolException {
        // Create the folder
        File validator = new File(validatorRootPath);
        if (!validator.exists()) {
            if (!validator.mkdirs()) {
                throw new StrutsToolException(Messages.createModelValidatorFolderError);
            }
        }
    }
}
