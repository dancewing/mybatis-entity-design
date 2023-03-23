/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.instance;

import com.intellij.openapi.options.ConfigurableBase;
import com.intellij.openapi.project.Project;
import io.entframework.med.configurable.SettingManager;
import io.entframework.med.configurable.model.Variables;
import io.entframework.med.configurable.ui.VariableUI;
import org.jetbrains.annotations.NotNull;

/**
 * 变量配置
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/03/17
 */
public class VariablesConfigurable extends ConfigurableBase<VariableUI, Variables> {

    @NotNull
    private final Project myProject;

    public VariablesConfigurable(@NotNull Project myProject) {
        super("Mybatis.Med.Variables", "Variables", "");
        this.myProject = myProject;
    }

    @Override
    protected VariableUI createUi() {
        return new VariableUI(myProject);
    }

    @Override
    protected @NotNull Variables getSettings() {
        return this.myProject.getService(SettingManager.class).getVariables();
    }

}
