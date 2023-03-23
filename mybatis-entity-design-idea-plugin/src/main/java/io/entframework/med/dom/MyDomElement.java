/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.dom;

import com.intellij.icons.AllIcons;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import io.entframework.med.MedIcons;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class MyDomElement implements DomElement {

    public @Nullable DomMed getRoot() {
        DomElement parent = getParent();
        if (parent == null) {
            return null;
        }
        DomElement _parent = parent;
        while (_parent != null && !DomMed.class.isAssignableFrom(_parent.getClass())) {
            _parent = _parent.getParent();
        }
        return (DomMed) _parent;
    }

    public Icon getIcon() {
        if (this instanceof DomEntity) {
            return (MedIcons.ENTITY);
        }
        if (this instanceof DomId) {
            return (AllIcons.Nodes.FieldPK);
        }
        if (this instanceof DomEnumAttribute) {
            return (AllIcons.Nodes.Enum);
        }
        if (this instanceof DomBasic) {
            return (AllIcons.Nodes.Field);
        }
        if (this instanceof DomVersion) {
            return (AllIcons.Nodes.Field);
        }
        if (this instanceof DomManyToOne) {
            return (AllIcons.Nodes.Related);
        }
        if (this instanceof DomOneToMany) {
            return (AllIcons.Nodes.Related);
        }

        if (this instanceof DomEnum) {
            return (AllIcons.Nodes.Enum);
        }
        if (this instanceof DomEnumField) {
            return (AllIcons.Nodes.Field);
        }

        if (this instanceof DomModule) {
            return (MedIcons.ENTITY_MAPPINGS_ICON);
        }

        if (this instanceof DomEnumDefinition) {
            return (MedIcons.ENUM_DEFINITION_ICON);
        }

        if (this instanceof DomMed) {
            return (MedIcons.ICON);
        }
        return null;
    }

    public XmlFile getFile() {
        return DomUtil.getFile(this);
    }
}
