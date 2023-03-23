/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml.model;

import io.entframework.med.MedIcons;
import io.entframework.med.dom.DomEntity;
import io.entframework.med.util.DomSupport;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

public final class MedEntityNodeData implements MedNodeData {

    private final DomEntity entity;
    private List<MedEntityNodeField> properties;

    public MedEntityNodeData(@NotNull DomEntity entity) {
        this.entity = entity;
    }

    public void setProperties(List<MedEntityNodeField> properties) {
        this.properties = properties;
    }

    @Override
    public @NotNull String getName() {
        return DomSupport.getValue(this.entity.getName(), "");
    }

    @Override
    public Icon getIcon() {
        return MedIcons.ENTITY;
    }

    @Override
    public Object getSource() {
        return this.entity;
    }

    @Override
    public String getDescription() {
        return DomSupport.getValue(this.entity.getDescription(), "");
    }

    public @NotNull List<MedEntityNodeField> getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedEntityNodeData nodeData = (MedEntityNodeData) o;
        return entity.equals(nodeData.entity) && Objects.equals(properties, nodeData.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, properties);
    }
}
