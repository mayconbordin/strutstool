package com.struts.tool;

/**
 *
 * @author maycon
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        StrutsTool tool = new StrutsTool();

        if (args.length == 0) {
            return;
        }

        if (args.length == 1) {
            return;
        }

        if (args.length == 3) {
            
            // NEW -------------------------------------------------------------
            if (args[0].equals("new")) {

                // NEW PROJECT -------------------------------------------------
                if (args[1].equals("project")) {
                    System.out.println("Building project basic structure...");
                    if (tool.newProject(args[2])) {
                        System.out.println("Project '" + args[2] + "' created.");
                    } else {
                        System.out.println("Ocorreu um erro ao criar o projeto.");
                    }
                } // END: NEW PROJECT ------------------------------------------
 
            } // END: NEW ------------------------------------------------------
            
        }
        
        // SCAFFOLD ------------------------------------------------------------
        // scaffold com.package.EntityName param1:type param2:type ...
        if (args[0].equals("scaffold")) {
            if (tool.buildXmlExists()) {
                tool.scaffold(args);
                System.out.println("Scaffolding done.");
            } else {
                System.out.println("You must go to the root of the application.");
            }
        } // END: SCAFFOLD -----------------------------------------------------

    }

}
