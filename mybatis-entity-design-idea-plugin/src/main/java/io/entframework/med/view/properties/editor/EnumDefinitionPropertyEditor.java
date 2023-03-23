/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties.editor;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAwareAction;
import io.entframework.med.dom.DomEnumDefinition;
import io.entframework.med.view.properties.dom.MedDomColumnForm;
import io.entframework.med.view.properties.dom.MedDomEnumDefinitionForm;
import org.jetbrains.annotations.NotNull;

public class EnumDefinitionPropertyEditor extends AbstractPropertyEditor<DomEnumDefinition> {

    private static final Logger log = Logger.getInstance(EnumDefinitionPropertyEditor.class);

    private AnAction newEntityAction = new DumbAwareAction("New Entity") {
        @Override
        public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        }
    };

    public EnumDefinitionPropertyEditor(DomEnumDefinition value) {
        super(value);
    }

    @Override
    public DefaultActionGroup getActionGroup() {
        return new DefaultActionGroup(newEntityAction);
    }

    @Override
    public void buildEditor() {
        addPanel(createPanel("Basic", new MedDomEnumDefinitionForm(value)));
    }

}
