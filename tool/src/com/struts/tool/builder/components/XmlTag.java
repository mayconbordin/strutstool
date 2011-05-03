package com.struts.tool.builder.components;

import com.struts.tool.util.ExtArrayList;

/**
 *
 * @author maycon
 */
public class XmlTag {
    private String name;
    private String content;
    private ExtArrayList<XmlAttribute> attributes = new ExtArrayList();
    private ExtArrayList<XmlTag> childrens = new ExtArrayList();
    private XmlTag parent;

    public XmlTag() {}

    public XmlTag(String name) {
        this.name = name;
    }

    public XmlTag(String name, XmlTag parent) {
        this.name = name;
        this.parent = parent;
    }

    public ExtArrayList<XmlAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ExtArrayList<XmlAttribute> attributes) {
        this.attributes = attributes;
    }

    public ExtArrayList<XmlTag> getChildrens() {
        return childrens;
    }

    public void setChildren(ExtArrayList<XmlTag> childrens) {
        this.childrens = childrens;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public XmlTag getParent() {
        return parent;
    }

    public void setParent(XmlTag parent) {
        this.parent = parent;
    }

    public void addAttribute(String name, String value) {
        attributes.add(new XmlAttribute(name, value));
    }

    public void addChilren(XmlTag children) {
        childrens.add(children);
    }

    public void addChilrens(XmlTag... children) {
        childrens.addAll(children);
    }
}
