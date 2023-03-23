/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med;

import io.entframework.med.dom.MyDomElement;
import io.entframework.med.model.ElementMeta;

import javax.xml.bind.annotation.XmlAttribute;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MedJaxbElementMetaService {

    private static final Map<Class<?>, List<ElementMeta>> rootClassInfoMap = new ConcurrentHashMap<>();

    public List<ElementMeta> getMeta(Class<? extends MyDomElement> clazz) {
        return rootClassInfoMap.computeIfAbsent(clazz, key -> {
            List<ElementMeta> results = new ArrayList<>();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(XmlAttribute.class)) {
                    XmlAttribute xmlAttribute = field.getAnnotation(XmlAttribute.class);
                    ElementMeta elementMeta = new ElementMeta();
                    elementMeta.setName(field.getName());
                    field.setAccessible(true);
                    elementMeta.setField(field);
                    elementMeta.setXmlTag(xmlAttribute.name());
                    elementMeta.setRequired(xmlAttribute.required());
                    results.add(elementMeta);
                }
            }
            return results;
        });
    }

}
