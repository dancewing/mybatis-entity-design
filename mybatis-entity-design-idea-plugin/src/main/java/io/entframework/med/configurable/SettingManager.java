/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable;

import com.github.hykes.codegen.constants.Defaults;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import io.entframework.med.configurable.model.ReverseSettings;
import io.entframework.med.configurable.model.Templates;
import io.entframework.med.configurable.model.Variables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@State(name = "Mybatis.Med", storages = {@Storage(value = "mybatis-med.xml")})
public class SettingManager implements PersistentStateComponent<SettingManager> {

    private Variables variables;

    private Templates templates;

    private ReverseSettings reverseSettings;

    public SettingManager() {
        this.variables = new Variables();
        this.templates = new Templates();
        this.reverseSettings = new ReverseSettings();
        init();
    }

    @Override
    public void loadState(@NotNull SettingManager formatSetting) {
        XmlSerializerUtil.copyBean(formatSetting, this);
        init();
    }

    /**
     * 初始化配置, 防止第一次使用插件（没有生成codegen.xml）不执行{@link #loadState(SettingManager)}函数
     */
    private void init() {
        if (templates != null && templates.getGroups().isEmpty()) {
            templates.setGroups(Defaults.getDefaultGroups());
        }
    }

    @Override
    @Nullable
    public SettingManager getState() {
        return this;
    }

    public Variables getVariables() {
        return variables;
    }

    public void setVariables(Variables variables) {
        this.variables = variables;
    }

    public Templates getTemplates() {
        return templates;
    }

    public void setTemplates(Templates templates) {
        this.templates = templates;
    }

    public ReverseSettings getReverseSettings() {
        return reverseSettings;
    }

    public void setReverseSettings(ReverseSettings reverseSettings) {
        this.reverseSettings = reverseSettings;
    }

}
