/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view;

import com.intellij.openapi.project.Project;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBUI;
import io.entframework.med.configurable.model.ReverseSettings;
import io.entframework.med.configurable.ui.dialog.ColumnEditDialog;
import io.entframework.med.model.Entity;
import io.entframework.med.model.Field;
import io.entframework.med.model.FieldImpl;
import io.entframework.med.model.JavaTypeImpl;
import io.entframework.med.view.action.ClassChooseActionListener;
import io.entframework.med.view.component.ClassChooserField;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2017/12/18
 */
public class TablePanel {

    private JPanel rootPanel;

    private JPanel mainPanel;

    private JTextField entityTextField;

    private JTextField tableTextField;

    private JPanel topPanel;

    private JPanel columnsPanel;

    private JTextField commentTextField;

    private ClassChooserField parentTextField;

    private JBTable fieldTable = new JBTable();

    public JTextField getModelTextField() {
        return entityTextField;
    }

    public JTextField getTableTextField() {
        return tableTextField;
    }

    public JTextField getCommentTextField() {
        return commentTextField;
    }

    private Project myProject;

    private ReverseSettings reverseSettings;

    public TablePanel(Entity entity, Project project, ReverseSettings reverseSettings) {
        this.myProject = project;
        this.reverseSettings = reverseSettings;
        createUIComponents();
        rootPanel.setBorder(JBUI.Borders.empty());
        parentTextField.addActionListener(new ClassChooseActionListener(myProject));

        // 不可整列移动
        fieldTable.getTableHeader().setReorderingAllowed(false);
        // 不可拉动表格
        fieldTable.getTableHeader().setResizingAllowed(false);
        fieldTable.getEmptyText().setText("No Columns");
        JPanel panel = ToolbarDecorator.createDecorator(fieldTable)
                .setAddAction(it -> addAction())
                .setRemoveAction(it -> removeAction())
                .setEditAction(it -> editAction())
                .createPanel();
        panel.setPreferredSize(JBUI.size(600, 200));
        columnsPanel.setBorder(IdeBorderFactory.createTitledBorder("Columns", false));
        columnsPanel.add(panel, BorderLayout.CENTER);

        mainPanel.add(columnsPanel);

        refresh(entity, reverseSettings);

        this.getRootComponent()
                .registerKeyboardAction(e -> this.getRootComponent().disable(),
                        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public void refresh(Entity entity, ReverseSettings reverseSettings) {
        this.reverseSettings = reverseSettings;
        entityTextField.setText(entity.getName().getText());
        tableTextField.setText(entity.getTable());
        commentTextField.setText(entity.getComment());
        initFields(entity.getFields());
    }

    /**
     * 添加
     */
    private void addAction() {
        ColumnEditDialog dialog = new ColumnEditDialog(myProject);
        dialog.setTitle("Add a Column");

        boolean result = dialog.showAndGet();
        if (result) {
            String fieldText = dialog.getFieldTextField().getText().trim();
            String fieldTypeText = dialog.getFieldTypeTextField().getText().trim();
            String columnText = dialog.getColumnTextField().getText();
            String columnTypeText = dialog.getColumnTypeTextField().getText().trim();
            String columnSizeText = dialog.getColumnSizeTextField().getText().trim();
            String commentText = dialog.getCommentTextField().getText().trim();

            DefaultTableModel tableModel = (DefaultTableModel) fieldTable.getModel();
            String[] rowValues = {fieldText, fieldTypeText, columnText, columnTypeText, "0", columnSizeText,
                    commentText};
            tableModel.addRow(rowValues);
        }
    }

    /**
     * 删除
     */
    private void removeAction() {
        int selectedRow = fieldTable.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel tableModel = (DefaultTableModel) fieldTable.getModel();
            tableModel.removeRow(selectedRow);
        }
        if (fieldTable.getRowCount() > 0) {
            fieldTable.setRowSelectionInterval(0, 0);
        }
    }

    /**
     * 编辑
     */
    private void editAction() {
        int selectedRow = fieldTable.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel tableModel = (DefaultTableModel) fieldTable.getModel();
            String oldFieldText = (String) tableModel.getValueAt(selectedRow, 0);
            String oldFieldTypeText = (String) tableModel.getValueAt(selectedRow, 1);
            String oldColumnText = (String) tableModel.getValueAt(selectedRow, 2);
            String oldColumnTypeText = (String) tableModel.getValueAt(selectedRow, 3);
            String oldColumnSizeText = (String) tableModel.getValueAt(selectedRow, 4);
            String oldCommentText = (String) tableModel.getValueAt(selectedRow, 5);

            ColumnEditDialog dialog = new ColumnEditDialog(myProject);
            dialog.setTitle("Edit a Column");

            dialog.getFieldTextField().setText(oldFieldText);
            dialog.getFieldTypeTextField().setText(oldFieldTypeText);
            dialog.getColumnTextField().setText(oldColumnText);
            dialog.getColumnTypeTextField().setText(oldColumnTypeText);
            dialog.getColumnSizeTextField().setText(oldColumnSizeText);
            dialog.getCommentTextField().setText(oldCommentText);

            if (dialog.showAndGet()) {
                String fieldText = dialog.getFieldTextField().getText().trim();
                String fieldTypeText = dialog.getFieldTypeTextField().getText().trim();
                String columnText = dialog.getColumnTextField().getText();
                String columnTypeText = dialog.getColumnTypeTextField().getText().trim();
                String columnSizeText = dialog.getColumnSizeTextField().getText().trim();
                String commentText = dialog.getCommentTextField().getText().trim();

                tableModel.setValueAt(fieldText, selectedRow, 0);
                tableModel.setValueAt(fieldTypeText, selectedRow, 1);
                tableModel.setValueAt(columnText, selectedRow, 2);
                tableModel.setValueAt(columnTypeText, selectedRow, 3);
                tableModel.setValueAt(columnSizeText, selectedRow, 4);
                tableModel.setValueAt(commentText, selectedRow, 5);
            }
        }
    }

    private void initFields(List<Field> fields) {
        // 列名
        String[] columnNames = {"Property", "Type", "Column Name", "Column Type", "Column Size", "Comment"};
        // 默认数据
        Object[][] tableVales = new String[fields.size()][6];
        for (int row = 0; row < fields.size(); row++) {
            tableVales[row][0] = fields.get(row).getName();
            tableVales[row][1] = fields.get(row).getJavaType().getFullyQualifiedName();
            tableVales[row][2] = fields.get(row).getColumn();
            tableVales[row][3] = fields.get(row).getColumnType();
            tableVales[row][4] = String.valueOf(fields.get(row).getLength());
            tableVales[row][5] = fields.get(row).getComment();
        }
        DefaultTableModel tableModel = new DefaultTableModel(tableVales, columnNames) {
            private static final long serialVersionUID = -9011814014486029286L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        fieldTable.setModel(tableModel);
    }

    public List<FieldImpl> getFields() {
        List<FieldImpl> fields = new ArrayList<>();
        List<String> ignoreList = new ArrayList<>();

        DefaultTableModel tableModel = (DefaultTableModel) fieldTable.getModel();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            FieldImpl field = new FieldImpl();
            // field
            field.setName(tableModel.getValueAt(i, 0).toString());
            field.setJavaType(new JavaTypeImpl(tableModel.getValueAt(i, 1).toString()));
            field.setColumn(tableModel.getValueAt(i, 2).toString());
            field.setColumnType(tableModel.getValueAt(i, 3).toString());
            if (Objects.nonNull(tableModel.getValueAt(i, 4))) {
                field.setLength(Integer.parseInt(tableModel.getValueAt(i, 4).toString()));
            }
            if (Objects.nonNull(tableModel.getValueAt(i, 5))) {
                field.setComment(tableModel.getValueAt(i, 5).toString());
            }
            // 过滤
            if (!ignoreList.contains(field.getColumn().toUpperCase().trim())) {
                fields.add(field);
            }
        }
        return fields;
    }

    public JComponent getRootComponent() {
        return rootPanel;
    }

    private void createUIComponents() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());
        topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        mainPanel = new JPanel(new GridLayout(1, 1));

        rootPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setLayout(new BorderLayout(0, 0));
        rootPanel.add(mainPanel, BorderLayout.CENTER);
        columnsPanel = new JPanel();
        columnsPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(columnsPanel, BorderLayout.CENTER);
        topPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(topPanel, BorderLayout.NORTH);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        topPanel.add(panel1,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null,
                        0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Comment");
        panel1.add(label1, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        commentTextField = new JTextField();
        panel1.add(commentTextField,
                new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null,
                        new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Table ");
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tableTextField = new JTextField();
        panel1.add(tableTextField,
                new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null,
                        new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Entity");
        panel1.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        entityTextField = new JTextField();
        panel1.add(entityTextField,
                new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null,
                        new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Parent");
        panel1.add(label4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        parentTextField = new ClassChooserField(myProject);
        panel1.add(parentTextField,
                new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null,
                        0, false));
    }

}
