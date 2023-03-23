/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/
package io.entframework.med.uml;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.ActionUtil;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class MedEditorWithPreview extends TextEditorWithPreview {

    public static final Key<MedEditorWithPreview> PARENT_SPLIT_EDITOR_KEY = Key.create("med.parentSplit");

    public MedEditorWithPreview(@NotNull TextEditor editor, @NotNull MedPreviewFileEditor preview) {
        super(editor, preview, "MED Preview", Layout.SHOW_EDITOR_AND_PREVIEW, false);

        editor.putUserData(PARENT_SPLIT_EDITOR_KEY, this);
        preview.putUserData(PARENT_SPLIT_EDITOR_KEY, this);

        preview.setMainEditor(editor.getEditor());

        // todo subscribe to settings change
    }

    @Override
    protected void onLayoutChange(Layout oldValue, Layout newValue) {
        super.onLayoutChange(oldValue, newValue);
        // Editor tab will lose focus after switching to JCEF preview for some reason.
        // So we should explicitly request focus for our editor here.
        if (newValue == Layout.SHOW_PREVIEW) {
            requestFocusForPreview();
        }
    }

    private void requestFocusForPreview() {
        final var preferredComponent = myPreview.getPreferredFocusedComponent();
        if (preferredComponent != null) {
            preferredComponent.requestFocus();
            return;
        }
        myPreview.getComponent().requestFocus();
    }

    @Override
    public void setLayout(@NotNull Layout layout) {
        super.setLayout(layout);
    }

    @Override
    public void setState(@NotNull FileEditorState state) {
        if (state instanceof MedEditorWithPreviewState actualState) {
            super.setState(actualState.getUnderlyingState());
        }
    }

    @Override
    public @NotNull FileEditorState getState(@NotNull FileEditorStateLevel level) {
        final var underlyingState = super.getState(level);
        return new MedEditorWithPreviewState(underlyingState);
    }

    @Override
    protected @NotNull ToggleAction getShowEditorAction() {
        return (ToggleAction) Objects.requireNonNull(ActionUtil.getAction("MED.Layout.EditorOnly"));
    }

    @Override
    protected @NotNull ToggleAction getShowEditorAndPreviewAction() {
        return (ToggleAction) Objects.requireNonNull(ActionUtil.getAction("MED.Layout.EditorAndPreview"));
    }

    @Override
    protected @NotNull ToggleAction getShowPreviewAction() {
        return (ToggleAction) Objects.requireNonNull(ActionUtil.getAction("MED.Layout.PreviewOnly"));
    }

    static @Nullable MedEditorWithPreview findSplitEditor(AnActionEvent event) {
        FileEditor fileEditor = event.getData(PlatformCoreDataKeys.FILE_EDITOR);
        if (fileEditor == null)
            return null;

        if (fileEditor instanceof MedEditorWithPreview medEditorWithPreview) {
            return medEditorWithPreview;
        }

        return fileEditor.getUserData(PARENT_SPLIT_EDITOR_KEY);
    }

    @NotNull
    protected ActionGroup createViewActionGroup() {
        return new DefaultActionGroup(getShowEditorAction(), getShowEditorAndPreviewAction(), getShowPreviewAction());
    }

}
