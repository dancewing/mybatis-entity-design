/*******************************************************************************
 * Copyright (c) 2019-2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;

public class MybatisBuilderToolWindowFactory implements ToolWindowFactory, DumbAware {

    public static final String TOOL_WINDOW_ID = "MED Designer";

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        project.getService(MybatisToolwindowView.class).initToolWindow(toolWindow);
    }

}
