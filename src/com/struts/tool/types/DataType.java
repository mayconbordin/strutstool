package com.struts.tool.types;

/**
 *
 * @author maycon
 */
public class DataType {
    private String rawType;
    private String javaType;
    private String javaTypeImport;
    private String hibernateType;

    public DataType(String rawType, String javaType, String javaTypeImport, String hbType) {
        this.rawType = rawType;
        this.javaType = javaType;
        this.javaTypeImport = javaTypeImport;
        this.hibernateType = hbType;
    }

    /**
     * @return the rawType
     */
    public String getRawType() {
        return rawType;
    }

    /**
     * @param rawType the rawType to set
     */
    public void setRawType(String rawType) {
        this.rawType = rawType;
    }

    /**
     * @return the javaType
     */
    public String getJavaType() {
        return javaType;
    }

    /**
     * @param javaType the javaType to set
     */
    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    /**
     * @return the javaTypeImport
     */
    public String getJavaTypeImport() {
        return javaTypeImport;
    }

    /**
     * @param javaTypeImport the javaTypeImport to set
     */
    public void setJavaTypeImport(String javaTypeImport) {
        this.javaTypeImport = javaTypeImport;
    }

    /**
     * @return the hbType
     */
    public String getHibernateType() {
        return hibernateType;
    }

    /**
     * @param hbType the hbType to set
     */
    public void setHibernateType(String hbType) {
        this.hibernateType = hbType;
    }

    @Override
    public String toString() {
        return javaType;
    }
}
