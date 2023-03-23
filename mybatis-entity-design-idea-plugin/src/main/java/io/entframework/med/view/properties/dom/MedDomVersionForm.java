/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties.dom;

import io.entframework.med.dom.DomVersion;

import javax.swing.*;

public class MedDomVersionForm extends AbstractDomElementComponent<DomVersion> {
    private JPanel contentPanel;

    public MedDomVersionForm(DomVersion domElement) {
        super(domElement);
    }

    @Override
    public JComponent getComponent() {
        return contentPanel;
    }
}
