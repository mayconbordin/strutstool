package com.struts.tool;

import com.struts.tool.output.MessageOutput;
import com.struts.tool.output.TerminalOutput;

/**
 *
 * @author mayconbordin
 * @version 0.1
 */
public class Main {
    private static MessageOutput out = new TerminalOutput();
   
    public static void main(String[] args) {
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
                try {
                    tool.newProject(args[2]);
                } catch (StrutsToolException ex) {
                    out.put("Error: " + ex.getMessage());
                    try {
                        tool.removeProject(args[2]);
                    } catch (StrutsToolException ex1) {
                        out.put("Error: " + ex1.getMessage());
                    }
                }
            }
        }

        if (args.length > 2 && args[0].equals("scaffold")) {
            try {
                if (tool.buildXmlExists()) {
                    tool.scaffold(args);
                } else {
                    out.put(Messages.goToRootOfApp);
                }
            } catch (StrutsToolException ex) {
                out.put("Error: " + ex.getMessage());
            }
        }
    }
}
