/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.ui.dialog;

import com.intellij.openapi.project.Project;
import io.entframework.med.view.ProjectDialogWrapper;
import io.entframework.med.view.component.ClassChooserField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ColumnEditDialog extends ProjectDialogWrapper {

    private JPanel contentPane;

    private JTextField fieldTextField;

    private JTextField columnTextField;

    private JTextField columnSizeTextField;

    private ClassChooserField fieldTypeTextField;

    private JTextField columnTypeTextField;

    private JTextField commentTextField;

    private JPanel editPanel;

    public ColumnEditDialog(@NotNull Project project) {
        super(project);
        init();
        setModal(true);
        setSize(500, 260);
        setResizable(false);
    }

    public JTextField getFieldTextField() {
        return fieldTextField;
    }

    public JTextField getColumnTextField() {
        return columnTextField;
    }

    public JTextField getColumnSizeTextField() {
        return columnSizeTextField;
    }

    public ClassChooserField getFieldTypeTextField() {
        return fieldTypeTextField;
    }

    public JTextField getColumnTypeTextField() {
        return columnTypeTextField;
    }

    public JTextField getCommentTextField() {
        return commentTextField;
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return contentPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        fieldTypeTextField = new ClassChooserField(myProject);
    }

}
