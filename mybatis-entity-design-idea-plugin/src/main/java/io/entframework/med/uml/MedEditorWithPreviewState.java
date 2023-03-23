/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml;

import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import org.jetbrains.annotations.NotNull;

final class MedEditorWithPreviewState implements FileEditorState {

    private final FileEditorState underlyingState;

    public MedEditorWithPreviewState(FileEditorState underlyingState) {
        this.underlyingState = underlyingState;
    }

    @Override
    public boolean canBeMergedWith(@NotNull FileEditorState otherState, @NotNull FileEditorStateLevel level) {
        return otherState instanceof MedEditorWithPreviewState
                && ((MedEditorWithPreviewState) otherState).underlyingState.canBeMergedWith(underlyingState, level);
    }

    public FileEditorState getUnderlyingState() {
        return underlyingState;
    }

}
