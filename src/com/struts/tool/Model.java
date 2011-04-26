package com.struts.tool;

import java.io.File;

/**
 *
 * @author maycon
 */
public class Model {
    private String packages;
    private String entityName;
    private String[] attr;
    private String[] type;

    public Model(String entityName, String packages) {
        this.packages = packages;
        this.entityName = entityName;
    }

    public void createModel(String[] args) {
        extractParams(args);
        makeEntity();
    }

    private void extractParams(String[] args) {
        attr = new String[args.length - 2];
        type = new String[args.length - 2];
        String[] temp;

        for (int i = 2; i < args.length; i++) {
            temp = args[i].split(":");
            attr[i - 2] = temp[0];
            type[i - 2] = temp[1];
        }
    }

    private void makeEntity() {
        // Create the folder
        new File("src/java/" + packages + "/model/entity").mkdirs();
        
        String refEntityPath = Directory.getInstallationDirectory()
                + "/resources/files/Entity.txt";

        String entityContent = FileUtil.toString(refEntityPath);

        // Create the attributes, getters and setters
        String attributes = "";
        String accessors = "";
        for (int i = 0; i < attr.length; i++) {
            attributes += "    private "+type[i]+" "+attr[i]+";\n";
            accessors += "    @NotNull\n"
                       + "    @XSSFilter\n"
                       + "    public "+type[i]+" get"+entityName+"() {\n"
                       + "        return "+entityName.toLowerCase()+";\n"
                       + "    }\n"
                       + "\n"
                       + "    public void set"+entityName+"("+type[i]+" "
                       + entityName.toLowerCase()+") {\n"
                       + "        this."+entityName.toLowerCase()+" = "+entityName.toLowerCase()+";\n"
                       + "    }\n";
        }

        // Replace the tags
        entityContent = entityContent.replaceAll("<<packages>>", packages.replace("/", "."));
        entityContent = entityContent.replaceAll("<<entityName>>", entityName);
        entityContent = entityContent.replaceAll("<<attributes>>", attributes);
        entityContent = entityContent.replaceAll("<<accessors>>", accessors);
        entityContent = entityContent.replaceAll("<<imports>>", "");

        String entityPath = "src/java/" + packages + "/model/entity/"
                + entityName + ".java";

        FileUtil.toFile(entityPath, entityContent);
    }

    private void makeFolder() {
        new File("src/java/" + packages + "/model/entity").mkdirs();
        new File("src/java/" + packages + "/model/mapping").mkdirs();
        new File("src/java/" + packages + "/model/repository").mkdirs();
        new File("src/java/" + packages + "/model/service").mkdirs();
        new File("src/java/" + packages + "/model/validator").mkdirs();
    }
}