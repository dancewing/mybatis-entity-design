/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Desc: Mail: hehaiyangwork@gmail.com Date: 2017/6/6
 */
public class CodeGroup implements Serializable {

    private static final long serialVersionUID = -8843957173498883576L;

    public CodeGroup() {
    }

    public CodeGroup(String id, String name, List<CodeTemplate> templates) {
        this.id = id;
        this.name = name;
        this.templates = templates;
    }

    public CodeGroup(String name) {
        this(UUID.randomUUID().toString(), name, new ArrayList<>());
    }

    /**
     * 分组ID
     */
    private String id;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 模版列表
     */
    private List<CodeTemplate> templates;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CodeTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<CodeTemplate> templates) {
        this.templates = templates;
    }

}
