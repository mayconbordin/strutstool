package com.struts.tool.builder;

import com.struts.tool.builder.components.MarkUpAttribute;
import com.struts.tool.builder.components.MarkUpTag;

/**
 *
 * @author maycon
 */
public class MarkUpBuilder {
    private MarkUpTag parent;

    public static final String DEFAULT_TAB = "    ";

    public MarkUpBuilder(MarkUpTag parent) {
        this.parent = parent;
    }

    public MarkUpBuilder() {}

    public String build() {
        return buildTag(parent, DEFAULT_TAB);
    }

    public String build(MarkUpTag parent) {
        return buildTag(parent, DEFAULT_TAB);
    }

    public String build(MarkUpTag parent, String tab) {
        return buildTag(parent, tab);
    }

    private String buildTag(MarkUpTag tag, String tab) {
        String content = "";

        content += tab + "<" + tag.getName() + buildAttributes(tag);

        if (tag.getContent() == null) {
            if (tag.getChildrens().size() > 0) {
                content += ">\n";
                for (MarkUpTag children : tag.getChildrens()) {
                    content += buildTag(children, tab + DEFAULT_TAB);
                }
                content += tab + "</" + tag.getName() + ">\n";
            } else {
                content += " />\n";
            }
        } else {
            content += ">" + tag.getContent() + "</" + tag.getName() + ">\n";
        }

        return content;
    }

    private String buildAttributes(MarkUpTag tag) {
        String attrStr = "";

        for (int i = 0; i < tag.getAttributes().size(); i++) {
            MarkUpAttribute attr = tag.getAttributes().get(i);
            
            if (i == 0) attrStr += " ";
            attrStr += attr.getName() + "=\"" + attr.getValue() + "\"";
            if (i != (tag.getAttributes().size() - 1)) attrStr += " ";
        }

        return attrStr;
    }
}
