/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

import java.io.Serializable;
import java.util.List;

/**
 * Desc: 表结构相关的参数数据
 * <p>
 * Mail: hehaiyangwork@gmail.com Date: 17/3/21
 */
public class CodeContext implements Serializable {

    private static final long serialVersionUID = 6235968905610310027L;

    public CodeContext(String model, String table, String comment, List<FieldImpl> fields) {
        this.model = model;
        this.table = table;
        this.comment = comment;
        this.fields = fields;
    }

    private String model;

    private String table;

    private String comment;

    private List<FieldImpl> fields;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<FieldImpl> getFields() {
        return fields;
    }

    public void setFields(List<FieldImpl> fields) {
        this.fields = fields;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
