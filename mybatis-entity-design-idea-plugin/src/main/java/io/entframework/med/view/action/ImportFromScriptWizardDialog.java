/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.action;

import com.intellij.openapi.project.Project;
import com.intellij.ui.wizard.WizardDialog;

public class ImportFromScriptWizardDialog extends WizardDialog<ImportFromScriptWizardModel> {

    public ImportFromScriptWizardDialog(Project project, boolean canBeParent, ImportFromScriptWizardModel model) {
        super(project, canBeParent, model);
    }

}
