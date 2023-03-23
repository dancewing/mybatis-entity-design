/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.ui;

import com.intellij.application.options.codeStyle.CommaSeparatedIdentifiersField;
import com.intellij.openapi.options.ConfigurableUi;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import io.entframework.med.configurable.SettingManager;
import io.entframework.med.configurable.model.ReverseSettings;
import io.entframework.med.model.ProjectContainer;
import io.entframework.med.view.component.ClassChooserField;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ReverseUI extends ProjectContainer implements ConfigurableUi<ReverseSettings> {

    private CommaSeparatedIdentifiersField ignoreTextField;

    private JTextField searchTextField;

    private JTextField replaceTextField;

    private JCheckBox forceBigDecimalsCheckBox;

    private ClassChooserField parentTextField;

    private JCheckBox useJSR310TypesCheckBox;

    private JPanel contentPanel;

    public ReverseUI(Project project) {
        super(project);
    }

    @Override
    public void reset(@NotNull ReverseSettings settings) {
        parentTextField.setText(StringUtil.defaultIfEmpty(settings.getParent(), ""));
        ignoreTextField.setText(StringUtil.defaultIfEmpty(settings.getIgnoreFields(), ""));
        searchTextField.setText(StringUtil.defaultIfEmpty(settings.getSearchString(), ""));
        replaceTextField.setText(StringUtil.defaultIfEmpty(settings.getReplaceString(), ""));
        forceBigDecimalsCheckBox.setSelected(settings.isForceBigDecimals());
        useJSR310TypesCheckBox.setSelected(settings.isUseJSR310Types());
    }

    @Override
    public boolean isModified(@NotNull ReverseSettings settings) {
        if (StringUtil.equals(settings.getParent(), StringUtil.defaultIfEmpty(parentTextField.getText(), ""))
                && StringUtil.equals(settings.getIgnoreFields(),
                StringUtil.defaultIfEmpty(ignoreTextField.getText(), ""))
                && StringUtil.equals(settings.getSearchString(),
                StringUtil.defaultIfEmpty(searchTextField.getText(), ""))
                && StringUtil.equals(settings.getReplaceString(),
                StringUtil.defaultIfEmpty(replaceTextField.getText(), ""))
                && settings.isForceBigDecimals() == forceBigDecimalsCheckBox.isSelected()
                && settings.isUseJSR310Types() == useJSR310TypesCheckBox.isSelected()) {
            return false;
        }
        return true;
    }

    @Override
    public void apply(@NotNull ReverseSettings settings) throws ConfigurationException {
        SettingManager settingManager = myProject.getService(SettingManager.class);
        settings.setParent(StringUtil.defaultIfEmpty(parentTextField.getText(), ""));
        settings.setIgnoreFields(StringUtil.defaultIfEmpty(ignoreTextField.getText(), ""));
        settings.setSearchString(StringUtil.defaultIfEmpty(searchTextField.getText(), ""));
        settings.setReplaceString(StringUtil.defaultIfEmpty(replaceTextField.getText(), ""));
        settings.setForceBigDecimals(forceBigDecimalsCheckBox.isSelected());
        settings.setUseJSR310Types(useJSR310TypesCheckBox.isSelected());
        settingManager.setReverseSettings(settings);
    }

    @Override
    public @NotNull JComponent getComponent() {
        return contentPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        parentTextField = new ClassChooserField(myProject);
    }

}
