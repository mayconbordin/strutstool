package com.struts.tool.attributes;

import com.struts.tool.helpers.StringHelper;
import com.struts.tool.types.DataType;

/**
 *
 * @author mayconbordin
 * @version 0.1
 */
public class Attribute {
    private String name;
    private DataType type;
    private String relatedWith;
    private int size;

    public Attribute() {}

    public Attribute(String name, DataType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getNameFirstUpper() {
        return StringHelper.firstToUpperCase(name);
    }

    public String getNameLower() {
        return StringHelper.firstToLowerCase(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public String getRelatedWith() {
        return relatedWith;
    }

    public void setRelatedWith(String relatedWith) {
        this.relatedWith = relatedWith;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return name;
    }
}
