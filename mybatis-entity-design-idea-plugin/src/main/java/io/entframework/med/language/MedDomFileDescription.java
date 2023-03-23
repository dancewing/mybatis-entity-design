/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.language;

import com.intellij.openapi.module.Module;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileDescription;
import io.entframework.med.dom.DomMed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MedDomFileDescription extends DomFileDescription<DomMed> {

    private static final String ROOT_TAG_NAME = "med";

    public MedDomFileDescription() {
        super(DomMed.class, ROOT_TAG_NAME);
    }


    @Override
    public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
        return super.isMyFile(file, module);
    }
}
