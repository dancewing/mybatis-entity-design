/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.treeStructure.SimpleTree;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MybatisToolwindowView implements Disposable {

    private static final Logger log = Logger.getInstance(MybatisToolwindowView.class);

    private final @NotNull Project myProject;

    private ContentManager myContentManager;

    private MybatisDesignerWindowPanel mybatisDesignerWindow;

    public MybatisToolwindowView(@NotNull Project myProject) {
        this.myProject = myProject;
    }

    public void initToolWindow(@NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content designerContent = contentFactory.createContent(null, "", false);

        mybatisDesignerWindow = new MybatisDesignerWindowPanel(myProject);
        designerContent.setComponent(mybatisDesignerWindow);
        Disposer.register(this, mybatisDesignerWindow);

        myContentManager = toolWindow.getContentManager();

        designerContent.setCloseable(true);
        myContentManager.addContent(designerContent);
    }

    @Override
    public void dispose() {

    }

    public void updateStructureUI() {
        if (mybatisDesignerWindow != null) {
            mybatisDesignerWindow.init();
        }

    }

    public void resetStructureUI() {
        if (mybatisDesignerWindow != null) {
            mybatisDesignerWindow.reset();
        }
    }

    public void setPropertyEditor(JComponent component) {
        mybatisDesignerWindow.setPropertyEditor(component);
    }

    public SimpleTree getTree() {
        return mybatisDesignerWindow.getTree();
    }

}
