/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.util;

import com.intellij.ide.extensionResources.ExtensionsRootType;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import io.entframework.med.MedPluginId;
import io.entframework.med.script.ScriptException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ScriptUtil {
    private ScriptUtil() {
    }

    public static File getScriptRoot() throws ScriptException {
        try {
            ExtensionsRootType.getInstance().extractBundledResources(MedPluginId.get(), "");
            Path directory = ExtensionsRootType.getInstance().findResourceDirectory(MedPluginId.get(), "", false);
            if (Files.isDirectory(directory)) {
                return directory.toFile();
            }
        } catch (IOException e) {
            throw new ScriptException(e.getMessage());
        }
        return null;
    }

    public static String getContent(@NotNull String basePath) throws ScriptException {
        String path = basePath;
        if (StringUtil.startsWith(basePath, "/")) {
            path = StringUtil.substringAfter(basePath, "/");
        }
        try {
            File scriptRoot = getScriptRoot();
            File resource = new File(scriptRoot, path);
            VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(resource);
            if (virtualFile != null) {
                return FileUtil.loadTextAndClose(virtualFile.getInputStream());
            }
        } catch (IOException e) {
            throw new ScriptException(e.getMessage());
        }
        return null;
    }
}
