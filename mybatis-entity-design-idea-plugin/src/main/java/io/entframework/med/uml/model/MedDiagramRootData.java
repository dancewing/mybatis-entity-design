/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml.model;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.pointers.VirtualFilePointer;
import io.entframework.med.MedIcons;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public final class MedDiagramRootData implements MedNodeData {

    private final VirtualFilePointer virtualFile;

    private final String name;


    public MedDiagramRootData(VirtualFilePointer virtualFile) {
        this.virtualFile = virtualFile;
        this.name = virtualFile.getFileName();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return name;
    }

    @Override
    public Object getSource() {
        return this.virtualFile;
    }

    @Override
    public Icon getIcon() {
        return MedIcons.ICON;
    }

    public @Nullable VirtualFile getVirtualFile() {
        return virtualFile.getFile();
    }

}
