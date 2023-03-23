/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.idea.action;

import com.github.hykes.codegen.utils.PsiUtil;
import com.intellij.database.dialects.DatabaseDialect;
import com.intellij.database.model.DasColumn;
import com.intellij.database.psi.DbElement;
import com.intellij.database.psi.DbTable;
import com.intellij.database.util.DasUtil;
import com.intellij.database.util.JdbcUtil;
import com.intellij.database.view.DatabaseContextFun;
import com.intellij.database.view.DatabaseView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.util.containers.JBIterable;
import io.entframework.med.MedBundle;
import io.entframework.med.MedIcons;
import io.entframework.med.dialog.ReverseSettingDialog;
import io.entframework.med.model.*;
import io.entframework.med.util.TextUtil;
import org.jetbrains.annotations.NotNull;

import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MedReverseAction extends AnAction implements DumbAware {

    private static final Logger log = Logger.getInstance(MedReverseAction.class);

    public MedReverseAction() {
        super(MedIcons.ICON);
    }

    @Override
    public void update(AnActionEvent e) {
        DatabaseView view = DatabaseView.DATABASE_VIEW_KEY.getData(e.getDataContext());
        if (view == null) {
            e.getPresentation().setEnabledAndVisible(false);
            return;
        }

        Iterator<DbElement> iterator = DatabaseContextFun.getSelectedDbElements(e.getDataContext(), DbElement.class)
                .toSet()
                .iterator();

        boolean hasTable = false;
        while (iterator.hasNext()) {
            DbElement table = iterator.next();
            if (table instanceof DbTable) {
                hasTable = true;
                break;
            }
        }
        e.getPresentation().setEnabledAndVisible(hasTable);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = PsiUtil.getProject(e);
        DumbService dumbService = DumbService.getInstance(project);
        if (dumbService.isDumb()) {
            dumbService.showDumbModeNotification(MedBundle.message("codegen.plugin.is.not.available.during.indexing"));
            return;
        }

        Iterator<DbElement> iterator = DatabaseContextFun.getSelectedDbElements(e.getDataContext(), DbElement.class)
                .toSet()
                .iterator();

        List<DbTable> dbTables = new ArrayList<>();
        while (iterator.hasNext()) {
            DbElement table = iterator.next();
            if (table instanceof DbTable dbTable) {
                dbTables.add(dbTable);
            }
        }

        List<Entity> entities = new ArrayList<>();
        for (DbTable dbTable : dbTables) {
            DatabaseDialect databaseDialect = dbTable.getDataSource().getDatabaseDialect();
            EntityImpl entity = new EntityImpl();
            entity.setTable(dbTable.getName());
            entity.setName(Name.of(TextUtil.underlineToCamel(dbTable.getName(), true)));
            entity.setComment(dbTable.getComment());

            List<Field> fields = new ArrayList<>();

            JBIterable<? extends DasColumn> columnsIter = DasUtil.getColumns(dbTable);
            List<? extends DasColumn> dasColumns = columnsIter.toList();
            for (DasColumn dasColumn : dasColumns) {
                FieldImpl field = new FieldImpl();
                field.setColumn(dasColumn.getName());
                field.setName(TextUtil.underlineToCamel(dasColumn.getName()));
                field.setColumnType(dasColumn.getDasType().toDataType().typeName);
                field.setLength(dasColumn.getDasType().toDataType().size);
                field.setComment(dasColumn.getComment());
                field.setNullable(!dasColumn.isNotNull());
                if (DasUtil.isPrimary(dasColumn)) {
                    field.setPK(true);
                }

                field.setColumnType(JdbcUtil.getJdbcTypeName(dasColumn.getDasType().toDataType(), false));
                //int jdbcType = JdbcUtil.guessJdbcTypeByName(field.getColumnType());
                int jdbcType = databaseDialect.getJavaTypeForNativeType(field.getColumnType());
                field.setJdbcType(JDBCType.valueOf(jdbcType));
                fields.add(field);
            }
            entity.setFields(fields);
            entities.add(entity);
        }

        ReverseSettingDialog settingDialog = new ReverseSettingDialog(project, entities);
        settingDialog.showAndGet();
    }

}