package com.struts.tool.builder.components;

import com.struts.tool.util.ExtArrayList;
import java.util.List;

/**
 *
 * @author maycon
 */
public class Attribute {
    private String name;
    private Type type;
    private String accessType;
    private ExtArrayList<Annotation> annotations = new ExtArrayList();
    private Integer size = 0;
    private String relatedWith;

    public Attribute() {}

    public Attribute(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Attribute(String name, Type type, String accessType, ExtArrayList<Annotation> annotations) {
        this.name = name;
        this.type = type;
        this.accessType = accessType;
        this.annotations = annotations;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public ExtArrayList<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(ExtArrayList<Annotation> annotations) {
        this.annotations = annotations;
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

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getRelatedWith() {
        return relatedWith;
    }

    public void setRelatedWith(String relatedWith) {
        this.relatedWith = relatedWith;
    }

    @Override
    public String toString() {
        return name;
    }
}
