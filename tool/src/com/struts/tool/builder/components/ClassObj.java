package com.struts.tool.builder.components;

import com.struts.tool.helpers.StringHelper;
import com.struts.tool.util.ExtArrayList;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maycon
 */
public class ClassObj {
    private String name;
    private String packageName;
    private String accessType;
    private ExtArrayList<Annotation> annotations = new ExtArrayList();
    private ClassObj extendClass;
    private ExtArrayList<ClassObj> implementClasses = new ExtArrayList();
    private ExtArrayList<String> imports = new ExtArrayList();
    private ExtArrayList<Method> methods = new ExtArrayList();
    private ExtArrayList<Attribute> attributes = new ExtArrayList();

    public static final String PUBLIC = "public";
    public static final String PRIVATE = "private";
    public static final String PROTECTED = "protected";

    public ClassObj(String name, String packageName) {
        this.name = name;
        this.packageName = packageName;
    }

    public ClassObj() {}

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(ExtArrayList<Annotation> annotations) {
        this.annotations = annotations;
    }

    public ExtArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ExtArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }

    public ClassObj getExtendClass() {
        return extendClass;
    }

    public void setExtendClass(ClassObj extendClass) {
        this.extendClass = extendClass;
    }

    public ExtArrayList<ClassObj> getImplementClasses() {
        return implementClasses;
    }

    public void setImplementClasses(ExtArrayList<ClassObj> implementClasses) {
        this.implementClasses = implementClasses;
    }

    public ExtArrayList<String> getImports() {
        return imports;
    }

    public void setImports(ExtArrayList<String> imports) {
        this.imports = imports;
    }

    public ExtArrayList<Method> getMethods() {
        return methods;
    }

    public void setMethods(ExtArrayList<Method> methods) {
        this.methods = methods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
