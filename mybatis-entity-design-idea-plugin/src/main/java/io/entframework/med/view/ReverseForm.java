/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.components.JBTabbedPane;
import io.entframework.med.model.Entity;
import io.entframework.med.model.ProjectContainer;
import io.entframework.med.util.ReverseUtil;
import io.entframework.med.view.component.ClassChooserField;
import io.entframework.med.view.component.PackageChooserField;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReverseForm extends ProjectContainer {

    private JPanel contentPanel;

    private JScrollPane scrollPane;

    private ClassChooserField parentTextField;

    private JTextField ignoreTextField;

    private JTextField replaceTextField;

    private JCheckBox forceBigDecimalsCheckBox;

    private JCheckBox useJSR310TypesCheckBox;

    private JTextField searchTextField;

    private JButton applyButton;

    private JTabbedPane entityTabbedPane;

    private PackageChooserField packageChooserField;

    private JLabel packageLabel;

    private List<Entity> myEntities;

    private final List<Entity> mySourceEntities;

    private final ReverseFormSettings reverseSettings;

    private final Map<Entity, TablePanel> tablePanelMap = new HashMap<>();

    public ReverseForm(@NotNull Project project, List<Entity> entities, ReverseFormSettings reverseFormSettings) {
        this(project, entities, reverseFormSettings, Mode.DB);
    }

    public ReverseForm(@NotNull Project project, List<Entity> entities, ReverseFormSettings reverseFormSettings,
                       Mode mode) {
        super(project);
        reverseSettings = reverseFormSettings;

        parentTextField.setText(StringUtil.defaultIfEmpty(reverseSettings.getParent(), ""));
        ignoreTextField.setText(StringUtil.defaultIfEmpty(reverseSettings.getIgnoreFields(), ""));
        searchTextField.setText(StringUtil.defaultIfEmpty(reverseSettings.getSearchString(), ""));
        replaceTextField.setText(StringUtil.defaultIfEmpty(reverseSettings.getReplaceString(), ""));
        forceBigDecimalsCheckBox.setSelected(reverseSettings.isForceBigDecimals());
        useJSR310TypesCheckBox.setSelected(reverseSettings.isUseJSR310Types());
        this.mySourceEntities = entities;
        this.myEntities = ReverseUtil.apply(entities, reverseSettings);
        for (Entity entity : this.myEntities) {
            TablePanel tablePanel = new TablePanel(entity, this.myProject, reverseSettings);
            entityTabbedPane.add(tablePanel.getRootComponent(), entity.getTable());
            tablePanelMap.put(entity, tablePanel);
        }

        applyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                reverseSettings.setParent(StringUtil.defaultIfEmpty(parentTextField.getText(), ""));
                reverseSettings.setIgnoreFields(StringUtil.defaultIfEmpty(ignoreTextField.getText(), ""));
                reverseSettings.setSearchString(StringUtil.defaultIfEmpty(searchTextField.getText(), ""));
                reverseSettings.setReplaceString(StringUtil.defaultIfEmpty(replaceTextField.getText(), ""));
                reverseSettings.setForceBigDecimals(forceBigDecimalsCheckBox.isSelected());
                reverseSettings.setUseJSR310Types(useJSR310TypesCheckBox.isSelected());
                reverseSettings.setPackage(StringUtil.defaultIfEmpty(packageChooserField.getText(), ""));
                myEntities = ReverseUtil.apply(mySourceEntities, reverseSettings);
                myEntities.forEach(entity -> {
                    if (tablePanelMap.containsKey(entity)) {
                        tablePanelMap.get(entity).refresh(entity, reverseSettings);
                    }
                });
            }
        });

        if (mode == Mode.SCRIPT) {
            packageChooserField.setVisible(false);
            packageLabel.setVisible(false);
        }
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    private void createUIComponents() {
        scrollPane = new JScrollPane();
        entityTabbedPane = new JBTabbedPane();
        scrollPane.setViewportView(entityTabbedPane);
        parentTextField = new ClassChooserField(myProject);
        packageChooserField = new PackageChooserField(myProject);
    }

    @NotNull
    public ReverseFormSettings getReverseSettings() {
        return reverseSettings;
    }

    public List<Entity> getEntities() {
        return myEntities;
    }

    public enum Mode {

        DB, SCRIPT;

    }

}
