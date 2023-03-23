/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package com.github.hykes.codegen.parser;

import com.github.hykes.codegen.utils.StringUtils;
import io.entframework.med.model.JavaType;
import io.entframework.med.model.JavaTypeImpl;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析工具
 *
 * @author chk19940609@gmail.com
 * @date 2017/06/21
 */
public class ParserUtils {

    /**
     * 未知字段类型
     */
    public static String UNKNOWN_FIELD = "UNKNOWN";

    /**
     * sqlType <-> javaType
     * <p>
     * 如果要转javaType的枚举, 可以使用Types
     *
     * @see Types
     */
    private static Map<String, JavaType> sqlTypes = new HashMap<>();

    static {
        // mysql
        // https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-type-conversions.html
        sqlTypes.put(UNKNOWN_FIELD, new JavaTypeImpl(UNKNOWN_FIELD));
        sqlTypes.put("BIT", new JavaTypeImpl("java.lang.Boolean"));
        sqlTypes.put("BOOL", new JavaTypeImpl("java.lang.Boolean"));
        sqlTypes.put("BOOLEAN", new JavaTypeImpl("java.lang.Boolean"));
        sqlTypes.put("TINYINT", new JavaTypeImpl("java.lang.Integer"));
        sqlTypes.put("SMALLINT", new JavaTypeImpl("java.lang.Integer"));
        sqlTypes.put("MEDIUMINT", new JavaTypeImpl("java.lang.Integer"));
        sqlTypes.put("INT", new JavaTypeImpl("java.lang.Integer"));
        sqlTypes.put("INTEGER", new JavaTypeImpl("java.lang.Integer"));
        sqlTypes.put("REAL", new JavaTypeImpl("java.lang.Float"));
        // 也用Double吧
        sqlTypes.put("FLOAT", new JavaTypeImpl("java.lang.Double"));
        sqlTypes.put("DOUBLE", new JavaTypeImpl("java.lang.Double"));
        sqlTypes.put("BIGINT", new JavaTypeImpl("java.lang.Long"));
        sqlTypes.put("STRING", new JavaTypeImpl("java.lang.String"));
        sqlTypes.put("CHAR", new JavaTypeImpl("java.lang.String"));
        sqlTypes.put("VARCHAR", new JavaTypeImpl("java.lang.String"));
        sqlTypes.put("TINYTEXT", new JavaTypeImpl("java.lang.String"));
        sqlTypes.put("TEXT", new JavaTypeImpl("java.lang.String"));
        sqlTypes.put("MEDIUMTEXT", new JavaTypeImpl("java.lang.String"));
        sqlTypes.put("LONGTEXT", new JavaTypeImpl("java.lang.String"));
        sqlTypes.put("SET", new JavaTypeImpl("java.lang.String"));
        // java.sql.Date ?
        sqlTypes.put("DATE", new JavaTypeImpl("java.util.Date"));
        // java.sql.Timestamp ?
        sqlTypes.put("DATETIME", new JavaTypeImpl("java.util.Date"));
        // java.sql.Timestamp ?
        sqlTypes.put("TIMESTAMP", new JavaTypeImpl("java.util.Date"));
        // java.sql.Time ?
        sqlTypes.put("TIME", new JavaTypeImpl("java.util.Date"));
        sqlTypes.put("DECIMAL", new JavaTypeImpl("java.math.BigDecimal"));
        sqlTypes.put("BINARY", new JavaTypeImpl("java.lang.Byte[]"));
        sqlTypes.put("VARBINARY", new JavaTypeImpl("java.lang.Byte[]"));
        sqlTypes.put("BLOB", new JavaTypeImpl("java.sql.Blob"));
        sqlTypes.put("TINYBLOB", new JavaTypeImpl("java.sql.Blob"));
        sqlTypes.put("MEDIUMBLOB", new JavaTypeImpl("java.sql.Blob"));
        sqlTypes.put("LONGBLOB", new JavaTypeImpl("java.sql.Blob"));

        // oracle https://docs.oracle.com/cd/B19306_01/java.102/b14188/datamap.htm
        // 与上重复的忽略
        sqlTypes.put("CLOB", new JavaTypeImpl("java.sql.Clob"));
        sqlTypes.put("NCLOB", new JavaTypeImpl("java.sql.NClob"));
        sqlTypes.put("CHARACTER", new JavaTypeImpl("java.lang.String"));
        sqlTypes.put("VARCHAR2", new JavaTypeImpl("java.lang.String"));
        sqlTypes.put("NCHAR", new JavaTypeImpl("java.lang.String"));
        sqlTypes.put("NVARCHAR2", new JavaTypeImpl("java.lang.String"));
        sqlTypes.put("RAW", new JavaTypeImpl("java.lang.Byte[]"));
        sqlTypes.put("LONG RAW", new JavaTypeImpl("java.lang.Byte[]"));
        sqlTypes.put("BINARY_INTEGER", new JavaTypeImpl("java.lang.Integer"));
        sqlTypes.put("NATURAL", new JavaTypeImpl("java.lang.Integer"));
        sqlTypes.put("NATURALN", new JavaTypeImpl("java.lang.Integer"));
        sqlTypes.put("PLS_INTEGER", new JavaTypeImpl("java.lang.Integer"));
        sqlTypes.put("POSITIVE", new JavaTypeImpl("java.lang.Integer"));
        sqlTypes.put("POSITIVEN", new JavaTypeImpl("java.lang.Integer"));
        sqlTypes.put("SIGNTYPE", new JavaTypeImpl("java.lang.Integer"));
        sqlTypes.put("DEC", new JavaTypeImpl("java.math.BigDecimal"));
        sqlTypes.put("NUMBER", new JavaTypeImpl("java.math.BigDecimal"));
        sqlTypes.put("NUMERIC", new JavaTypeImpl("java.math.BigDecimal"));
        sqlTypes.put("DOUBLE PRECISION", new JavaTypeImpl("Double"));
        sqlTypes.put("ROWID", new JavaTypeImpl("java.sql.RowId"));
        sqlTypes.put("UROWID", new JavaTypeImpl("java.sql.RowId"));
        sqlTypes.put("VARRAY", new JavaTypeImpl("java.sql.Array"));

        // sql server
        // https://docs.microsoft.com/en-us/sql/connect/jdbc/using-basic-data-types
        // 与上重复的忽略
        // java.sql.Timestamp ?
        sqlTypes.put("DATETIME2", new JavaTypeImpl("java.util.Date"));
        // java.sql.Timestamp ?
        sqlTypes.put("SMALLDATETIME", new JavaTypeImpl("java.util.Date"));
        sqlTypes.put("IMAGE", new JavaTypeImpl("java.lang.Byte[]"));
        sqlTypes.put("MONEY", new JavaTypeImpl("java.math.BigDecimal"));
        sqlTypes.put("SMALLMONEY", new JavaTypeImpl("java.math.BigDecimal"));
        sqlTypes.put("NTEXT", new JavaTypeImpl("java.lang.String"));
        sqlTypes.put("NVARCHAR", new JavaTypeImpl("java.lang.String"));
        sqlTypes.put("UNIQUEIDENTIFIER", new JavaTypeImpl("java.lang.String"));
        sqlTypes.put("UDT", new JavaTypeImpl("java.lang.Byte[]"));
        // sqlTypes.put("VARBINARY", new JavaTypeImpl("Byte[]", "ByteArray"))
        sqlTypes.put("XML", new JavaTypeImpl("java.sql.SQLXML"));

        // postgresql https://www.postgresql.org/docs/9.5/datatype.html
        sqlTypes.put("JSON", new JavaTypeImpl("java.lang.String"));
        sqlTypes.put("JSONB", new JavaTypeImpl("java.lang.Byte[]"));
        // sqlTypes.put("XML", new JavaTypeImpl("java.lang.String"));
        sqlTypes.put("UUID", new JavaTypeImpl("java.lang.String"));
        sqlTypes.put("CHARACTER VARYING", new JavaTypeImpl("java.lang.String"));
        sqlTypes.put("CHARACTER VARYING[]", new JavaTypeImpl("java.lang.String[]"));
        sqlTypes.put("TEXT[]", new JavaTypeImpl("java.lang.String[]"));
        sqlTypes.put("INTEGER[]", new JavaTypeImpl("java.lang.Integer[]"));
        sqlTypes.put("HSTORE", new JavaTypeImpl("java.util.Map<String, String>"));
    }

    /**
     * 此处就不转成java的Types了, 直接由columnName去匹配字段对应的类型
     *
     * @return 对应字段的java类型
     */
    public static JavaType getFieldType(String typeName) {
        if (StringUtils.isBlank(typeName)) {
            sqlTypes.get(UNKNOWN_FIELD);
        }
        JavaType fieldType = sqlTypes.get(typeName.trim().toUpperCase());
        if (fieldType == null) {
            return sqlTypes.get(UNKNOWN_FIELD);
        }
        return fieldType;
    }

    /**
     * 根据Types获取字段类型
     *
     * @return 对应字段的java类型
     * @see Types
     */
    public static JavaType getFieldType(Integer sqlType) {
        JavaType fieldType = sqlTypes.get(UNKNOWN_FIELD);
        if (sqlType == null) {
            return fieldType;
        }

        // https://docs.oracle.com/javase/1.5.0/docs/guide/jdbc/getstart/mapping.html
        if (sqlType == Types.INTEGER) {
            fieldType = sqlTypes.get("INTEGER");
        } else if (sqlType == Types.VARCHAR) {
            fieldType = sqlTypes.get("STRING");
        } else if (sqlType == Types.CHAR) {
            fieldType = sqlTypes.get("STRING");
        } else if (sqlType == Types.LONGVARCHAR) {
            fieldType = sqlTypes.get("STRING");
        } else if (sqlType == Types.NVARCHAR) {
            fieldType = sqlTypes.get("STRING");
        } else if (sqlType == Types.NCHAR) {
            fieldType = sqlTypes.get("STRING");
        } else if (sqlType == Types.LONGNVARCHAR) {
            fieldType = sqlTypes.get("STRING");
        } else if (sqlType == Types.NUMERIC) {
            fieldType = sqlTypes.get("DECIMAL");
        } else if (sqlType == Types.DECIMAL) {
            fieldType = sqlTypes.get("DECIMAL");
        } else if (sqlType == Types.BIT) {
            fieldType = sqlTypes.get("BOOLEAN");
        } else if (sqlType == Types.BOOLEAN) {
            fieldType = sqlTypes.get("BOOLEAN");
        } else if (sqlType == Types.TINYINT) {
            fieldType = sqlTypes.get("INTEGER");
        } else if (sqlType == Types.SMALLINT) {
            fieldType = sqlTypes.get("INTEGER");
        } else if (sqlType == Types.BIGINT) {
            fieldType = sqlTypes.get("BIGINT");
        } else if (sqlType == Types.REAL) {
            fieldType = sqlTypes.get("REAL");
        } else if (sqlType == Types.FLOAT) {
            fieldType = sqlTypes.get("FLOAT");
        } else if (sqlType == Types.DOUBLE) {
            fieldType = sqlTypes.get("DOUBLE");
        } else if (sqlType == Types.DATE) {
            // java.sql.Date ?
            fieldType = sqlTypes.get("DATE");
        } else if (sqlType == Types.TIME) {
            // java.sql.Time ?
            fieldType = sqlTypes.get("TIME");
        } else if (sqlType == Types.TIMESTAMP) {
            // java.sql.Timestamp ?
            fieldType = sqlTypes.get("TIMESTAMP");
        } else if (sqlType == Types.BINARY || sqlType == Types.VARBINARY) {
            fieldType = sqlTypes.get("BINARY");
        } else if (sqlType == Types.CLOB) {
            fieldType = sqlTypes.get("CLOB");
        } else if (sqlType == Types.BLOB || sqlType == Types.LONGVARBINARY) {
            fieldType = sqlTypes.get("BLOB");
        } else {
            // DISTINCT, ARRAY, STRUCT, REF, JAVA_OBJECT.
            return fieldType;
        }
        return fieldType;
    }

}
