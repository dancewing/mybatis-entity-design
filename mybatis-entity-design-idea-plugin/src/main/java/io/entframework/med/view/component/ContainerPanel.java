/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.component;

import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.util.ui.JBUI;

import javax.swing.*;

public class ContainerPanel extends JPanel {

    public ContainerPanel() {
        super(new VerticalFlowLayout(true, false));
        setBorder(JBUI.Borders.empty());
        setFocusable(false);
    }

}
