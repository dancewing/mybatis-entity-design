/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml;

import com.intellij.diagram.AbstractDiagramElementManager;
import com.intellij.diagram.DiagramBuilder;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.pointers.VirtualFilePointerManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.ui.SimpleColoredText;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.ArrayUtil;
import io.entframework.med.uml.model.*;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

final class MedUmlElementManager extends AbstractDiagramElementManager<MedNodeData> {

    @Override
    public @Nullable MedNodeData findInDataContext(@NotNull DataContext dataContext) {
        var file = CommonDataKeys.PSI_FILE.getData(dataContext);
        if (!(file instanceof XmlFile))
            return null;

        var virtualFile = file.getVirtualFile();
        if (virtualFile == null)
            return null;

        return getRootData(file.getProject(), virtualFile);
    }

    @NotNull
    static MedDiagramRootData getRootData(Project project, VirtualFile virtualFile) {
        var disposable = project.getService(MedDiagramService.class);
        var filePointer = VirtualFilePointerManager.getInstance().create(virtualFile, disposable, null);
        return new MedDiagramRootData(filePointer);
    }

    @Override
    public Object @NotNull [] getNodeItems(MedNodeData nodeElement) {
        if (nodeElement instanceof MedEntityNodeData medEntityNodeData) {
            return medEntityNodeData.getProperties().toArray();
        }

        if (nodeElement instanceof MedEnumNodeData medEnumNodeData) {
            return medEnumNodeData.getOptions().toArray();
        }

        return ArrayUtil.EMPTY_OBJECT_ARRAY;
    }

    @Override
    public boolean canBeBuiltFrom(@Nullable Object element) {
        return element instanceof MedDiagramRootData || super.canBeBuiltFrom(element);
    }

    @Override
    public boolean isAcceptableAsNode(@Nullable Object o) {
        return o instanceof MedEntityNodeData || o instanceof MedEnumNodeData;
    }

    @Override
    public @Nullable SimpleColoredText getItemName(@Nullable MedNodeData nodeElement, @Nullable Object nodeItem,
                                                   @NotNull DiagramBuilder builder) {

        if (nodeItem instanceof MedNode node) {
            SimpleColoredText coloredText = new SimpleColoredText(node.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
            if (builder.getDataModel().getNodeContentManager().isCategoryEnabled(MedUmlCategoryManager.DESCRIPTION)) {
                String description = node.getDescription();
                if (!StringUtil.isEmptyOrSpaces(description)) {
                    coloredText.append(" " + description, SimpleTextAttributes.GRAYED_SMALL_ATTRIBUTES);
                }
            }
            return coloredText;
        }
        return null;
    }

    @Override
    public @Nullable SimpleColoredText getItemType(@Nullable MedNodeData nodeElement, @Nullable Object nodeItem,
                                                   @Nullable DiagramBuilder builder) {
        if (nodeItem instanceof MedEntityNodeField medEntityNodeField) {
            String type = medEntityNodeField.getType();
            if (type == null)
                return null;

            return new SimpleColoredText(type, SimpleTextAttributes.GRAY_ATTRIBUTES);
        }
        return null;
    }

    @Override
    public @Nullable Icon getItemIcon(@Nullable MedNodeData nodeElement, @Nullable Object nodeItem,
                                      @Nullable DiagramBuilder builder) {
        if (nodeItem instanceof MedEntityNodeField medEntityNodeField) {
            return medEntityNodeField.getIcon();
        }
        if (nodeItem instanceof MedEnumNodeItem enumNodeItem) {
            return enumNodeItem.getIcon();
        }
        return null;
    }

    @Override
    public @Nullable @Nls String getElementTitle(MedNodeData node) {
        return node.getName();
    }


    @Override
    public @Nullable @Nls String getNodeTooltip(MedNodeData node) {
        return null;
    }

}
