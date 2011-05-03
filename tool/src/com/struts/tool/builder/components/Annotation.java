package com.struts.tool.builder.components;

import com.struts.tool.util.ExtArrayList;
import java.util.List;

/**
 *
 * @author maycon
 */
public class Annotation {
    private String content;
    private ExtArrayList<String> importStr = new ExtArrayList();

    public Annotation() {}

    public Annotation(String content, ExtArrayList<String> importStr) {
        this.content = content;
        this.importStr = importStr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ExtArrayList<String> getImportStr() {
        return importStr;
    }

    public void setImportStr(ExtArrayList<String> importStr) {
        this.importStr = importStr;
    }
}
