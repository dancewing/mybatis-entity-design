/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.dom;

import com.intellij.util.xml.SubTag;
import com.intellij.util.xml.SubTagList;

import java.util.List;

public abstract class DomAttributes extends MyDomElement {

    @SubTag("id")
    public abstract DomId getId();

    @SubTagList("basic")
    public abstract List<DomBasic> getBasicAttributes();

    @SubTagList("enum")
    public abstract List<DomEnumAttribute> getEnumAttributes();

    @SubTag("version")
    public abstract DomVersion getVersionAttribute();

    @SubTagList("many-to-one")
    public abstract List<DomManyToOne> getManyToOneAttributes();

    @SubTagList("one-to-many")
    public abstract List<DomOneToMany> getOneToManyAttributes();

    public abstract DomBasic addBasicAttribute();
    public abstract DomEnumAttribute addEnumAttribute();
    public abstract DomManyToOne addManyToOne();
    public abstract DomOneToMany addOneToMany();
}
