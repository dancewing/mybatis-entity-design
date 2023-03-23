/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml;

import com.intellij.diagram.AbstractDiagramNodeContentManager;
import com.intellij.diagram.DiagramBuilder;
import com.intellij.diagram.DiagramCategory;
import com.intellij.icons.AllIcons;
import io.entframework.med.uml.model.MedEntityNodeField;
import io.entframework.med.uml.model.MedEnumNodeItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class MedUmlCategoryManager extends AbstractDiagramNodeContentManager {

    private static final @NotNull DiagramCategory FIELDS = new DiagramCategory(() -> "Fields", AllIcons.Nodes.Field,
            true, false);

    private static final @NotNull DiagramCategory ENUM_VALUES = new DiagramCategory(() -> "Enum Values",
            AllIcons.Nodes.Enum, true, false);

    public static final @NotNull DiagramCategory DESCRIPTION = new DiagramCategory(() -> "Description",
            AllIcons.General.Balloon, false, false);

    private static final DiagramCategory[] CATEGORIES = new DiagramCategory[]{ENUM_VALUES, FIELDS, DESCRIPTION};

    @Override
    public DiagramCategory @NotNull [] getContentCategories() {
        return CATEGORIES;
    }

    @Override
    public boolean isInCategory(@Nullable Object nodeElement, @Nullable Object item, @NotNull DiagramCategory category,
                                @Nullable DiagramBuilder builder) {
        if (nodeElement instanceof MedEntityNodeField) {
            return category == FIELDS;
        }
        if (nodeElement instanceof MedEnumNodeItem) {
            return category == ENUM_VALUES;
        }
        return super.isInCategory(nodeElement, item, category, builder);
    }

}
