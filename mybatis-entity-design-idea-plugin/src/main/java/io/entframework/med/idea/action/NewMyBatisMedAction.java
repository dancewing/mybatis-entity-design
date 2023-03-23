/*******************************************************************************
 * Copyright (c) 2019-2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.idea.action;

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import io.entframework.med.MedIcons;
import org.jetbrains.annotations.NotNull;

public class NewMyBatisMedAction extends CreateFileFromTemplateAction implements DumbAware {

    public NewMyBatisMedAction() {
        super("MyBatis Entity Design", "Creates new MyBatis entity design", MedIcons.ICON);
    }

    @Override
    protected void buildDialog(Project project, PsiDirectory psiDirectory,
                               CreateFileFromTemplateDialog.Builder builder) {
        builder.setTitle("New MyBatis Entity Design").addKind("MyBatis Entity Design", MedIcons.ICON, "MyMed.med");
    }

    @Override
    protected String getActionName(PsiDirectory psiDirectory, @NotNull String s, String s1) {
        return "MyBatis Entity Design";
    }

    @Override
    protected PsiFile createFile(String name, String templateName, PsiDirectory dir) {
        final FileTemplate template = FileTemplateManager.getInstance(dir.getProject()).getJ2eeTemplate(templateName);
        return createFileFromTemplate(name, template, dir);
    }

}
