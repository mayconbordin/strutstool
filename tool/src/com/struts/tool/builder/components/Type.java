package com.struts.tool.builder.components;

/**
 *
 * @author maycon
 */
public class Type {
    private String name;
    private String javaName;
    private String importPath;
    private String hibernateName;
    private String classification;

    public static final String VOID = "void";
    public static final String NUMERIC = "numeric";
    public static final String BOOLEAN = "boolean";
    public static final String CHARACTER = "character";
    public static final String DATE = "date";
    public static final String CURRENCY = "currency";
    public static final String COLLECTION = "collection";
    public static final String BLOB = "blob";
    public static final String CLOB = "clob";
    public static final String ENTITY = "entity";

    public Type() {}

    public Type(String javaName) {
        this.javaName = javaName;
    }

    public Type(String javaName, String importPath) {
        this.javaName = javaName;
        this.importPath = importPath;
    }

    public Type(String name, String javaName, String importPath, String hibernateName, String classification) {
        this.name = name;
        this.javaName = javaName;
        this.importPath = importPath;
        this.hibernateName = hibernateName;
        this.classification = classification;
    }

    public String getHibernateName() {
        return hibernateName;
    }

    public void setHibernateName(String hibernateName) {
        this.hibernateName = hibernateName;
    }

    public String getImportPath() {
        return importPath;
    }

    public void setImportPath(String importPath) {
        this.importPath = importPath;
    }

    public String getJavaName() {
        return javaName;
    }

    public void setJavaName(String javaName) {
        this.javaName = javaName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}
