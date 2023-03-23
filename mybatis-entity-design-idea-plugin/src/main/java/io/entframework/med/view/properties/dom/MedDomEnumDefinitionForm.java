/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties.dom;

import io.entframework.med.dom.DomEnumDefinition;

import javax.swing.*;

public class MedDomEnumDefinitionForm extends AbstractDomElementComponent<DomEnumDefinition> {
    private JPanel contentPanel;

    public MedDomEnumDefinitionForm(DomEnumDefinition domElement) {
        super(domElement);
    }

    @Override
    public JComponent getComponent() {
        return contentPanel;
    }
}
