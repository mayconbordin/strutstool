package com.framework.util.search;

import com.framework.util.collection.DataCollection;

/**
 *
 * @author maycon
 */
public interface EntitySearchMap {
    /**
     * @return DataCollection<fieldName, isString>
     */
    public DataCollection<String, EntityField> getFields();
}
