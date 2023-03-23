/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties.dom;

import io.entframework.med.dom.DomEntity;
import io.entframework.med.view.properties.component.SingleLineTextPanel;

import javax.swing.*;

public class MedDomEntityForm extends AbstractDomElementComponent<DomEntity> {
    private JPanel contentPanel;
    private SingleLineTextPanel myName;
    private SingleLineTextPanel myDescription;


    public MedDomEntityForm(DomEntity domElement) {
        super(domElement);
    }

    @Override
    public JComponent getComponent() {
        return contentPanel;
    }
}
