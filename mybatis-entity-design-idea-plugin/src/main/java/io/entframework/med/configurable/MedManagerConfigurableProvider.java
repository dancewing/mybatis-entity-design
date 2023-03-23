/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurableProvider;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MedManagerConfigurableProvider extends ConfigurableProvider {

    @NotNull
    private final Project myProject;

    public MedManagerConfigurableProvider(@NotNull Project myProject) {
        this.myProject = myProject;
    }

    @Override
    public @Nullable Configurable createConfigurable() {
        return new MedManagerConfigurable(myProject);
    }

}
