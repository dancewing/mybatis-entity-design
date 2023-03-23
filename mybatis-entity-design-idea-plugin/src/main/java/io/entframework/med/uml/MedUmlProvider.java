/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml;

import com.intellij.diagram.*;
import com.intellij.diagram.extras.DiagramExtras;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import io.entframework.med.uml.model.MedNodeData;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.diagram.DiagramRelationshipManager.NO_RELATIONSHIP_MANAGER;

final class MedUmlProvider extends DiagramProvider<MedNodeData> {

    private final DiagramVfsResolver<MedNodeData> vfsResolver = new MedUmlVfsResolver();

    private final DiagramElementManager<MedNodeData> elementManager = new MedUmlElementManager();

    private final MedDiagramExtras diagramExtras = new MedDiagramExtras();

    @Pattern("[a-zA-Z0-9_-]*")
    @Override
    public @NotNull String getID() {
        return "EntMed";
    }

    @SuppressWarnings("DialogTitleCapitalization")
    @Override
    public @NotNull String getPresentableName() {
        return "Mybatis Entities";
    }

    public MedUmlProvider() {
        this.elementManager.setUmlProvider(this);
    }

    @Override
    public @NotNull DiagramDataModel<MedNodeData> createDataModel(@NotNull Project project,
                                                                  @Nullable MedNodeData seedData, @Nullable VirtualFile umlVirtualFile,
                                                                  @NotNull DiagramPresentationModel diagramPresentationModel) {
        var model = new MedUmlDataModel(project, this, seedData);
        if (seedData != null) {
            model.addElement(seedData);
        }
        return model;
    }

    @Override
    public @NotNull DiagramVisibilityManager createVisibilityManager() {
        return EmptyDiagramVisibilityManager.INSTANCE;
    }

    @Override
    public @NotNull DiagramElementManager<MedNodeData> getElementManager() {
        return elementManager;
    }

    @Override
    public @NotNull DiagramVfsResolver<MedNodeData> getVfsResolver() {
        return vfsResolver;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull DiagramRelationshipManager<MedNodeData> getRelationshipManager() {
        return (DiagramRelationshipManager<MedNodeData>) NO_RELATIONSHIP_MANAGER;
    }

    @Override
    public @NotNull DiagramNodeContentManager createNodeContentManager() {
        return new MedUmlCategoryManager();
    }

    @Override
    public @NotNull DiagramExtras<MedNodeData> getExtras() {
        return diagramExtras;
    }

}
