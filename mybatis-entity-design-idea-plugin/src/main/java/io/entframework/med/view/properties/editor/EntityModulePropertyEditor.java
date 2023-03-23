/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties.editor;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAwareAction;
import io.entframework.med.dom.DomModule;
import io.entframework.med.view.properties.dom.MedDomModuleForm;
import org.jetbrains.annotations.NotNull;

public class EntityModulePropertyEditor extends AbstractPropertyEditor<DomModule> {

    private static final Logger log = Logger.getInstance(EntityModulePropertyEditor.class);

    private AnAction newEntityAction = new DumbAwareAction("New Entity") {
        @Override
        public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        }
    };

    public EntityModulePropertyEditor(DomModule value) {
        super(value);
    }

    @Override
    public DefaultActionGroup getActionGroup() {
        return new DefaultActionGroup(newEntityAction);
    }

    @Override
    public void buildEditor() {
        addPanel(createPanel("Basic", new MedDomModuleForm(value)));
    }

}
