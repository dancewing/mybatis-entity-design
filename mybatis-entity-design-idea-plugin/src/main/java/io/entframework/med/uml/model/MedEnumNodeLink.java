/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml.model;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class MedEnumNodeLink {

    private final MedEntityNodeData entity;

    private final MedEnumNodeData enumeration;

    public MedEnumNodeLink(@NotNull MedEntityNodeData entity, @NotNull MedEnumNodeData enumeration) {
        this.entity = entity;
        this.enumeration = enumeration;
    }

    public @NotNull MedEntityNodeData getEntity() {
        return entity;
    }

    public @NotNull MedEnumNodeData getEnumeration() {
        return enumeration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MedEnumNodeLink that = (MedEnumNodeLink) o;
        return entity.equals(that.entity) && enumeration.equals(that.enumeration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, enumeration);
    }

}
