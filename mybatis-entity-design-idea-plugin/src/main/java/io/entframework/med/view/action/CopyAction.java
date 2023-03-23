/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.action;

import com.intellij.ide.dnd.FileCopyPasteUtil;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.treeStructure.SimpleTree;
import io.entframework.med.dom.MyDomElement;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;
import java.util.List;

public class CopyAction extends BaseTreeAction {

    public static final DataFlavor ourFlavor = FileCopyPasteUtil.createJvmDataFlavor(CopyMedDomTransferable.class);

    private static final String ACTION_ID = "MyBatisMed.Tree.Copy";

    public static AnAction getInstance() {
        return ActionManager.getInstance().getAction(ACTION_ID);
    }

    @Override
    public void doActionPerformed(@NotNull AnActionEvent e, SimpleTree simpleTree) {
        TreeSelectionModel selectionModel = simpleTree.getSelectionModel();
        TreePath[] selectionPaths = selectionModel.getSelectionPaths();
        if (selectionPaths == null || selectionPaths.length == 0) {
            Messages.showWarningDialog("Message: Must selected one Node", "Copy Node Error");
            return;
        } else if (selectionPaths.length > 1) {
            int k = -1;
            for (TreePath treePath : selectionPaths) {
                if (k != -1 && treePath.getPathCount() != k) {
                    Messages.showWarningDialog("Message: Selected nodes must be same level", "Copy Node Error");
                    return;
                }
                k = treePath.getPathCount();
            }
        }
        List<MyDomElement> elementList = new ArrayList<>();
        for (TreePath path : selectionPaths) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            Object userObject = node.getUserObject();
            if (userObject instanceof MyDomElement element) {
                elementList.add(element);
            }
        }
        if (!elementList.isEmpty()) {
            CopyPasteManager.getInstance().setContents(new CopyMedDomTransferable(elementList));
        }
    }
}
