package com.struts.tool.builder;

import com.struts.tool.builder.components.XmlAttribute;
import com.struts.tool.builder.components.XmlTag;

/**
 *
 * @author maycon
 */
public class XmlBuilder {
    private XmlTag parent;

    public static final String DEFAULT_TAB = "    ";

    public XmlBuilder(XmlTag parent) {
        this.parent = parent;
    }

    public String build() {
        return buildTag(parent, DEFAULT_TAB);
    }

    private String buildTag(XmlTag tag, String tab) {
        String content = "";

        content += tab + "<" + tag.getName() + buildAttributes(tag);

        if (tag.getContent() == null) {
            if (tag.getChildrens().size() > 0) {
                content += ">\n";
                for (XmlTag children : tag.getChildrens()) {
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

    private String buildAttributes(XmlTag tag) {
        String attrStr = "";

        for (int i = 0; i < tag.getAttributes().size(); i++) {
            XmlAttribute attr = tag.getAttributes().get(i);
            
            if (i == 0) attrStr += " ";
            attrStr += attr.getName() + "=\"" + attr.getValue() + "\"";
            if (i != (tag.getAttributes().size() - 1)) attrStr += " ";
        }

        return attrStr;
    }
}
