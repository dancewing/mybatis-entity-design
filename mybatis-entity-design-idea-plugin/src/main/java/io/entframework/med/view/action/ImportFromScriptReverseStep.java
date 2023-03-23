/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.action;

import com.intellij.openapi.project.Project;
import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import io.entframework.med.MedBundle;
import io.entframework.med.configurable.SettingManager;
import io.entframework.med.view.ReverseForm;
import io.entframework.med.view.ReverseFormSettings;

import javax.swing.*;

public class ImportFromScriptReverseStep extends WizardStep<ImportFromScriptWizardModel> {

    private ReverseForm reverseForm;

    private Project myProject;

    private WizardNavigationState state;

    private ImportFromScriptWizardModel myModel;

    public ImportFromScriptReverseStep(Project project, ImportFromScriptWizardModel model) {
        super(MedBundle.message("import.reverse.step"));
        this.myProject = project;
        this.myModel = model;
    }

    public void setState(WizardNavigationState state) {
        this.state = state;
    }

    @Override
    public JComponent prepare(WizardNavigationState state) {
        // enableFinish();
        reverseForm = new ReverseForm(myProject, myModel.getFilter(),
                ReverseFormSettings.from(myProject.getService(SettingManager.class).getReverseSettings()),
                ReverseForm.Mode.SCRIPT);
        return reverseForm.getContentPanel();
    }

}
