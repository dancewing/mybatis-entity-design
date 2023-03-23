/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.ui;

import com.intellij.openapi.options.ConfigurableUi;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.ui.GuiUtils;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import io.entframework.med.configurable.SettingManager;
import io.entframework.med.configurable.model.Variables;
import io.entframework.med.configurable.ui.dialog.VariableEditDialog;
import io.entframework.med.model.ProjectContainer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hehaiyang@terminus.io
 * @date 2017/12/20
 */
public class VariableUI extends ProjectContainer implements ConfigurableUi<Variables> {

    private JPanel rootPanel;

    private JPanel varPanel;

    private JPanel descPanel;

    private JSplitPane jSplitPane;

    private JPanel ignorePane;

    private JPanel splitPanel;

    private JBTable varTable;

    private JTextArea descArea;

    public VariableUI(@NotNull Project project) {
        super(project);
        GuiUtils.replaceJSplitPaneWithIDEASplitter(splitPanel);
        this.myProject = project;
        setVariables(this.myProject.getService(SettingManager.class).getVariables());
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        splitPanel = new JPanel();
        splitPanel.setPreferredSize(JBUI.size(300, 400));
        jSplitPane = new JSplitPane();
        jSplitPane.setOrientation(0);
        jSplitPane.setContinuousLayout(true);
        jSplitPane.setBorder(BorderFactory.createEmptyBorder());
        varPanel = new JPanel(new BorderLayout());
        varPanel.setBorder(IdeBorderFactory.createTitledBorder("Predefined Variables", false));
        varTable = new JBTable();
        varTable.getEmptyText().setText("No Variables");
        // 不可整列移动
        varTable.getTableHeader().setReorderingAllowed(false);
        // 不可拉动表格
        varTable.getTableHeader().setResizingAllowed(false);

        JPanel panel = ToolbarDecorator.createDecorator(varTable)
                .setAddAction(it -> addAction())
                .setRemoveAction(it -> removeAction())
                .setEditAction(it -> editAction())
                .createPanel();
        varPanel.add(panel, BorderLayout.CENTER);

        descPanel = new JPanel(new BorderLayout());
        descPanel.setBorder(IdeBorderFactory.createTitledBorder("Default Variables \\& Directives", false));

        String inHouseVariables;
        try {
            inHouseVariables = FileUtil.loadTextAndClose(VariableUI.class.getResourceAsStream("/variables.md"));
        } catch (Exception e) {
            inHouseVariables = "something error";
        }
        descArea = new JTextArea();
        descArea.setText(inHouseVariables);
        descArea.setEditable(false);
        descPanel.add(ScrollPaneFactory.createScrollPane(descArea), BorderLayout.CENTER);

        // ignore fields
        ignorePane = new JPanel();
        ignorePane.setBorder(IdeBorderFactory.createTitledBorder("The Ignore Fields", false));
    }

    private void setVariables(Variables variables) {
        // 列名
        String[] columnNames = {"Key", "Value"};
        // 默认数据
        Map<String, String> map = variables.getParams();
        Object[][] tableVales = new String[map.size()][2];
        Object[] keys = map.keySet().toArray();
        Object[] values = map.values().toArray();
        for (int row = 0; row < tableVales.length; row++) {
            tableVales[row][0] = keys[row];
            tableVales[row][1] = values[row];
        }
        DefaultTableModel tableModel = new DefaultTableModel(tableVales, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        varTable.setModel(tableModel);
    }

    private void addAction() {
        VariableEditDialog dialog = new VariableEditDialog(myProject);
        dialog.setTitle("Create New Variable");
        if (dialog.showAndGet()) {
            String key = dialog.getKeyTextField().getText().trim();
            String value = dialog.getValueTextField().getText().trim();
            DefaultTableModel tableModel = (DefaultTableModel) varTable.getModel();
            String[] rowValues = {key, value};
            tableModel.addRow(rowValues);
        }
    }

    private void removeAction() {
        int selectedRow = varTable.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel tableModel = (DefaultTableModel) varTable.getModel();
            tableModel.removeRow(selectedRow);
        }
        if (varTable.getRowCount() > 0) {
            varTable.setRowSelectionInterval(0, 0);
        }
    }

    private void editAction() {
        int selectedRow = varTable.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel tableModel = (DefaultTableModel) varTable.getModel();
            String oldKey = (String) tableModel.getValueAt(selectedRow, 0);
            String oldValue = (String) tableModel.getValueAt(selectedRow, 1);

            VariableEditDialog dialog = new VariableEditDialog(myProject);
            dialog.setTitle("Edit Variable");
            dialog.getKeyTextField().setText(oldKey);
            dialog.getValueTextField().setText(oldValue);
            if (dialog.showAndGet()) {
                String key = dialog.getKeyTextField().getText().trim();
                String value = dialog.getValueTextField().getText().trim();
                tableModel.setValueAt(key, selectedRow, 0);
                tableModel.setValueAt(value, selectedRow, 1);
            }
        }
    }

    /**
     * 是否已修改
     */
    @Override
    public boolean isModified(Variables settings) {
        DefaultTableModel tableModel = (DefaultTableModel) varTable.getModel();
        if (settings.getParams().size() != tableModel.getRowCount()) {
            return true;
        }
        Map<String, String> params = settings.getParams();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String key = tableModel.getValueAt(i, 0).toString();
            String value = tableModel.getValueAt(i, 1).toString();
            if (!params.containsKey(key)) {
                return true;
            } else if (params.containsKey(key) && !params.get(key).equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 应用
     */
    @Override
    public void apply(Variables settings) {
        Map<String, String> params = new HashMap<>();
        DefaultTableModel tableModel = (DefaultTableModel) varTable.getModel();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            params.put(tableModel.getValueAt(i, 0).toString().trim(), tableModel.getValueAt(i, 1).toString().trim());
        }
        SettingManager settingManager = myProject.getService(SettingManager.class);
        settingManager.getVariables().setParams(params);
    }

    @Override
    public @NotNull JComponent getComponent() {
        return rootPanel;
    }

    /**
     * 重置
     */
    @Override
    public void reset(Variables settings) {
        setVariables(settings);
    }

}
