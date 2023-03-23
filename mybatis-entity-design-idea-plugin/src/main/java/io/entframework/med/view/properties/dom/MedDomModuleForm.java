/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties.dom;

import com.intellij.util.xml.ui.PsiClassPanel;
import io.entframework.med.dom.DomModule;
import io.entframework.med.view.properties.component.SingleLineTextPanel;

import javax.swing.*;

public class MedDomModuleForm extends AbstractDomElementComponent<DomModule> {
    private JPanel contentPanel;
    private SingleLineTextPanel myName;
    private SingleLineTextPanel myDescription;
    private SingleLineTextPanel myPackage;
    private PsiClassPanel myBaseEntity;

    public MedDomModuleForm(DomModule domElement) {
        super(domElement);
    }

    @Override
    public JComponent getComponent() {
        return contentPanel;
    }
}
