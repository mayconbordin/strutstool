package com.struts.tool.builder.components;

import com.struts.tool.util.ExtArrayList;
import java.util.List;

/**
 *
 * @author maycon
 */
public class Method {
    private String name;
    private Type returnType;
    private String accessType;
    private String content;
    private ExtArrayList<Parameter> parameters = new ExtArrayList();
    private ExtArrayList<Annotation> annotations = new ExtArrayList();
    private ExtArrayList<ClassObj> throwExceptions = new ExtArrayList();

    public Method() {}

    public Method(String name, Type returnType, String accessType, String content, ExtArrayList<Annotation> annotations) {
        this.name = name;
        this.returnType = returnType;
        this.accessType = accessType;
        this.content = content;
        this.annotations = annotations;
    }

    public Method(String name, Type returnType, String accessType, String content, ExtArrayList<Parameter> parameters, ExtArrayList<Annotation> annotations) {
        this.name = name;
        this.returnType = returnType;
        this.accessType = accessType;
        this.content = content;
        this.parameters = parameters;
        this.annotations = annotations;
    }

    public ExtArrayList<ClassObj> getThrowExceptions() {
        return throwExceptions;
    }

    public void setThrowExceptions(ExtArrayList<ClassObj> throwExceptions) {
        this.throwExceptions = throwExceptions;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExtArrayList<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(ExtArrayList<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public ExtArrayList<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(ExtArrayList<Annotation> annotations) {
        this.annotations = annotations;
    }
}
