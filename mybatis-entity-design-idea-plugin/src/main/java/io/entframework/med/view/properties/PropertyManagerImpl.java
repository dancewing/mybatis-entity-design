/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties;

import com.intellij.openapi.project.Project;
import io.entframework.med.dom.MyDomElement;

public class PropertyManagerImpl extends PropertyManager {

    private Project myProject;

    public PropertyManagerImpl(Project myProject) {
        this.myProject = myProject;
    }

    @Override
    public void treeNodeSelected(MyDomElement child) {
        PropertyEvent propertyEvent = new PropertyEvent(child)
                .withMode(PropertyEvent.Mode.EDIT);
        publisher().diagramNodeSelected(propertyEvent);
    }

    @Override
    public void treeNodeCreated(MyDomElement child) {

    }

    @Override
    public void treeNodeRemoved(MyDomElement child) {

    }

    private PropertyListener publisher() {
        return this.myProject.getMessageBus().syncPublisher(PropertyListener.TOPIC);
    }

}
