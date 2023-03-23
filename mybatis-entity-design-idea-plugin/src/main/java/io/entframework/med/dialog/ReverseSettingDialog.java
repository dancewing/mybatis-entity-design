/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.dialog;

import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.fileChooser.FileSaverDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import io.entframework.med.configurable.SettingManager;
import io.entframework.med.language.MedXmlFileType;
import io.entframework.med.model.Entity;
import io.entframework.med.view.ProjectDialogWrapper;
import io.entframework.med.view.ReverseForm;
import io.entframework.med.view.ReverseFormSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.nio.file.Path;
import java.util.List;

public class ReverseSettingDialog extends ProjectDialogWrapper {

    private ReverseForm reverseForm;

    public ReverseSettingDialog(@NotNull Project project, List<Entity> entities) {
        super(project, true);
        ReverseFormSettings reverseFormSettings = ReverseFormSettings.from(project.getService(SettingManager.class).getReverseSettings());
        reverseForm = new ReverseForm(project, entities, reverseFormSettings);
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return reverseForm.getContentPanel();
    }

    @Override
    protected void doOKAction() {
        FileSaverDescriptor descriptor = new FileSaverDescriptor("Save to Med file", "Save to Med file", MedXmlFileType.DEFAULT_EXTENSION);
        FileSaverDialog saveFileDialog = FileChooserFactory.getInstance().createSaveFileDialog(descriptor, myProject);
        VirtualFileWrapper fileWrapper = saveFileDialog.save((Path) null, null);
        if (fileWrapper != null) {
//            File file = fileWrapper.getFile();
//            if (!StringUtil.endsWith(file.getName(), MedXmlFileType.DOT_DEFAULT_EXTENSION)) {
//                file = new File(file.getParent(), file.getName() + MedXmlFileType.DOT_DEFAULT_EXTENSION);
//            }
//            MedJaxbService jaxbService = myProject.getService(MedJaxbService.class);
//            JaxbMed med = new JaxbMed();
//            med.setVersion(MedConstants.XSD_VERSION);
//            JaxbModule jaxbModule = ObjectFactoryUtil.getInstance().createJaxbModule();
//            jaxbModule.setName("change later");
//            ReverseFormSettings reverseSettings = reverseForm.getReverseSettings();
//            if (!StringUtil.isEmptyOrSpaces(reverseSettings.getPackage())) {
//                jaxbModule.setPackage(reverseSettings.getPackage());
//            } else {
//                jaxbModule.setPackage("");
//            }
//            if (!StringUtil.isEmptyOrSpaces(reverseSettings.getParent())) {
//                jaxbModule.setBaseEntity(reverseSettings.getParent());
//            }
//
//            MedDomUtil.addEntity(reverseForm.getEntities(), jaxbModule);
//            med.getModules().add(jaxbModule);
//            if (!file.exists()) {
//                try {
//                    file.createNewFile();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file);
//            ApplicationManager.getApplication().runWriteAction(() -> {
//                try {
//
//                    VfsUtil.saveText(virtualFile, jaxbService.toString(med));
//                } catch (Exception ex) {
//                    NotificationHelper.getInstance()
//                            .createNotification(
//                                    ExceptionUtil.getThrowableText(ExceptionUtil.getRootCause(ex), "com.intellij."),
//                                    NotificationType.ERROR)
//                            .notify(myProject);
//                }
//            });
            super.doOKAction();

        }

    }
}
