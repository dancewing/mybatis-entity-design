/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import io.entframework.med.dom.MyDomElement;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class CreateNewNodeAction extends AnAction {

    private MyDomElement myElement;
    private CreateCallback myAfterCallable;
    private String title;
    private InputValidator validator;

    private CreateNewNodeAction(String title, MyDomElement baseElement, InputValidator validator,
                                CreateCallback afterCallable) {
        super(() -> title, AllIcons.General.Add);
        this.title = title;
        this.myElement = baseElement;
        this.validator = validator;
        this.myAfterCallable = afterCallable;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        String inputName = Messages.showInputDialog(project, title, title, (Icon)null, null, validator);
        if (!StringUtil.isEmptyOrSpaces(inputName)) {
            this.myAfterCallable.call(inputName);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String title;

        private MyDomElement element;

        private InputValidator validator;

        private CreateCallback myAfterCallable;

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withElement(MyDomElement element) {
            this.element = element;
            return this;
        }

        public Builder withAfterCallable(CreateCallback myAfterCallable) {
            this.myAfterCallable = myAfterCallable;
            return this;
        }

        public Builder withValidator(InputValidator validator) {
            this.validator = validator;
            return this;
        }

        public CreateNewNodeAction build() {
            return new CreateNewNodeAction(title, element, validator, myAfterCallable);
        }

    }

}
