package com.struts.tool.builder.components;

/**
 *
 * @author maycon
 */
public class MarkUpAttribute {
    private String name;
    private String value;

    public MarkUpAttribute() {}

    public MarkUpAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
