/*******************************************************************************
 * Copyright (c) 2019-2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.idea.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import io.entframework.med.dom.DomEntity;
import io.entframework.med.dom.DomMed;
import io.entframework.med.dom.DomModule;
import io.entframework.med.idea.NotificationHelper;
import io.entframework.med.language.MedXmlFileType;
import io.entframework.med.model.GeneratorRuntime;
import io.entframework.med.model.RuntimeTemplate;
import io.entframework.med.service.FileWriteService;
import io.entframework.med.service.TemplateProcessService;
import io.entframework.med.util.DomSupport;
import io.entframework.med.util.MedDomUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Run Mybatis Generator on xml file.
 *
 * @author Tony Ho
 */
public class MedCodeGeneratorAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);
        if (psiFile instanceof XmlFile medFile) {
            FileDocumentManager.getInstance().saveAllDocuments();

            Project project = Objects.requireNonNull(event.getProject());
            TemplateProcessService templateProcessService = project.getService(TemplateProcessService.class);
            FileWriteService fileWriteService = project.getService(FileWriteService.class);
            try {
                DomMed jaxbMed = DomSupport.getMed(medFile);
                CodeGeneratorForm generatorForm = new CodeGeneratorForm(project, jaxbMed);
                if (generatorForm.showAndGet()) {
                    DomModule mappings = Objects.requireNonNull(generatorForm.getJaxbModule());
                    List<DomEntity> entities = generatorForm.getEntities();
                    List<RuntimeTemplate> runtimeTemplates = generatorForm.getRuntimeTemplates();
                    GeneratorRuntime generatorRuntime = MedDomUtil.convertToRuntime(mappings, entities);
                    List<RuntimeTemplate> results = templateProcessService.process(generatorRuntime, runtimeTemplates);
                    fileWriteService.write(results);
                }
            } catch (Exception ex) {
                NotificationHelper.getInstance().notifyException(ex, project);
            }

        }
    }

    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return Objects.requireNonNull(ActionUpdateThread.BGT);
    }

    public void update(@NotNull AnActionEvent event) {
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);
        if (psiFile == null || psiFile.getFileType() != MedXmlFileType.INSTANCE) {
            event.getPresentation().setVisible(false);
        }
    }

}
