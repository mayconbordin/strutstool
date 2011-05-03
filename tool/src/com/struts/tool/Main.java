package com.struts.tool;

import com.struts.tool.output.MessageOutput;
import com.struts.tool.output.MessageOutputFactory;

/**
 *
 * @author mayconbordin
 * @version 0.1
 */
public class Main {
    private static MessageOutput out = MessageOutputFactory.getTerminalInstance();
   
    public static void main(String[] args) {
        StrutsTool tool = new StrutsTool();
        
        if (args.length == 0) {
            out.put(Messages.usage, "");
            return;
        }

        if (args.length == 1) {
            return;
        }

        if (args.length > 3 && args[0].equals("new")) {
            // Project
            if (args[1].equals("project")) {
                try {
                    tool.newProject(args[2], args[3]);
                } catch (StrutsToolException ex) {
                    out.put(" error  " + ex.getMessage());
                    try {
                        tool.removeProject(args[2]);
                    } catch (StrutsToolException ex1) {
                        out.put(" error  " + ex1.getMessage());
                    }
                }
            }

            // Controller
            else if (args[1].equals("controller")) {
                try {
                    tool.newController(args);
                } catch (StrutsToolException ex) {
                    out.put(" error  " + ex.getMessage());
                }
            }
        }

        if (args.length > 2 && args[0].equals("scaffold")) {
            try {
                tool.scaffold(args);
            } catch (StrutsToolException ex) {
                out.put(" error  " + ex.getMessage());
                out.put(" cause  " + ex.getCause().getMessage());
            }
        }
    }
}
