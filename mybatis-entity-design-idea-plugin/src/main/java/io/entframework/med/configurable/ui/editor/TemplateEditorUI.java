/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.ui.editor;

import com.github.hykes.codegen.provider.FileProviderFactory;
import com.intellij.ide.extensionResources.ExtensionsRootType;
import com.intellij.ide.util.PsiNavigationSupport;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.JBUI;
import io.entframework.med.MedPluginId;
import io.entframework.med.configurable.TemplateEvent;
import io.entframework.med.configurable.TemplateEventManager;
import io.entframework.med.idea.NotificationHelper;
import io.entframework.med.model.CodeTemplate;
import io.entframework.med.model.ProjectContainer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.nio.file.Path;

public class TemplateEditorUI extends ProjectContainer implements Disposable {

    private JPanel rootPanel;

    private JTextField displayTextField;

    private JComboBox<String> templateLangComboBox;
    private JComboBox<String> fileExtensionComboBox;
    private JTextField packageTextField;
    private JTextField fileNameTextField;
    private JComboBox<String> templateLevelComboBox;
    private JPanel editorPanel;
    private JTextField templatePathField;
    private JButton openTemplateButton;
    private final EditorFactory factory = EditorFactory.getInstance();

    private final EditorHighlighterFactory highlighterFactory = EditorHighlighterFactory.getInstance();

    private Editor editor;
    private String templateId;
    private boolean isLoading;
    private boolean myEditorReleased;

    private MyDocumentListener myDocumentListener = new MyDocumentListener();

    public TemplateEditorUI(@NotNull Project project) {
        super(project);
        this.rootPanel.setPreferredSize(JBUI.size(340, 100));
        emptyEditor();
        fileExtensionComboBox.addItem("java");
        fileExtensionComboBox.addItem("xml");
        templateLangComboBox.addItem(CodeTemplate.TemplateLanguage.GROOVY.name());
        templateLangComboBox.addItem(CodeTemplate.TemplateLanguage.VELOCITY.name());

        for (CodeTemplate.TemplateLevel level : CodeTemplate.TemplateLevel.values()) {
            templateLevelComboBox.addItem(level.name());
        }

        addEventListener();
        openTemplateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String templatePath = templatePathField.getText();
                if (!StringUtil.isEmptyOrSpaces(templatePath)) {
                    try {
                        Path resource = ExtensionsRootType.getInstance().findResource(MedPluginId.get(), templatePath);
                        VirtualFile virtualFile = resource == null ? null : LocalFileSystem.getInstance().refreshAndFindFileByNioFile(resource);
                        if (virtualFile != null) {
                            PsiNavigationSupport.getInstance().createNavigatable(project, virtualFile, -1).navigate(true);
                        }
                    } catch (IOException ex) {
                        NotificationHelper.getInstance().notifyError("Can't find template:  " + templatePath, project);
                    }
                }
            }
        });
    }

    public void refresh(@NotNull CodeTemplate codeTemplate) {
        this.templateId = codeTemplate.getId();
        isLoading = true;
        displayTextField.setText(codeTemplate.getDisplay());
        templateLangComboBox.setSelectedItem(codeTemplate.getLanguage().name());
        fileExtensionComboBox.setSelectedItem(codeTemplate.getExtension());
        packageTextField.setText(codeTemplate.getPackage());
        fileNameTextField.setText(codeTemplate.getFileName());
        templateLevelComboBox.setSelectedItem(codeTemplate.getLevel().name());
        templatePathField.setText(codeTemplate.getTemplatePath());

        // create editor
        String template = "";
        if (!StringUtil.isEmptyOrSpaces(codeTemplate.getTemplatePath())) {
            try {
                Path resource = ExtensionsRootType.getInstance().findResource(MedPluginId.get(), codeTemplate.getTemplatePath());
                VirtualFile virtualFile = resource == null ? null : LocalFileSystem.getInstance().refreshAndFindFileByNioFile(resource);
                if (virtualFile != null) {
                    template = FileUtil.loadTextAndClose(virtualFile.getInputStream());
                }
            } catch (IOException ex) {
            }
        }
        String extension = codeTemplate.getLanguage().getExtension();

        createEditor(template, extension);
        isLoading = false;
    }

    private void emptyEditor() {
        createEditor("", "vm");
    }

    /**
     * 创建编辑器
     */
    private void createEditor(String template, String extension) {
        if (editor != null) {
            this.editorPanel.removeAll();
        }

        Document velocityTemplate = factory.createDocument(template);
        editor = factory.createEditor(velocityTemplate, myProject, FileProviderFactory.getFileType(extension), false);
        EditorSettings editorSettings = editor.getSettings();
        editorSettings.setLineNumbersShown(true);
        if (editor instanceof EditorEx editorEx) {
            editorEx.setHighlighter(
                    highlighterFactory.createEditorHighlighter(myProject, FileProviderFactory.getFileType(extension)));
            editorEx.setInsertMode(false);
        }
        ApplicationManager.getApplication().runWriteAction(() -> editor.getDocument().setText(template));
        //editor.getDocument().setText(template);
        this.editorPanel.add(this.editor.getComponent(), BorderLayout.CENTER);
        this.editorPanel.updateUI();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = new JPanel();
        rootPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public CodeTemplate getCodeTemplate() {
        CodeTemplate codeTemplate = new CodeTemplate();
        codeTemplate.setId(templateId);
        codeTemplate.setDisplay(displayTextField.getText().trim());
        codeTemplate.setExtension((String) fileExtensionComboBox.getSelectedItem());
        codeTemplate.setFileName(fileNameTextField.getText().trim());
        codeTemplate.setPackage(packageTextField.getText().trim());
        codeTemplate.setLevel(CodeTemplate.TemplateLevel.valueOf((String) templateLevelComboBox.getSelectedItem()));
        codeTemplate.setLanguage(CodeTemplate.TemplateLanguage.valueOf((String) templateLangComboBox.getSelectedItem()));
        return codeTemplate;
    }

    public String getTemplateContent() {
        return this.editor.getDocument().getText();
    }

    private void addEventListener() {
        displayTextField.getDocument().addDocumentListener(myDocumentListener);
        fileNameTextField.getDocument().addDocumentListener(myDocumentListener);
        packageTextField.getDocument().addDocumentListener(myDocumentListener);
        templateLangComboBox.addItemListener(myDocumentListener);
        fileExtensionComboBox.addItemListener(myDocumentListener);
    }

    @Override
    public void dispose() {
        if (!myEditorReleased && editor != null) {
            myEditorReleased = true;
            factory.releaseEditor(editor);
        }
    }

    public class MyDocumentListener implements DocumentListener, ItemListener, com.intellij.openapi.editor.event.DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            fireChanged();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            fireChanged();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            fireChanged();
        }

        private void fireChanged() {
            TemplateEventManager templateEventManager = myProject.getService(TemplateEventManager.class);
            if (!isLoading) {
                templateEventManager.sendChanged(new TemplateEvent(getCodeTemplate()));
            }
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                fireChanged();
            }
        }

        @Override
        public void documentChanged(com.intellij.openapi.editor.event.@NotNull DocumentEvent event) {
            fireChanged();
        }
    }
}
