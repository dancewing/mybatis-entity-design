/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable;

import io.entframework.med.model.CodeTemplate;

import java.util.EventObject;

public class TemplateEvent extends EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public TemplateEvent(CodeTemplate source) {
        super(source);
    }

    public CodeTemplate getTemplate() {
        return (CodeTemplate) source;
    }

}
