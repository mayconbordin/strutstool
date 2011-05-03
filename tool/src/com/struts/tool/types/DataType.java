package com.struts.tool.types;

/**
 *
 * @author mayconbordin
 * @version 0.1
 */
public class DataType {
    private String raw;
    private String java;
    private String hibernate;
    private String javaImport;
    private String classification;

    public DataType(String java, String javaImport) {
        this.java = java;
        this.javaImport = javaImport;
    }

    public DataType(String raw, String java, String javaImport, String hibernate, String classification) {
        this.raw = raw;
        this.java = java;
        this.javaImport = javaImport;
        this.hibernate = hibernate;
        this.classification = classification;
    }

    /**
     * @return the rawType
     */
    public String getRaw() {
        return raw;
    }

    /**
     * @param raw the rawType to set
     */
    public void setRaw(String raw) {
        this.raw = raw;
    }

    /**
     * @return the javaType
     */
    public String getJava() {
        return java;
    }

    /**
     * @param java the javaType to set
     */
    public void setJava(String java) {
        this.java = java;
    }

    /**
     * @return the javaTypeImport
     */
    public String getJavaImport() {
        return javaImport;
    }

    /**
     * @param javaImport the javaTypeImport to set
     */
    public void setJavaImport(String javaImport) {
        this.javaImport = javaImport;
    }

    /**
     * @return the hbType
     */
    public String getHibernate() {
        return hibernate;
    }

    /**
     * @param hibernate the hbType to set
     */
    public void setHibernate(String hibernate) {
        this.hibernate = hibernate;
    }

    @Override
    public String toString() {
        return java;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}
