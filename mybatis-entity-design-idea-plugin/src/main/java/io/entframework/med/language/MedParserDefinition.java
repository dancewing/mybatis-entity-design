/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.language;

import com.intellij.lang.xml.XMLParserDefinition;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.xml.XmlFileImpl;
import com.intellij.psi.tree.IFileElementType;
import org.jetbrains.annotations.NotNull;

public class MedParserDefinition extends XMLParserDefinition {

    public static final IFileElementType MED_FILE = new IFileElementType("MED_FILE", MedXMLLanguage.INSTANCE);

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new XmlFileImpl(viewProvider, MED_FILE);
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return MED_FILE;
    }

}
