/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.SimpleTree;
import io.entframework.med.view.MybatisToolwindowView;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class BaseTreeAction extends DumbAwareAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        doActionPerformed(e, getTree(project));
    }

    public abstract void doActionPerformed(@NotNull AnActionEvent e, SimpleTree simpleTree);

    public final SimpleTree getTree(Project project) {
        MybatisToolwindowView mybatisToolwindowView = project.getService(MybatisToolwindowView.class);
        return mybatisToolwindowView.getTree();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            e.getPresentation().setEnabled(false);
        } else {
            SimpleTree tree = getTree(project);
            Object treeObject = getSelectedTreeObject(tree);
            if (treeObject == null) {
                e.getPresentation().setEnabled(false);
            } else {
                e.getPresentation().setEnabled(true);
                update(e, treeObject);
            }
        }
    }

    protected void update(@NotNull AnActionEvent e, @NotNull Object userObject) {
    }

    public Object getSelectedTreeObject(SimpleTree tree) {
        if (tree == null) {
            return null;
        }
        Object component = tree.getLastSelectedPathComponent();
        if (component == null) {
            return null;
        }
        if (component instanceof DefaultMutableTreeNode treeNode) {
            return treeNode.getUserObject();
        }
        return null;
    }

}
