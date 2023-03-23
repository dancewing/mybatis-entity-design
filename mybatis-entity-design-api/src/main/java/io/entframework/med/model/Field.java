/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

import java.sql.JDBCType;

public interface Field {
    String getName();

    void setName(String name);

    boolean isPK();

    void setPK(boolean pk);

    String getColumn();

    void setColumn(String column);

    String getColumnType();

    void setColumnType(String columnType);

    JDBCType getJdbcType();

    void setJdbcType(JDBCType jdbcType);

    String getComment();

    void setComment(String comment);

    boolean isNullable();

    void setNullable(boolean nullable);

    int getLength();

    void setLength(int length);

    int getScale();

    void setScale(int scale);

    JavaType getJavaType();

    void setJavaType(JavaType javaType);

    String getTypeHandler();

    void setTypeHandler(String typeHandler);
}
