/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.ui.dialog;

import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import io.entframework.med.model.CodeTemplate;
import io.entframework.med.view.ProjectDialogWrapper;
import io.entframework.med.view.component.TemplateFileChooserField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.nio.file.Path;
import java.util.UUID;

public class TemplateEditDialog extends ProjectDialogWrapper {

    private JPanel contentPane;

    private JTextField displayTextField;

    private JTextField fileNameTextField;
    private JComboBox<String> fileExtensionComboBox;
    private JTextField packageTextField;
    private JComboBox<String> templateLevelComboBox;
    private TemplateFileChooserField fileChooserField;
    private CodeTemplate codeTemplate = new CodeTemplate();

    public TemplateEditDialog(@NotNull Project project) {
        super(project);
        init();
        fileExtensionComboBox.addItem("java");
        fileExtensionComboBox.addItem("xml");
        for (CodeTemplate.TemplateLevel level : CodeTemplate.TemplateLevel.values()) {
            templateLevelComboBox.addItem(level.name());
        }
    }

    public CodeTemplate getCodeTemplate() {
        codeTemplate.setId(UUID.randomUUID().toString());
        codeTemplate.setDisplay(displayTextField.getText().trim());
        codeTemplate.setExtension((String) fileExtensionComboBox.getSelectedItem());
        codeTemplate.setFileName(fileNameTextField.getText().trim());
        codeTemplate.setPackage(packageTextField.getText().trim());
        codeTemplate.setLevel(CodeTemplate.TemplateLevel.valueOf((String) templateLevelComboBox.getSelectedItem()));
        codeTemplate.setTemplatePath(fileChooserField.getText());
        return codeTemplate;
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return contentPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        fileChooserField = new TemplateFileChooserField(myProject);
        fileChooserField.addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                String text = fileChooserField.getText();
                if (!StringUtil.isEmptyOrSpaces(text)) {
                    Path path = Path.of(text);
                    String fileName = path.getFileName().toString();
                    displayTextField.setText(StringUtil.substringBefore(fileName, "."));
                    codeTemplate.setLanguage(CodeTemplate.TemplateLanguage.valueOf(StringUtil.substringAfter(fileName, ".")));
                }
            }
        });
    }

}
