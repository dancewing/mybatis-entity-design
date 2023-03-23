/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

import java.io.Serializable;
import java.sql.JDBCType;

public class FieldImpl implements Serializable, Field {

    private static final long serialVersionUID = -7928412682947631640L;

    /**
     * 字段名
     */
    private String name;

    /**
     * 字段Java类型
     */
    private JavaType javaType;

    /**
     * 数据库字段名
     */
    private String column;

    /**
     * 数据库字段类型
     */
    private String columnType;

    private JDBCType jdbcType;

    /**
     * 数据库字段长度
     */
    private int length;

    private boolean nullable;

    private int scale;

    private boolean pk;

    /**
     * 备注
     */
    private String comment;

    private String typeHandler;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public boolean isPK() {
        return pk;
    }

    public void setPK(boolean pk) {
        this.pk = pk;
    }

    @Override
    public String getColumn() {
        return column;
    }

    @Override
    public void setColumn(String column) {
        this.column = column;
    }

    @Override
    public String getColumnType() {
        return columnType;
    }

    @Override
    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    @Override
    public JDBCType getJdbcType() {
        return jdbcType;
    }

    @Override
    public void setJdbcType(JDBCType jdbcType) {
        this.jdbcType = jdbcType;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    @Override
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public int getScale() {
        return scale;
    }

    @Override
    public void setScale(int scale) {
        this.scale = scale;
    }

    @Override
    public JavaType getJavaType() {
        return javaType;
    }

    @Override
    public void setJavaType(JavaType javaType) {
        this.javaType = javaType;
    }

    @Override
    public String getTypeHandler() {
        return typeHandler;
    }

    @Override
    public void setTypeHandler(String typeHandler) {
        this.typeHandler = typeHandler;
    }
}
