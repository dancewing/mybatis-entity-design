/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.language;

import com.intellij.ide.highlighter.DomSupportEnabled;
import com.intellij.openapi.fileTypes.LanguageFileType;
import io.entframework.med.MedIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MedXmlFileType extends LanguageFileType implements DomSupportEnabled {

    public static final MedXmlFileType INSTANCE = new MedXmlFileType();

    public static final String DEFAULT_EXTENSION = "med";

    public static final String DOT_DEFAULT_EXTENSION = "." + DEFAULT_EXTENSION;

    private MedXmlFileType() {
        super(MedXMLLanguage.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "MED";
    }

    @Override
    public @NotNull String getDescription() {
        return "Mybatis Entity Design File";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Override
    public Icon getIcon() {
        return MedIcons.ICON;
    }

}
