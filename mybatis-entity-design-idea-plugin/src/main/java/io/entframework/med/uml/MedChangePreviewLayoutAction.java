/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.fileEditor.TextEditorWithPreview;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

import static io.entframework.med.uml.MedEditorWithPreview.findSplitEditor;

abstract class MedChangePreviewLayoutAction extends ToggleAction implements DumbAware {

    private final TextEditorWithPreview.Layout layout;

    MedChangePreviewLayoutAction(TextEditorWithPreview.Layout layout) {
        super(layout.getName(), layout.getName(), layout.getIcon(null));
        this.layout = layout;
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        var editor = findSplitEditor(e);
        if (editor == null)
            return false;

        return editor.getLayout() == layout;
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        var editor = findSplitEditor(e);
        if (editor == null)
            return;

        editor.setLayout(layout);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);

        var editor = findSplitEditor(e);
        if (editor == null)
            return;
        e.getPresentation().setIcon(layout.getIcon(editor));
    }

    static class EditorOnly extends MedChangePreviewLayoutAction {

        EditorOnly() {
            super(TextEditorWithPreview.Layout.SHOW_EDITOR);
        }

    }

    static class EditorAndPreview extends MedChangePreviewLayoutAction {

        EditorAndPreview() {
            super(TextEditorWithPreview.Layout.SHOW_EDITOR_AND_PREVIEW);
        }

    }

    static class PreviewOnly extends MedChangePreviewLayoutAction {

        PreviewOnly() {
            super(TextEditorWithPreview.Layout.SHOW_PREVIEW);
        }

    }

}
