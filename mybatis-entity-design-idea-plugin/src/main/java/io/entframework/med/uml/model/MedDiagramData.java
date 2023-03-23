/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml.model;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public final class MedDiagramData {

    private Collection<MedEntityNodeData> entities;

    private Collection<MedEnumNodeData> enums;

    private Collection<MedEntityNodeLink> entityLinks;

    private Collection<MedEnumNodeLink> enumLinks;

    public MedDiagramData() {
        this.entities = Collections.emptyList();
        this.enums = Collections.emptyList();
        this.entityLinks = Collections.emptyList();
        this.enumLinks = Collections.emptyList();
    }

    public MedDiagramData(@NotNull Collection<MedEntityNodeData> entities, @NotNull Collection<MedEnumNodeData> enums,
                          @NotNull Collection<MedEntityNodeLink> entityLinks, @NotNull Collection<MedEnumNodeLink> enumLinks) {
        this.entities = entities;
        this.enums = enums;
        this.entityLinks = entityLinks;
        this.enumLinks = enumLinks;
    }

    public @NotNull Collection<MedEntityNodeData> getEntities() {
        return entities;
    }

    public @NotNull Collection<MedEnumNodeData> getEnums() {
        return enums;
    }

    public @NotNull Collection<MedEntityNodeLink> getEntityLinks() {
        return entityLinks;
    }

    public @NotNull Collection<MedEnumNodeLink> getEnumLinks() {
        return enumLinks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MedDiagramData that = (MedDiagramData) o;
        return entities.equals(that.entities) && enums.equals(that.enums) && entityLinks.equals(that.entityLinks)
                && enumLinks.equals(that.enumLinks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entities, enums, entityLinks, enumLinks);
    }

}
