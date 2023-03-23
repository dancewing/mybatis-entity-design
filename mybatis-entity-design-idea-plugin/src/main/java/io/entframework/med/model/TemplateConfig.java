/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

public class TemplateConfig {

    private boolean enable;
    private boolean customConfig;
    private String directory;
    private WriteMode writeMode;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isCustomConfig() {
        return customConfig;
    }

    public void setCustomConfig(boolean customConfig) {
        this.customConfig = customConfig;
    }

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
}
