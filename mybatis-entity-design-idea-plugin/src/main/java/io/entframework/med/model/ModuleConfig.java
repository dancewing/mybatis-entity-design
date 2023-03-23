/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

import java.util.List;

public class ModuleConfig {
    private String directory;
    private WriteMode writeMode;
    private List<String> selectedEntities;

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public WriteMode getWriteMode() {
        return writeMode;
    }

    public void setWriteMode(WriteMode writeMode) {
        this.writeMode = writeMode;
    }

    public List<String> getSelectedEntities() {
        return selectedEntities;
    }

    public void setSelectedEntities(List<String> selectedEntities) {
        this.selectedEntities = selectedEntities;
    }
}
