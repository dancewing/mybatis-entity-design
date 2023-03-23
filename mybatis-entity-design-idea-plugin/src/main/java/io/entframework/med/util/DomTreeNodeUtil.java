/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.util;

import io.entframework.med.dom.*;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;
import java.util.Optional;

public final class DomTreeNodeUtil {

    public static void convert(DefaultMutableTreeNode root, DomMed domMed) {
        domMed.getModules().forEach(domModule -> {
            root.add(convertModule(domModule));
        });
        List<DomEnumDefinition> enumDefinitions = domMed.getEnumDefinitions();

        enumDefinitions.forEach(ed -> {
            DefaultMutableTreeNode enumDefinitionsNode = new DefaultMutableTreeNode(ed, true);
            ed.getEnums().forEach(medDomEnum -> {
                DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(medDomEnum, true);
                Optional.ofNullable(medDomEnum.getFields()).ifPresent(jaxbEnumFields -> {
                    jaxbEnumFields
                            .forEach(jaxbEnumField -> treeNode.add(new DefaultMutableTreeNode(jaxbEnumField, false)));
                });
                enumDefinitionsNode.add(treeNode);
            });
            root.add(enumDefinitionsNode);
        });

    }

    public static DefaultMutableTreeNode convertModule(DomModule domModule) {
        DefaultMutableTreeNode domModuleNode = new DefaultMutableTreeNode(domModule, true);
        List<DomEntity> jaxbEntities = domModule.getEntities();
        jaxbEntities.forEach(jaxbEntity -> {
            domModuleNode.add(convertEntity(jaxbEntity));
        });
        return domModuleNode;
    }

    public static DefaultMutableTreeNode convertEntity(DomEntity domEntity) {
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(domEntity, true);
        DomAttributes attributes = domEntity.getAttributes();
        if (attributes != null && attributes.exists()) {
            if (attributes.getId() !=null && attributes.getId().exists()) {
                Optional.ofNullable(attributes.getId())
                        .ifPresent(jaxbId -> treeNode.add(new DefaultMutableTreeNode(jaxbId, false)));
            }
            Optional.ofNullable(attributes.getBasicAttributes())
                    .ifPresent(jaxbBasics -> jaxbBasics
                            .forEach(jaxbBasic -> treeNode.add(new DefaultMutableTreeNode(jaxbBasic, false))));
            Optional.ofNullable(attributes.getEnumAttributes())
                    .ifPresent(jaxbEnumAttributes -> jaxbEnumAttributes.forEach(jaxbEnumAttribute -> treeNode
                            .add(new DefaultMutableTreeNode(jaxbEnumAttribute, false))));
            if (attributes.getVersionAttribute()!=null && attributes.getVersionAttribute().exists()) {
                Optional.ofNullable(attributes.getVersionAttribute())
                        .ifPresent(jaxbVersion -> treeNode.add(new DefaultMutableTreeNode(jaxbVersion, false)));

            }
            Optional.ofNullable(attributes.getManyToOneAttributes())
                    .ifPresent(jaxbManyToOnes -> jaxbManyToOnes
                            .forEach(jaxbManyToOne -> treeNode.add(new DefaultMutableTreeNode(jaxbManyToOne, false))));
            Optional.ofNullable(attributes.getOneToManyAttributes())
                    .ifPresent(jaxbOneToManies -> jaxbOneToManies
                            .forEach(jaxbOneToMany -> treeNode.add(new DefaultMutableTreeNode(jaxbOneToMany, false))));
        }
        return treeNode;
    }
}
