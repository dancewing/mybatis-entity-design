/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml;

import com.intellij.diagram.extras.DiagramExtras;
import com.intellij.diagram.extras.UmlNodeHighlighter;
import io.entframework.med.uml.model.MedNodeData;
import org.jetbrains.annotations.Nullable;

public class MedDiagramExtras extends DiagramExtras<MedNodeData> {

    @Override
    public @Nullable UmlNodeHighlighter<MedNodeData> getNodeHighlighter() {
        return new MedUmlNodeHighlighter();
    }
}
