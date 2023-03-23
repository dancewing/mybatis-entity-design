/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.dom;

import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.SubTag;

public abstract class DomEntity extends DescribableDomElement {
    @SubTag("base-entity")
    public abstract GenericDomValue<String> getBaseEntity();

    @SubTag("table")
    public abstract DomTable getTable();

    @SubTag("attributes")
    public abstract DomAttributes getAttributes();
}
