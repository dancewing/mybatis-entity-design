/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.action;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import io.entframework.med.MedBundle;
import io.entframework.med.model.Entity;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Arrays;
import java.util.List;

public class ImportFromScriptChooseEntityStep extends WizardStep<ImportFromScriptWizardModel> {

    private Project myProject;

    private WizardNavigationState state;

    private ImportFromScriptWizardModel myModel;

    private JPanel contentPanel;

    private MyListener listener = new MyListener();

    public ImportFromScriptChooseEntityStep(Project project, ImportFromScriptWizardModel model) {
        super(MedBundle.message("import.choose.step"));
        this.myProject = project;
        this.myModel = model;
        contentPanel = new JPanel(new VerticalFlowLayout(true, false));
    }

    public void setState(WizardNavigationState state) {
        this.state = state;
    }

    @Override
    public JComponent prepare(WizardNavigationState state) {
        // enableFinish();
        if (myModel.getEntities() != null && !myModel.getEntities().isEmpty()) {
            addCheckbox(myModel.getEntities());
        }
        return contentPanel;
    }

    private void addCheckbox(List<Entity> entities) {
        contentPanel.removeAll();
        if (myModel.getFilter() == null) {
            myModel.setFilter(entities);
        }
        entities.forEach(entity -> {
            JBCheckBox checkBox = new JBCheckBox(entity.getName().getText());
            checkBox.setSelected(myModel.getFilter().contains(entity));
            checkBox.addChangeListener(listener);
            contentPanel.add(checkBox);
        });
    }

    private class MyListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            state.NEXT.setEnabled(false);
            List<String> checked = Arrays.stream(contentPanel.getComponents())
                    .filter(JBCheckBox.class::isInstance)
                    .map(JBCheckBox.class::cast)
                    .filter(AbstractButton::isSelected)
                    .map(JBCheckBox::getText)
                    .toList();
            state.NEXT.setEnabled(!checked.isEmpty());
            myModel.setFilter(
                    myModel.getEntities().stream().filter(entity -> checked.contains(entity.getName())).toList());
        }

    }

}
