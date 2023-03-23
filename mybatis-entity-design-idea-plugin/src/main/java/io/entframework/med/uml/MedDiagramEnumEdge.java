/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml;

import com.intellij.diagram.DiagramEdgeBase;
import com.intellij.diagram.DiagramNode;
import com.intellij.diagram.DiagramRelationshipInfo;
import com.intellij.diagram.DiagramRelationshipInfoAdapter;
import com.intellij.diagram.presentation.DiagramLineType;
import io.entframework.med.uml.model.MedNodeData;
import org.jetbrains.annotations.NotNull;

final class MedDiagramEnumEdge extends DiagramEdgeBase<MedNodeData> {

    public MedDiagramEnumEdge(@NotNull DiagramNode<MedNodeData> source, @NotNull DiagramNode<MedNodeData> target) {
        super(source, target, USE_ENUM);
    }

    static final DiagramRelationshipInfo USE_ENUM = (new DiagramRelationshipInfoAdapter.Builder()).setName("TO_ONE")
            .setLineType(DiagramLineType.DASHED)
            .setSourceArrow(DiagramRelationshipInfo.NONE)
            .setTargetArrow(DiagramRelationshipInfo.ANGLE)
            .create();

}
