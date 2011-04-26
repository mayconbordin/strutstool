package com.struts.tool;

import java.io.File;

/**
 *
 * @author maycon
 */
public class View {
    private String entityName;
    private String packages;
    private String path;

    public View(String entityName, String packages) {
        this.entityName = entityName;
        this.packages = packages;
        this.path = "web/WEB-INF/";
    }

    public void makeView() {
        new File(path + entityName.toLowerCase()).mkdirs();
    }
}
