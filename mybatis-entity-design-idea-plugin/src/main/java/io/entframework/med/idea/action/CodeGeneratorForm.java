/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.idea.action;

import com.github.hykes.codegen.utils.PsiUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBTabbedPane;
import io.entframework.med.configurable.SettingManager;
import io.entframework.med.dom.DomEntity;
import io.entframework.med.dom.DomMed;
import io.entframework.med.dom.DomModule;
import io.entframework.med.model.CodeGroup;
import io.entframework.med.model.CodeTemplate;
import io.entframework.med.model.RuntimeTemplate;
import io.entframework.med.model.WriteMode;
import io.entframework.med.service.MyBatisGeneratorConfigManager;
import io.entframework.med.util.DomSupport;
import io.entframework.med.view.ProjectDialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.*;

public class CodeGeneratorForm extends ProjectDialogWrapper {
    private JPanel contentPanel;
    private JComboBox<DomModule> moduleComboBox;
    private JComboBox<CodeGroup> groupComboBox;
    private JPanel entityPanel;
    private JBTabbedPane templateSettingPanel;
    private TextFieldWithBrowseButton directoryField;
    private JComboBox<WriteMode> writeModeComboBox;
    private Project myProject;

    private Set<TemplateSettingPanel> templateSettingPanels = new HashSet<>();

    public CodeGeneratorForm(@NotNull Project project, @NotNull DomMed jaxbMed) {
        super(project);
        this.myProject = project;
        init();

        jaxbMed.getModules().forEach(jaxbModule -> {
            moduleComboBox.addItem(jaxbModule);
        });
        SettingManager settingManager = project.getService(SettingManager.class);
        List<CodeGroup> codeRoots = settingManager.getTemplates().getGroups();
        codeRoots.forEach(group -> groupComboBox.addItem(group));

        directoryField.addActionListener(new MyActionListener());
        for (WriteMode mode : WriteMode.values()) {
            writeModeComboBox.addItem(mode);
        }

        ModuleCellRenderer moduleCellRenderer = new ModuleCellRenderer();
        moduleComboBox.setRenderer(moduleCellRenderer);
        moduleComboBox.addItemListener(moduleCellRenderer);
        GroupRenderer groupRenderer = new GroupRenderer();
        groupComboBox.setRenderer(groupRenderer);
        groupComboBox.addItemListener(groupRenderer);

        MyBatisGeneratorConfigManager myBatisGeneratorConfigManager = myProject.getService(MyBatisGeneratorConfigManager.class);
        if (myBatisGeneratorConfigManager != null) {
            jaxbMed.getModules().stream()
                    .filter(module -> StringUtil.equals(myBatisGeneratorConfigManager.getLastSelectModule(), DomSupport.getValue(module.getName(), "")))
                    .findFirst().ifPresent(module -> moduleComboBox.setSelectedItem(module));
        }
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return contentPanel;
    }

    private void addEntity(DomModule module) {
        entityPanel.removeAll();
        module.getEntities().forEach(jaxbEntity -> {
            JBCheckBox checkBox = new JBCheckBox(DomSupport.getValue(jaxbEntity.getName(), ""), true);
            entityPanel.add(checkBox);
        });
    }


    private void addTemplateSetting(CodeGroup group) {
        List<CodeTemplate> templates = group.getTemplates();
        templateSettingPanel.removeAll();
        templateSettingPanels.clear();
        if (templates != null && !templates.isEmpty()) {
            templates.forEach(codeTemplate -> {
                TemplateSettingPanel panel = new TemplateSettingPanel(myProject, codeTemplate);
                templateSettingPanels.add(panel);
                templateSettingPanel.add(codeTemplate.getDisplay(), panel.getContentPanel());
            });
        }
    }

    public class ModuleCellRenderer extends JLabel implements ListCellRenderer<DomModule>, ItemListener {

        @Override
        public Component getListCellRendererComponent(JList list, DomModule value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            try {
                this.setText(DomSupport.getValue(value.getName(), ""));
            } catch (Exception e) {
                this.setText(String.valueOf(index));
            }
            this.setHorizontalAlignment(LEFT);
            return this;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object selected = moduleComboBox.getSelectedItem();
                if (selected instanceof DomModule module) {
                    addEntity(module);
                    MyBatisGeneratorConfigManager myBatisGeneratorConfigManager = myProject.getService(MyBatisGeneratorConfigManager.class);
                    if (myBatisGeneratorConfigManager != null) {
                        myBatisGeneratorConfigManager.setLastSelectModule(DomSupport.getValue(module.getName(), ""));
                    }
                }
            }
        }

    }

    public class GroupRenderer extends JLabel implements ListCellRenderer<CodeGroup>, ItemListener {

        @Override
        public Component getListCellRendererComponent(JList list, CodeGroup value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            try {
                this.setText(value.getName());
            } catch (Exception e) {
                this.setText(String.valueOf(index));
            }
            this.setHorizontalAlignment(LEFT);
            return this;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object selected = groupComboBox.getSelectedItem();
                if (selected instanceof CodeGroup root) {
                    addTemplateSetting(root);
                }
            }
        }
    }

    public class MyActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            VirtualFile virtualDirectory = null;
            if (!StringUtil.isEmptyOrSpaces(directoryField.getText())) {
                virtualDirectory = LocalFileSystem.getInstance().findFileByPath(directoryField.getText());
            }

            VirtualFile virtualFile = PsiUtil.chooseFolder(myProject, "Directory Chooser",
                    "Select Export Directory", true, true, virtualDirectory);
            if (virtualFile != null && virtualFile.isDirectory()) {
                directoryField.setText(virtualFile.getPath());
            }
        }
    }

    public List<RuntimeTemplate> getRuntimeTemplates() {
        List<RuntimeTemplate> templates = new ArrayList<>();
        templateSettingPanels.forEach(panel -> {
            RuntimeTemplate runtimeTemplate = panel.getRuntimeTemplate();
            if (runtimeTemplate != null) {
                checkGlobalSettings(runtimeTemplate);
                templates.add(runtimeTemplate);
            }
        });
        return templates;
    }

    public List<DomEntity> getEntities() {
        DomModule jaxbEntityMappings = (DomModule) moduleComboBox.getSelectedItem();
        if (jaxbEntityMappings != null) {
            List<String> selectedEntityNames = Arrays.stream(entityPanel.getComponents())
                    .map(JBCheckBox.class::cast)
                    .filter(AbstractButton::isSelected)
                    .map(AbstractButton::getText).toList();
            return jaxbEntityMappings.getEntities().stream()
                    .filter(jaxbEntity -> selectedEntityNames.contains(jaxbEntity.getName()))
                    .toList();
        }
        return Collections.emptyList();
    }

    public DomModule getJaxbModule() {
        return (DomModule) moduleComboBox.getSelectedItem();
    }

    private void checkGlobalSettings(RuntimeTemplate runtimeTemplate) {
        if (StringUtil.isEmptyOrSpaces(runtimeTemplate.getDirectory())) {
            runtimeTemplate.setDirectory(directoryField.getText());
        }
        if (runtimeTemplate.getWriteMode() == null) {
            runtimeTemplate.setWriteMode((WriteMode) writeModeComboBox.getSelectedItem());
        }
    }
}
