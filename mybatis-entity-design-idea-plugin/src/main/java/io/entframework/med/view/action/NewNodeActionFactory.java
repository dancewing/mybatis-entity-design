/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.util.ui.tree.TreeUtil;
import io.entframework.med.dom.*;
import io.entframework.med.util.TextUtil;
import io.entframework.med.view.validator.EntityValidator;

import javax.swing.tree.DefaultMutableTreeNode;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.List;

public final class NewNodeActionFactory {
    public static List<AnAction> createAction(MyDomElement baseElement, SimpleTree objectTree, DefaultMutableTreeNode parent) {
        List<AnAction> results = new ArrayList<>();
        if (baseElement instanceof DomMed jaxbMed) {
            results.addAll(createModuleAction(jaxbMed, objectTree, parent));
            results.addAll(createEnumDefinitionAction(jaxbMed, objectTree, parent));
        } else if (baseElement instanceof DomModule domModule) {
            results.addAll(createEntityAction(domModule, objectTree, parent));
        }else if (baseElement instanceof DomEntity jaxbEntity) {
            results.addAll(createEntitySubAction(jaxbEntity, objectTree, parent));
        }
        return results;
    }

    private static List<AnAction> createEntityAction(DomModule domModule, SimpleTree objectTree, DefaultMutableTreeNode parent) {
        List<AnAction> results = new ArrayList<>();
        results.add(CreateNewNodeAction.builder()
                .withTitle("Create Entity")
                .withElement(domModule)
                .withValidator(new EntityValidator(domModule, null))
                .withAfterCallable((name) -> {
                    WriteCommandAction.writeCommandAction(domModule.getFile().getProject(), domModule.getFile()).run(() -> {
                        DomEntity domEntity = domModule.addEntity();
                        domEntity.getName().setValue(name);
                        domEntity.getTable().getName().setValue(TextUtil.camelToUnderline(name));
                        DomId domId = domEntity.getAttributes().getId();
                        domId.getName().setValue("id");
                        domId.getColumn().getName().setValue("id");
                        domId.getColumn().getJdbcType().setValue(JDBCType.BIGINT);
                        PsiClass idClass = JavaPsiFacade.getInstance(domModule.getFile().getProject()).findClass("java.lang.Long", domModule.getResolveScope());
                        domId.getType().setValue(idClass);
                        DefaultMutableTreeNode treeEntityNode = new DefaultMutableTreeNode(domEntity, true);
                        treeEntityNode.add(new DefaultMutableTreeNode(domId));
                        parent.add(treeEntityNode);
                        objectTree.updateUI();
                        TreeUtil.selectNode(objectTree, treeEntityNode);
                    });
                })
                .build());
        return results;
    }

    private static List<AnAction> createEntitySubAction(DomEntity jaxbEntity, SimpleTree objectTree, DefaultMutableTreeNode parent) {
        List<AnAction> results = new ArrayList<>();
        DomAttributes attributes = jaxbEntity.getAttributes();
        if (attributes.exists()) {
            if (!attributes.getId().exists()) {
                    results.add(CreateNewNodeAction.builder()
                            .withTitle("Create primary key")
                            .withElement(jaxbEntity)
                            .withAfterCallable((name) -> {
                                WriteCommandAction.writeCommandAction(jaxbEntity.getFile().getProject(), jaxbEntity.getFile()).run(() -> {
                                    DomId domId = attributes.getId();
                                    domId.getName().setValue(name);
                                    domId.getColumn().getName().setValue(TextUtil.camelToUnderline(name));
                                    domId.getColumn().getNullable().setValue(true);
                                    domId.getColumn().getJdbcType().setValue(JDBCType.BIGINT);
                                    PsiClass idClass = JavaPsiFacade.getInstance(jaxbEntity.getFile().getProject()).findClass("java.lang.Long", jaxbEntity.getResolveScope());
                                    domId.getType().setValue(idClass);
                                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(domId, true);
                                    parent.add(node);
                                    objectTree.updateUI();
                                    TreeUtil.selectNode(objectTree, node);
                                });
                            })
                            .build());
            }

            results.add(CreateNewNodeAction.builder()
                    .withTitle("Create basic attribute")
                    .withElement(jaxbEntity)
                    .withAfterCallable((name) -> {
                        WriteCommandAction.writeCommandAction(jaxbEntity.getFile().getProject(), jaxbEntity.getFile()).run(() -> {
                            DomBasic domBasic = attributes.addBasicAttribute();
                            domBasic.getName().setValue(name);
                            domBasic.getColumn().getName().setValue(TextUtil.camelToUnderline(name));
                            domBasic.getColumn().getJdbcType().setValue(JDBCType.VARCHAR);
                            PsiClass idClass = JavaPsiFacade.getInstance(jaxbEntity.getFile().getProject()).findClass("java.lang.String", jaxbEntity.getResolveScope());
                            domBasic.getType().setValue(idClass);
                            DefaultMutableTreeNode node = new DefaultMutableTreeNode(domBasic, true);
                            parent.add(node);
                            objectTree.updateUI();
                            TreeUtil.selectNode(objectTree, node);
                        });
                    })
                    .build());

            results.add(CreateNewNodeAction.builder()
                    .withTitle("Create enum attribute")
                    .withElement(jaxbEntity)
                    .withAfterCallable((name) -> {
                        WriteCommandAction.writeCommandAction(jaxbEntity.getFile().getProject(), jaxbEntity.getFile()).run(() -> {
                            DomEnumAttribute domEnumAttribute = attributes.addEnumAttribute();
                            domEnumAttribute.getName().setValue(name);
                            domEnumAttribute.getColumn().getName().setValue(TextUtil.camelToUnderline(name));
                            domEnumAttribute.getColumn().getJdbcType().setValue(JDBCType.VARCHAR);
                            DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(domEnumAttribute, true);
                            parent.add(treeNode);
                            objectTree.updateUI();
                            TreeUtil.selectNode(objectTree, treeNode);
                        });
                    })
                    .build());
            if (!attributes.getVersionAttribute().exists()) {
                results.add(CreateNewNodeAction.builder()
                        .withTitle("Create version attribute")
                        .withElement(jaxbEntity)
                        .withAfterCallable((name) -> {
                            WriteCommandAction.writeCommandAction(jaxbEntity.getFile().getProject(), jaxbEntity.getFile()).run(() -> {
                                DomVersion domEnumAttribute = attributes.getVersionAttribute();
                                domEnumAttribute.getName().setValue(name);
                                domEnumAttribute.getColumn().getName().setValue(TextUtil.camelToUnderline(name));
                                domEnumAttribute.getColumn().getJdbcType().setValue(JDBCType.BIGINT);
                                DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(domEnumAttribute, true);
                                parent.add(treeNode);
                                objectTree.updateUI();
                                TreeUtil.selectNode(objectTree, treeNode);
                            });
                        })
                        .build());
            }

        }

        return results;
    }

    private static List<AnAction> createModuleAction(DomMed jaxbMed, SimpleTree objectTree, DefaultMutableTreeNode parent) {
        List<AnAction> results = new ArrayList<>();
        results.add(CreateNewNodeAction.builder()
                .withTitle("Create module")
                .withElement(jaxbMed)
                .withAfterCallable((name) -> {
                    WriteCommandAction.writeCommandAction(jaxbMed.getFile().getProject(), jaxbMed.getFile()).run(() -> {
                        DomModule domModule = jaxbMed.addModule();
                        domModule.getName().setValue(name);
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode(domModule, true);
                        parent.add(node);
                        objectTree.updateUI();
                        TreeUtil.selectNode(objectTree, node);
                    });
                })
                .build());
        return results;
    }
    private static List<AnAction> createEnumDefinitionAction(DomMed jaxbMed, SimpleTree objectTree, DefaultMutableTreeNode parent) {
        List<AnAction> results = new ArrayList<>();
        results.add(CreateNewNodeAction.builder()
                .withTitle("Create enum definition")
                .withElement(jaxbMed)
                .withAfterCallable((name) -> {
                    WriteCommandAction.writeCommandAction(jaxbMed.getFile().getProject(), jaxbMed.getFile()).run(() -> {
                        DomEnumDefinition domEnumDefinition = jaxbMed.addEnumDefinition();
                        domEnumDefinition.getName().setValue(name);
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode(domEnumDefinition, true);
                        parent.add(node);
                        objectTree.updateUI();
                        TreeUtil.selectNode(objectTree, node);
                    });
                })
                .build());
        return results;
    }

}
