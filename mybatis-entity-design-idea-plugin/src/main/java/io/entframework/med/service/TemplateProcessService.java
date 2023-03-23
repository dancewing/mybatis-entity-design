/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.service;

import com.github.hykes.codegen.utils.VelocityUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import io.entframework.med.idea.NotificationHelper;
import io.entframework.med.model.*;
import io.entframework.med.model.Enum;
import io.entframework.med.model.Module;
import io.entframework.med.script.GeneratorBindings;
import io.entframework.med.script.ScriptEngine;
import io.entframework.med.script.ScriptEngineFactory;
import io.entframework.med.script.ScriptException;
import org.apache.velocity.VelocityContext;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TemplateProcessService {

    private static final Logger LOGGER = Logger.getInstance(TemplateProcessService.class);
    private Project myProject;

    public TemplateProcessService(@NotNull Project project) {
        this.myProject = project;
    }

    public List<RuntimeTemplate> process(@NotNull GeneratorRuntime generatorRuntime, @NotNull List<RuntimeTemplate> templates) {

        List<RuntimeTemplate> results = new ArrayList<>();

        Module module = generatorRuntime.getModule();
        List<RuntimeTemplate> runtimeTemplates = templates.stream()
                .filter(runtimeTemplate -> runtimeTemplate.getLevel() == CodeTemplate.TemplateLevel.MODULE)
                .toList();

        runtimeTemplates.forEach(t -> process(t, module).ifPresent(results::add));

        List<Entity> entities = generatorRuntime.getEntities();
        runtimeTemplates = templates.stream()
                .filter(runtimeTemplate -> runtimeTemplate.getLevel() == CodeTemplate.TemplateLevel.ENTITY)
                .toList();
        runtimeTemplates.forEach(t -> entities.forEach(entity -> process(t, entity).ifPresent(results::add)));

        List<Enum> enums = generatorRuntime.getEnums();
        runtimeTemplates = templates.stream()
                .filter(runtimeTemplate -> runtimeTemplate.getLevel() == CodeTemplate.TemplateLevel.ENUM)
                .toList();
        runtimeTemplates.forEach(t -> enums.forEach(eum -> process(t, eum).ifPresent(results::add)));
        return results;
    }

    private Optional<RuntimeTemplate> process(RuntimeTemplate t, GeneratorObject object) {
        final ScriptEngine engine = ScriptEngineFactory.getInstance().getEngine(t.getLanguage().getExtension());
        if (engine != null) {
            RuntimeTemplate template = t.copy();
            try {
                Object value = engine.eval(template.getTemplatePath(), prepareEngine(object, template));
                if (value instanceof String code) {
                    template.setContent(code);
                }
                return Optional.of(template);
            } catch (ScriptException ex) {
                NotificationHelper.getInstance().notifyError(ex.getMessage(), myProject);
                LOGGER.error(ex.getMessage(), ex);
            }

        }
        return Optional.empty();
    }

    public static Map<String, Object> prepareEngine(
            @NotNull GeneratorObject object, @NotNull RuntimeTemplate template) {
        Map<String, Object> map = new HashMap<>();
        map.put("object", object);
        VelocityContext velocityContext = new VelocityContext(map);
        template.setFileName(VelocityUtil.evaluate(velocityContext, template.getFileName()));
        template.setPackage(VelocityUtil.evaluate(velocityContext, template.getPackage()));

        Map<String, Object> context = new HashMap<>();
        context.put(GeneratorBindings.GENERATOR_OBJECT, object);
        context.put(GeneratorBindings.GENERATOR_RUNTIME, template);
        return context;
    }
}
