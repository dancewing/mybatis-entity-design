/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml;

import com.intellij.diagram.DiagramDataModel;
import com.intellij.diagram.DiagramEdge;
import com.intellij.diagram.DiagramNode;
import com.intellij.diagram.DiagramProvider;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.psi.xml.XmlFile;
import io.entframework.med.dom.DomMed;
import io.entframework.med.language.MedXMLLanguage;
import io.entframework.med.uml.model.*;
import io.entframework.med.util.DomSupport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

final class MedUmlDataModel extends DiagramDataModel<MedNodeData> {

    private static final Logger log = Logger.getInstance(MedUmlDataModel.class);

    private final List<MedDiagramNode> nodes = new ArrayList<>();

    private final List<DiagramEdge<MedNodeData>> edges = new ArrayList<>();

    private final MedNodeData seedData;

    private MedDiagramData diagramData;

    public MedUmlDataModel(@NotNull Project project, @NotNull DiagramProvider<MedNodeData> provider,
                           @Nullable MedNodeData seedData) {
        super(project, provider);
        this.seedData = seedData;
    }

    @Override
    public @NotNull ModificationTracker getModificationTracker() {
        var psiModificationTracker = PsiModificationTracker.getInstance(getProject());
        return psiModificationTracker.forLanguage(MedXMLLanguage.INSTANCE);
    }

    @Override
    public @NotNull Collection<? extends DiagramNode<MedNodeData>> getNodes() {
        return nodes;
    }

    @Override
    public @NotNull String getNodeName(@NotNull DiagramNode<MedNodeData> diagramNode) {
        return diagramNode.getIdentifyingElement().getName();
    }

    @Override
    public @Nullable DiagramNode<MedNodeData> addElement(@Nullable MedNodeData data) {
        if (data == null)
            return null;

        if (data instanceof MedDiagramRootData medDiagramRootData) {
            this.diagramData = MedUmlDataModel.extractData(getProject(), medDiagramRootData.getVirtualFile());

            var entityMapping = new HashMap<MedEntityNodeData, DiagramNode<MedNodeData>>();
            var enumMapping = new HashMap<MedEnumNodeData, DiagramNode<MedNodeData>>();

            for (var entity : diagramData.getEntities()) {
                entityMapping.put(entity, addElement(entity));
            }
            for (var enumeration : diagramData.getEnums()) {
                enumMapping.put(enumeration, addElement(enumeration));
            }

            for (MedEntityNodeLink entityLink : diagramData.getEntityLinks()) {
                var from = entityMapping.get(entityLink.getFromEntity());
                var to = entityMapping.get(entityLink.getToEntity());
                edges.add(new MedDiagramEntityEdge(from, to, entityLink.getType()));
            }

            for (MedEnumNodeLink enumLink : diagramData.getEnumLinks()) {
                var from = entityMapping.get(enumLink.getEntity());
                var to = enumMapping.get(enumLink.getEnumeration());
                edges.add(new MedDiagramEnumEdge(from, to));
            }

            return null;
        }

        var node = new MedDiagramNode(data, getProvider());
        this.nodes.add(node);
        return node;
    }

    @Override
    public @NotNull Collection<? extends DiagramEdge<MedNodeData>> getEdges() {
        return edges;
    }

    @Override
    public void dispose() {
    }

    public static @NotNull MedDiagramData extractData(@NotNull Project project, @Nullable VirtualFile file) {
        if (file == null)
            return new MedDiagramData(List.of(), List.of(), List.of(), List.of());

        var psiFile = PsiManager.getInstance(project).findFile(file);
        if (!(psiFile instanceof XmlFile))
            return new MedDiagramData(List.of(), List.of(), List.of(), List.of());

        return extractData((XmlFile) psiFile);
    }

    public static @NotNull MedDiagramData extractData(@NotNull XmlFile file) {

        DomMed domMed = DomSupport.getMed(file);
        MedDiagramData result = null;
        if (domMed != null) {
            result = DomSupport.convertToDiagramData(domMed);
        }
        return Objects.requireNonNullElseGet(result, MedDiagramData::new);
    }

    @Override
    public void refreshDataModel() {
        if (seedData instanceof MedDiagramRootData medDiagramRootData) {
            MedDiagramData newDiagramData = MedUmlDataModel.extractData(getProject(),
                    medDiagramRootData.getVirtualFile());

            if (newDiagramData.equals(diagramData))
                return; // nothing changed

            removeAll();

            nodes.clear();
            edges.clear();

            addElement(seedData);
        }
    }

    @Override
    public void expandNode(@NotNull DiagramNode<MedNodeData> node) {
        super.expandNode(node);
    }

}
