package com.struts.tool.generators;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.helpers.DirectoryHelper;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.helpers.StringHelper;
import com.struts.tool.output.MessageOutput;
import com.struts.tool.types.DataType;
import com.struts.tool.types.DataTypeCollection;
import java.io.File;
import java.io.IOException;
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

    private MessageOutput out;

    public View(String entityName, String packages, Map<String, String> params, MessageOutput out) {
        this.entityName = entityName;
        this.packages = packages;
        this.path = "web/WEB-INF/" + entityName.toLowerCase();
        this.params = params;
    }

    public void makeView() throws StrutsToolException {
        makeFolder();
        makeIndexPage();
        makeAddPage();
        makeEditPage();
        makeFormPage();
        addPagesToTilesConfig();
    }

    private void makeFolder() throws StrutsToolException {
        File folder = new File(path);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new StrutsToolException(Messages.createViewFolderError);
            }
        }
    }

    private void makeIndexPage() throws StrutsToolException {
        try {
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
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createViewIndexPageError, ex);
        }
    }

    private void makeAddPage() throws StrutsToolException {
        try {
            String refPagePath = DirectoryHelper.getInstallationDirectory()
                    + "/resources/files/AddView.txt";

            String pageContent = FileHelper.toString(refPagePath);

            // Replace the tags
            pageContent = pageContent.replaceAll("<<namespace>>", entityName.toLowerCase());
            pageContent = pageContent.replaceAll("<<entityName>>", entityName);

            String pagePath = path + "/add.jsp";

            FileHelper.toFile(pagePath, pageContent);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createViewAddPageError, ex);
        }
    }

    private void makeEditPage() throws StrutsToolException {
        try {
            String refPagePath = DirectoryHelper.getInstallationDirectory()
                    + "/resources/files/EditView.txt";

            String pageContent = FileHelper.toString(refPagePath);

            // Replace the tags
            pageContent = pageContent.replaceAll("<<namespace>>", entityName.toLowerCase());
            pageContent = pageContent.replaceAll("<<entityName>>", entityName);

            String pagePath = path + "/edit.jsp";

            FileHelper.toFile(pagePath, pageContent);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createViewEditPageError, ex);
        }
    }

    private void makeFormPage() throws StrutsToolException {
        try {
            String refPagePath = DirectoryHelper.getInstallationDirectory()
                    + "/resources/files/FormView.txt";

            String pageContent = FileHelper.toString(refPagePath);

            String inputs = "";
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String attr = entry.getKey();
                DataType type = DataTypeCollection.types.get(entry.getValue().toLowerCase());

                if (!attr.equals("id")) {
                    inputs += "    <p>\n"
                            + "        <s:label key=\"label."+attr+"\" />\n";

                    if (type.getRawType().equals("date")) {
                        inputs += "        <sj:datepicker name=\""+attr+"\" "
                                + "displayFormat=\"dd/mm/yy\" />\n";
                    } else {
                        inputs += "        <s:textfield name=\""+attr+"\" />\n";
                    }

                    inputs += "        <s:fielderror fieldName=\""+attr+"\" />\n"
                            + "    </p>\n";
                }
            }

            // Replace the tags
            pageContent = pageContent.replaceAll("<<namespace>>", entityName.toLowerCase());
            pageContent = pageContent.replaceAll("<<inputs>>", inputs);

            String pagePath = path + "/form.jsp";

            FileHelper.toFile(pagePath, pageContent);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createViewFormPageError, ex);
        }
    }

    private void addPagesToTilesConfig() throws StrutsToolException {
        try {
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
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.addingViewsToTilesConfError, ex);
        }
    }
}
