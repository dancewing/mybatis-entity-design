/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties.editor;

import com.intellij.openapi.actionSystem.DefaultActionGroup;
import io.entframework.med.dom.DomBasic;
import io.entframework.med.view.properties.dom.MedDomBasicForm;
import io.entframework.med.view.properties.dom.MedDomColumnForm;

public class BasicPropertyEditor extends AbstractPropertyEditor<DomBasic> {

    public BasicPropertyEditor(DomBasic value) {
        super(value);
    }

    @Override
    public DefaultActionGroup getActionGroup() {
        return null;
    }

    @Override
    public void buildEditor() {
        addPanel(createPanel("Basic", new MedDomBasicForm(value)));
        addPanel(createPanel("Column", new MedDomColumnForm(value.getColumn())));
    }

}
