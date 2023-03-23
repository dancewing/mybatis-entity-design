/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml;

import com.intellij.diagram.DiagramVfsResolver;
import com.intellij.openapi.project.Project;
import io.entframework.med.uml.model.MedNodeData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class MedUmlVfsResolver implements DiagramVfsResolver<MedNodeData> {

    @Override
    public @Nullable String getQualifiedName(@Nullable MedNodeData data) {
        if (data == null)
            return null;

        String name = data.getName();
        return name != null ? name : "";
    }

    @Override
    public @Nullable MedNodeData resolveElementByFQN(@NotNull String s, @NotNull Project project) {
        return null;
    }

}
