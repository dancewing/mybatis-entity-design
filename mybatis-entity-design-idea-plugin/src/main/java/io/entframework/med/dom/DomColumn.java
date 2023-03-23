/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.dom;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.*;

import java.sql.JDBCType;

public abstract class DomColumn extends MyDomElement {
    @Required
    @Attribute("name")
    public abstract GenericAttributeValue<String> getName();
    @Attribute("unique")
    public abstract GenericAttributeValue<Boolean> getUnique();

    @Attribute("nullable")
    public abstract GenericAttributeValue<Boolean> getNullable();

    @Attribute("length")
    @Convert(IntegerConverter.class)
    public abstract GenericAttributeValue<Integer> getLength();

    @Attribute("precision")
    @Convert(IntegerConverter.class)
    public abstract GenericAttributeValue<Integer> getPrecision();

    @Attribute("scale")
    @Convert(IntegerConverter.class)
    public abstract GenericAttributeValue<Integer> getScale();

    @Attribute("jdbc-type")
    @Convert(JdbcTypeConverter.class)
    public abstract GenericAttributeValue<JDBCType> getJdbcType();

    @Attribute("type-handler")
    @Convert(PsiClassConverter.class)
    public abstract GenericAttributeValue<PsiClass> getTypeHandler();
}
