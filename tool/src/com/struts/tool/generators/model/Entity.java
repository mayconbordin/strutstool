package com.struts.tool.generators.model;

import com.struts.tool.Messages;
import com.struts.tool.StrutsToolException;
import com.struts.tool.builder.ClassBuilder;
import com.struts.tool.builder.components.Attribute;
import com.struts.tool.builder.components.ClassObj;
import com.struts.tool.builder.components.Method;
import com.struts.tool.builder.components.Parameter;
import com.struts.tool.builder.components.Type;
import com.struts.tool.builder.components.TypeCollection;
import com.struts.tool.builder.components.factory.AnnotationFactory;
import com.struts.tool.builder.components.factory.ClassObjFactory;
import com.struts.tool.generators.Model;
import com.struts.tool.generators.Project;
import com.struts.tool.helpers.FileHelper;
import com.struts.tool.helpers.StringHelper;
import com.struts.tool.output.MessageOutput;
import com.struts.tool.output.MessageOutputFactory;
import com.struts.tool.util.ExtArrayList;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author maycon
 */
public class Entity {
    private Model model;
    private ClassObj entity;
    private AnnotationFactory annotationFactory;
    private ClassObjFactory classObjFactory;

    private String entityRootPath;

    private MessageOutput out;

    public Entity(Model model) {
        this.model = model;
        this.out = MessageOutputFactory.getTerminalInstance();
    }

    public void create(boolean validate, boolean search, boolean hibernate) throws StrutsToolException {
        loadPaths();
        makeFolder();
        makeBasicEntity();

        if (validate) addValidation();
        if (search) addSearch();
        if (hibernate) addHibernate();

        buildAndSave();
    }

    private void loadPaths() {
        entityRootPath = Project.SRC_PATH
                           + model.getProject().getPackages()
                           + "/" + Project.MODEL_ENTITY_FOLDER;
    }

    private void makeFolder() throws StrutsToolException {
        // Create the folder
        File entityFolder = new File(entityRootPath);

        if (!entityFolder.exists()) {
            out.put("create  " + entityRootPath);
            if (!entityFolder.mkdirs()) {
                throw new StrutsToolException(Messages.createModelRepositoryFolderError);
            }
        } else {
            out.put("exists  " + entityRootPath);
        }
    }

    private void addHibernate() {
        entity.getImplementClasses().add( getClassObjFactory().getSerializable() );
    }

    private void addSearch() {
        // Add indexed to entity class
        entity.getAnnotations().add(getAnnotationFactory().getIndexed());

        // Now run through attributes for annotation population
        for (Attribute attr : entity.getAttributes()) {
            // Id is a documented id
            if (attr.getName().equals("id")) {
                attr.getAnnotations().add(getAnnotationFactory().getDocumentId());
            }

            // Date bridge
            else if (attr.getType().getClassification().equals(Type.DATE)) {
                attr.getAnnotations().add(getAnnotationFactory().getFieldUntokenStore());
                attr.getAnnotations().add(getAnnotationFactory().getDateBridgeSecondRes());
            }

            // All other attribute types, but collections
            else if (!attr.getType().getClassification().equals(Type.COLLECTION)
                    && !attr.getType().getClassification().equals(Type.ENTITY)) {
                attr.getAnnotations().add(getAnnotationFactory().getFieldTokenStore());
            }
        }
    }

    private void addValidation() {
        for (Method method : entity.getMethods()) {
            // Validation is put on getters, id is not validated, neither is collections
            if (method.getName().startsWith("get")
                    && !method.getName().endsWith("Id")
                    && !method.getReturnType().getClassification().equals(Type.COLLECTION)) {

                // Add annotations to method
                method.getAnnotations().addAll(
                        getAnnotationFactory().getNotNull(),
                        getAnnotationFactory().getXssFilter());

                // Get respective attribute
                Attribute getterAttr = entity.getAttributes().get("id");

                // If method attribute is character and size is set, add lenght validator
                if (getterAttr.getType().getClassification().equals(Type.CHARACTER)
                        && getterAttr.getSize() > 0) {
                    method.getAnnotations().add(
                            getAnnotationFactory().getLenght(0, getterAttr.getSize()));
                }
            }
        }
    }

    private void makeBasicEntity() throws StrutsToolException {
        String packageStr = model.getProject().getPackages().replace("/", ".")
                + ".model.entity";

        entity = new ClassObj();
        entity.setName(model.getEntityName());
        entity.setPackageName(packageStr);
        entity.setAccessType(ClassObj.PUBLIC);

        // Attributes
        for (Attribute attr : model.getAttributes()) {
            if (attr.getAccessType() == null) {
                attr.setAccessType(ClassObj.PRIVATE);
            }
            entity.getAttributes().add(attr);
        }

        // Methods
        for (Attribute attr : model.getAttributes()) {
            String upperName = StringHelper.ucfirst(attr.getName());
            String lowerName = StringHelper.lcfirst(attr.getName());

            Method getter = new Method();
            getter.setName("get" + upperName);
            getter.setReturnType(attr.getType());
            getter.setAccessType(ClassObj.PUBLIC);
            getter.setContent("return " + lowerName + ";");
            entity.getMethods().add(getter);

            Method setter = new Method();
            setter.setName("set" + upperName);
            setter.setReturnType(TypeCollection.get("void"));
            setter.setAccessType(ClassObj.PUBLIC);
            setter.setContent("this." + lowerName + " = " + lowerName + ";");
            setter.setParameters(new ExtArrayList(new Parameter(attr.getType(), lowerName)));
            entity.getMethods().add(setter);
        }
    }

    private void buildAndSave() throws StrutsToolException {
        try {
            ClassBuilder builder = new ClassBuilder(entity);
            
            String entityPath = entityRootPath + "/"
                              + model.getEntityName() + ".java";

            out.put("create  " + entityPath);

            FileHelper.toFile(entityPath, builder.build());
        } catch (IOException ex) {
            throw new StrutsToolException(Messages.createModelEntityError, ex);
        }
    }

    public static boolean exists(String entityName, String packages) {
        String path = Project.SRC_PATH
                + packages + "/"
                + Project.MODEL_ENTITY_FOLDER + "/"
                + entityName + ".java";

        File entity = new File(path);

        return entity.exists();
    }

    public AnnotationFactory getAnnotationFactory() {
        if (annotationFactory == null) {
            annotationFactory = new AnnotationFactory();
        }
        return annotationFactory;
    }

    public ClassObjFactory getClassObjFactory() {
        if (classObjFactory == null) {
            classObjFactory = new ClassObjFactory();
        }
        return classObjFactory;
    }
}
