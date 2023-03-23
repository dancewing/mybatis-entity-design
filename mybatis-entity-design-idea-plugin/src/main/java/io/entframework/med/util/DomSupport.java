/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.util;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import com.intellij.util.xml.GenericDomValue;
import io.entframework.med.dom.*;
import io.entframework.med.uml.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class DomSupport {
    public static DomMed getMed(XmlFile xmlFile) {
        final DomManager domManager = DomManager.getDomManager(xmlFile.getProject());
        final DomFileElement<DomMed> fileElement = domManager.getFileElement(xmlFile, DomMed.class);
        return fileElement != null ? fileElement.getRootElement() : null;
    }

    public static MedDiagramData convertToDiagramData(@NotNull DomMed domMed) {

        Map<String, MedEntityNodeData> entities = new HashMap<>();
        Map<String, MedEnumNodeData> enums = new HashMap<>();

        List<DomEntity> jaxbEntities = new ArrayList<>();
        domMed.getModules()
                .forEach(jaxbEntityMappings -> jaxbEntities.addAll(jaxbEntityMappings.getEntities()));
        List<DomEnum> jaxbEnums = new ArrayList<>();
        domMed.getEnumDefinitions().forEach(enumDefinitions -> jaxbEnums.addAll(enumDefinitions.getEnums()));
        List<MedEntityNodeData> entityNodeDataList = convertEntity(jaxbEntities);
        List<MedEnumNodeData> enumNodeDataList = convertEnum(jaxbEnums);
        entityNodeDataList
                .forEach(medEntityNodeData -> entities.put(medEntityNodeData.getName(), medEntityNodeData));
        enumNodeDataList.forEach(medEnumNodeData -> enums.put(medEnumNodeData.getName(), medEnumNodeData));

        List<MedEntityNodeLink> entityLinks = new ArrayList<>();
        List<MedEnumNodeLink> enumLinks = new ArrayList<>();

        for (var jaxbEntity : jaxbEntities) {
            String key = StringUtil.defaultIfEmpty(jaxbEntity.getName().getStringValue(), "").trim();
            if (entities.containsKey(key)) {

                if (jaxbEntity.getAttributes()!=null && jaxbEntity.getAttributes().exists()) {
                    List<DomManyToOne> manyToOneAttributes = jaxbEntity.getAttributes().getManyToOneAttributes();
                    manyToOneAttributes.stream()
                            .filter(jaxbManyToOne -> !StringUtil.isEmptyOrSpaces(getValue(jaxbManyToOne.getTargetEntity(), "")))
                            .forEach(jaxbManyToOne -> {
                                String targetEntity = StringUtil.defaultIfEmpty(getValue(jaxbManyToOne.getTargetEntity(), ""), "");
                                if (entities.containsKey(targetEntity)) {
                                    entityLinks.add(new MedEntityNodeLink(MedEntityNodeLinkType.MANY_TO_ONE,
                                            entities.get(targetEntity), entities.get(key)));
                                }
                            });

                    List<DomOneToMany> oneToManyAttributes = jaxbEntity.getAttributes().getOneToManyAttributes();
                    oneToManyAttributes.stream()
                            .filter(jaxbOneToMany -> !StringUtil.isEmptyOrSpaces(getValue(jaxbOneToMany.getTargetEntity(), "")))
                            .forEach(jaxbOneToMany -> {
                                String targetEntity = getValue(jaxbOneToMany.getTargetEntity(), "");
                                if (entities.containsKey(targetEntity)) {
                                    entityLinks.add(new MedEntityNodeLink(MedEntityNodeLinkType.ONE_TO_MANY,
                                            entities.get(targetEntity), entities.get(key)));
                                }
                            });

                    List<DomEnumAttribute> enumAttributes = jaxbEntity.getAttributes().getEnumAttributes();
                    enumAttributes.forEach(jaxbEnumAttribute -> {
                        var enumNodeData = enums.get(getValue(jaxbEnumAttribute.getTargetEnum(), ""));
                        if (enumNodeData != null) {
                            enumLinks.add(new MedEnumNodeLink(entities.get(key), enumNodeData));
                        }
                    });
                }
            }
        }
        return new MedDiagramData(entityNodeDataList, enumNodeDataList, entityLinks, enumLinks);
    }

    public static List<MedEntityNodeData> convertEntity(List<DomEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        List<MedEntityNodeData> results = new ArrayList<>();
        entities.forEach(jaxbEntity -> {
            MedEntityNodeData nodeData = new MedEntityNodeData(jaxbEntity);
            nodeData.setProperties(toEntityProperties(jaxbEntity.getAttributes()));
            results.add(nodeData);
        });
        return results;
    }

    public static List<MedEnumNodeData> convertEnum(List<DomEnum> enums) {
        if (enums == null || enums.isEmpty()) {
            return Collections.emptyList();
        }
        List<MedEnumNodeData> results = new ArrayList<>();
        enums.forEach(jaxbEnum -> {
            MedEnumNodeData nodeData = new MedEnumNodeData(jaxbEnum);
            nodeData.setOptions(toEnumOptions(jaxbEnum.getFields()));
            results.add(nodeData);
        });
        return results;
    }

    public static <T> T getValue(GenericDomValue<T> domValue, T defaultValue) {
        if (domValue == null) {
            return defaultValue;
        }
        if (StringUtil.isEmptyOrSpaces(domValue.getRawText())) {
            return defaultValue;
        }
        return domValue.getValue();
    }

    public static String getPsiClassValue(GenericDomValue<PsiClass> domValue, String defaultValue) {
        PsiClass psiClass = domValue.getValue();
        if (psiClass!=null) {
            return psiClass.getQualifiedName();
        }
        return defaultValue;
    }

    private static List<MedEnumNodeItem> toEnumOptions(List<DomEnumField> fields) {
        if (fields == null) {
            return Collections.emptyList();
        }
        List<MedEnumNodeItem> results = new ArrayList<>();
        fields.forEach(jaxbEnumField -> results
                .add(new MedEnumNodeItem(getValue(jaxbEnumField.getName(), ""))));

        return results;
    }

    private static List<MedEntityNodeField> toEntityProperties(DomAttributes attributes) {
        if (attributes == null) {
            return Collections.emptyList();
        }
        List<MedEntityNodeField> fields = new ArrayList<>();
        if (attributes.getId()!=null && attributes.getId().exists()) {
            Optional.ofNullable(attributes.getId())
                    .ifPresent(jaxbId -> fields.add(new MedEntityNodeField(getValue(jaxbId.getName(), ""),
                            getPsiClassValue(jaxbId.getType(), ""),
                            getValue(jaxbId.getDescription(), ""),
                            jaxbId.getIcon())));
        }

        Optional.ofNullable(attributes.getBasicAttributes()).ifPresent(jaxbBasics -> jaxbBasics.forEach(jaxbBasic -> {
            fields.add(new MedEntityNodeField(getValue(jaxbBasic.getName(), ""),
                    getPsiClassValue(jaxbBasic.getType(), ""),
                    getValue(jaxbBasic.getDescription(), ""), jaxbBasic.getIcon()));
        }));
        Optional.ofNullable(attributes.getEnumAttributes())
                .ifPresent(jaxbEnumAttributes -> jaxbEnumAttributes.forEach(jaxbEnumAttribute -> {
                    fields.add(new MedEntityNodeField(getValue(jaxbEnumAttribute.getName(), ""),
                            getValue(jaxbEnumAttribute.getTargetEnum(), ""),
                            getValue(jaxbEnumAttribute.getDescription(), ""), jaxbEnumAttribute.getIcon()));
                }));

        if (attributes.getVersionAttribute()!=null && attributes.getVersionAttribute().exists()) {
            Optional.ofNullable(attributes.getVersionAttribute()).ifPresent(jaxbVersion -> {

                fields.add(new MedEntityNodeField(getValue(jaxbVersion.getName(), ""),
                        getPsiClassValue(jaxbVersion.getType(), ""),
                        getValue(jaxbVersion.getDescription(), ""), jaxbVersion.getIcon()));
            });
        }
        Optional.ofNullable(attributes.getManyToOneAttributes())
                .ifPresent(jaxbManyToOnes -> jaxbManyToOnes.forEach(jaxbManyToOne -> {
                    fields.add(new MedEntityNodeField(getValue(jaxbManyToOne.getName(), ""),
                            getValue(jaxbManyToOne.getTargetEntity(), ""),
                            getValue(jaxbManyToOne.getDescription(), ""), jaxbManyToOne.getIcon()));
                }));
        Optional.ofNullable(attributes.getOneToManyAttributes())
                .ifPresent(jaxbOneToManies -> jaxbOneToManies.forEach(jaxbOneToMany -> {
                    fields.add(new MedEntityNodeField(getValue(jaxbOneToMany.getName(), ""),
                            getValue(jaxbOneToMany.getTargetEntity(), ""),
                            getValue(jaxbOneToMany.getDescription(), ""), jaxbOneToMany.getIcon()));
                }));
        return fields;
    }
}
