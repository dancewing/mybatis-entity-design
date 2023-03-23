/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

public final class MedEntityNodeField implements MedNode {

    private String name;

    private String type;

    private String description;

    private Icon icon;

    public MedEntityNodeField() {
    }

    public MedEntityNodeField(String name, String type, String description, Icon icon) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.icon = icon;
    }

    public @NotNull String getName() {
        return name;
    }

    public @Nullable String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedEntityNodeField that = (MedEntityNodeField) o;
        return Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(description, that.description) && Objects.equals(icon, that.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, description, icon);
    }
}
