/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.dom;

import com.intellij.util.xml.DefinesXml;
import com.intellij.util.xml.SubTagList;

import java.util.List;

@DefinesXml
public abstract class DomMed extends MyDomElement {
    @SubTagList("module")
    public abstract List<DomModule> getModules();

    @SubTagList("enum-definition")
    public abstract List<DomEnumDefinition> getEnumDefinitions();

    public abstract DomModule addModule();

    public abstract DomEnumDefinition addEnumDefinition();
}
