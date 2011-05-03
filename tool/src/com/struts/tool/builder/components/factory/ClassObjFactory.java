package com.struts.tool.builder.components.factory;

import com.struts.tool.builder.components.ClassObj;

/**
 *
 * @author maycon
 */
public class ClassObjFactory {
    public ClassObj getSerializable() {
        ClassObj serializable = new ClassObj();
        serializable.setName("Serializable");
        serializable.setPackageName("java.io");

        return serializable;
    }
}
