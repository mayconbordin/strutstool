package com.struts.tool.output;

/**
 *
 * @author maycon
 */
public class TerminalOutput implements MessageOutput {

    public void put(String message) {
        System.out.println(message);
    }
}
