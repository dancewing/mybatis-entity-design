/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med;

import com.intellij.openapi.extensions.PluginId;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class MedPluginId {
    private static final String PLUGIN_ID = "io.entframework.mybatis.generator-idea-plugin";

    private MedPluginId() {
    }

    public static @NotNull PluginId get() {
        return Objects.requireNonNull(PluginId.findId(PLUGIN_ID));
    }
}
