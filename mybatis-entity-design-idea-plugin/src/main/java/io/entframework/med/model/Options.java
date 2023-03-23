/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

public class Options {
    private String directory;
    private WriteMode writeMode;
    private String fileName;

    public Options(String directory, WriteMode writeMode) {
        this.directory = directory;
        this.writeMode = writeMode;
    }

    public String getDirectory() {
        return directory;
    }

    public WriteMode getWriteMode() {
        return writeMode;
    }

    public String getFileName() {
        return fileName;
    }
}
