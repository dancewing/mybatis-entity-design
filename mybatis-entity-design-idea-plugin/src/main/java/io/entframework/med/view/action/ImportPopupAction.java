/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.action;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.ui.treeStructure.SimpleTree;
import io.entframework.med.dom.DomModule;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.InputEvent;

public class ImportPopupAction extends BaseTreeAction {

    private static final String ACTION_ID = "MyBatisMed.Tree.Import";

    public static AnAction getInstance() {
        return ActionManager.getInstance().getAction(ACTION_ID);
    }

    @Override
    public void doActionPerformed(@NotNull AnActionEvent e, SimpleTree simpleTree) {
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        populate(actionGroup);
        InputEvent inputEvent = e.getInputEvent();
        if (inputEvent != null) {
            Component component = (Component) inputEvent.getSource();
            if (component.isShowing()) {
                ListPopup popup = JBPopupFactory.getInstance()
                        .createActionGroupPopup("", actionGroup, e.getDataContext(),
                                JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, true, null, 10);
                showBelowComponent(popup, component);
            }
        }
    }

    // dynamically
    private void populate(DefaultActionGroup actionGroup) {
        actionGroup.add(ImportFromScriptAction.getInstance());
        actionGroup.add(ImportFromDataSourceAction.getInstance());
    }

    private void showBelowComponent(ListPopup popup, Component component) {
        Point locationOnScreen = component.getLocationOnScreen();
        Point location = new Point((int) (locationOnScreen.getX()),
                (int) locationOnScreen.getY() + component.getHeight());
        popup.showInScreenCoordinates(component, location);
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Object userObject) {
        if (userObject instanceof DomModule) {
            e.getPresentation().setEnabled(true);
        } else {
            e.getPresentation().setEnabled(false);
        }
    }

}
