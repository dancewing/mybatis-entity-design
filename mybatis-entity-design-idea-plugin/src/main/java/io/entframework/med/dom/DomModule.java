/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.dom;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.*;

import java.util.List;

public abstract class DomModule extends DescribableDomElement {

    @SubTag("base-entity")
    @Convert(PsiClassConverter.class)
    public abstract GenericDomValue<PsiClass> getBaseEntity();

    @SubTagList("entity")
    public abstract List<DomEntity> getEntities();

    @Attribute("package")
    public abstract GenericAttributeValue<String> getPackage();
    public abstract DomEntity addEntity();
}
