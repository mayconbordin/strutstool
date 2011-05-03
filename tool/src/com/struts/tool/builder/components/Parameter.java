package com.struts.tool.builder.components;

/**
 *
 * @author maycon
 */
public class Parameter {
    private Type type;
    private String name;

    public Parameter(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
