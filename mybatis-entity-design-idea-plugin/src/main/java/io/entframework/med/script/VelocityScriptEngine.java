/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.script;

import com.intellij.openapi.util.text.StringUtil;
import io.entframework.med.util.ScriptUtil;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

public class VelocityScriptEngine implements ScriptEngine {
    @Override
    public Object eval(@NotNull String path, @NotNull Map<String, Object> context) throws ScriptException {
        if (StringUtil.isEmptyOrSpaces(path)) {
            return null;
        }
        File scriptRoot = ScriptUtil.getScriptRoot();
        Properties properties = new Properties();
        //设置velocity资源加载方式为file
        properties.setProperty("resource.loader", "file");
        properties.setProperty("path", scriptRoot.getAbsolutePath());
        //设置velocity资源加载方式为file时的处理类
        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
        properties.setProperty("file.resource.loader.path", scriptRoot.getAbsolutePath());
        properties.put("input.encoding", "UTF-8");
        properties.put("output.encoding", "UTF-8");
        //实例化一个VelocityEngine对象
        VelocityEngine velocityEngine = new VelocityEngine(properties);
        ExtendedProperties extendedProperties = new ExtendedProperties();
        extendedProperties.setProperty("input.encoding", "UTF-8");
        extendedProperties.setProperty("output.encoding", "UTF-8");
        extendedProperties.setProperty("path", scriptRoot.getAbsolutePath());
        velocityEngine.setExtendedProperties(extendedProperties);
        velocityEngine.init();

        Template template = velocityEngine.getTemplate(path, "UTF-8");

        VelocityContext velocityContext = new VelocityContext();
        context.forEach(velocityContext::put);
        StringWriter stringWriter = new StringWriter();
        template.merge(velocityContext, stringWriter);
        return stringWriter.toString();
    }
}
