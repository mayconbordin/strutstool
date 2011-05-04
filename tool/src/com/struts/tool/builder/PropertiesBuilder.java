package com.struts.tool.builder;

import com.struts.tool.builder.components.Property;
import com.struts.tool.builder.components.PropertyFile;

/**
 *
 * @author maycon
 */
public class PropertiesBuilder {
    public String build(PropertyFile properties) {
        String buildStr = "";

        for (Property property : properties.getProperties()) {
            buildStr += property.getKey() + "=" + property.getValue() + "\n";
        }

        return buildStr;
    }
}
