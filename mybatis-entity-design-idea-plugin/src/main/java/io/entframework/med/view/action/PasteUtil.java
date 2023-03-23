/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.action;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.ui.treeStructure.SimpleTree;
import io.entframework.med.dom.*;
import io.entframework.med.util.DomTreeNodeUtil;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

public final class PasteUtil {
    private PasteUtil(){}

    public static void paste(DefaultMutableTreeNode currentNode, List<MyDomElement> elements, SimpleTree simpleTree) {
        MyDomElement parent = (MyDomElement)currentNode.getUserObject();
        for (MyDomElement element : elements) {
            if (element instanceof DomModule domModule && parent instanceof DomMed domMed) {
                pasteModule(domModule, domMed, currentNode, simpleTree);
            } else if (element instanceof DomEntity domEntity && parent instanceof DomModule domModule) {
                pasteEntity(domEntity, domModule, currentNode, simpleTree);
            } else if (element instanceof DomId domId && parent instanceof DomEntity domEntity) {
                pasteEntityId(domId, domEntity, currentNode, simpleTree);
            } else if (element instanceof DomBasic domBasic && parent instanceof DomEntity domEntity) {
                pasteEntityBasic(domBasic, domEntity, currentNode, simpleTree);
            } else if (element instanceof DomEnumAttribute domEnumAttribute && parent instanceof DomEntity domEntity) {
                pasteEntityEnum(domEnumAttribute, domEntity, currentNode, simpleTree);
            } else if (element instanceof DomVersion domVersion && parent instanceof DomEntity domEntity) {
                pasteEntityVersion(domVersion, domEntity, currentNode, simpleTree);
            } else if (element instanceof DomManyToOne domManyToOne && parent instanceof DomEntity domEntity) {
                pasteEntityManyToOne(domManyToOne, domEntity, currentNode, simpleTree);
            } else if (element instanceof DomOneToMany domOneToMany && parent instanceof DomEntity domEntity) {
                pasteEntityOneToMany(domOneToMany, domEntity, currentNode, simpleTree);
            }
        }
        simpleTree.updateUI();
    }

    private static void pasteModule(DomModule source, DomMed parent, DefaultMutableTreeNode parentNode, SimpleTree simpleTree) {
        WriteCommandAction.writeCommandAction(source.getFile().getProject(), source.getFile()).run(() -> {
            DomModule module = parent.addModule();
            module.copyFrom(source);
            DefaultMutableTreeNode addedNode = DomTreeNodeUtil.convertModule(module);
            parentNode.add(addedNode);
        });
    }

    private static void pasteEntity(DomEntity source, DomModule parent, DefaultMutableTreeNode parentNode, SimpleTree simpleTree) {
        WriteCommandAction.writeCommandAction(source.getFile().getProject(), source.getFile()).run(() -> {
            DomEntity module = parent.addEntity();
            module.copyFrom(source);
            DefaultMutableTreeNode addedNode = DomTreeNodeUtil.convertEntity(module);
            parentNode.add(addedNode);
        });
    }

    private static void pasteEntityId(DomId source, DomEntity parent, DefaultMutableTreeNode parentNode, SimpleTree simpleTree) {
        if (!parent.getAttributes().getId().exists()) {
            WriteCommandAction.writeCommandAction(source.getFile().getProject(), source.getFile()).run(() -> {
                parent.getAttributes().getId().copyFrom(source);
                DefaultMutableTreeNode addedNode = new DefaultMutableTreeNode(parent.getAttributes().getId());
                parentNode.add(addedNode);
            });
        }
    }

    private static void pasteEntityBasic(DomBasic source, DomEntity parent, DefaultMutableTreeNode parentNode, SimpleTree simpleTree) {
        WriteCommandAction.writeCommandAction(source.getFile().getProject(), source.getFile()).run(() -> {
            DomBasic domBasic = parent.getAttributes().addBasicAttribute();
            domBasic.copyFrom(source);
            DefaultMutableTreeNode addedNode = new DefaultMutableTreeNode(domBasic);
            parentNode.add(addedNode);
        });
    }

    private static void pasteEntityEnum(DomEnumAttribute source, DomEntity parent, DefaultMutableTreeNode parentNode, SimpleTree simpleTree) {
        WriteCommandAction.writeCommandAction(source.getFile().getProject(), source.getFile()).run(() -> {
            DomEnumAttribute domEnumAttribute = parent.getAttributes().addEnumAttribute();
            domEnumAttribute.copyFrom(source);
            DefaultMutableTreeNode addedNode = new DefaultMutableTreeNode(domEnumAttribute);
            parentNode.add(addedNode);
        });
    }

    private static void pasteEntityVersion(DomVersion source, DomEntity parent, DefaultMutableTreeNode parentNode, SimpleTree simpleTree) {
        if (!parent.getAttributes().getVersionAttribute().exists()) {
            WriteCommandAction.writeCommandAction(source.getFile().getProject(), source.getFile()).run(() -> {
                DomVersion versionAttribute = parent.getAttributes().getVersionAttribute();
                versionAttribute.copyFrom(source);
                DefaultMutableTreeNode addedNode = new DefaultMutableTreeNode(versionAttribute);
                parentNode.add(addedNode);
            });
        }
    }

    private static void pasteEntityManyToOne(DomManyToOne source, DomEntity parent, DefaultMutableTreeNode parentNode, SimpleTree simpleTree) {
        WriteCommandAction.writeCommandAction(source.getFile().getProject(), source.getFile()).run(() -> {
            DomManyToOne versionAttribute = parent.getAttributes().addManyToOne();
            versionAttribute.copyFrom(source);
            DefaultMutableTreeNode addedNode = new DefaultMutableTreeNode(versionAttribute);
            parentNode.add(addedNode);
        });
    }

    private static void pasteEntityOneToMany(DomOneToMany source, DomEntity parent, DefaultMutableTreeNode parentNode, SimpleTree simpleTree) {
        WriteCommandAction.writeCommandAction(source.getFile().getProject(), source.getFile()).run(() -> {
            DomOneToMany versionAttribute = parent.getAttributes().addOneToMany();
            versionAttribute.copyFrom(source);
            DefaultMutableTreeNode addedNode = new DefaultMutableTreeNode(versionAttribute);
            parentNode.add(addedNode);
        });
    }
}
