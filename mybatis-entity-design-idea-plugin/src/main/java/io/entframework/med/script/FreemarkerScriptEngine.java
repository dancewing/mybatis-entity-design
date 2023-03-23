/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.script;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.entframework.med.util.ScriptUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class FreemarkerScriptEngine implements ScriptEngine {
    @Override
    public Object eval(@NotNull String path, @NotNull Map<String, Object> context) throws ScriptException {

        try {
            File basePath = ScriptUtil.getScriptRoot();
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
            cfg.setDirectoryForTemplateLoading(ScriptUtil.getScriptRoot());
            TemplateLoader templateLoader = new FileTemplateLoader(basePath);
            cfg.setTemplateLoader(templateLoader);
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            Template template = cfg.getTemplate(path);
            Writer writer = new StringWriter();
            template.process(context, writer);
            return writer.toString();
        } catch (IOException | TemplateException ex) {
            throw new ScriptException(ex.getMessage());
        }
    }
}
