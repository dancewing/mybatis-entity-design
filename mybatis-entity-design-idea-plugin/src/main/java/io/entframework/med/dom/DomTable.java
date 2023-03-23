/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.dom;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;

public abstract class DomTable extends MyDomElement {
    @Required
    @Attribute("name")
    public abstract GenericAttributeValue<String> getName();
}
