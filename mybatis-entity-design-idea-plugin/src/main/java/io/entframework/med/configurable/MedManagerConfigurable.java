/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ex.SortedConfigurableGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.VcsBundle;
import io.entframework.med.configurable.instance.ReverseSettingsConfigurable;
import io.entframework.med.configurable.instance.TemplatesConfigurable;
import io.entframework.med.configurable.instance.VariablesConfigurable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class MedManagerConfigurable extends SortedConfigurableGroup {

    private static final String ID = "mybatis.med.propSupport";

    private static final int GROUP_WEIGHT = 45;

    @NotNull
    private final Project myProject;

    public MedManagerConfigurable(@NotNull Project project) {
        super(ID, VcsBundle.message("version.control.main.configurable.name"),
                VcsBundle.message("version.control.main.configurable.description"), null, GROUP_WEIGHT);
        myProject = project;
    }

    @Override
    protected Configurable[] buildConfigurables() {
        List<Configurable> result = new ArrayList<>();

        result.add(new ReverseSettingsConfigurable(this.myProject));
        result.add(new VariablesConfigurable(this.myProject));
        result.add(new TemplatesConfigurable(this.myProject));

        return result.toArray(new Configurable[0]);
    }

}