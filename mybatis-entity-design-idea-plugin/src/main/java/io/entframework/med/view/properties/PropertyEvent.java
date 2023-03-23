/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties;

import io.entframework.med.dom.MyDomElement;

import java.util.EventObject;

public class PropertyEvent extends EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public PropertyEvent(MyDomElement source) {
        super(source);
    }

    private Mode mode;


    @Override
    public MyDomElement getSource() {
        return (MyDomElement) super.getSource();
    }

    public Mode getMode() {
        return mode;
    }

    public PropertyEvent withMode(Mode mode) {
        this.mode = mode;
        return this;
    }

    public enum Mode {

        NEW, EDIT, NONE,

    }

}
