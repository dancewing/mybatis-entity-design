/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml;

import com.intellij.diagram.DiagramEdgeBase;
import com.intellij.diagram.DiagramNode;
import com.intellij.diagram.DiagramRelationshipInfo;
import com.intellij.diagram.DiagramRelationshipInfoAdapter;
import com.intellij.diagram.presentation.DiagramLineType;
import io.entframework.med.uml.model.MedEntityNodeLinkType;
import io.entframework.med.uml.model.MedNodeData;
import org.jetbrains.annotations.NotNull;

final class MedDiagramEntityEdge extends DiagramEdgeBase<MedNodeData> {

    public MedDiagramEntityEdge(@NotNull DiagramNode<MedNodeData> source, @NotNull DiagramNode<MedNodeData> target,
                                @NotNull MedEntityNodeLinkType linkType) {
        super(source, target, toRelationshipInfo(linkType));
    }

    private static DiagramRelationshipInfo toRelationshipInfo(MedEntityNodeLinkType linkType) {
        switch (linkType) {
            case ONE_TO_ONE:
                return MedDiagramEntityEdge.ONE_TO_ONE;
            case MANY_TO_MANY:
                return MedDiagramEntityEdge.MANY_TO_MANY;
            case MANY_TO_ONE:
                return MedDiagramEntityEdge.MANY_TO_ONE;
            case ONE_TO_MANY:
                return MedDiagramEntityEdge.ONE_TO_MANY;
        }
        return DiagramRelationshipInfo.NO_RELATIONSHIP;
    }

    static final DiagramRelationshipInfo ONE_TO_ONE = (new DiagramRelationshipInfoAdapter.Builder()).setName("TO_ONE")
            .setLineType(DiagramLineType.SOLID)
            .setSourceArrow(DiagramRelationshipInfo.ANGLE)
            .setTargetArrow(DiagramRelationshipInfo.ANGLE)
            .setUpperTargetLabel("1")
            .setUpperSourceLabel("1")
            .create();

    static final DiagramRelationshipInfo ONE_TO_MANY = (new DiagramRelationshipInfoAdapter.Builder()).setName("TO_MANY")
            .setLineType(DiagramLineType.SOLID)
            .setSourceArrow(DiagramRelationshipInfo.ANGLE)
            .setTargetArrow(DiagramRelationshipInfo.DIAMOND)
            .setUpperTargetLabel("1")
            .setUpperSourceLabel("*")
            .create();

    static final DiagramRelationshipInfo MANY_TO_ONE = (new DiagramRelationshipInfoAdapter.Builder()).setName("TO_MANY")
            .setLineType(DiagramLineType.SOLID)
            .setSourceArrow(DiagramRelationshipInfo.DIAMOND)
            .setTargetArrow(DiagramRelationshipInfo.ANGLE)
            .setUpperTargetLabel("*")
            .setUpperSourceLabel("1")
            .create();

    static final DiagramRelationshipInfo MANY_TO_MANY = (new DiagramRelationshipInfoAdapter.Builder())
            .setName("TO_MANY")
            .setLineType(DiagramLineType.SOLID)
            .setSourceArrow(DiagramRelationshipInfo.DIAMOND)
            .setTargetArrow(DiagramRelationshipInfo.DIAMOND)
            .setUpperTargetLabel("*")
            .setUpperSourceLabel("*")
            .create();

}
