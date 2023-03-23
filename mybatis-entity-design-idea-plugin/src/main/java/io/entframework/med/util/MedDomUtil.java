/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import io.entframework.med.dom.DomColumn;
import io.entframework.med.dom.DomEntity;
import io.entframework.med.dom.DomMed;
import io.entframework.med.dom.DomModule;
import io.entframework.med.model.Entity;
import io.entframework.med.model.FieldImpl;
import io.entframework.med.model.GeneratorRuntime;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class MedDomUtil {

    private static final Logger log = Logger.getInstance(MedDomUtil.class);

    public static boolean save(DomMed jaxbMed, Project project) {
//        try {
//            XmlFile xmlFile = jaxbMed.getXmlFile();
//            Document document = FileDocumentManager.getInstance().getDocument(xmlFile.getVirtualFile());
//            if (document != null) {
//                MedJaxbService medJaxbService = project.getService(MedJaxbService.class);
//                String text = medJaxbService.toString(jaxbMed);
//                WriteCommandAction.runWriteCommandAction(project, () -> document.setText(text));
//            }
//        } catch (Exception ex) {
//            NotificationHelper.getInstance()
//                    .createNotification(ExceptionUtil.getThrowableText(ExceptionUtil.getRootCause(ex), "com.intellij."),
//                            NotificationType.ERROR)
//                    .notify(project);
//            log.error(ex.getMessage(), ex);
//            return false;
//        }
        return true;
    }

//    public static void remove(BaseElement parent, BaseElement child) {
//        if (child instanceof JaxbModule jaxbModule && parent instanceof JaxbMed jaxbMed) {
//            jaxbMed.getModules().remove(jaxbModule);
//        }
//        if (parent instanceof JaxbEntity jaxbEntity) {
//            if (child instanceof JaxbId) {
//                jaxbEntity.getAttributes().setId(null);
//            }
//            if (child instanceof JaxbBasic jaxbBasic) {
//                jaxbEntity.getAttributes().getBasicAttributes().remove(jaxbBasic);
//            }
//            if (child instanceof JaxbEnumAttribute enumAttribute ) {
//                jaxbEntity.getAttributes().getEnumAttributes().remove(enumAttribute);
//            }
//            if (child instanceof JaxbVersion) {
//                jaxbEntity.getAttributes().setVersionAttribute(null);
//            }
//            if (child instanceof JaxbManyToOne jaxbManyToOne) {
//                jaxbEntity.getAttributes().getManyToOneAttributes().remove(jaxbManyToOne);
//            }
//            if (child instanceof JaxbOneToMany jaxbOneToMany) {
//                jaxbEntity.getAttributes().getOneToManyAttributes().remove(jaxbOneToMany);
//            }
//        }
//    }

    public static void addEntity(List<Entity> entities, DomModule jaxbModule) {
//        entities.forEach(entity -> {
//            DomEntity jaxbEntity = ObjectFactoryUtil.getInstance().createJaxbEntity();
//            jaxbEntity.setName(entity.getName().getText());
//            if (!StringUtil.isEmptyOrSpaces(entity.getComment())) {
//                jaxbEntity.setDescription(entity.getComment());
//            }
//            JaxbTable jaxbTable = ObjectFactoryUtil.getInstance().createJaxbTable();
//            jaxbTable.setName(entity.getTable());
//            jaxbEntity.setTable(jaxbTable);
//            JaxbAttributes attributes = ObjectFactoryUtil.getInstance().createJaxbAttributes();
//            jaxbEntity.setAttributes(attributes);
//            List<Field> fields = entity.getFields();
//            for (Field field : fields) {
//                if (field.isPK()) {
//                    JaxbId jaxbId = ObjectFactoryUtil.getInstance().createJaxbId();
//                    jaxbId.setName(field.getName());
//                    jaxbId.setType(field.getJavaType().getFullyQualifiedName());
//                    if (!StringUtil.isEmptyOrSpaces(field.getComment())) {
//                        jaxbId.setDescription(field.getComment());
//                    }
//                    JaxbColumn column = ObjectFactoryUtil.getInstance().createJaxbColumn();
//                    column.setName(field.getColumn());
//                    column.setJdbcType(JaxbJdbcType.valueOf(StringUtil.toUpperCase(field.getColumnType())));
//                    if (field.getLength() > 0) {
//                        column.setLength(field.getLength());
//                    }
//                    if (field.getScale() > 0) {
//                        column.setScale(field.getScale());
//                    }
//                    if (!field.isNullable()) {
//                        column.setNullable(field.isNullable());
//                    }
//                    jaxbId.setColumn(column);
//                    attributes.setId(jaxbId);
//                } else {
//                    JaxbBasic basic = ObjectFactoryUtil.getInstance().createJaxbBasic();
//                    basic.setName(field.getName());
//                    basic.setType(field.getJavaType().getFullyQualifiedName());
//                    if (!StringUtil.isEmptyOrSpaces(field.getComment())) {
//                        basic.setDescription(field.getComment());
//                    }
//                    JaxbColumn column = ObjectFactoryUtil.getInstance().createJaxbColumn();
//                    column.setName(field.getColumn());
//                    column.setJdbcType(JaxbJdbcType.valueOf(StringUtil.toUpperCase(field.getJdbcType().getName())));
//                    if (field.getLength() > 0) {
//                        column.setLength(field.getLength());
//                    }
//                    if (field.getScale() > 0) {
//                        column.setScale(field.getScale());
//                    }
//                    if (!field.isNullable()) {
//                        column.setNullable(field.isNullable());
//                    }
//                    basic.setColumn(column);
//                    attributes.getBasicAttributes().add(basic);
//                }
//            }
//
//            jaxbModule.getEntities().add(jaxbEntity);
//        });
    }

    public static GeneratorRuntime convertToRuntime(@NotNull DomModule jaxbModule, @NotNull List<DomEntity> entities) {
        GeneratorRuntime generatorRuntime = new GeneratorRuntime();
//        ModuleImpl module = new ModuleImpl(JavaPackageImpl.of(jaxbModule.getPackage()), jaxbModule.getName());
//
//        List<Entity> results = new ArrayList<>();
//
//        entities.forEach(jaxbEntity -> {
//            EntityImpl entity = new EntityImpl(module.getPackage());
//            entity.setName(Name.of(jaxbEntity.getName()));
//
//            if (jaxbEntity.getTable() != null) {
//                entity.setTable(jaxbEntity.getTable().getName());
//            }
//            if (StringUtil.isEmptyOrSpaces(entity.getTable())) {
//                entity.setTable(TextUtil.camelToUnderline(entity.getName().getText()));
//            }
//
//            List<Field> fields = new ArrayList<>();
//            JaxbAttributes attributes = jaxbEntity.getAttributes();
//            if (attributes != null) {
//                JaxbId jaxbId = attributes.getId();
//                if (jaxbId != null) {
//                    FieldImpl field = new FieldImpl();
//                    field.setName(jaxbId.getName());
//                    field.setJavaType(new JavaTypeImpl(jaxbId.getType()));
//                    field.setComment(jaxbId.getDescription());
//                    if (jaxbId.getColumn() != null) {
//                        copyColumnValue(field, jaxbId.getColumn());
//                    }
//                    fields.add(field);
//                }
//                List<JaxbBasic> basicAttributes = attributes.getBasicAttributes();
//                basicAttributes.forEach(jaxbBasic -> {
//                    FieldImpl field = new FieldImpl();
//                    field.setName(jaxbBasic.getName());
//                    field.setJavaType(new JavaTypeImpl(jaxbBasic.getType()));
//                    field.setComment(jaxbBasic.getDescription());
//                    if (jaxbBasic.getColumn() != null) {
//                        copyColumnValue(field, jaxbBasic.getColumn());
//                    }
//                    fields.add(field);
//                });
//            }
//
//
//            entity.setFields(fields);
//            results.add(entity);
//        });
//
//        module.setEntities(results);
//        generatorRuntime.setModule(module);
        return generatorRuntime;
    }

    private static void copyColumnValue(FieldImpl field, DomColumn column) {
//        field.setNullable(column.isNullable() == null || column.isNullable());
//        field.setLength(column.getLength() == null ? -1 : column.getLength());
//        field.setScale(column.getScale() == null ? 0 : column.getScale());
//        field.setColumn(column.getName());
//        field.setTypeHandler(column.getTypeHandler());
    }

}
