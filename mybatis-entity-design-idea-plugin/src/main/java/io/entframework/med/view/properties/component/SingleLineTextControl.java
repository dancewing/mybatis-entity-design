/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties.component;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFileFactory;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.ReferenceEditorWithBrowseButton;
import com.intellij.util.Function;
import com.intellij.util.ui.JBUI;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.Required;
import com.intellij.util.xml.ui.DomWrapper;
import com.intellij.util.xml.ui.EditorTextFieldControl;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class SingleLineTextControl extends EditorTextFieldControl<SingleLineTextPanel> {
    public SingleLineTextControl(DomWrapper<String> domWrapper, boolean commitOnEveryChange) {
        super(domWrapper, commitOnEveryChange);
    }

    public SingleLineTextControl(DomWrapper<String> domWrapper) {
        super(domWrapper);
    }

    @Override
    protected EditorTextField getEditorTextField(@NotNull SingleLineTextPanel panel) {
        final Component component = panel.getComponent(0);
        if (component instanceof ReferenceEditorWithBrowseButton) {
            return ((ReferenceEditorWithBrowseButton) component).getEditorTextField();
        }
        return (EditorTextField) component;
    }

    @Override
    protected SingleLineTextPanel createMainComponent(SingleLineTextPanel boundedComponent, Project project) {
        if (boundedComponent == null) {
            boundedComponent = new SingleLineTextPanel();
        }
        boundedComponent.removeAll();

        final Function<String, Document> factory = s -> PsiDocumentManager.getInstance(project)
                .getDocument(PsiFileFactory.getInstance(project).createFileFromText("a.txt", PlainTextLanguage.INSTANCE, "", true, false));
        final EditorTextField editorTextField = new EditorTextField(factory.fun(""), project, FileTypes.PLAIN_TEXT) {
            @Override
            protected boolean isOneLineMode() {
                return true;
            }
        };

        boundedComponent.add(editorTextField);
        return boundedComponent;
    }

    @Override
    public @NotNull String getValue() {
        return super.getValue();
    }

    @Override
    protected boolean isCommitted() {
        final EditorTextField textField = getEditorTextField(getComponent());
        DomElement domElement = getDomElement();
        if (domElement.getAnnotation(Required.class) != null && StringUtil.isEmptyOrSpaces(textField.getText())) {
            textField.setBorder(JBUI.Borders.customLine(Color.RED));
            String domName = domElement.getNameStrategy().convertName(domElement.getXmlElementName());
            textField.setToolTipText(domName + " is required");
            return true;
        } else {
            textField.setBorder(JBUI.Borders.empty());
            textField.setToolTipText(null);
        }
        return super.isCommitted();
    }
}
