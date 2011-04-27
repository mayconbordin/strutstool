package com.struts.tool;

/**
 *
 * @author maycon
 */
public class Messages {    
    public static String usage = "Usage:\n"
            + "\n"
            + "Project:\n"
            + "  strutstool new project [project name]\n"
            + "\n"
            + "Scaffolding:\n"
            + "  strutstool scaffold [fullClassName] [parameter:dataType ...]\n"
            + "  Ex.: strutstool scaffold com.application.User name:String passw:String\n"
            + "\n"
            + "";

    // MESSAGES
    public static String buildingProject = "Building project...";
    public static String projectCreated = "Project {name} created!";
    public static String creatingProjectFolder = "Creating project folder...";
    public static String unzipProjectFiles = "Unzipping project basic files...";
    public static String configuringNetBeans = "Configuring NetBeans information...";

    public static String scaffoldingInProgress= "Scaffolding of entity {entity} in progress...";
    public static String scaffoldingDone = "Scaffolding of entity {entity} is done.";
    public static String extractingParams = "Retrieving entity attributes...";
    public static String configPackAndEntityNames = "Retrieving package and entity names...";

    public static String creatingControllerFiles = "Generating controller files...";

    public static String creatingModelFiles = "Generating model files...";

    public static String creatingViewFiles = "Generating view files...";


    // WARNING
    public static String goToRootOfApp = "You must be on the root folder of the application to run it!";

    // ERRORS
    public static String newProjectDirError = "Error while creating project directory.";
    public static String unzipProjectError = "Error while creating the project basic structure.";
    public static String renameNetbeansProjectError = "An error ocurred while configuring NetBeans project.";
    public static String extractParamsError = "Error while extracting the parameters from command-line.";
    public static String invalidDataType = "Invalid data type: {type}.";

    public static String createControllerFolderError = "Unable to create the controller folders.";
    public static String createStrutsConfigError = "Unable to create struts config file for controller.";
    public static String addingStrutsConfError = "Unable to add controller configuration to struts.xml.";
    public static String createControllerPropError = "Unable to create the controller properties file.";
    public static String createControllerValidatorError = "Unable to create the controller validation file.";

    public static String createModelControllerError = "Unable to create model driven controller.";
    public static String createModelValidatorFolderError = "Unable to create model validator folder.";
    public static String createModelServiceFolderError = "Unable to create model service folder.";
    public static String createModelServiceError = "Unable to create model service classes.";
    public static String createModelRepositoryFolderError = "Unable to create model repository folder.";
    public static String createModelRepositoryError = "Unable to create model repository classes.";
    public static String createModelMappingFolderError = "Unable to create model mapping folder.";
    public static String createModelEntityFolderError = "Unable to create model entity folder.";
    public static String createModelMappingError = "Unable to create model mapping files.";
    public static String addingMappingToConfigError = "Unable to add model mapping to hibernate.cfg.xml.";
    public static String createModelEntityError = "Unable to create the model entity class.";
    
    public static String createViewFolderError = "Unable to create the view folder.";
    public static String createViewIndexPageError = "Unable to create the index view page.";
    public static String createViewEditPageError = "Unable to create the edit view page.";
    public static String createViewAddPageError = "Unable to create the add view page.";
    public static String createViewFormPageError = "Unable to create the form view page.";
    public static String addingViewsToTilesConfError = "Unable to add view pages to tiles.xml.";
}
