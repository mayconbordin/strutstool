package com.struts.tool.generators;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.attributes.Attribute;
import com.struts.tool.helpers.DirectoryHelper;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.helpers.StringHelper;
import com.struts.tool.output.MessageOutput;
import com.struts.tool.output.MessageOutputFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author mayconbordin
 * @version 0.1
 */
public class View {
    private String entityName;
    private Project project;
    private List<Attribute> attributes;

    private String viewRootPath;
    private String templatePath;

    private MessageOutput out;

    public View(String entityName, Project project, List<Attribute> attributes) {
        this.entityName = entityName;
        this.project = project;
        this.attributes = attributes;
        this.out = MessageOutputFactory.getTerminalInstance();
    }

    public View(String entityName, Project project) {
        this.entityName = entityName;
        this.project = project;
        this.out = MessageOutputFactory.getTerminalInstance();
    }

    private void loadPaths() {
        viewRootPath = Project.WEB_PATH
                     + StringHelper.firstToLowerCase(entityName);

        templatePath = DirectoryHelper.getInstallationDirectory()
                     + Project.TEMPLATES_PATH
                     + Project.VIEW_FOLDER;
    }

    public void createFromModel() throws StrutsToolException {
        loadPaths();
        makeFolder();
        makeIndexPage();
        makeAddPage();
        makeEditPage();
        makeFormPage();

        String[] actions = {"index", "add", "edit"};
        addPagesToTilesConfig(actions);
    }

    public void create(List<String> actions) throws StrutsToolException {
        loadPaths();
        makeFolder();

        String[] actionsArr = (String[]) actions.toArray();
        makePages(actionsArr);
        addPagesToTilesConfig(actionsArr);
    }

    private void makeFolder() throws StrutsToolException {
        File folder = new File(viewRootPath);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new StrutsToolException(Messages.createViewFolderError);
            }
        }
    }

    private void makePages(String[] actions) throws StrutsToolException {
        try {
            String refPagePath = templatePath + "/GenericView";

            String pageContent = FileHelper.toString(refPagePath);

            for (String action : actions) {
                // Replace the tags
                pageContent = pageContent.replace("<<action>>",
                        StringHelper.firstToUpperCase(action));
                pageContent = pageContent.replace("<<entityName>>", entityName);

                String pagePath = viewRootPath + "/" 
                                + StringHelper.firstToLowerCase(action)
                                + ".jsp";

                FileHelper.toFile(pagePath, pageContent);
            }
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createViewAddPageError, ex);
        }
    }

    private void makeIndexPage() throws StrutsToolException {
        try {
            String refPagePath = templatePath + "/IndexView";

            String pageContent = FileHelper.toString(refPagePath);

            String tableContents = "";
            for (Attribute attr : attributes) {
                if (!attr.getName().equals("id")) {
                    tableContents += "\t<display:column property=\""+attr+"\" "
                                   + "value=\"label."+attr+"\" sortable=\"true\" />\n";
                }
            }

            // Replace the tags
            pageContent = pageContent.replace("<<namespace>>", entityName.toLowerCase());
            pageContent = pageContent.replace("<<entityName>>", entityName);
            pageContent = pageContent.replace("<<tableContents>>", tableContents);

            String pagePath = viewRootPath + "/index.jsp";

            FileHelper.toFile(pagePath, pageContent);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createViewIndexPageError, ex);
        }
    }

    private void makeAddPage() throws StrutsToolException {
        try {
            String refPagePath = templatePath + "/AddView";

            String pageContent = FileHelper.toString(refPagePath);

            // Replace the tags
            pageContent = pageContent.replace("<<namespace>>", entityName.toLowerCase());
            pageContent = pageContent.replace("<<entityName>>", entityName);

            String pagePath = viewRootPath + "/add.jsp";

            FileHelper.toFile(pagePath, pageContent);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createViewAddPageError, ex);
        }
    }

    private void makeEditPage() throws StrutsToolException {
        try {
            String refPagePath = templatePath + "/EditView";

            String pageContent = FileHelper.toString(refPagePath);

            // Replace the tags
            pageContent = pageContent.replace("<<namespace>>", entityName.toLowerCase());
            pageContent = pageContent.replace("<<entityName>>", entityName);

            String pagePath = viewRootPath + "/edit.jsp";

            FileHelper.toFile(pagePath, pageContent);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createViewEditPageError, ex);
        }
    }

    private void makeFormPage() throws StrutsToolException {
        try {
            String refPagePath = templatePath + "/FormView";

            String pageContent = FileHelper.toString(refPagePath);

            String inputs = "";
            for (Attribute attr : attributes) {
                //String attr = entry.getKey();
                //DataType type = DataTypeCollection.types.get(entry.getValue().toLowerCase());

                if (!attr.getName().equals("id")) {
                    inputs += "\t<p>\n"
                            + "\t\t<s:label key=\"label."+attr+"\" />\n";

                    if (attr.getType().getRaw().equals("date")) {
                        inputs += "\t\t<sj:datepicker name=\""+attr+"\" "
                                + "displayFormat=\"dd/mm/yy\" />\n";
                    } else {
                        inputs += "\t\t<s:textfield name=\""+attr+"\" />\n";
                    }

                    inputs += "\t\t<s:fielderror fieldName=\""+attr+"\" />\n"
                            + "\t</p>\n";
                }
            }

            // Replace the tags
            pageContent = pageContent.replace("<<namespace>>", entityName.toLowerCase());
            pageContent = pageContent.replace("<<inputs>>", inputs);

            String pagePath = viewRootPath + "/form.jsp";

            FileHelper.toFile(pagePath, pageContent);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createViewFormPageError, ex);
        }
    }

    private void addPagesToTilesConfig(String[] actions) throws StrutsToolException {
        try {
            String tilesConfig = "web/WEB-INF/tiles.xml";

            String configContent = FileHelper.toString(tilesConfig);

            String pages = "<!-- generator:pages -->\n";
            for (String action : actions) {
                pages += "\t<definition name=\""+entityName.toLowerCase()
                      + StringHelper.firstToUpperCase(action)+"\" extends=\"baseLayout\">\n"
                      + "\t\t<put-attribute name=\"title\" value=\""
                      + StringHelper.firstToUpperCase(action)+" of "+entityName+"\"/>\n"
                      + "\t\t<put-attribute name=\"body\"  value=\"/WEB-INF/"
                      + ""+entityName.toLowerCase()+"/"+action+".jsp\"/>\n"
                      + "\t</definition>\n";
            }

            // Replace the tags
            configContent = configContent.replace("<!-- generator:pages -->", pages);

            FileHelper.toFile(tilesConfig, configContent);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.addingViewsToTilesConfError, ex);
        }
    }
}
