/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.util;

import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.openapi.util.text.StringUtil;
import io.entframework.med.configurable.model.ReverseSettings;
import io.entframework.med.model.*;
import io.entframework.med.service.JavaTypeResolver;
import io.entframework.med.service.JavaTypeResolverDefaultImpl;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReverseUtil {

    public static List<Entity> apply(List<Entity> entities, ReverseSettings settings) {
        if (entities == null || entities.isEmpty() || settings == null) {
            return Collections.emptyList();
        }

        JavaTypeResolver typeResolver = new JavaTypeResolverDefaultImpl(settings.isForceBigDecimals(), settings.isUseJSR310Types());

        entities.forEach(entity -> {

            if (!StringUtil.isEmptyOrSpaces(settings.getSearchString()) && settings.getReplaceString() != null) {
                Pattern pattern = Pattern.compile(settings.getSearchString());
                Matcher matcher = pattern.matcher(entity.getTable());
                String entityName = TextUtil.underlineToCamel(matcher.replaceAll(settings.getReplaceString()), true);
                entity.setName(Name.of(entityName));
            } else {
                entity.setName(Name.of(TextUtil.underlineToCamel(entity.getTable(), true)));
            }

            List<Field> fields = entity.getFields();
            if (settings.getIgnoreFields() != null) {
                List<String> ignoreFields = StringUtils.splitToList(settings.getIgnoreFields(), ",", true);
                if (!ignoreFields.isEmpty()) {
                    fields = fields.stream()
                            .filter(field -> !ignoreFields.contains(field.getName().toUpperCase()))
                            .toList();
                }
            }
            fields.forEach(field -> {
                JavaType fieldType = typeResolver.calculateJavaType(field);
                if (fieldType != null) {
                    field.setJavaType(fieldType);
                } else {
                    field.setJavaType(new JavaTypeImpl("java.lang.String"));
                }
            });
            entity.setFields(fields);
        });
        return entities;
    }

}
