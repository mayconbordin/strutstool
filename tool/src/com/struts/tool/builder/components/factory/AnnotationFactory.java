package com.struts.tool.builder.components.factory;

import com.struts.tool.builder.components.Annotation;

/**
 *
 * @author maycon
 */
public class AnnotationFactory {
    public Annotation getNotNull() {
        Annotation notNull = new Annotation();
        notNull.setContent("@NotNull");
        notNull.getImportStr().add("com.framework.util.validator.constraints.NotNull");

        return notNull;
    }

    public Annotation getXssFilter() {
        Annotation xssFilter = new Annotation();
        xssFilter.setContent("@XSSFilter");
        xssFilter.getImportStr().add("com.framework.util.validator.constraints.XSSFilter");

        return xssFilter;
    }

    public Annotation getLenght(int min, int max) {
        String minStr = "";
        String maxStr = "";
        String midStr = "";

        if (min > 0) minStr = "min = " + min;
        if (max > 0) maxStr = "max = " + max;
        if (min > 0 && max > 0) midStr = ",";
        
        Annotation lenght = new Annotation();
        lenght.setContent("@Length(" + minStr + midStr + maxStr + ")");
        lenght.getImportStr().add("org.hibernate.validator.constraints.Length");

        return lenght;
    }

    public Annotation getIndexed() {
        Annotation indexed = new Annotation();
        indexed.setContent("@Indexed");
        indexed.getImportStr().add("org.hibernate.search.annotations.Indexed");

        return indexed;
    }

    public Annotation getDocumentId() {
        Annotation documentId = new Annotation();
        documentId.setContent("@DocumentId");
        documentId.getImportStr().add("org.hibernate.search.annotations.DocumentId");

        return documentId;
    }

    public Annotation getFieldUntokenStore() {
        Annotation fieldUnTokenStore = new Annotation();
        fieldUnTokenStore.setContent("@Field(index=Index.UN_TOKENIZED, store=Store.YES)");
        fieldUnTokenStore.getImportStr().addAll(
                "org.hibernate.search.annotations.Field",
                "org.hibernate.search.annotations.Index",
                "org.hibernate.search.annotations.Store");

        return fieldUnTokenStore;
    }

    public Annotation getDateBridgeSecondRes() {
        Annotation dateBridgeSecondRes = new Annotation();
        dateBridgeSecondRes.setContent("@DateBridge(resolution=Resolution.SECOND)");
        dateBridgeSecondRes.getImportStr().addAll(
                "org.hibernate.search.annotations.DateBridge",
                "org.hibernate.search.annotations.Resolution");

        return dateBridgeSecondRes;
    }

    public Annotation getFieldTokenStore() {
        Annotation fieldTokenStore = new Annotation();
        fieldTokenStore.setContent("@Field(index=Index.TOKENIZED,store=Store.YES)");
        fieldTokenStore.getImportStr().addAll(
                "org.hibernate.search.annotations.Field",
                "org.hibernate.search.annotations.Index",
                "org.hibernate.search.annotations.Store");

        return fieldTokenStore;
    }
}
