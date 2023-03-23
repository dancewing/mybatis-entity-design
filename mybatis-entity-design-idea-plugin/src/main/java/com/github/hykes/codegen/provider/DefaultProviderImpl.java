/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package com.github.hykes.codegen.provider;

import com.github.hykes.codegen.utils.StringUtils;
import com.github.hykes.codegen.utils.VelocityUtil;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import io.entframework.med.model.CodeContext;
import io.entframework.med.model.CodeTemplate;
import io.entframework.med.util.BuilderUtil;
import org.apache.velocity.VelocityContext;

import java.util.Map;

/**
 * 默认文件提供者
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/04/07
 */
public class DefaultProviderImpl extends AbstractFileProvider {

    private static final Logger LOGGER = Logger.getInstance(DefaultProviderImpl.class);

    protected FileType languageFileType;

    public DefaultProviderImpl(Project project, String outputPath, FileType languageFileType) {
        super(project, outputPath);
        this.languageFileType = languageFileType;
    }

    @Override
    public void create(CodeTemplate template, CodeContext context, Map<String, Object> extraMap) {

        VelocityContext velocityContext = new VelocityContext(BuilderUtil.transBean2Map(context));
        velocityContext.put("serialVersionUID",
                BuilderUtil.computeDefaultSUID(context.getModel(), context.getFields()));
        // $!dateFormatUtils.format($!now,'yyyy-MM-dd')
        velocityContext.put("dateFormatUtils", new org.apache.commons.lang.time.DateFormatUtils());
        if (extraMap != null && extraMap.size() > 0) {
            for (Map.Entry<String, Object> entry : extraMap.entrySet()) {
                velocityContext.put(entry.getKey(), entry.getValue());
            }
        }

        String fileName = VelocityUtil.evaluate(velocityContext, template.getFileName());
        String subPath = VelocityUtil.evaluate(velocityContext, template.getPackage());
        //String temp = VelocityUtil.evaluate(velocityContext, template.getContent());
        String temp = "";

        WriteCommandAction.runWriteCommandAction(this.project, () -> {
            try {
                VirtualFile vFile = VfsUtil.createDirectoryIfMissing(outputPath);
                PsiDirectory psiDirectory = PsiDirectoryFactory.getInstance(this.project).createDirectory(vFile);
                PsiDirectory directory = subDirectory(psiDirectory, subPath);
                // TODO: 这里干啥用的, 没用的话是不是可以删除了
                if (JavaFileType.INSTANCE == this.languageFileType) {
                    PsiPackage psiPackage = JavaDirectoryService.getInstance().getPackage(directory);
                    if (psiPackage != null && !StringUtils.isEmpty(psiPackage.getQualifiedName())) {
                        extraMap.put(fileName,
                                new StringBuilder(psiPackage.getQualifiedName()).append(".").append(fileName));
                    }
                }
                createFile(project, directory, fileName + "." + this.languageFileType.getDefaultExtension(), temp,
                        this.languageFileType);
            } catch (Exception e) {
                LOGGER.error(StringUtils.getStackTraceAsString(e));
            }
        });
    }

}