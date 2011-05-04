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

        if (args[0].equals("new")) {
            
            // Project
            if (args[1].equals("project")) {
                try {
                    if (args.length > 3) tool.newProject(args[2], args[3]);
                    else out.put(Messages.usage, "");
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
                    if (args.length > 2) tool.newController(args);
                    else out.put(Messages.usage, "");
                } catch (StrutsToolException ex) {
                    out.put(" error  " + ex.getMessage());
                }
            }

            // Model
            else if (args[1].equals("model")) {
                try {
                    if (args.length > 3) tool.newModel(args);
                    else out.put(Messages.usage, "");
                } catch (StrutsToolException ex) {
                    out.put(" error  " + ex.getMessage());
                }
            }
        }

        if (args[0].equals("scaffold")) {
            try {
                if (args.length > 2) tool.scaffold(args);
                else out.put(Messages.usage, "");
            } catch (StrutsToolException ex) {
                out.put(" error  " + ex.getMessage());
            }
        }
    }
}
