/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.action;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.treeStructure.SimpleTree;
import io.entframework.med.dom.DomMed;
import io.entframework.med.dom.MyDomElement;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class RemoveNodeAction extends BaseTreeAction {

    private static final String ACTION_ID = "MyBatisMed.Tree.RemoveNode";

    public static AnAction getInstance() {
        return ActionManager.getInstance().getAction(ACTION_ID);
    }

    @Override
    public void doActionPerformed(@NotNull AnActionEvent e, @NotNull SimpleTree simpleTree) {
        Project project = e.getProject();
        TreeSelectionModel selectionModel = simpleTree.getSelectionModel();
        TreePath[] selectionPaths = selectionModel.getSelectionPaths();
        if (selectionPaths == null || selectionPaths.length == 0) {
            Messages.showWarningDialog("Message: Must selected one Node", "Remove Node Error");
            return;
        } else if (selectionPaths.length > 1) {
            int k = -1;
            for (TreePath treePath : selectionPaths) {
                if (k != -1 && treePath.getPathCount() != k) {
                    Messages.showWarningDialog("Message: Selected nodes must be same level", "Remove Node Error");
                    return;
                }
                k = treePath.getPathCount();
            }
        }

        String existsTitle = "Remove Node";
        String filePrompt = "Would you like to remove it?";
        int answer = Messages.showYesNoDialog(filePrompt, existsTitle, Messages.getQuestionIcon());
        if (answer == Messages.YES) {
            try {
                TreePath firstPath = selectionPaths[0];
                for (TreePath path : selectionPaths) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
                    Object userObject = node.getUserObject();
                    Object parentUserObject = parentNode.getUserObject();
                    if (userObject instanceof MyDomElement childElement
                            && parentUserObject instanceof MyDomElement parentElement) {
                        //  MedDomUtil.remove(parentElement, childElement);

                        WriteCommandAction.writeCommandAction(project, childElement.getFile()).run(childElement::undefine);
                        DefaultTreeModel model = (DefaultTreeModel) simpleTree.getModel();
                        model.removeNodeFromParent(node);
                    }
                }
                if (firstPath.getParentPath() != null) {
                    simpleTree.setSelectionPath(firstPath.getParentPath());
                }
            } catch (Exception ex) {
                Messages.showErrorDialog("Message:" + ex.getMessage(), "Remove Node Error");
            }
        }
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Object userObject) {
        if (userObject instanceof DomMed) {
            e.getPresentation().setEnabled(false);
        }
    }

}
