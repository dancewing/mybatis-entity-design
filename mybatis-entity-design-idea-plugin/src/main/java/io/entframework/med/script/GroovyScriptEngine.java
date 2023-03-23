/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.script;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.ide.script.IdeScriptEngine;
import com.intellij.ide.script.IdeScriptEngineManager;
import com.intellij.openapi.util.text.StringUtil;
import io.entframework.med.MedPluginId;
import io.entframework.med.util.ScriptUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class GroovyScriptEngine implements ScriptEngine {
    @Override
    public Object eval(@NotNull String path, @NotNull Map<String, Object> context) throws ScriptException {
        IdeScriptEngine ideScriptEngine = getEngine();
        String content = ScriptUtil.getContent(path);
        if (StringUtil.isEmptyOrSpaces(content)) {
            throw new ScriptException("Can't load template content from :" + path);
        }
        if (ideScriptEngine != null) {
            context.forEach(ideScriptEngine::setBinding);
            try {
                return ideScriptEngine.eval(content);
            } catch (Exception ex) {
                throw new ScriptException(ex.getMessage());
            }

        }
        return null;
    }

    private static @Nullable IdeScriptEngine getEngine() {

        IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(MedPluginId.get());
        plugin = plugin != null ? plugin : PluginManagerCore.getPlugin(PluginManagerCore.CORE_ID);
        IdeaPluginDescriptor descriptor = Objects.requireNonNull(plugin);
        ClassLoader loader = descriptor.getPluginClassLoader();
        return IdeScriptEngineManager.getInstance()
                .getEngineByFileExtension("groovy", loader);
    }
}
