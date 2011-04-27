package com.struts.tool;

/**
 *
 * @author maycon
 */
public class StrutsToolException extends Exception {
    public StrutsToolException() {
        super();
    }

    public StrutsToolException(String message) {
        super(message);
    }

    public StrutsToolException(String message, Throwable cause) {
        super(message, cause);
    }

    public StrutsToolException(Throwable cause) {
        super(cause);
    }
}
