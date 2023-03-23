/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class TemplateEventManager {

    private Project myProject;

    public TemplateEventManager(@NotNull Project project) {
        this.myProject = project;
    }

    public void sendChanged(TemplateEvent event) {
        publisher().templateChanged(event);
    }

    private TemplateListener publisher() {
        return this.myProject.getMessageBus().syncPublisher(TemplateListener.TOPIC);
    }
}
