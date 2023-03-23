/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.service;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import io.entframework.med.model.ModuleConfig;
import io.entframework.med.model.TemplateConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@State(name = MyBatisGeneratorConfigManager.STATE_NAME, storages = @Storage(MyBatisGeneratorConfigManager.STORAGE_FILE))
public class MyBatisGeneratorConfigManager implements PersistentStateComponent<MyBatisGeneratorConfigManager> {

    static final String STATE_NAME = "Mybatis.MED.Generator";
    static final String STORAGE_FILE = "mybatis-generator.xml";

    private String lastSelectModule;

    private String lastSelectGroup;

    private Map<String, ModuleConfig> moduleConfigMap = new HashMap<>();

    private Map<String, TemplateConfig> templateConfigMap = new HashMap<>();

    @Nullable
    @Override
    public MyBatisGeneratorConfigManager getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull MyBatisGeneratorConfigManager config) {
        XmlSerializerUtil.copyBean(config, this);
    }

    public ModuleConfig get(String module) {
        return moduleConfigMap.computeIfAbsent(module, (key) -> new ModuleConfig());
    }

    public String getLastSelectModule() {
        return lastSelectModule;
    }

    public String getLastSelectGroup() {
        return lastSelectGroup;
    }

    public Map<String, ModuleConfig> getModuleConfigMap() {
        return moduleConfigMap;
    }

    public Map<String, TemplateConfig> getTemplateConfigMap() {
        return templateConfigMap;
    }

    public void setLastSelectModule(String lastSelectModule) {
        this.lastSelectModule = lastSelectModule;
    }

    public void setLastSelectGroup(String lastSelectGroup) {
        this.lastSelectGroup = lastSelectGroup;
    }

    public void setModuleConfigMap(Map<String, ModuleConfig> moduleConfigMap) {
        this.moduleConfigMap = moduleConfigMap;
    }

    public void setTemplateConfigMap(Map<String, TemplateConfig> templateConfigMap) {
        this.templateConfigMap = templateConfigMap;
    }
}
