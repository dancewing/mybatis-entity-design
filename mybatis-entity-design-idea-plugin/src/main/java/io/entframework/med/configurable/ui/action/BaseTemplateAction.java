/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.ui.action;

import com.intellij.ide.ui.laf.darcula.ui.DarculaTextBorder;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.util.NlsActions;
import com.intellij.ui.JBColor;
import com.intellij.ui.treeStructure.Tree;
import io.entframework.med.configurable.ui.TemplatesUI;
import io.entframework.med.model.CodeGroup;
import io.entframework.med.model.CodeTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.util.function.Supplier;

/**
 * Desc: 模板配置的抽象类
 * <p>
 * Mail: chk19940609@gmail.com Created by IceMimosa Date: 2018/1/5
 */
public abstract class BaseTemplateAction extends AnAction {

    /**
     * templates ui 对象
     */
    protected TemplatesUI templatesUI;

    protected Tree templateTree;

    protected BaseTemplateAction(TemplatesUI templatesUI) {
        this.templatesUI = templatesUI;
        this.templateTree = templatesUI.getTemplateTree();
    }

    public BaseTemplateAction(@NotNull Supplier<@NlsActions.ActionText String> dynamicText, @Nullable Icon icon, TemplatesUI templatesUI) {
        super(dynamicText, icon);
        this.templatesUI = templatesUI;
        this.templateTree = templatesUI.getTemplateTree();
    }

    /**
     * 获取TemplatesUI的tree中选中的node
     */
    protected DefaultMutableTreeNode getSelectedNode() {
        return (DefaultMutableTreeNode) this.templateTree.getLastSelectedPathComponent();
    }

    /**
     * 显示对话框
     */
    protected void showDialog(JDialog dialog, int width, int height) {
        dialog.setSize(width, height);
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(this.templatesUI);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    /**
     * 显示JTextField是否为红色边框
     */
    protected void showErrorBorder(JTextField field, boolean show) {
        if (show) {
            field.setBorder(BorderFactory.createLineBorder(JBColor.RED));
        } else {
            field.setBorder(new DarculaTextBorder());
        }
    }

    /**
     * 新增group节点
     */
    protected void addCodeGroup(CodeGroup codeGroup) {
        DefaultMutableTreeNode selectedNode = getSelectedNode();
        if (selectedNode == null) {
            return;
        }
        Object object = selectedNode.getUserObject();
        if (object == null) {
            return;
        }
        // 2. 如果是codeGroup
        else if (object instanceof CodeGroup) {
            addNode((DefaultMutableTreeNode) selectedNode.getParent(), new DefaultMutableTreeNode(codeGroup));
        }
        // 3. 如果是codeTemplate
        else if (object instanceof CodeTemplate) {
            addNode((DefaultMutableTreeNode) selectedNode.getParent().getParent(),
                    new DefaultMutableTreeNode(codeGroup));
        }
    }

    /**
     * 新增template节点
     */
    protected void addCodeTemplate(CodeTemplate codeTemplate) {
        DefaultMutableTreeNode selectedNode = getSelectedNode();
        if (selectedNode == null) {
            return;
        }
        Object object = selectedNode.getUserObject();
        if (object == null) {
            return;
        }
        // 1. 如果是codeGroup
        else if (object instanceof CodeGroup) {
            addNode(selectedNode, new DefaultMutableTreeNode(codeTemplate));
        }
        // 2. 如果是codeTemplate
        else if (object instanceof CodeTemplate) {
            addNode((DefaultMutableTreeNode) selectedNode.getParent(), new DefaultMutableTreeNode(codeTemplate));
        }
    }

    /**
     * 直接通过model来添加新节点，则无需通过调用JTree的updateUI方法 model.insertNodeInto(newNode, selectedNode,
     * selectedNode.getChildCount()); 直接通过节点添加新节点，则需要调用tree的updateUI方法
     */
    private void addNode(DefaultMutableTreeNode pNode, MutableTreeNode newNode) {
        pNode.add(newNode);
        // --------下面代码实现显示新节点（自动展开父节点）-------
        DefaultTreeModel model = (DefaultTreeModel) this.templateTree.getModel();
        TreeNode[] nodes = model.getPathToRoot(newNode);
        TreePath path = new TreePath(nodes);
        this.templateTree.scrollPathToVisible(path);
        this.templateTree.updateUI();
    }

}
