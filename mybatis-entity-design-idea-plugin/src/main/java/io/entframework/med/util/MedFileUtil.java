/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.util;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public final class MedFileUtil {

    public static VirtualFile getCurrentOpened(@NotNull Project project) {
        FileEditor fileEditor = FileEditorManager.getInstance(project).getSelectedEditor();
        if (fileEditor != null) {
            return fileEditor.getFile();
        }
        return null;
    }

}
