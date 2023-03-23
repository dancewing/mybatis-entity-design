/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.action;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.util.ui.tree.TreeUtil;
import io.entframework.med.dom.MyDomElement;
import io.entframework.med.idea.NotificationHelper;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.util.List;


public class PasteAction extends BaseTreeAction {

    private static final String ACTION_ID = "MyBatisMed.Tree.Paste";

    public static AnAction getInstance() {
        return ActionManager.getInstance().getAction(ACTION_ID);
    }

    @Override
    public void doActionPerformed(@NotNull AnActionEvent e, SimpleTree simpleTree) {

        TreeSelectionModel selectionModel = simpleTree.getSelectionModel();
        TreePath[] selectionPaths = selectionModel.getSelectionPaths();
        if (selectionPaths == null || selectionPaths.length != 1) {
            Messages.showWarningDialog("Message: Must selected one node to paste", "Paste Error");
            return;
        }
        TreePath currentPath = selectionPaths[0];
        DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
        CopyPasteManager copyPasteManager = CopyPasteManager.getInstance();
        if (copyPasteManager.areDataFlavorsAvailable(CopyAction.ourFlavor)) {
            try {
                List<MyDomElement> transferData = copyPasteManager.getContents(CopyAction.ourFlavor);
                if (transferData == null || transferData.isEmpty()) {
                    return;
                }
                MyDomElement firstElement = transferData.get(0);
                DefaultMutableTreeNode node = TreeUtil.findNodeWithObject((DefaultMutableTreeNode) simpleTree.getModel().getRoot(), firstElement);
                TreePath firstPath = TreeUtil.getPath((DefaultMutableTreeNode) simpleTree.getModel().getRoot(), node);
                if (firstPath.getPathCount() != currentPath.getPathCount() + 1) {
                    Messages.showErrorDialog("Message: Copied nodes are not children of target node", "Paste Error");
                    return;
                }
                PasteUtil.paste(lastPathComponent, transferData, simpleTree);
            } catch (Exception ex) {
                NotificationHelper.getInstance().notifyException(ex, e.getProject());
            }
        }
    }
}
