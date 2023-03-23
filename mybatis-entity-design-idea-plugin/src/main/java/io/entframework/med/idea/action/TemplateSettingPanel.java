/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.idea.action;

import com.github.hykes.codegen.utils.PsiUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import io.entframework.med.model.CodeTemplate;
import io.entframework.med.model.ProjectContainer;
import io.entframework.med.model.RuntimeTemplate;
import io.entframework.med.model.WriteMode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

public class TemplateSettingPanel extends ProjectContainer {
    private JPanel contentPanel;
    private JCheckBox customConfigCheckBox;
    private JComboBox<WriteMode> writeModeComboBox;
    private TextFieldWithBrowseButton directoryField;
    private JCheckBox enableCheckbox;
    private CodeTemplate myTemplate;

    public TemplateSettingPanel(@NotNull Project project, CodeTemplate codeTemplate) {
        super(project);
        this.myTemplate = codeTemplate;
        enableCheckbox.addItemListener(e -> {
            customConfigCheckBox.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            if (customConfigCheckBox.isSelected()) {
                directoryField.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
                writeModeComboBox.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            }
        });

        customConfigCheckBox.addItemListener(e -> {
            directoryField.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            writeModeComboBox.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
        });

        directoryField.addActionListener(new MyActionListener());

        for (WriteMode mode : WriteMode.values()) {
            writeModeComboBox.addItem(mode);
        }

        enableCheckbox.setSelected(true);
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public class MyActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            VirtualFile virtualDirectory = null;
            if (!StringUtil.isEmptyOrSpaces(directoryField.getText())) {
                virtualDirectory = LocalFileSystem.getInstance().findFileByPath(directoryField.getText());
            }

            VirtualFile virtualFile = PsiUtil.chooseFolder(myProject, "Directory Chooser",
                    "Select Export Directory", true, true, virtualDirectory);
            if (virtualFile != null && virtualFile.isDirectory()) {
                directoryField.setText(virtualFile.getPath());
            }
        }
    }

    public RuntimeTemplate getRuntimeTemplate() {
        if (enableCheckbox.isSelected()) {
            RuntimeTemplate template = new RuntimeTemplate();

            template.setId(myTemplate.getId());
            template.setDisplay(myTemplate.getDisplay());
            template.setExtension(myTemplate.getExtension());
            template.setFileName(myTemplate.getFileName());
            template.setPackage(myTemplate.getPackage());
            template.setLevel(myTemplate.getLevel());
            template.setLanguage(myTemplate.getLanguage());
            template.setTemplatePath(myTemplate.getTemplatePath());

            if (customConfigCheckBox.isEnabled() && customConfigCheckBox.isSelected()) {
                template.setDirectory(directoryField.getText());
                template.setWriteMode((WriteMode) writeModeComboBox.getSelectedItem());
            }
            return template;
        }
        return null;
    }
}
