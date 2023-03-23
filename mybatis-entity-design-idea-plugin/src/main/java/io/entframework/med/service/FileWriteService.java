/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.service;

import com.github.javaparser.ast.CompilationUnit;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ExceptionUtil;
import io.entframework.med.idea.NotificationHelper;
import io.entframework.med.model.RuntimeTemplate;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class FileWriteService {
    private static final Logger log = Logger.getInstance(FileWriteService.class);
    private Project myProject;

    public FileWriteService(@NotNull Project project) {
        this.myProject = project;
    }

    public void write(@NotNull CompilationUnit cu, @NotNull RuntimeTemplate runtime) {

        if (StringUtil.isEmptyOrSpaces(runtime.getDirectory())) {
            return;
        }

        String fileName = runtime.getFileName();
        VirtualFile path = LocalFileSystem.getInstance().findFileByPath(runtime.getDirectory());
        if (StringUtil.isEmptyOrSpaces(fileName) || path == null || !path.isDirectory()) {
            return;
        }
        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                VirtualFile outPath = path;
                if (cu.getPackageDeclaration().isPresent()) {
                    String packagePath = cu.getPackageDeclaration().get().getNameAsString();
                    if (!StringUtil.isEmptyOrSpaces(packagePath)) {
                        outPath = createSubDirectory(path, packagePath);
                    }
                }

                VirtualFile saveFile = outPath.createChildData(this,
                        fileName + ".java");
                VfsUtil.saveText(saveFile, cu.toString());
            } catch (Exception ex) {
                NotificationHelper.getInstance()
                        .createNotification(
                                ExceptionUtil.getThrowableText(ExceptionUtil.getRootCause(ex), "com.intellij."),
                                NotificationType.ERROR)
                        .notify(myProject);
                log.error(ex.getMessage(), ex);
            }
        });
    }

    private VirtualFile createSubDirectory(@NotNull VirtualFile path, @NotNull String packagePath) throws Exception {
        List<String> list = StringUtil.split(packagePath, ".");
        File file = new File(path.getPath(), StringUtil.join(list, File.separator));
        FileUtils.forceMkdir(file);
        return Objects.requireNonNull(LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file));
    }

    public void write(RuntimeTemplate template) {
        if (StringUtil.isEmptyOrSpaces(template.getDirectory())) {
            return;
        }
        VirtualFile path = LocalFileSystem.getInstance().findFileByPath(template.getDirectory());
        if (path == null || !path.isDirectory()) {
            return;
        }

        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                VirtualFile outPath = path;
                if (!StringUtil.isEmptyOrSpaces(template.getPackage())) {
                    outPath = createSubDirectory(path, template.getPackage());
                }
                VirtualFile saveFile = outPath.createChildData(this,
                        template.getFileName() + "." + template.getExtension());
                VfsUtil.saveText(saveFile, template.getContent());
            } catch (Exception ex) {
                NotificationHelper.getInstance().notifyException(ex, myProject);
                log.error(ex.getMessage(), ex);
            }
        });
    }

    public void write(@NotNull List<RuntimeTemplate> templates) {
        templates.forEach(this::write);
    }
}
