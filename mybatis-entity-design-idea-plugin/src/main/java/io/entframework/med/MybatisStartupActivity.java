/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.fileEditor.ex.FileEditorWithProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.messages.MessageBusConnection;
import io.entframework.med.language.MedXmlFileType;
import io.entframework.med.util.MedFileUtil;
import io.entframework.med.view.MybatisBuilderToolWindowFactory;
import io.entframework.med.view.MybatisToolwindowView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MybatisStartupActivity implements StartupActivity, Disposable {

    private static final Logger log = Logger.getInstance(MybatisStartupActivity.class);

    private MessageBusConnection connection;

    @Override
    public void runActivity(@NotNull Project project) {

        connection = project.getMessageBus().connect();
        connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListener() {
            @Override
            public void fileOpenedSync(@NotNull FileEditorManager source, @NotNull VirtualFile file,
                                       @NotNull List<FileEditorWithProvider> editorsWithProviders) {
                log.info("fileOpenedSync");
            }

            @Override
            public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                log.info("fileOpened");

                PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
                if (psiFile instanceof XmlFile && file.getFileType() == MedXmlFileType.INSTANCE) {
                    updateStructureUI(project);
                } else {
                    resetStructureUI(project);
                }
            }

            @Override
            public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                log.info("fileClosed");
                VirtualFile currentOpened = MedFileUtil.getCurrentOpened(project);
                if (currentOpened == null || currentOpened.equals(file)) {
                    PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
                    if (psiFile instanceof XmlFile && file.getFileType() == MedXmlFileType.INSTANCE) {
                        resetStructureUI(project);
                    }
                }
            }

            @Override
            public void selectionChanged(@NotNull FileEditorManagerEvent event) {
                log.info("selectionChanged");
                if (event.getNewFile() != null && event.getOldFile() != null) {
                    VirtualFile file = event.getNewFile();
                    PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
                    if (psiFile instanceof XmlFile && file.getFileType() == MedXmlFileType.INSTANCE) {
                        updateStructureUI(project);
                    }
                }
            }
        });

        FileEditor fileEditor = FileEditorManager.getInstance(project).getSelectedEditor();
        if (fileEditor != null) {
            VirtualFile file = fileEditor.getFile();
            PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
            if (psiFile instanceof XmlFile && file.getFileType() == MedXmlFileType.INSTANCE) {
                log.info("Med File Opened");
                updateStructureUI(project);
            }
        }
    }

    private void updateStructureUI(@NotNull Project project) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow toolWindow = toolWindowManager.getToolWindow(MybatisBuilderToolWindowFactory.TOOL_WINDOW_ID);
        if (toolWindow != null) {
            if (!toolWindow.isVisible()) {
                toolWindow.show();
            }
        }
        MybatisToolwindowView mybatisToolwindowView = project.getService(MybatisToolwindowView.class);
        if (mybatisToolwindowView != null) {
            mybatisToolwindowView.updateStructureUI();
        }

    }

    private void resetStructureUI(@NotNull Project project) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow toolWindow = toolWindowManager.getToolWindow(MybatisBuilderToolWindowFactory.TOOL_WINDOW_ID);
        if (toolWindow != null) {
            MybatisToolwindowView mybatisToolwindowView = project.getService(MybatisToolwindowView.class);
            if (mybatisToolwindowView != null) {
                mybatisToolwindowView.resetStructureUI();
            }
        }
    }

    @Override
    public void dispose() {
        if (connection != null) {
            Disposer.dispose(connection);
        }
    }

}
