/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml;

import com.intellij.diagram.DiagramBuilder;
import com.intellij.diagram.DiagramBuilderFactory;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.graph.GraphDataKeys;
import com.intellij.openapi.graph.services.GraphLayoutService;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.JBColor;
import com.intellij.uml.UmlFileEditorImpl;
import com.intellij.uml.components.UmlGraphZoomableViewport;
import com.intellij.util.ui.JBUI;
import io.entframework.med.MedBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static com.intellij.openapi.actionSystem.PlatformCoreDataKeys.BGT_DATA_PROVIDER;
import static com.intellij.openapi.graph.services.GraphLayoutService.GraphLayoutQueryParams.FitContentOption.AFTER;
import static io.entframework.med.uml.MedUmlElementManager.getRootData;

@SuppressWarnings("UnstableApiUsage")
final class MedDiagramPanel implements Disposable {

    private static final Logger log = Logger.getInstance(MedDiagramPanel.class);

    @Nullable
    private DiagramBuilder builder;

    private final MyPanel chartPanel = new MyPanel();

    private final MedUmlProvider umlProvider = new MedUmlProvider();

    private final MedPreviewFileEditor fileEditor;

    public MedDiagramPanel(MedPreviewFileEditor fileEditor) {
        this.fileEditor = fileEditor;
    }

    @Override
    public void dispose() {
    }

    JComponent getComponent() {
        return chartPanel;
    }

    public void draw() {
        DumbService dumbService = DumbService.getInstance(fileEditor.getProject());
        if (dumbService.isDumb()) {
            dumbService.showDumbModeNotification(MedBundle.message("codegen.plugin.is.not.available.during.indexing"));
            return;
        }

        if (builder == null) {
            Project project = fileEditor.getProject();
            VirtualFile virtualFile = fileEditor.getFile();

            builder = DiagramBuilderFactory.getInstance()
                    .create(project, umlProvider, getRootData(project, virtualFile), null);
            Disposer.register(this, builder);
            builder.getView().setFitContentOnResize(true);
            JComponent graphView = createSimpleGraphView(builder);
            chartPanel.add(graphView, BorderLayout.CENTER);

            var actionsProvider = builder.getProvider().getExtras().getToolbarActionsProvider();
            var actionGroup = actionsProvider.createToolbarActions(builder);
            var actionToolbar = ActionManager.getInstance().createActionToolbar("MED.UML", actionGroup, true);
            actionToolbar.setTargetComponent(graphView);
            actionToolbar.getComponent().setBorder(JBUI.Borders.customLine(JBColor.border(), 0, 0, 1, 0));

            chartPanel.add(actionToolbar.getComponent(), BorderLayout.NORTH);

            builder.queryUpdate().withDataReload().withPresentationUpdate().withRelayout().runAsync();
        }
    }

    private JComponent createSimpleGraphView(@NotNull DiagramBuilder builder) {
        builder.getPresentationModel().registerActions();

        var view = builder.getView();
        GraphDataKeys.addDataProvider(view, chartPanel);

        view.getCanvasComponent().setBackground(JBColor.GRAY);
        GraphLayoutService.getInstance().queryLayout(builder.getGraphBuilder()).withFitContent(AFTER).run();
        return new UmlGraphZoomableViewport(builder);
    }

    private class MyPanel extends JPanel implements DataProvider {

        public MyPanel() {
            super(new BorderLayout());
        }

        @Override
        public @Nullable Object getData(@NotNull @NonNls String dataId) {
            if (builder == null)
                return null;

            if (CommonDataKeys.PROJECT.is(dataId)) {
                return fileEditor != null ? fileEditor.getProject() : null;
            }

            if (CommonDataKeys.VIRTUAL_FILE.getName().equals(dataId)) {
                return fileEditor != null ? fileEditor.getFile() : null;
            }

            if (GraphDataKeys.GRAPH_BUILDER.getName().equals(dataId)) {
                return builder;
            }
            if (GraphDataKeys.GRAPH.getName().equals(dataId)) {
                return builder.getGraph();
            }

            if (BGT_DATA_PROVIDER.is(dataId)) {
                return (DataProvider) this::getSlowData;
            }

            return UmlFileEditorImpl.getData(dataId, builder);
        }

        private @Nullable Object getSlowData(@NotNull @NonNls String dataId) {

            if (CommonDataKeys.PSI_FILE.is(dataId)) {
                if (fileEditor == null) {
                    return null;
                }
                return PsiManager.getInstance(fileEditor.getProject()).findFile(fileEditor.getFile());
            }

            return null;
        }

    }

}
