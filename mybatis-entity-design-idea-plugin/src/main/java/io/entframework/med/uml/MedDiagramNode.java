/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml;

import com.intellij.diagram.DiagramNode;
import com.intellij.diagram.DiagramProvider;
import com.intellij.openapi.util.UserDataHolderBase;
import io.entframework.med.uml.model.MedNodeData;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

final class MedDiagramNode extends UserDataHolderBase implements DiagramNode<MedNodeData> {

    private final MedNodeData data;

    private final DiagramProvider<MedNodeData> provider;

    public MedDiagramNode(MedNodeData data, DiagramProvider<MedNodeData> provider) {
        this.data = data;
        this.provider = provider;
    }

    @Override
    public @NotNull MedNodeData getIdentifyingElement() {
        return data;
    }

    @Override
    public @NotNull DiagramProvider<MedNodeData> getProvider() {
        return provider;
    }

    @Override
    public @Nullable @Nls String getTooltip() {
        return null;
    }

    @Override
    public @Nullable Icon getIcon() {
        return data.getIcon();
    }
}
