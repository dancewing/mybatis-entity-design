/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.dom;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;
import com.intellij.util.xml.SubTag;

public abstract class DomEnumAttribute extends DescribableDomElement {
    @Required
    @Attribute("target-enum")
    public abstract GenericAttributeValue<String> getTargetEnum();

    @SubTag("column")
    public abstract DomColumn getColumn();
}
