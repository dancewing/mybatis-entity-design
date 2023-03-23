/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties.editor;

import com.intellij.openapi.actionSystem.DefaultActionGroup;
import io.entframework.med.dom.DomVersion;
import io.entframework.med.view.properties.dom.MedDomColumnForm;
import io.entframework.med.view.properties.dom.MedDomVersionForm;

public class VersionPropertyEditor extends AbstractPropertyEditor<DomVersion> {

    public VersionPropertyEditor(DomVersion value) {
        super(value);
    }

    @Override
    public DefaultActionGroup getActionGroup() {
        return null;
    }

    @Override
    public void buildEditor() {
        addPanel(createPanel("Basic", new MedDomVersionForm(value)));
        addPanel(createPanel("Column", new MedDomColumnForm(value.getColumn())));
    }

}
