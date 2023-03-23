/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ProjectContainer {

    protected Project myProject;

    public ProjectContainer(@NotNull Project project) {
        this.myProject = project;
    }

}
