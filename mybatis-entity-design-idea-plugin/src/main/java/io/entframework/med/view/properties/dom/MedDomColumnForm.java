/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties.dom;

import com.intellij.ui.components.fields.IntegerField;
import com.intellij.util.xml.ui.*;
import io.entframework.med.dom.DomColumn;
import io.entframework.med.view.properties.component.SingleLineTextPanel;

import javax.swing.*;

public class MedDomColumnForm extends AbstractDomElementComponent<DomColumn> {
    private JPanel contentPanel;
    private SingleLineTextPanel myName;
    private JCheckBox myUnique;
    private JCheckBox myNullable;
    private IntegerField myLength;
    private IntegerField myPrecision;
    private IntegerField myScale;
    private JComboBox myJdbcType;
    private PsiClassPanel myTypeHandler;

    public MedDomColumnForm(DomColumn domElement) {
        super(domElement);

        myLength.setDefaultValue(-1);
        myPrecision.setDefaultValue(-1);
        myScale.setDefaultValue(-1);
    }

    @Override
    public JComponent getComponent() {
        return contentPanel;
    }

    @Override
    protected void afterBuild() {

    }
}
