/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

public class RuntimeTemplate extends CodeTemplate {
    private String directory;
    private WriteMode writeMode;
    private String content;

    public RuntimeTemplate() {
    }

    public RuntimeTemplate(String id, String display, String extension, String filename, String template, String _package) {
        super(id, display, extension, filename, template, _package);
    }

    public RuntimeTemplate(CodeTemplate source) {
        this.setId(source.getId());
        this.setDisplay(source.getDisplay());
        this.setExtension(source.getExtension());
        this.setFileName(source.getFileName());
        this.setPackage(source.getPackage());
        this.setGroup(source.getGroup());
        this.setLevel(source.getLevel());
        this.setLanguage(source.getLanguage());
        this.setTemplatePath(source.getTemplatePath());
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

    public RuntimeTemplate copy() {
        RuntimeTemplate runtimeTemplate = new RuntimeTemplate();
        runtimeTemplate.setDirectory(this.directory);
        runtimeTemplate.setWriteMode(this.writeMode);
        runtimeTemplate.setId(this.getId());
        runtimeTemplate.setDisplay(this.getDisplay());
        runtimeTemplate.setExtension(this.getExtension());
        runtimeTemplate.setFileName(this.getFileName());
        runtimeTemplate.setPackage(this.getPackage());
        runtimeTemplate.setLevel(this.getLevel());
        runtimeTemplate.setContent(this.getContent());
        runtimeTemplate.setTemplatePath(this.getTemplatePath());
        runtimeTemplate.setLanguage(this.getLanguage());
        return runtimeTemplate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
