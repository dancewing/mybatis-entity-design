/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.instance;

import com.intellij.openapi.options.ConfigurableBase;
import com.intellij.openapi.project.Project;
import io.entframework.med.configurable.SettingManager;
import io.entframework.med.configurable.model.ReverseSettings;
import io.entframework.med.configurable.ui.ReverseUI;
import org.jetbrains.annotations.NotNull;

public class ReverseSettingsConfigurable extends ConfigurableBase<ReverseUI, ReverseSettings> {

    @NotNull
    private final Project myProject;

    public ReverseSettingsConfigurable(@NotNull Project myProject) {
        super("Mybatis.Med.ReverseSettings", "Reverse Settings", "");
        this.myProject = myProject;
    }

    @Override
    protected @NotNull ReverseSettings getSettings() {
        SettingManager settingManager = myProject.getService(SettingManager.class);
        return settingManager.getReverseSettings();
    }

    @Override
    protected ReverseUI createUi() {
        return new ReverseUI(myProject);
    }

}
