/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.dom;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.*;

public abstract class DomId extends DescribableDomElement {
    @Required
    @Attribute("type")
    @Convert(PsiClassConverter.class)
    public abstract GenericAttributeValue<PsiClass> getType();

    @SubTag("column")
    public abstract DomColumn getColumn();
}
