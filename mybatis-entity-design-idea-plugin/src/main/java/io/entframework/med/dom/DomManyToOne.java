/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.dom;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;

public abstract class DomManyToOne extends DescribableDomElement {
    @Required
    @Attribute("target-entity")
    public abstract GenericAttributeValue<String> getTargetEntity();

}
