/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface TemplateListener extends EventListener {
    default void templateChanged(TemplateEvent event) {
    }

    @Topic.ProjectLevel
    Topic<TemplateListener> TOPIC = Topic.create("template property listener", TemplateListener.class);

}
