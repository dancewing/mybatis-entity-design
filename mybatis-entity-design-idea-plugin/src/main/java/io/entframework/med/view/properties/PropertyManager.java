/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties;

import io.entframework.med.dom.MyDomElement;

public abstract class PropertyManager {

    public abstract void treeNodeSelected(MyDomElement child);

    public abstract void treeNodeCreated(MyDomElement child);

    public abstract void treeNodeRemoved(MyDomElement child);
}
