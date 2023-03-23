/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.action;

import com.github.hykes.codegen.parser.DefaultParser;
import com.github.hykes.codegen.parser.Parser;
import com.github.hykes.codegen.provider.FileProviderFactory;
import com.github.hykes.codegen.utils.NotifyUtil;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import com.intellij.util.ui.JBUI;
import io.entframework.med.MedBundle;
import io.entframework.med.model.Entity;
import io.entframework.med.view.cmt.JBalloon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ImportFromScriptEditorStep extends WizardStep<ImportFromScriptWizardModel> {

    private static final Logger LOGGER = Logger.getInstance(ImportFromScriptEditorStep.class);

    private JPanel rootPanel;

    private Editor sqlTextArea;

    private JPanel sqlPanel;

    private JScrollPane sqlScrollPane;

    private Project myProject;

    private WizardNavigationState state;

    private ImportFromScriptWizardModel myModel;

    public ImportFromScriptEditorStep(@NotNull Project project, ImportFromScriptWizardModel model) {
        super(MedBundle.message("import.script.step"));
        this.myProject = project;
        this.myModel = model;
    }

    public void setState(WizardNavigationState state) {
        this.state = state;
    }

    protected @Nullable JComponent createCenterPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = new JPanel();
        sqlPanel = new JPanel();
        sqlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        sqlPanel.setPreferredSize(JBUI.size(600, 360));
        // 设置 sql text editor
        Document document = EditorFactory.getInstance().createDocument("");
        sqlTextArea = EditorFactory.getInstance()
                .createEditor(document, myProject, FileProviderFactory.getFileType("SQL"), false);

        sqlTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                state.NEXT.setEnabled(valid());
            }
        });
        sqlScrollPane = new JBScrollPane();
        sqlScrollPane.setViewportView(sqlTextArea.getComponent());
    }

    public JComponent getRootComponent() {
        return rootPanel;
    }

    /**
     * 执行OK的动作
     */

    public void ok() {
        try {
            String sqls = sqlTextArea.getDocument().getText();
            Parser parser = new DefaultParser();
            List<Entity> entities = parser.parseSQLs(sqls);
            if (entities == null || entities.isEmpty()) {
                NotifyUtil.notice("CodeGen-SQL", "please check sql format !", MessageType.ERROR);
                return;
            }

            // ColumnEditorFrame frame = new ColumnEditorFrame(myProject);
            // frame.newColumnEditorBySql(ideaContext, entities);
            // MyDialogWrapper frameWrapper = new MyDialogWrapper(myProject,
            // frame.getRootPane());
            // frameWrapper.setActionOperator(frame);
            // frameWrapper.setTitle("CodeGen-SQL");
            // frameWrapper.setSize(800, 550);
            // frameWrapper.setResizable(false);
            // frameWrapper.show();

            // disable();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        // this.rootPanel.registerKeyboardAction(e -> disable(),
        // KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        // JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    // @Override
    public void cancel() {
    }

    // @Override
    protected @NotNull List<ValidationInfo> doValidateAll() {
        List<ValidationInfo> validationInfos = new ArrayList<>();

        String script = sqlTextArea.getDocument().getText();
        if (StringUtil.isEmptyOrSpaces(script)) {
            validationInfos
                    .add(new ValidationInfo("Input script can not be empty!", sqlTextArea.getContentComponent()));
        } else {
            Parser parser = new DefaultParser();
            List<Entity> entities = parser.parseSQLs(script);
            if (entities == null || entities.isEmpty()) {
                validationInfos.add(new ValidationInfo("Can not parse script, please check the format!!",
                        sqlTextArea.getContentComponent()));
            }
        }

        return validationInfos;
    }

    // @Override
    public boolean valid() {
        // 1. check empty
        String sqls = sqlTextArea.getDocument().getText();
        if (StringUtils.isBlank(sqls)) {
            JBalloon.buildSimple("Input script can not be empty!").show(RelativePoint.getSouthOf(this.sqlScrollPane));
            return false;
        }
        // 2. check parse
        Parser parser = new DefaultParser();
        List<Entity> entities = parser.parseSQLs(sqls);
        if (entities == null || entities.isEmpty()) {
            JBalloon.buildSimple("Can not parse sqls, please check sql format!")
                    .show(RelativePoint.getSouthOf(this.sqlScrollPane));
            return false;
        }
        myModel.setEntities(entities);
        myModel.setFilter(null);
        return true;
    }

    @Override
    public JComponent prepare(WizardNavigationState state) {
        state.NEXT.setEnabled(myModel.getEntities() != null && !myModel.getEntities().isEmpty());
        return rootPanel;
    }

}
