package com.framework.util.repository;

import com.framework.util.exception.ApplicationException;

/**
 *
 * @author maycon
 */
public class RepositoryException extends ApplicationException {
    public RepositoryException() {
        super();
    }

    public RepositoryException( String message ) {
        super(message);
    }

    public RepositoryException( Exception ex , String message ) {
        super(ex, message);
    }

    public RepositoryException( Exception ex ) {
        super(ex);
    }
}
