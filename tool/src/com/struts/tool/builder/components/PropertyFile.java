package com.struts.tool.builder.components;

import com.struts.tool.util.ExtArrayList;

/**
 *
 * @author maycon
 */
public class PropertyFile {
    private ExtArrayList<Property> properties;

    public ExtArrayList<Property> getProperties() {
        return properties;
    }

    public void setProperties(ExtArrayList<Property> properties) {
        this.properties = properties;
    }

    public void addProperty(String key, String value) {
        properties.add(new Property(key, value));
    }
}
