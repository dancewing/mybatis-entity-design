/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.language;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.xml.XmlSchemaProvider;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

public class MedXmlSchemaProvider extends XmlSchemaProvider {

    @Override
    public @Nullable XmlFile getSchema(@NotNull @NonNls String url, @Nullable Module module,
                                       @NotNull PsiFile baseFile) {
        URL schemaFileURL = null;

        if (baseFile instanceof XmlFile && baseFile.getFileType() == MedXmlFileType.INSTANCE) {
            schemaFileURL = MedXmlSchemaProvider.class.getResource("/dtd/mybatis-mapping-bindings-1.0.dtd");
        }

        if (schemaFileURL != null) {
            VirtualFile virtualFile = VfsUtil.findFileByURL(schemaFileURL);

            if (virtualFile != null) {
                PsiManager psiManager = PsiManager.getInstance(baseFile.getProject());

                PsiFile targetFile = psiManager.findFile(virtualFile);
                if (targetFile instanceof XmlFile xsdFile) {
                    return xsdFile;
                }
            }
        }

        return null;
    }

    @Override
    public boolean isAvailable(@NotNull XmlFile file) {
        if (file.getFileType() == MedXmlFileType.INSTANCE) {
            return true;
        }
        return super.isAvailable(file);
    }

}
