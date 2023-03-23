/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties.dom;

import io.entframework.med.dom.DomEnumAttribute;

import javax.swing.*;

public class MedDomEnumAttributeForm extends AbstractDomElementComponent<DomEnumAttribute> {
    private JPanel contentPanel;

    public MedDomEnumAttributeForm(DomEnumAttribute domElement) {
        super(domElement);
    }


    @Override
    public JComponent getComponent() {
        return contentPanel;
    }
}
