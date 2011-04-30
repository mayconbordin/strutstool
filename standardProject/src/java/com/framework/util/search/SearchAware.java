package com.framework.util.search;

/**
 *
 * @author maycon
 */
public interface SearchAware {
    public boolean isSearchValid();

    public EntitySearchMap getEntitySearchMap();
}
