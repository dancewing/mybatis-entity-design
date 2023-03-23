/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties.dom;

import com.intellij.util.xml.ui.PsiClassPanel;
import io.entframework.med.dom.DomId;

import javax.swing.*;

public class MedDomIdForm extends AbstractDomElementComponent<DomId>{
    private JPanel contentPanel;
    private PsiClassPanel myType;

    public MedDomIdForm(DomId domElement) {
        super(domElement);
    }

    @Override
    public JComponent getComponent() {
        return contentPanel;
    }
}
