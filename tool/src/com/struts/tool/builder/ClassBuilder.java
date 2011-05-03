package com.struts.tool.builder;

import com.struts.tool.builder.components.Annotation;
import com.struts.tool.builder.components.Attribute;
import com.struts.tool.builder.components.ClassObj;
import com.struts.tool.builder.components.Method;
import com.struts.tool.helpers.ListHelper;
import java.util.Collections;

/**
 *
 * @author maycon
 */
public class ClassBuilder {
    private ClassObj obj;

    private String packageStr;
    private String importsStr;
    private String classAnnotations;
    private String className;
    private String extendStr;
    private String implementsStr;
    private String attributesStr;
    private String methodsStr;

    public ClassBuilder(ClassObj obj) {
        this.obj = obj;
    }

    public String build() {
        buildPackage();
        buildImports();
        buildClassAnnotations();
        buildClassName();
        buildExtend();
        buildImplements();
        buildAttributes();
        buildMethods();

        return packageStr
               + "/* generator:imports */\n"
               + importsStr
               + "\n"
               + classAnnotations
               + className
               + extendStr
               + implementsStr
               + " {\n"
               + "    /* generator:attributes */\n"
               + attributesStr
               + "    /* generator:methods */\n"
               + methodsStr
               + "}";
    }

    public String buildPackage() {
        packageStr = "package " + obj.getPackageName() + ";\n\n";
        return packageStr;
    }

    public String buildImports() {
        importsStr = "";

        // Remove duplicated imports
        ListHelper.removeDuplicate(obj.getImports());

        // Sort in alphabetical order
        Collections.sort(obj.getImports());
        
        for (String importStr : obj.getImports()) {
            importsStr += "import " + importStr + ";\n";
        }

        return importsStr;
    }

    public String buildMethods() {
        methodsStr = "";
        for (Method method : obj.getMethods()) {
            // Annotations
            for (Annotation ann : method.getAnnotations()) {
                methodsStr += "    " + ann.getContent() + "\n";

                if (ann.getImportStr() != null) {
                    obj.getImports().addAll(ann.getImportStr());
                }
            }

            // Method header
            methodsStr += "    " + method.getAccessType() + " "
                        + method.getReturnType().getJavaName()  + " "
                        + method.getName() + "(";

            if (method.getReturnType() != null && method.getReturnType().getImportPath() != null) {
                obj.getImports().add(method.getReturnType().getImportPath());
            }

            // Parameters
            if (method.getParameters() != null) {
                for (int i = 0; i < method.getParameters().size(); i++) {
                    methodsStr += method.getParameters().get(i).getType().getJavaName()
                                + " " + method.getParameters().get(i).getName();

                    // Include parameter type to import
                    if (method.getParameters().get(i).getType().getImportPath() != null) {
                        obj.getImports().add(method.getParameters().get(i).getType().getImportPath());
                    }

                    if (i != (method.getParameters().size() - 1)) {
                        methodsStr += ", ";
                    }
                }
            }

            methodsStr += ")";

            // Exceptions
            if (method.getThrowExceptions() != null) {
                if (method.getThrowExceptions().size() > 0) {
                    methodsStr += " throws";
                }
                for (int i = 0; i < method.getThrowExceptions().size(); i++) {
                    methodsStr += " " + method.getThrowExceptions().get(i).getName();

                    // Include the exception to import
                    String imp = method.getThrowExceptions().get(i).getPackageName() + "."
                        + method.getThrowExceptions().get(i).getName();
                    obj.getImports().add(imp);

                    if (i != method.getThrowExceptions().size()) {
                        methodsStr += ", ";
                    }
                }
            }
            
            methodsStr += " {\n        " + method.getContent() + "\n    }\n\n";
        }

        if (obj.getMethods().size() > 0) {
            buildImports();
        }

        return methodsStr;
    }

    public String buildClassAnnotations() {
        classAnnotations = "";
        for (Annotation annotation : obj.getAnnotations()) {
            classAnnotations += annotation.getContent() + "\n";

            if (annotation.getImportStr() != null) {
                obj.getImports().addAll(annotation.getImportStr());
            }
        }

        if (obj.getAnnotations().size() > 0) {
            buildImports();
        }

        return classAnnotations;
    }

    public String buildClassName() {
        className = obj.getAccessType() + " class " + obj.getName();
        return className;
    }

    public String buildExtend() {
        extendStr = "";
        if (obj.getExtendClass() != null) {
            extendStr = " extends " + obj.getExtendClass().getName();

            // Include extended class to import
            String imp = obj.getExtendClass().getPackageName() + "."
                    + obj.getExtendClass().getName();
            obj.getImports().add(imp);
            buildImports();
        }

        return extendStr;
    }

    public String buildImplements() {
        implementsStr = "";
        if (obj.getImplementClasses().size() > 0) {
            implementsStr += " implements";
        }
        for (int i = 0; i < obj.getImplementClasses().size(); i++) {
            implementsStr += " " + obj.getImplementClasses().get(i).getName();

            if (i != (obj.getImplementClasses().size() - 1)) {
                implementsStr += ",";
            }

            // Include implemented interface to import
            String imp = obj.getImplementClasses().get(i).getPackageName() + "."
                    + obj.getImplementClasses().get(i).getName();
            obj.getImports().add(imp);
        }

        if (obj.getImplementClasses().size() > 0) {
            buildImports();
        }

        return implementsStr;
    }

    public String buildAttributes() {
        attributesStr = "";
        for (Attribute attr : obj.getAttributes()) {
            // Annotations
            for (Annotation ann : attr.getAnnotations()) {
                attributesStr += "    " + ann.getContent() + "\n";

                // Include annotation class to import
                if (ann.getImportStr() != null) {
                    obj.getImports().addAll(ann.getImportStr());
                }
            }
            attributesStr += "    " + attr.getAccessType() + " "
                           + attr.getType().getJavaName()
                           + " " + attr.getName() + ";\n\n";

            // Includes other data types to import
            if (attr.getType().getImportPath() != null) {
                obj.getImports().add(attr.getType().getImportPath());
            }
        }

        if (obj.getAttributes().size() > 0) {
            buildImports();
        }

        return attributesStr;
    }
}
