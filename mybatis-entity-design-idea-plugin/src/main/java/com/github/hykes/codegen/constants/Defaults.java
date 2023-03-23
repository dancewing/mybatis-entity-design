/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package com.github.hykes.codegen.constants;

import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.openapi.diagnostic.Logger;
import io.entframework.med.model.CodeGroup;
import io.entframework.med.model.CodeTemplate;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.*;

/**
 * 内置参数
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/4/16
 */
public class Defaults {

    private static final Logger LOGGER = Logger.getInstance(Defaults.class);

    public static Map<String, String> getDefaultVariables() {
        Map<String, String> context = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        context.put("YEAR", String.valueOf(calendar.get(Calendar.YEAR)));
        context.put("MONTH", String.valueOf(calendar.get(Calendar.MONTH) + 1));
        context.put("DAY", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        context.put("DATE", DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd"));
        context.put("TIME", DateFormatUtils.format(calendar.getTime(), "HH:mm:ss"));
        context.put("NOW", DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd HH:mm:ss"));
        context.put("USER", System.getProperty("user.name"));
        return context;
    }

    /**
     * 获取默认的模板
     */
    public static List<CodeGroup> getDefaultGroups() {
        // 设置默认分组和模板
        List<CodeGroup> groups = new ArrayList<>();
        try {
            List<CodeTemplate> templates = new ArrayList<>();
            CodeTemplate template = new CodeTemplate(UUID.randomUUID().toString(), "Model", "java", "${object.name}",
                    "ent/ModelTemplate.vm",
                    "${object.package}");
            templates.add(template);

            template = new CodeTemplate(UUID.randomUUID().toString(), "Service", "java", "${object.name}Service",
                    "ent/ServiceTemplate.vm",
                    "${object.package.parent}.service");
            templates.add(template);

            template = new CodeTemplate(UUID.randomUUID().toString(), "ServiceImpl", "java", "${object.name}ServiceImpl",
                    "ent/ServiceImplTemplate.vm",
                    "${object.package.parent}.service.impl");
            templates.add(template);

            template = new CodeTemplate(UUID.randomUUID().toString(), "DynamicSqlSupport", "java", "${object.name}DynamicSqlSupport",
                    "ent/DynamicSqlSupport.groovy",
                    "${object.package}", CodeTemplate.TemplateLanguage.GROOVY, CodeTemplate.TemplateLevel.ENTITY);
            templates.add(template);

            template = new CodeTemplate(UUID.randomUUID().toString(), "MapperInterface", "java", "${object.name}Mapper",
                    "ent/MapperInterface.ftl",
                    "${object.package.parent}.mapper", CodeTemplate.TemplateLanguage.FREEMARKER, CodeTemplate.TemplateLevel.ENTITY);
            templates.add(template);

            CodeGroup modelGroup = new CodeGroup(UUID.randomUUID().toString(), "ent", templates);
            groups.add(modelGroup);
        } catch (Exception e) {
            LOGGER.error(StringUtils.getStackTraceAsString(e));
        }
        return groups;
    }

}
