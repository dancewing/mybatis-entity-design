/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml;

import com.intellij.diagram.DiagramProvider;
import com.intellij.openapi.graph.view.Graph2D;
import com.intellij.openapi.graph.view.Graph2DSelectionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.uml.presentation.DiagramPresentationModelImpl;
import org.jetbrains.annotations.NotNull;

public class MedDiagramPresentationModel extends DiagramPresentationModelImpl {

    public MedDiagramPresentationModel(@NotNull Graph2D graph, @NotNull Project project,
                                       @NotNull DiagramProvider<?> provider) {
        super(graph, project, provider);
    }

    @Override
    public void onSelectionChanged(@NotNull Graph2DSelectionEvent e) {
        super.onSelectionChanged(e);
    }

}
