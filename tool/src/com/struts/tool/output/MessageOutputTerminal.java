package com.struts.tool.output;

/**
 *
 * @author mayconbordin
 * @version 0.1
 */
public class MessageOutputTerminal implements MessageOutput {

    public void put(String message) {
        System.out.println("\t" + message);
    }
}
