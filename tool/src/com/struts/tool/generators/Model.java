package com.struts.tool.generators;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.builder.components.Attribute;
import com.struts.tool.generators.model.Entity;
import com.struts.tool.generators.model.Mapping;
import com.struts.tool.generators.model.Repository;
import com.struts.tool.generators.model.SearchMap;
import com.struts.tool.generators.model.Service;
import com.struts.tool.generators.model.Validator;
import com.struts.tool.output.MessageOutput;
import com.struts.tool.output.MessageOutputFactory;
import java.io.File;
import java.util.List;

/**
 *
 * @author mayconbordin
 * @version 0.1
 */
public class Model {
    private String entityName;
    private Project project;
    private List<Attribute> attributes;

    private String modelRootPath;

    private MessageOutput out;

    public Model(String entityName, Project project, List<Attribute> attributes) {
        this.entityName = entityName;
        this.project = project;
        this.attributes = attributes;
        this.out = MessageOutputFactory.getTerminalInstance();
    }

    public Model(String entityName, Project project) {
        this.entityName = entityName;
        this.project = project;
        this.out = MessageOutputFactory.getTerminalInstance();
    }

    private void loadPaths() {
        modelRootPath = Project.SRC_PATH
                        + project.getPackages()
                        + "/" + Project.MODEL_FOLDER;
    }

    public void createModel(boolean enableHibernate, boolean enableValidator,
            boolean enableLuceneSearch) throws StrutsToolException {
        loadPaths();
        makeFolder();

        // Hibernate Search (Lucene) needs hibernate repository to work
        enableLuceneSearch = (enableHibernate) ? enableLuceneSearch : false;

        Entity entity = new Entity(this);
        entity.create(enableValidator, enableLuceneSearch, enableHibernate);

        if (enableHibernate) {
            Mapping mapping = new Mapping(this);
            mapping.create();

            Repository repository = new Repository(this);
            repository.create();

            Service service = new Service(this);
            service.create();

            SearchMap searchMap = new SearchMap(this);
            searchMap.create();
        }

        if (enableValidator) {
            Validator validator = new Validator(this);
            validator.create();
        }
    }

    private void makeFolder() throws StrutsToolException {
        // Create the folder
        File model = new File(modelRootPath);

        if (!model.exists()) {
            out.put("create  " + modelRootPath);
            if (!model.mkdirs()) {
                throw new StrutsToolException(Messages.createModelRepositoryFolderError);
            }
        } else {
            out.put("exists  " + modelRootPath);
        }
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
}