package com.struts.tool.generators.model;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.generators.Model;
import com.struts.tool.generators.Project;
import com.struts.tool.output.MessageOutput;
import com.struts.tool.output.MessageOutputFactory;
import java.io.File;

/**
 *
 * @author maycon
 */
public class Validator {
    private Model model;

    private String validatorRootPath;

    private MessageOutput out;

    public Validator(Model model) {
        this.model = model;
        this.out = MessageOutputFactory.getTerminalInstance();
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
            out.put("create  " + validatorRootPath);
            if (!validator.mkdirs()) {
                throw new StrutsToolException(Messages.createModelValidatorFolderError);
            }
        } else {
            out.put("exists  " + validatorRootPath);
        }
    }
}
