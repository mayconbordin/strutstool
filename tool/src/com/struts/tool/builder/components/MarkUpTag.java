package com.struts.tool.builder.components;

import com.struts.tool.util.ExtArrayList;

/**
 *
 * @author maycon
 */
public class MarkUpTag {
    private String name;
    private String content;
    private ExtArrayList<MarkUpAttribute> attributes = new ExtArrayList();
    private ExtArrayList<MarkUpTag> childrens = new ExtArrayList();
    private MarkUpTag parent;

    public MarkUpTag() {}

    public MarkUpTag(String name) {
        this.name = name;
    }

    public MarkUpTag(String name, MarkUpTag parent) {
        this.name = name;
        this.parent = parent;
    }

    public ExtArrayList<MarkUpAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ExtArrayList<MarkUpAttribute> attributes) {
        this.attributes = attributes;
    }

    public ExtArrayList<MarkUpTag> getChildrens() {
        return childrens;
    }

    public void setChildren(ExtArrayList<MarkUpTag> childrens) {
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

    public MarkUpTag getParent() {
        return parent;
    }

    public void setParent(MarkUpTag parent) {
        this.parent = parent;
    }

    public void addAttribute(String name, String value) {
        attributes.add(new MarkUpAttribute(name, value));
    }

    public void addChilren(MarkUpTag children) {
        childrens.add(children);
    }

    public void addChilrens(MarkUpTag... children) {
        childrens.addAll(children);
    }
}
