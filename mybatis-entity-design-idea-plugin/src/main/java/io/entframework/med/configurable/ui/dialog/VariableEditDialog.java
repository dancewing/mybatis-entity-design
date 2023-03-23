/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.ui.dialog;

import com.intellij.openapi.project.Project;
import io.entframework.med.view.ProjectDialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class VariableEditDialog extends ProjectDialogWrapper {

    private JPanel contentPane;

    private JTextField keyTextField;

    private JTextField valueTextField;

    public JTextField getKeyTextField() {
        return keyTextField;
    }

    public JTextField getValueTextField() {
        return valueTextField;
    }

    public VariableEditDialog(@NotNull Project project) {
        super(project);
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return contentPane;
    }

}
