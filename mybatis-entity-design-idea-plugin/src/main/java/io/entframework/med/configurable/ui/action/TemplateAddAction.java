/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.ui.action;

import com.intellij.icons.AllIcons;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import io.entframework.med.configurable.ui.TemplatesUI;
import io.entframework.med.configurable.ui.dialog.TemplateEditDialog;
import io.entframework.med.configurable.ui.dialog.TemplateGroupEditDialog;
import io.entframework.med.model.CodeGroup;
import io.entframework.med.model.CodeTemplate;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Desc: 模板页面的添加事件
 * <p>
 * Mail: chk19940609@gmail.com Created by IceMimosa Date: 2018/1/5
 */
public class TemplateAddAction extends BaseTemplateAction implements AnActionButtonRunnable {

    public TemplateAddAction(TemplatesUI templatesUI) {
        super(templatesUI);
    }

    @Override
    public void run(AnActionButton button) {
        // 获取选中节点
        final DefaultMutableTreeNode selectedNode = getSelectedNode();
        List<AnAction> actions = getMultipleActions(selectedNode);
        if (actions == null || actions.isEmpty()) {
            return;
        }
        // 显示新增按钮
        final DefaultActionGroup group = new DefaultActionGroup(actions);
        JBPopupFactory.getInstance()
                .createActionGroupPopup(null, group, DataManager.getInstance().getDataContext(button.getContextComponent()),
                        JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, true)
                .show(button.getPreferredPopupPoint());
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

    }

    /**
     * 获取点击加号按钮的各种添加操作, 包括root/group/template
     */
    private List<AnAction> getMultipleActions(DefaultMutableTreeNode selectedNode) {
        // 初始化所有的AnAction
        List<AnAction> actions = new ArrayList<>();
        CodeGroupAddAction groupAction = new CodeGroupAddAction();
        CodeTemplateAddAction templateAction = new CodeTemplateAddAction();

        // 0. 如果未选中节点
        if (selectedNode == null) {
            actions.add(groupAction);
            return actions;
        }

        Object object = selectedNode.getUserObject();
        // 2. 如果选中的是group, 则可以新增root, group以及template
        if (object instanceof CodeGroup) {
            actions.add(groupAction);
            actions.add(templateAction);
        }
        // 3. 如果选中的是template, 则可以新增root, group以及template
        if (object instanceof CodeTemplate) {
            actions.add(groupAction);
            actions.add(templateAction);
        }
        return actions;
    }

    /**
     * 新增CodeGroup的事件
     */
    class CodeGroupAddAction extends AnAction {

        public CodeGroupAddAction() {
            super("Code Group", null, AllIcons.Nodes.Folder);
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            // 1. 显示CodeGroup新增dialog
            TemplateGroupEditDialog dialog = new TemplateGroupEditDialog(e.getProject());
            dialog.setTitle("Create New Group");
            if (dialog.showAndGet()) {
                // 获取名称和level, 校验
                String name = dialog.getNameTextField().getText();
                // 新增group
                addCodeGroup(new CodeGroup(name.trim()));
            }
        }

    }

    /**
     * 新增CodeTemplate的事件
     */
    public class CodeTemplateAddAction extends AnAction {

        public CodeTemplateAddAction() {
            super("Code Template", null, AllIcons.FileTypes.Text);
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            // 1. 显示CodeTemplate新增dialog
            TemplateEditDialog dialog = new TemplateEditDialog(Objects.requireNonNull(e.getProject()));
            dialog.setTitle("Create New Template");
            if (dialog.showAndGet()) {
                addCodeTemplate(dialog.getCodeTemplate());
            }
        }

    }

}
