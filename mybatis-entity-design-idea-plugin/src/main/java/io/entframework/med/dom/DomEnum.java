/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.dom;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.SubTagList;

import java.util.List;

public abstract class DomEnum extends DescribableDomElement {
    @SubTagList("field")
    public abstract List<DomEnumField> getFields();

    @Attribute("type-handler")
    public abstract GenericAttributeValue<PsiClass> getTypeHandler();
}
