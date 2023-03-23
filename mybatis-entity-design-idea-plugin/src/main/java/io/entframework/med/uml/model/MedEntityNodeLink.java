/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml.model;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class MedEntityNodeLink {

    private final MedEntityNodeLinkType type;

    private final MedEntityNodeData fromEntity;

    private final MedEntityNodeData toEntity;

    public MedEntityNodeLink(@NotNull MedEntityNodeLinkType type, @NotNull MedEntityNodeData fromEntity,
                             @NotNull MedEntityNodeData toEntity) {
        this.type = type;
        this.fromEntity = fromEntity;
        this.toEntity = toEntity;
    }

    public @NotNull MedEntityNodeLinkType getType() {
        return type;
    }

    public @NotNull MedEntityNodeData getFromEntity() {
        return fromEntity;
    }

    public @NotNull MedEntityNodeData getToEntity() {
        return toEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MedEntityNodeLink that = (MedEntityNodeLink) o;
        return type == that.type && fromEntity.equals(that.fromEntity) && toEntity.equals(that.toEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, fromEntity, toEntity);
    }

}
