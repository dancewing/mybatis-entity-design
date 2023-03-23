/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.DialogWrapperPeer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.function.Function;

public abstract class ProjectDialogWrapper extends DialogWrapper {

    protected Project myProject;

    public ProjectDialogWrapper(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
        myProject = project;
    }

    public ProjectDialogWrapper(@Nullable Project project, boolean canBeParent,
                                @NotNull IdeModalityType ideModalityType) {
        super(project, canBeParent, ideModalityType);
        myProject = project;
    }

    public ProjectDialogWrapper(@Nullable Project project, @Nullable Component parentComponent, boolean canBeParent,
                                @NotNull IdeModalityType ideModalityType) {
        super(project, parentComponent, canBeParent, ideModalityType);
        myProject = project;
    }

    public ProjectDialogWrapper(@Nullable Project project, @Nullable Component parentComponent, boolean canBeParent,
                                @NotNull IdeModalityType ideModalityType, boolean createSouth) {
        super(project, parentComponent, canBeParent, ideModalityType, createSouth);
        myProject = project;
    }

    public ProjectDialogWrapper(@Nullable Project project) {
        super(project);
        myProject = project;
    }

    public ProjectDialogWrapper(boolean canBeParent) {
        super(canBeParent);
    }

    public ProjectDialogWrapper(boolean canBeParent, boolean applicationModalIfPossible) {
        super(canBeParent, applicationModalIfPossible);
    }

    public ProjectDialogWrapper(Project project, boolean canBeParent, boolean applicationModalIfPossible) {
        super(project, canBeParent, applicationModalIfPossible);
        myProject = project;
    }

    public ProjectDialogWrapper(@NotNull Component parent, boolean canBeParent) {
        super(parent, canBeParent);
    }

    public ProjectDialogWrapper(@NotNull Function<DialogWrapper, DialogWrapperPeer> peerFactory) {
        super(peerFactory);
    }

    public void init() {
        super.init();
    }

}
