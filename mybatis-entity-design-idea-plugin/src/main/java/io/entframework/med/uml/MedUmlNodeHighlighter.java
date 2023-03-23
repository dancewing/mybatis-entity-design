/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml;

import com.intellij.diagram.DiagramBuilder;
import com.intellij.diagram.DiagramDataModel;
import com.intellij.diagram.DiagramNode;
import com.intellij.diagram.extras.UmlNodeHighlighter;
import com.intellij.diagram.util.DiagramSelectionService;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import io.entframework.med.dom.MyDomElement;
import io.entframework.med.uml.model.MedNodeData;
import io.entframework.med.view.properties.PropertyManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MedUmlNodeHighlighter implements UmlNodeHighlighter<MedNodeData> {

    private static final Logger log = Logger.getInstance(MedUmlNodeHighlighter.class);

    @Override
    public @NotNull List<DiagramNode<MedNodeData>> onNodeSelected(@NotNull List<DiagramNode<MedNodeData>> list,
                                                                  @NotNull DiagramDataModel<MedNodeData> diagramDataModel) {
        log.info("onNodeSelected");
        return list;
    }

    @Override
    public void selectionChanged(@NotNull DiagramBuilder diagramBuilder) {
        Project project = diagramBuilder.getProject();
        List<DiagramNode<?>> selectedNodes = DiagramSelectionService.getInstance().getSelectedNodes(diagramBuilder);
        if (selectedNodes.size() == 1) {
            DiagramNode<?> diagramNode = selectedNodes.get(0);
            if (diagramNode.getIdentifyingElement() instanceof MedNodeData nodeData) {
                if (nodeData.getSource() instanceof MyDomElement baseElement) {
                    project.getService(PropertyManager.class).treeNodeSelected(baseElement);
                }
            }
        }
        log.info("selectionChanged");
    }

}
