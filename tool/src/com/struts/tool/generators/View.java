package com.struts.tool.generators;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.builder.MarkUpBuilder;
import com.struts.tool.builder.components.Attribute;
import com.struts.tool.builder.components.MarkUpTag;
import com.struts.tool.builder.components.Type;
import com.struts.tool.helpers.DirectoryHelper;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.helpers.ListHelper;
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
                     + StringHelper.lcfirst(entityName);

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

        String[] actionsArr = ListHelper.toStringArray(actions);
        makePages(actionsArr);
        addPagesToTilesConfig(actionsArr);
    }

    private void makeFolder() throws StrutsToolException {
        File folder = new File(viewRootPath);
        if (!folder.exists()) {
            out.put("create  " + viewRootPath);
            if (!folder.mkdirs()) {
                throw new StrutsToolException(Messages.createViewFolderError);
            }
        } else {
            out.put("exists  " + viewRootPath);
        }
    }

    private void makePages(String[] actions) throws StrutsToolException {
        try {
            String refPagePath = templatePath + "/GenericView";

            String pageContent = FileHelper.toString(refPagePath);

            for (String action : actions) {
                // Replace the tags
                pageContent = pageContent.replace("<<action>>",
                        StringHelper.ucfirst(action));
                pageContent = pageContent.replace("<<entityName>>", entityName);

                String pagePath = viewRootPath + "/" 
                                + StringHelper.lcfirst(action)
                                + ".jsp";

                out.put("create  " + pagePath);

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
            MarkUpBuilder builder = new MarkUpBuilder();
            
            for (Attribute attr : attributes) {
                if (!attr.getName().equals("id")
                        && !attr.getType().getClassification().equals(Type.COLLECTION)) {
                    
                    MarkUpTag display = new MarkUpTag("display:column");
                    display.addAttribute("property", attr.getName());
                    display.addAttribute("value", "label." + attr.getName());
                    display.addAttribute("sortable", "true");

                    tableContents += builder.build(display);
                }
            }

            // Replace the tags
            pageContent = pageContent.replace("<<namespace>>", entityName.toLowerCase());
            pageContent = pageContent.replace("<<entityName>>", entityName);
            pageContent = pageContent.replace("<<tableContents>>", tableContents);

            String pagePath = viewRootPath + "/index.jsp";

            out.put("create  " + pagePath);

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

            out.put("create  " + pagePath);

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

            out.put("create  " + pagePath);

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
            MarkUpBuilder builder = new MarkUpBuilder();

            for (Attribute attr : attributes) {

                if (!attr.getName().equals("id")
                        && !attr.getType().getClassification().equals(Type.COLLECTION)) {
                    
                    MarkUpTag paragraph = new MarkUpTag("p");

                    MarkUpTag label = new MarkUpTag("s:label");
                    label.addAttribute("key", "label." + attr.getName());
                    paragraph.addChilren(label);

                    if (attr.getType().getName().equals("date")) {
                        MarkUpTag datepicker = new MarkUpTag("sj:datepicker");
                        datepicker.addAttribute("name", attr.getName());
                        datepicker.addAttribute("displayFormat", "dd/mm/yy");
                        paragraph.addChilren(datepicker);
                    }

                    else if (attr.getType().getClassification().equals(Type.ENTITY)) {
                        MarkUpTag autocompleter = new MarkUpTag("sj:autocompleter");
                        autocompleter.addAttribute("id", attr.getName());
                        autocompleter.addAttribute("name", attr.getName() + ".id");
                        autocompleter.addAttribute("list", "%{"
                                + StringHelper.lcfirst(attr.getName()) + "List}");
                        autocompleter.addAttribute("listValue", "id");
                        autocompleter.addAttribute("listKey", "id");
                        autocompleter.addAttribute("selectBox", "true");
                        paragraph.addChilren(autocompleter);
                    }

                    else {
                        MarkUpTag textfield = new MarkUpTag("s:textfield");
                        textfield.addAttribute("name", attr.getName());
                        paragraph.addChilren(textfield);
                    }

                    MarkUpTag fielderror = new MarkUpTag("s:fielderror");
                    fielderror.addAttribute("fieldName", attr.getName());
                    paragraph.addChilren(fielderror);

                    inputs += builder.build(paragraph, "    ");
                }
            }

            // Replace the tags
            pageContent = pageContent.replace("<<namespace>>", entityName.toLowerCase());
            pageContent = pageContent.replace("<<inputs>>", inputs);

            String pagePath = viewRootPath + "/form.jsp";

            out.put("create  " + pagePath);

            FileHelper.toFile(pagePath, pageContent);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createViewFormPageError, ex);
        }
    }

    private void addPagesToTilesConfig(String[] actions) throws StrutsToolException {
        try {
            String tilesConfig = "web/WEB-INF/tiles.xml";
            out.put("modify  " + tilesConfig);

            String configContent = FileHelper.toString(tilesConfig);

            String pages = "<!-- generator:pages -->\n";
            MarkUpBuilder builder = new MarkUpBuilder();

            for (String action : actions) {
                MarkUpTag definition = new MarkUpTag("definition");
                definition.addAttribute("name", entityName.toLowerCase() 
                        + StringHelper.ucfirst(action));
                definition.addAttribute("extends", "baseLayout");

                MarkUpTag title = new MarkUpTag("put-attribute");
                title.addAttribute("name", "title");
                title.addAttribute("value", StringHelper.ucfirst(action) 
                        + " of " + entityName);
                definition.addChilren(title);

                MarkUpTag body = new MarkUpTag("put-attribute");
                body.addAttribute("name", "body");
                body.addAttribute("value", "/WEB-INF/"
                        + entityName.toLowerCase() + "/" + action + ".jsp");
                definition.addChilren(body);

                pages += builder.build(definition, "    ");
            }

            // Replace the tags
            configContent = configContent.replace("<!-- generator:pages -->", pages);

            FileHelper.toFile(tilesConfig, configContent);
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.addingViewsToTilesConfError, ex);
        }
    }
}
