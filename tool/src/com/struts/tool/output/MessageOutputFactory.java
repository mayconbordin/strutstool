package com.struts.tool.output;

/**
 *
 * @author maycon
 */
public class MessageOutputFactory {
    public static MessageOutputTerminal terminal;

    public static MessageOutput getTerminalInstance() {
        if (terminal == null) {
            terminal = new MessageOutputTerminal();
        }
        return terminal;
    }
}
