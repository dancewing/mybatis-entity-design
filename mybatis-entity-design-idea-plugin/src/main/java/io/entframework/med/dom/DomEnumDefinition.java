/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.dom;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.SubTagList;

import java.util.List;

public abstract class DomEnumDefinition extends DescribableDomElement {
    @Attribute("package")
    public abstract GenericAttributeValue<String> getName();

    @SubTagList("enum")
    public abstract List<DomEnum> getEnums();
}
