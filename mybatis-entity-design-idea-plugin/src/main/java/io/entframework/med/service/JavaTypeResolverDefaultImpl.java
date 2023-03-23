/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.service;

import io.entframework.med.model.Field;
import io.entframework.med.model.JavaType;
import io.entframework.med.model.JavaTypeImpl;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JavaTypeResolverDefaultImpl implements JavaTypeResolver {

    private boolean forceBigDecimals;

    private boolean useJSR310Types;

    protected final Map<Integer, JdbcTypeInformation> typeMap;

    public JavaTypeResolverDefaultImpl(boolean forceBigDecimals, boolean useJSR310Types) {
        super();
        this.forceBigDecimals = forceBigDecimals;
        this.useJSR310Types = useJSR310Types;
        typeMap = new HashMap<>();

        typeMap.put(Types.ARRAY, new JdbcTypeInformation("ARRAY", //$NON-NLS-1$
                new JavaTypeImpl(Object.class.getName())));
        typeMap.put(Types.BIGINT, new JdbcTypeInformation("BIGINT", //$NON-NLS-1$
                new JavaTypeImpl(Long.class.getName())));
        typeMap.put(Types.BINARY, new JdbcTypeInformation("BINARY", //$NON-NLS-1$
                new JavaTypeImpl("byte[]"))); //$NON-NLS-1$
        typeMap.put(Types.BIT, new JdbcTypeInformation("BIT", //$NON-NLS-1$
                new JavaTypeImpl(Boolean.class.getName())));
        typeMap.put(Types.BLOB, new JdbcTypeInformation("BLOB", //$NON-NLS-1$
                new JavaTypeImpl("byte[]"))); //$NON-NLS-1$
        typeMap.put(Types.BOOLEAN, new JdbcTypeInformation("BOOLEAN", //$NON-NLS-1$
                new JavaTypeImpl(Boolean.class.getName())));
        typeMap.put(Types.CHAR, new JdbcTypeInformation("CHAR", //$NON-NLS-1$
                new JavaTypeImpl(String.class.getName())));
        typeMap.put(Types.CLOB, new JdbcTypeInformation("CLOB", //$NON-NLS-1$
                new JavaTypeImpl(String.class.getName())));
        typeMap.put(Types.DATALINK, new JdbcTypeInformation("DATALINK", //$NON-NLS-1$
                new JavaTypeImpl(Object.class.getName())));
        typeMap.put(Types.DATE, new JdbcTypeInformation("DATE", //$NON-NLS-1$
                new JavaTypeImpl(Date.class.getName())));
        typeMap.put(Types.DECIMAL, new JdbcTypeInformation("DECIMAL", //$NON-NLS-1$
                new JavaTypeImpl(BigDecimal.class.getName())));
        typeMap.put(Types.DISTINCT, new JdbcTypeInformation("DISTINCT", //$NON-NLS-1$
                new JavaTypeImpl(Object.class.getName())));
        typeMap.put(Types.DOUBLE, new JdbcTypeInformation("DOUBLE", //$NON-NLS-1$
                new JavaTypeImpl(Double.class.getName())));
        typeMap.put(Types.FLOAT, new JdbcTypeInformation("FLOAT", //$NON-NLS-1$
                new JavaTypeImpl(Double.class.getName())));
        typeMap.put(Types.INTEGER, new JdbcTypeInformation("INTEGER", //$NON-NLS-1$
                new JavaTypeImpl(Integer.class.getName())));
        typeMap.put(Types.JAVA_OBJECT, new JdbcTypeInformation("JAVA_OBJECT", //$NON-NLS-1$
                new JavaTypeImpl(Object.class.getName())));
        typeMap.put(Types.LONGNVARCHAR, new JdbcTypeInformation("LONGNVARCHAR", //$NON-NLS-1$
                new JavaTypeImpl(String.class.getName())));
        typeMap.put(Types.LONGVARBINARY, new JdbcTypeInformation("LONGVARBINARY", //$NON-NLS-1$
                new JavaTypeImpl("byte[]"))); //$NON-NLS-1$
        typeMap.put(Types.LONGVARCHAR, new JdbcTypeInformation("LONGVARCHAR", //$NON-NLS-1$
                new JavaTypeImpl(String.class.getName())));
        typeMap.put(Types.NCHAR, new JdbcTypeInformation("NCHAR", //$NON-NLS-1$
                new JavaTypeImpl(String.class.getName())));
        typeMap.put(Types.NCLOB, new JdbcTypeInformation("NCLOB", //$NON-NLS-1$
                new JavaTypeImpl(String.class.getName())));
        typeMap.put(Types.NVARCHAR, new JdbcTypeInformation("NVARCHAR", //$NON-NLS-1$
                new JavaTypeImpl(String.class.getName())));
        typeMap.put(Types.NULL, new JdbcTypeInformation("NULL", //$NON-NLS-1$
                new JavaTypeImpl(Object.class.getName())));
        typeMap.put(Types.NUMERIC, new JdbcTypeInformation("NUMERIC", //$NON-NLS-1$
                new JavaTypeImpl(BigDecimal.class.getName())));
        typeMap.put(Types.OTHER, new JdbcTypeInformation("OTHER", //$NON-NLS-1$
                new JavaTypeImpl(Object.class.getName())));
        typeMap.put(Types.REAL, new JdbcTypeInformation("REAL", //$NON-NLS-1$
                new JavaTypeImpl(Float.class.getName())));
        typeMap.put(Types.REF, new JdbcTypeInformation("REF", //$NON-NLS-1$
                new JavaTypeImpl(Object.class.getName())));
        typeMap.put(Types.SMALLINT, new JdbcTypeInformation("SMALLINT", //$NON-NLS-1$
                new JavaTypeImpl(Short.class.getName())));
        typeMap.put(Types.STRUCT, new JdbcTypeInformation("STRUCT", //$NON-NLS-1$
                new JavaTypeImpl(Object.class.getName())));
        typeMap.put(Types.TIME, new JdbcTypeInformation("TIME", //$NON-NLS-1$
                new JavaTypeImpl(Date.class.getName())));
        typeMap.put(Types.TIMESTAMP, new JdbcTypeInformation("TIMESTAMP", //$NON-NLS-1$
                new JavaTypeImpl(Date.class.getName())));
        typeMap.put(Types.TINYINT, new JdbcTypeInformation("TINYINT", //$NON-NLS-1$
                new JavaTypeImpl(Byte.class.getName())));
        typeMap.put(Types.VARBINARY, new JdbcTypeInformation("VARBINARY", //$NON-NLS-1$
                new JavaTypeImpl("byte[]"))); //$NON-NLS-1$
        typeMap.put(Types.VARCHAR, new JdbcTypeInformation("VARCHAR", //$NON-NLS-1$
                new JavaTypeImpl(String.class.getName())));
        // JDK 1.8 types
        typeMap.put(Types.TIME_WITH_TIMEZONE, new JdbcTypeInformation("TIME_WITH_TIMEZONE", //$NON-NLS-1$
                new JavaTypeImpl("java.time.OffsetTime"))); //$NON-NLS-1$
        typeMap.put(Types.TIMESTAMP_WITH_TIMEZONE, new JdbcTypeInformation("TIMESTAMP_WITH_TIMEZONE", //$NON-NLS-1$
                new JavaTypeImpl("java.time.OffsetDateTime"))); //$NON-NLS-1$
    }

    @Override
    public JavaType calculateJavaType(Field field) {
        JavaTypeImpl answer = null;
        JdbcTypeInformation jdbcTypeInformation = typeMap.get(field.getJdbcType().getVendorTypeNumber());

        if (jdbcTypeInformation != null) {
            answer = jdbcTypeInformation.getJavaTypeImpl();
            answer = overrideDefaultType(field, answer);
        }
        return answer;
    }

    protected JavaTypeImpl overrideDefaultType(Field field,
                                               JavaTypeImpl defaultType) {
        JavaTypeImpl answer = defaultType;

        switch (field.getJdbcType().getVendorTypeNumber()) {
            case Types.BIT:
                answer = calculateBitReplacement(field, defaultType);
                break;
            case Types.DATE:
                answer = calculateDateType(field, defaultType);
                break;
            case Types.DECIMAL:
            case Types.NUMERIC:
                answer = calculateBigDecimalReplacement(field, defaultType);
                break;
            case Types.TIME:
                answer = calculateTimeType(field, defaultType);
                break;
            case Types.TIMESTAMP:
                answer = calculateTimestampType(field, defaultType);
                break;
            default:
                break;
        }

        return answer;
    }

    protected JavaTypeImpl calculateDateType(Field field, JavaTypeImpl defaultType) {
        JavaTypeImpl answer;

        if (useJSR310Types) {
            answer = new JavaTypeImpl("java.time.LocalDate"); //$NON-NLS-1$
        } else {
            answer = defaultType;
        }

        return answer;
    }

    protected JavaTypeImpl calculateTimeType(Field field, JavaTypeImpl defaultType) {
        JavaTypeImpl answer;

        if (useJSR310Types) {
            answer = new JavaTypeImpl("java.time.LocalTime"); //$NON-NLS-1$
        } else {
            answer = defaultType;
        }

        return answer;
    }

    protected JavaTypeImpl calculateTimestampType(Field field,
                                                  JavaTypeImpl defaultType) {
        JavaTypeImpl answer;

        if (useJSR310Types) {
            answer = new JavaTypeImpl("java.time.LocalDateTime"); //$NON-NLS-1$
        } else {
            answer = defaultType;
        }

        return answer;
    }

    protected JavaTypeImpl calculateBitReplacement(Field field,
                                                   JavaTypeImpl defaultType) {
        JavaTypeImpl answer;

        if (field.getLength() > 1) {
            answer = new JavaTypeImpl("byte[]"); //$NON-NLS-1$
        } else {
            answer = defaultType;
        }

        return answer;
    }

    protected JavaTypeImpl calculateBigDecimalReplacement(Field field,
                                                          JavaTypeImpl defaultType) {
        JavaTypeImpl answer;

        if (field.getScale() > 0 || field.getLength() > 18 || forceBigDecimals) {
            answer = defaultType;
        } else if (field.getLength() > 9) {
            answer = new JavaTypeImpl(Long.class.getName());
        } else if (field.getLength() > 4) {
            answer = new JavaTypeImpl(Integer.class.getName());
        } else {
            answer = new JavaTypeImpl(Short.class.getName());
        }

        return answer;
    }

    @Override
    public String calculateJdbcTypeName(Field field) {
        String answer = null;
        JdbcTypeInformation jdbcTypeInformation = typeMap.get(field.getJdbcType().getVendorTypeNumber());

        if (jdbcTypeInformation != null) {
            answer = jdbcTypeInformation.getJdbcTypeName();
        }

        return answer;
    }


    public static class JdbcTypeInformation {

        private final String jdbcTypeName;

        private final JavaTypeImpl JavaTypeImpl;

        public JdbcTypeInformation(String jdbcTypeName, JavaTypeImpl JavaTypeImpl) {
            this.jdbcTypeName = jdbcTypeName;
            this.JavaTypeImpl = JavaTypeImpl;
        }

        public String getJdbcTypeName() {
            return jdbcTypeName;
        }

        public JavaTypeImpl getJavaTypeImpl() {
            return JavaTypeImpl;
        }

    }
}
