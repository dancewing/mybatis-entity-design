/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.ui.dialog;

import com.intellij.openapi.project.Project;
import io.entframework.med.view.ProjectDialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TemplateGroupEditDialog extends ProjectDialogWrapper {

    private JPanel contentPane;

    private JTextField nameTextField;

    public JTextField getNameTextField() {
        return nameTextField;
    }

    public TemplateGroupEditDialog(@NotNull Project project) {
        super(project);
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return contentPane;
    }

}
