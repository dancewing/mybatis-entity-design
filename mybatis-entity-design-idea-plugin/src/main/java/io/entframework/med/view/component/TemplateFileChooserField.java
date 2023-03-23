/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.component;

import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.ui.FixedSizeButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.EditorTextField;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TemplateFileChooserField extends ComponentWithBrowseButton<EditorTextField> {

    private Project myProject;

    public TemplateFileChooserField() {
        super(new EditorTextField(""), null);
    }

    public TemplateFileChooserField(@NotNull Project myProject) {
        super(new EditorTextField("", myProject, PlainTextFileType.INSTANCE) {
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

    public void addDocumentListener(@NotNull DocumentListener documentListener) {
        EditorTextField textField = getChildComponent();
        textField.getDocument().addDocumentListener(documentListener);
    }

    public void setText(final String s) {
        String path = s;
        if (StringUtil.startsWith(s, "/")) {
            path = StringUtil.substringAfter(s, "/");
        }
        getChildComponent().setText(path);
    }

    public String getText() {
        final String value = getChildComponent().getText();
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
                        && fixedSizeButton.getParent() instanceof TemplateFileChooserField host) {
                    TemplateTreeChooser treeChooser = new TemplateTreeChooser(myProject);
                    if (treeChooser.showAndGet()) {
                        String path = treeChooser.getTemplatePath();
                        if (path != null) {
                            host.setText(path);
                        }
                    }
                }
            }
        }

    }

}
