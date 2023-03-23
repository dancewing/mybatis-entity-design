/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.action;

import com.github.hykes.codegen.utils.PsiUtil;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.SimpleTree;
import io.entframework.med.MedBundle;
import io.entframework.med.dom.DomModule;
import io.entframework.med.model.Entity;
import io.entframework.med.util.MedDomUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ImportFromDataSourceAction extends BaseTreeAction {

    private static final String ACTION_ID = "MyBatisMed.Tree.ImportFromDataSource";

    public static AnAction getInstance() {
        return ActionManager.getInstance().getAction(ACTION_ID);
    }

    @Override
    public void doActionPerformed(@NotNull AnActionEvent e, SimpleTree simpleTree) {
        Project project = PsiUtil.getProject(e);
        DumbService dumbService = DumbService.getInstance(project);
        if (dumbService.isDumb()) {
            dumbService.showDumbModeNotification(MedBundle.message("codegen.plugin.is.not.available.during.indexing"));
            return;
        }

        ImportFromDataSourceWizardModel wizardModel = new ImportFromDataSourceWizardModel();
        ImportFromDataSourceChooseStep dataSourceChooseStep = new ImportFromDataSourceChooseStep(project, wizardModel);

        wizardModel.add(dataSourceChooseStep);
        dataSourceChooseStep.setState(wizardModel.getCurrentNavigationState());

        ImportFromDataSourceWizardDialog wizardDialog = new ImportFromDataSourceWizardDialog(project, false,
                wizardModel);
        wizardDialog.show();

        if (wizardDialog.isWizardGoalAchieved()) {
            List<Entity> entities = wizardModel.getFilter();
            if (!entities.isEmpty()) {
                Object userObject = getSelectedTreeObject(simpleTree);
                if (userObject instanceof DomModule jaxbEntityMappings) {
                    MedDomUtil.addEntity(entities, jaxbEntityMappings);
                    MedDomUtil.save(jaxbEntityMappings.getRoot(), project);
                }
            }
        }
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Object userObject) {
        if (userObject instanceof DomModule) {
            e.getPresentation().setEnabled(true);
        } else {
            e.getPresentation().setEnabled(false);
        }
    }

}
