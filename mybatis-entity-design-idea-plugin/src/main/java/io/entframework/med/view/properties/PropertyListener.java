/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface PropertyListener extends EventListener {

    default void diagramNodeSelected(PropertyEvent event) {
    }

    default void medFileChanged() {
    }

    default void sendCreating(PropertyEvent event) {
    }

    default void nodeAdded(PropertyEvent event) {

    }

    @Topic.ProjectLevel
    Topic<PropertyListener> TOPIC = Topic.create("med property listener", PropertyListener.class);

}
