package com.struts.tool;

import com.struts.tool.output.MessageOutput;
import com.struts.tool.output.TerminalOutput;

/**
 *
 * @author maycon
 */
public class Main {
    private static MessageOutput out = new TerminalOutput();
   
    public static void main(String[] args) {
        try {
            StrutsTool tool = new StrutsTool(out);

            if (args.length == 0) {
                out.put(Messages.usage);
                return;
            }

            if (args.length == 1) {
                return;
            }

            if (args.length == 3 && args[0].equals("new")) {
                if (args[1].equals("project")) {
                    out.put(Messages.buildingProject);
                    tool.newProject(args[2]);
                    out.put(Messages.projectCreated.replace("{name}", args[2]));
                }
            }

            if (args.length > 2 && args[0].equals("scaffold")) {
                if (tool.buildXmlExists()) {
                    out.put(Messages.scaffoldingInProgress.replace("{entity}", args[1]));
                    tool.scaffold(args);
                    out.put(Messages.scaffoldingDone.replace("{entity}", args[1]));
                } else {
                    out.put(Messages.goToRootOfApp);
                }
            }
            
        } catch (StrutsToolException ex) {
            out.put("Error: " + ex.getMessage());
        }
    }
}
