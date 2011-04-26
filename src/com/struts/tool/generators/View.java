package com.struts.tool.generators;

import com.struts.tool.helpers.DirectoryHelper;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.helpers.StringHelper;
import java.io.File;
import java.util.Map;

/**
 *
 * @author maycon
 */
public class View {
    private String entityName;
    private String packages;
    private String path;
    private Map<String, String> params;

    public View(String entityName, String packages, Map<String, String> params) {
        this.entityName = entityName;
        this.packages = packages;
        this.path = "web/WEB-INF/" + entityName.toLowerCase();
        this.params = params;
    }

    public void makeView() {
        makeFolder();
        makeIndexPage();
        makeAddPage();
        makeEditPage();
        makeFormPage();
        addPagesToTilesConfig();
    }

    private void makeFolder() {
        new File(path).mkdirs();
    }

    private void makeIndexPage() {
        String refPagePath = DirectoryHelper.getInstallationDirectory()
                + "/resources/files/IndexView.txt";

        String pageContent = FileHelper.toString(refPagePath);

        String tableHeader = "";
        String tableContents = "";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String attr = entry.getKey();

            tableHeader += "            <th><s:text name=\"label."+attr+"\" /></th>\n";
            tableContents += "            <td><s:property value=\""+attr+"\" /></td>\n";
        }

        // Replace the tags
        pageContent = pageContent.replaceAll("<<namespace>>", entityName.toLowerCase());
        pageContent = pageContent.replaceAll("<<entityName>>", entityName);
        pageContent = pageContent.replaceAll("<<tableHeader>>", tableHeader);
        pageContent = pageContent.replaceAll("<<tableContents>>", tableContents);

        String pagePath = path + "/index.jsp";

        FileHelper.toFile(pagePath, pageContent);
    }

    private void makeAddPage() {
        String refPagePath = DirectoryHelper.getInstallationDirectory()
                + "/resources/files/AddView.txt";

        String pageContent = FileHelper.toString(refPagePath);

        // Replace the tags
        pageContent = pageContent.replaceAll("<<namespace>>", entityName.toLowerCase());
        pageContent = pageContent.replaceAll("<<entityName>>", entityName);

        String pagePath = path + "/add.jsp";

        FileHelper.toFile(pagePath, pageContent);
    }

    private void makeEditPage() {
        String refPagePath = DirectoryHelper.getInstallationDirectory()
                + "/resources/files/EditView.txt";

        String pageContent = FileHelper.toString(refPagePath);

        // Replace the tags
        pageContent = pageContent.replaceAll("<<namespace>>", entityName.toLowerCase());
        pageContent = pageContent.replaceAll("<<entityName>>", entityName);

        String pagePath = path + "/edit.jsp";

        FileHelper.toFile(pagePath, pageContent);
    }

    private void makeFormPage() {
        String refPagePath = DirectoryHelper.getInstallationDirectory()
                + "/resources/files/FormView.txt";

        String pageContent = FileHelper.toString(refPagePath);

        String inputs = "";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String attr = entry.getKey();

            inputs += "    <p>\n"
                    + "        <s:label key=\"label."+attr+"\" />\n"
                    + "        <s:textfield name=\""+attr+"\" />\n"
                    + "        <s:fielderror fieldName=\""+attr+"\" />\n"
                    + "    </p>\n";
        }

        // Replace the tags
        pageContent = pageContent.replaceAll("<<namespace>>", entityName.toLowerCase());
        pageContent = pageContent.replaceAll("<<inputs>>", inputs);

        String pagePath = path + "/form.jsp";

        FileHelper.toFile(pagePath, pageContent);
    }

    private void addPagesToTilesConfig() {
        String[] actions = {"index", "add", "edit"};
        
        String tilesConfig = "web/WEB-INF/tiles.xml";

        String configContent = FileHelper.toString(tilesConfig);

        String pages = "<!-- generator:pages -->\n";
        for (String action : actions) {
            pages += "    <definition name=\""+entityName.toLowerCase()
                  + StringHelper.firstToUpperCase(action)+"\" extends=\"baseLayout\">\n"
                  + "        <put-attribute name=\"title\" value=\""
                  + StringHelper.firstToUpperCase(action)+" of "+entityName+"\"/>\n"
                  + "        <put-attribute name=\"body\"  value=\"/WEB-INF/"
                  + ""+entityName.toLowerCase()+"/"+action+".jsp\"/>\n"
                  + "    </definition>\n";
        }

        // Replace the tags
        configContent = configContent.replaceAll("<!-- generator:pages -->", pages);

        FileHelper.toFile(tilesConfig, configContent);
    }
}
