/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.action;

import com.intellij.database.dataSource.LocalDataSource;
import com.intellij.database.dataSource.SyncQueue;
import com.intellij.database.model.DasNamespace;
import com.intellij.database.model.DasTable;
import com.intellij.database.model.basic.BasicElement;
import com.intellij.database.psi.DbDataSource;
import com.intellij.database.util.*;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import com.intellij.util.containers.JBIterable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ImportFromDataSourceChooseStep extends WizardStep<ImportFromDataSourceWizardModel> {

    private JPanel contentPanel;

    private JComboBox<DbDataSource> dataSourceComboBox;

    private JButton refreshButton;

    private JPanel dbTablePanel;

    private JComboBox<DasNamespace> schemaComboBox;

    private Project myProject;

    private WizardNavigationState state;

    private ImportFromDataSourceWizardModel myModel;

    private JBIterable<? extends DasTable> dasTables;

    public ImportFromDataSourceChooseStep(Project project, ImportFromDataSourceWizardModel model) {
        this.myProject = project;
        this.myModel = model;
        refreshButton.setIcon(AllIcons.Actions.Refresh);

        dbTablePanel.setPreferredSize(new Dimension(400, 60));
        dbTablePanel.setSize(new Dimension(400, 60));
        dbTablePanel.setMaximumSize(new Dimension(400, 60));

        DbDataSourceCellRenderer dataSourceCellRenderer = new DbDataSourceCellRenderer();
        dataSourceComboBox.setRenderer(dataSourceCellRenderer);
        dataSourceComboBox.addItemListener(dataSourceCellRenderer);

        JBIterable<DbDataSource> dataSources = DbUtil.getDataSources(myProject);
        if (dataSources.isNotEmpty()) {
            dataSources.forEach(dbDataSource -> dataSourceComboBox.addItem(dbDataSource));
        }

        if (dataSources.isNotEmpty()) {
            dasTables = DasUtil.getTables(dataSources.get(0));
        }

        DbSchemaCellRenderer schemaCellRenderer = new DbSchemaCellRenderer();
        schemaComboBox.setRenderer(schemaCellRenderer);
        schemaComboBox.addItemListener(schemaCellRenderer);

        refreshButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DbDataSource dataSource = (DbDataSource) dataSourceComboBox.getSelectedItem();
                if (dataSource != null
                        && dataSource.getDelegateDataSource() instanceof LocalDataSource localDataSource) {
                    AsyncTask<SyncQueue.SyncResult> asyncTask = DataSourceUtil
                            .performAutoSyncTask(LoaderContext.selectGeneralTask(myProject, localDataSource));
                    asyncTask.whenComplete(null, ((syncResult, throwable) -> {
                        JBIterable<? extends DasNamespace> schemas = getSchemas(dataSource);
                        dasTables = DasUtil.getTables(dataSource);
                        addSchema(schemas);
                    }));
                }
            }
        });
    }

    private JBIterable<? extends DasNamespace> getSchemas(@NotNull DbDataSource dataSource) {
        if (dataSource.getDelegateDataSource() instanceof LocalDataSource localDataSource) {
            return DasUtil.getSchemas(dataSource)
                    .filter(dasNamespace -> localDataSource.getSchemaMapping().isIntrospected((BasicElement) dasNamespace));
        }
        return JBIterable.empty();
    }

    private void addSchema(JBIterable<? extends DasNamespace> schemas) {
        if (schemas != null) {
            schemaComboBox.removeAll();
            schemas.forEach(dasNamespace -> schemaComboBox.addItem(dasNamespace));
        }
    }

    private void addTable(@NotNull DasNamespace dasNamespace) {
        dbTablePanel.removeAll();
        List<? extends DasTable> tables = dasTables
                .filter(dasTable -> dasTable.getDasParent() != null
                        && StringUtil.equalsIgnoreCase(dasTable.getDasParent().getName(), dasNamespace.getName()))
                .toList();
        tables.forEach(dbTable -> {
            JBCheckBox checkBox = new JBCheckBox(dbTable.getName());
            dbTablePanel.add(checkBox);
        });
        dbTablePanel.updateUI();
    }

    public void setState(WizardNavigationState state) {
        this.state = state;
    }

    @Override
    public JComponent prepare(WizardNavigationState state) {
        return contentPanel;
    }

    public class DbDataSourceCellRenderer extends JLabel implements ListCellRenderer<DbDataSource>, ItemListener {

        @Override
        public Component getListCellRendererComponent(JList list, DbDataSource value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            setHorizontalAlignment(SwingConstants.CENTER);
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
                Object selected = dataSourceComboBox.getSelectedItem();
                if (selected instanceof DbDataSource dataSource) {
                    addSchema(getSchemas(dataSource));
                }
            }
        }

    }

    public class DbSchemaCellRenderer extends JLabel implements ListCellRenderer<DasNamespace>, ItemListener {

        @Override
        public Component getListCellRendererComponent(JList list, DasNamespace value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            setHorizontalAlignment(SwingConstants.CENTER);
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
                Object selected = schemaComboBox.getSelectedItem();
                if (selected instanceof DasNamespace dasNamespace) {
                    addTable(dasNamespace);
                }
            }
        }

    }

}
