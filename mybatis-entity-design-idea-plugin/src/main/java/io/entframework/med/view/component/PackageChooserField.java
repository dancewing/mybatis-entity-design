/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.component;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.module.ResourceFileUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.ui.FixedSizeButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.psi.*;
import com.intellij.psi.search.ProjectScope;
import com.intellij.ui.EditorTextField;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PackageChooserField extends ComponentWithBrowseButton<EditorTextField> {

    private Project myProject;

    private Document myDocument;

    public PackageChooserField() {
        super(new EditorTextField(""), null);
    }

    public PackageChooserField(@NotNull Project myProject) {
        super(new EditorTextField("", myProject, JavaFileType.INSTANCE) {
            @Override
            protected boolean shouldHaveBorder() {
                return false;
            }
        }, new MyActionListener(myProject));
        this.myProject = myProject;
        EditorTextField textField = getChildComponent();
        textField.setBorder(null);
        setText("");
    }

    public void setText(final String s) {
        final JavaCodeFragmentFactory factory = JavaCodeFragmentFactory.getInstance(myProject);
        PsiPackage defaultPackage = JavaPsiFacade.getInstance(myProject).findPackage("");
        final PsiCodeFragment fragment = factory.createReferenceCodeFragment(s, defaultPackage, true, true);
        myDocument = PsiDocumentManager.getInstance(myProject).getDocument(fragment);
        getChildComponent().setDocument(myDocument);
    }

    public String getText() {
        final String value = myDocument.getText();
        if (value.length() == 0) {
            return null;
        }
        return value.replace('$', '.'); // PSI works only with dots
    }

    private static class MyActionListener implements ActionListener {

        private final Project myProject;

        public MyActionListener(@NotNull Project project) {
            this.myProject = project;
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            Object source = e.getSource();
            if (source instanceof FixedSizeButton fixedSizeButton) {
                if (fixedSizeButton.getParent() != null
                        && fixedSizeButton.getParent() instanceof PackageChooserField host) {
                    EditorTextField editorTextField = host.getChildComponent();
                    final String className = editorTextField.getText();
                    PsiFile fromFile = null;
                    PsiClass fromClass = null;
                    if (StringUtil.isNotEmpty(className)) {
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
                    PackageChooserDialog chooserDialog = new PackageChooserDialog("Choose Package", myProject);
                    if (chooserDialog.showAndGet()) {
                        PsiPackage selectedPackage = chooserDialog.getSelectedPackage();
                        if (selectedPackage.isValid()) {
                            host.setText(selectedPackage.getQualifiedName());
                        }
                    }
                    // todo[anton] make it via providing proper parent
                    IdeFocusManager.getGlobalInstance()
                            .doWhenFocusSettlesDown(
                                    () -> IdeFocusManager.getGlobalInstance().requestFocus(editorTextField, true));
                }
            }

        }

    }

}
