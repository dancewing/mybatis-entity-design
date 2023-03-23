/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

import io.entframework.med.util.TextUtil;

public class Name {
    private final String identifier;

    private Name(String identifier) {
        this.identifier = identifier;
    }

    public static Name of(String identifier) {
        return new Name(identifier);
    }

    public String toString() {
        return identifier;
    }

    public String getText() {
        return identifier;
    }

    public String getPropertyName() {
        return TextUtil.getValidPropertyName(this.identifier);
    }
}
