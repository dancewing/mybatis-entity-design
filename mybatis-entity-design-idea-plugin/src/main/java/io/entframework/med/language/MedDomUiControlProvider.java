/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.language;

import com.intellij.util.Consumer;
import com.intellij.util.xml.ui.DomUIFactory;

public class MedDomUiControlProvider implements Consumer<DomUIFactory> {
    @Override
    public void consume(DomUIFactory factory) {
        //factory.registerCustomControl(Integer.class, wrapper -> new IntegerControl(wrapper, true));
    }
}
