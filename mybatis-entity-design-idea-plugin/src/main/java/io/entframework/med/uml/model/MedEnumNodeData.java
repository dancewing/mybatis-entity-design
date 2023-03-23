/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml.model;

import io.entframework.med.MedIcons;
import io.entframework.med.dom.DomEnum;
import io.entframework.med.util.DomSupport;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

public final class MedEnumNodeData implements MedNodeData {
    private final DomEnum domEnum;
    private List<MedEnumNodeItem> options;

    public MedEnumNodeData(@NotNull DomEnum domEnum) {
        this.domEnum = domEnum;
    }

    @Override
    public @NotNull String getName() {
        return DomSupport.getValue(this.domEnum.getName(), "");
    }

    @Override
    public Icon getIcon() {
        return MedIcons.ENUM;
    }

    @Override
    public String getDescription() {
        return DomSupport.getValue(this.domEnum.getDescription(), "");
    }

    @Override
    public Object getSource() {
        return domEnum;
    }

    public @NotNull List<@NotNull MedEnumNodeItem> getOptions() {
        return options;
    }

    public void setOptions(List<MedEnumNodeItem> options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedEnumNodeData that = (MedEnumNodeData) o;
        return domEnum.equals(that.domEnum) && Objects.equals(options, that.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domEnum, options);
    }
}
