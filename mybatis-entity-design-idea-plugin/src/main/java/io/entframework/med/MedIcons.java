/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.LayeredIcon;

import javax.swing.*;

public final class MedIcons {

    public static final Icon ICON = getIcon("/icons/med-file-type.svg");

    public static final Icon ENTITY_MAPPINGS_ICON = getIcon("/icons/entity-mappings.svg");

    public static final Icon ENUM_DEFINITION_ICON = getIcon("/icons/enum-definition.svg");

    private static final Icon REQUIRED_MARK_ICON = getIcon("/icons/required-mark.svg");

    public static final Icon PLUS_ICON = getIcon("/icons/plus.png");

    public static final Icon CONNECTION_ICON = getIcon("/icons/schema.svg");

    public static final Icon DATABASE_ICON = getIcon("/icons/dbms.svg");

    public static final Icon TABLE_ICON = getIcon("/icons/table.svg");

    public static final Icon COLUMN_ICON = getIcon("/icons/col.svg");

    public static final Icon COLUMN_PK_ICON = getIcon("/icons/colGoldKey.svg");

    public static final Icon SERVER_ICON = AllIcons.Webreferences.Server;

    public static final Icon REQUIRED_FIELD = new LayeredIcon(AllIcons.Nodes.Field, REQUIRED_MARK_ICON);

    public static final Icon RELATIONSHIP = AllIcons.General.InheritedMethod;

    public static final Icon ENTITY = AllIcons.Javaee.PersistenceEntity;

    public static final Icon ENUM = AllIcons.Nodes.Enum;

    public static Icon getConfigurationPropertyIcon() {
        return AllIcons.Nodes.PropertyWriteStatic;
    }

    public static Icon getPropertyIcon() {
        return AllIcons.Nodes.PropertyWrite;
    }

    public static Icon getConfigIcon() {
        return AllIcons.Json.Object;
    }

    public static Icon getApplicationIcon() {
        return AllIcons.RunConfigurations.Applet;
    }

    public static final Icon FIELD = AllIcons.Nodes.Field;

    public static Icon getConstantIcon() {
        return AllIcons.Nodes.Constant;
    }

    public static Icon getDeployIcon() {
        return AllIcons.Nodes.Deploy;
    }

    public static Icon getFieldConstraintIcon() {
        return AllIcons.General.InspectionsOK;
    }

    public static Icon getFieldTypeIcon() {
        return AllIcons.Nodes.Type;
    }

    private static Icon getIcon(String path) {
        return IconLoader.getIcon(path, MedIcons.class);
    }
}
