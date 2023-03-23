/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.ui.action;

import com.github.hykes.codegen.utils.PsiUtil;
import com.github.hykes.codegen.utils.ZipUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xmlb.XmlSerializer;
import io.entframework.med.configurable.ui.TemplatesUI;
import io.entframework.med.model.CodeGroup;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Objects;

public class TemplateExportAction extends BaseTemplateAction {

    private static final Logger LOGGER = Logger.getInstance(TemplateExportAction.class);

    public TemplateExportAction(TemplatesUI templatesUI) {
        super(() -> "Export", AllIcons.Actions.Download, templatesUI);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        try {
            VirtualFile virtualFile = PsiUtil.chooseFolder(null, "Directory Chooser",
                    "Select Export Directory", true, true, null);
            if (Objects.isNull(virtualFile)) {
                return;
            }
            if (!virtualFile.isDirectory()) {
                Messages.showInfoMessage("请选择导出文件夹", "ERROR");
                return;
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
            Object object = node.getUserObject();
            if (object instanceof CodeGroup codeGroup) {
                Element element = XmlSerializer.serialize(codeGroup, null);
                ZipUtil.export(JDOMUtil.write(element), String.format("%s/CodeGen-Template-%s.xml", virtualFile.getCanonicalPath(), codeGroup.getName()));
            }
            Messages.showInfoMessage("Export CodeGen templates success", "INFO");
        } catch (Exception var) {
            LOGGER.error(var.getMessage());
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
        if (node != null) {
            Object object = node.getUserObject();
            if (object instanceof CodeGroup) {
                e.getPresentation().setEnabled(true);
            } else {
                e.getPresentation().setEnabled(false);
            }
        }
    }
}
