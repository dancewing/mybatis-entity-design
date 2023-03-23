/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

import io.entframework.med.util.TextUtil;

import java.io.Serializable;
import java.util.List;


public class EntityImpl extends GeneratorObjectImpl implements Serializable, Entity {

    private static final long serialVersionUID = 6739294315192751908L;

    /**
     * 表对应的model名称（驼峰, 首字符大写）
     */
    private Name name;

    /**
     * 表名称
     */
    private String table;

    private String comment;

    /**
     * 表中的字段
     */
    private List<Field> fields;

    public EntityImpl() {
        super(null);
    }

    public EntityImpl(JavaPackage javaPackage) {
        super(javaPackage);
    }

    public EntityImpl(List<Field> fields) {
        super(null);
        this.fields = fields;
    }

    public EntityImpl(String table, List<Field> fields) {
        super(null);
        this.setTable(table);
        this.name = Name.of(TextUtil.underlineToCamel(table, true));
        this.fields = fields;
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public void setName(Name name) {
        this.name = name;
    }

    @Override
    public String getTable() {
        return table;
    }

    @Override
    public void setTable(String table) {
        this.table = table;
    }

    @Override
    public List<Field> getFields() {
        return fields;
    }

    @Override
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

}
