/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view;

import com.github.hykes.codegen.utils.PsiUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SaveFileDialog extends ProjectDialogWrapper {

    private JTextField nameTextField;

    private JPanel contentPanel;

    private TextFieldWithBrowseButton directory;

    private String myExtension;

    public SaveFileDialog(@NotNull Project project, @NotNull String extension) {
        super(project);
        this.myExtension = extension;
        init();
        setSize(400, 50);
        setResizable(false);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return contentPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        directory = new TextFieldWithBrowseButton();
        directory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VirtualFile virtualFile = PsiUtil.chooseFolder(myProject, "Directory Chooser", "Select Directory", true,
                        true, null);
                if (Objects.isNull(virtualFile)) {
                    return;
                }
                if (!virtualFile.isDirectory()) {
                    Messages.showInfoMessage("请选择导出文件夹", "ERROR");
                    return;
                }
                directory.setText(virtualFile.getPath());
            }
        });
    }

    @Override
    protected @NotNull List<ValidationInfo> doValidateAll() {
        List<ValidationInfo> validationInfos = new ArrayList<>();
        if (StringUtil.isEmptyOrSpaces(directory.getText())) {
            validationInfos.add(new ValidationInfo("Directory can't be empty", directory));
        } else {
            VirtualFile virtualDirectory = LocalFileSystem.getInstance().findFileByPath(directory.getText());
            if (virtualDirectory == null) {
                validationInfos.add(new ValidationInfo("Directory doesn't exist", directory));
            } else {
                if (!virtualDirectory.isDirectory()) {
                    validationInfos.add(new ValidationInfo("Directory is not legal", directory));
                }
            }
        }
        if (StringUtil.isEmptyOrSpaces(nameTextField.getText())) {
            validationInfos.add(new ValidationInfo("File name can't be empty", nameTextField));
        }
        if (validationInfos.isEmpty()) {
            File file = getFile();
            if (LocalFileSystem.getInstance().findFileByIoFile(file) != null) {
                validationInfos.add(new ValidationInfo("Target file exist already", nameTextField));
            }
        }

        return validationInfos;
    }

    public File getFile() {
        VirtualFile virtualDirectory = LocalFileSystem.getInstance().findFileByPath(directory.getText());
        return new File(virtualDirectory.getPath(), getFileName(nameTextField.getText()));
    }

    public VirtualFile getDirectory() {
        return LocalFileSystem.getInstance().findFileByPath(directory.getText());
    }

    public String getFileName() {
        return getFileName(nameTextField.getText());
    }

    private String getFileName(String text) {
        if (StringUtil.endsWith(text, myExtension)) {
            return text;
        }
        return text + myExtension;
    }

}
