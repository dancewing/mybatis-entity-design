/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.script;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface ScriptEngine {
    Object eval(@NotNull String path, @NotNull Map<String, Object> context) throws ScriptException;
}
