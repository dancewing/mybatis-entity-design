/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.instance;

import com.intellij.openapi.options.ConfigurableBase;
import com.intellij.openapi.project.Project;
import io.entframework.med.configurable.SettingManager;
import io.entframework.med.configurable.model.Templates;
import io.entframework.med.configurable.ui.TemplatesUI;
import org.jetbrains.annotations.NotNull;

/**
 * 模版配置
 *
 * @author hehaiyangwork@gmail.com
 * @date : 2017/30/17
 */
public class TemplatesConfigurable extends ConfigurableBase<TemplatesUI, Templates> {

    @NotNull
    private final Project myProject;

    public TemplatesConfigurable(@NotNull Project project) {
        super("Mybatis.Med.Templates", "Templates", "");
        this.myProject = project;
    }

    @Override
    protected TemplatesUI createUi() {
        return new TemplatesUI(myProject);
    }

    @Override
    protected @NotNull Templates getSettings() {
        return this.myProject.getService(SettingManager.class).getTemplates();
    }

}
