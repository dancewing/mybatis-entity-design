/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.ui.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import io.entframework.med.configurable.ui.TemplatesUI;
import io.entframework.med.configurable.ui.dialog.TemplateGroupEditDialog;
import io.entframework.med.model.CodeGroup;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Desc: 模板页面的编辑事件
 * <p>
 * Mail: chk19940609@gmail.com Created by IceMimosa Date: 2018/1/13
 */
public class TemplateEditAction extends BaseTemplateAction implements AnActionButtonRunnable {

    public TemplateEditAction(TemplatesUI templatesUI) {
        super(templatesUI);
    }

    @Override
    public void run(AnActionButton button) {
        // 获取选中节点
        DefaultMutableTreeNode selectedNode = getSelectedNode();
        if (selectedNode == null) {
            return;
        }
        Object object = selectedNode.getUserObject();
        // 2. 如果是codeGroup
        if (object instanceof CodeGroup group) {
            // 编辑模版组
            TemplateGroupEditDialog dialog = new TemplateGroupEditDialog(templatesUI.getProject());
            dialog.setTitle("Edit Group");
            dialog.getNameTextField().setText(group.getName());

            if (dialog.showAndGet()) {
                String name = dialog.getNameTextField().getText();
                group.setName(name.trim());
                selectedNode.setUserObject(group);
            }
        }
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

    }

}
