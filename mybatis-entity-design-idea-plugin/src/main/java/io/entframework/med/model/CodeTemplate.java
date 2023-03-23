/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class CodeTemplate implements Serializable {

    private static final long serialVersionUID = -329690965910519848L;

    public CodeTemplate() {
    }

    public CodeTemplate(String id, String display, String extension, String fileName, String templatePath, String _package) {
        this(id, display, extension, fileName, templatePath, _package, TemplateLanguage.VELOCITY, TemplateLevel.ENTITY);
    }

    public CodeTemplate(String id, String display, String extension, String fileName, String templatePath, String _package, TemplateLanguage language, TemplateLevel level) {
        this.id = id;
        this.display = display;
        this.extension = extension;
        this.fileName = fileName;
        this.templatePath = templatePath;
        this._package = _package;
        this.language = language;
        this.level = level;
    }

    /**
     * 模版ID，取 UUID 值
     */
    private String id;

    /**
     * 模版名称
     */
    private String display;

    /**
     * 扩展名
     */
    private String extension;

    /**
     * 文件名称
     */
    private String fileName;

    private String templatePath;

    /**
     * 包名
     */
    private String _package;

    private String group;

    private TemplateLanguage language;

    private TemplateLevel level;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPackage() {
        return _package;
    }

    public void setPackage(String _package) {
        this._package = _package;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public TemplateLevel getLevel() {
        if (level == null) {
            level = TemplateLevel.ENTITY;
        }
        return level;
    }

    public void setLevel(@NotNull TemplateLevel level) {
        this.level = level;
    }

    public TemplateLanguage getLanguage() {
        if (language == null) {
            language = TemplateLanguage.VELOCITY;
        }
        return language;
    }

    public void setLanguage(@NotNull TemplateLanguage language) {
        this.language = language;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public enum TemplateLevel {
        MODULE, ENTITY, ENUM;
    }

    public enum TemplateLanguage {
        GROOVY("groovy"),
        VELOCITY("vm"),
        FREEMARKER("ftl");

        private String extension;

        TemplateLanguage(String extension) {
            this.extension = extension;
        }

        public String getExtension() {
            return extension;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CodeTemplate template1 = (CodeTemplate) o;

        if (!id.equals(template1.id)) return false;
        if (!display.equals(template1.display)) return false;
        if (!extension.equals(template1.extension)) return false;
        if (!fileName.equals(template1.fileName)) return false;
        if (!_package.equals(template1._package)) return false;
        if (!Objects.equals(group, template1.group)) return false;
        if (language != template1.language) return false;
        return level == template1.level;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + display.hashCode();
        result = 31 * result + extension.hashCode();
        result = 31 * result + fileName.hashCode();
        result = 31 * result + _package.hashCode();
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + language.hashCode();
        result = 31 * result + level.hashCode();
        return result;
    }
}
