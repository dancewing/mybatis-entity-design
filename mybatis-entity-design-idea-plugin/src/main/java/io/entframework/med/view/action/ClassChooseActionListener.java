/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.action;

import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.module.ResourceFileUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.FixedSizeButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.ProjectScope;
import com.intellij.ui.components.fields.ExtendableTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClassChooseActionListener implements ActionListener {

    private Project myProject;

    public ClassChooseActionListener(Project myProject) {
        this.myProject = myProject;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final TreeClassChooserFactory factory = TreeClassChooserFactory.getInstance(myProject);
        PsiFile fromFile = null;
        PsiClass fromClass = null;
        Object source = e.getSource();
        if (source instanceof ExtendableTextField textField) {
            if (StringUtil.isNotEmpty(textField.getText())) {
                String className = textField.getText();
                String fileName = className.replace(".", "/") + ".java";
                VirtualFile formVFile = ResourceFileUtil.findResourceFileInScope(fileName, myProject,
                        ProjectScope.getAllScope(myProject));
                if (formVFile != null) {
                    fromFile = PsiManager.getInstance(myProject).findFile(formVFile);
                    if (fromFile instanceof PsiClass psiClass) {
                        fromClass = psiClass;
                    }
                }
            }
            TreeClassChooser fileChooser = factory.createInheritanceClassChooser("Choose Class",
                    ProjectScope.getAllScope(myProject), null, fromClass);
            fileChooser.showDialog();
            PsiClass selectedPsiClass = fileChooser.getSelected();
            if (selectedPsiClass != null) {
                PsiFile psiFile = selectedPsiClass.getContainingFile();
                if (psiFile instanceof PsiJavaFile javaFile) {
                    String packageName = javaFile.getPackageName();
                    String name = StringUtil.substringBefore(javaFile.getName(), ".");
                    if (StringUtil.isEmpty(packageName)) {
                        textField.setText(name);
                    } else {
                        textField.setText(packageName + "." + name);
                    }
                }
            }

        } else if (source instanceof FixedSizeButton fixedSizeButton) {
            if (StringUtil.isNotEmpty(fixedSizeButton.getText())) {
                String className = fixedSizeButton.getText();
                String fileName = className.replace(".", "/") + ".java";
                VirtualFile formVFile = ResourceFileUtil.findResourceFileInScope(fileName, myProject,
                        ProjectScope.getAllScope(myProject));
                if (formVFile != null) {
                    fromFile = PsiManager.getInstance(myProject).findFile(formVFile);
                    if (fromFile instanceof PsiClass psiClass) {
                        fromClass = psiClass;
                    }
                }
            }
            TreeClassChooser fileChooser = factory.createInheritanceClassChooser("Choose Class",
                    ProjectScope.getAllScope(myProject), null, fromClass);
            fileChooser.showDialog();
            PsiClass selectedPsiClass = fileChooser.getSelected();
            if (selectedPsiClass != null) {
                PsiFile psiFile = selectedPsiClass.getContainingFile();
                if (psiFile instanceof PsiJavaFile javaFile) {
                    String packageName = javaFile.getPackageName();
                    String name = StringUtil.substringBefore(javaFile.getName(), ".");
                    if (StringUtil.isEmpty(packageName)) {
                        fixedSizeButton.setText(name);
                    } else {
                        fixedSizeButton.setText(packageName + "." + name);
                    }
                }
            }
        }

    }

}
