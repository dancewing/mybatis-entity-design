/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.ui.action;

import com.github.hykes.codegen.utils.PsiUtil;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.icons.AllIcons;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xmlb.XmlSerializer;
import io.entframework.med.configurable.ui.TemplatesUI;
import io.entframework.med.model.CodeGroup;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public class TemplateImportAction extends BaseTemplateAction {

    private static final Logger LOGGER = Logger.getInstance(TemplateImportAction.class);

    public TemplateImportAction(TemplatesUI templatesUI) {
        super(() -> "Import", AllIcons.Actions.Upload, templatesUI);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        try {
            VirtualFile virtualFile = PsiUtil.chooseFile(null, "ZIP Chooser", "Select Import ZIP", true,
                    true, null);
            if (Objects.isNull(virtualFile)) {
                return;
            }
            if (virtualFile.getFileType() != XmlFileType.INSTANCE) {
                Messages.showInfoMessage("请选择模文件(.xml)", "ERROR");
                return;
            }

            CodeGroup codeGroup = XmlSerializer.deserialize(JDOMUtil.load(new File(virtualFile.getCanonicalPath())), CodeGroup.class);
            addCodeGroup(codeGroup);
            Messages.showInfoMessage("Import templates success", "INFO");
        } catch (Exception var) {
            LOGGER.error(StringUtils.getStackTraceAsString(var));
        }
    }

//    public void addCodeGroup(CodeGroup group) {
//        DefaultMutableTreeNode treeGroup = new DefaultMutableTreeNode(group, true);
//        group.getTemplates().forEach(template -> {
//            DefaultMutableTreeNode node = new DefaultMutableTreeNode(template, false);
//            treeGroup.add(node);
//        });
//        DefaultTreeModel treeModel= (DefaultTreeModel) templateTree.getModel();
//        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
//        root.add(treeGroup);
//    }
}
