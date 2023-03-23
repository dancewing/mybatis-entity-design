/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.script;

import org.jetbrains.annotations.NotNull;

public class Binding<T> {

    public final String name;

    public Binding(@NotNull String name) {
        this.name = name;
    }

}