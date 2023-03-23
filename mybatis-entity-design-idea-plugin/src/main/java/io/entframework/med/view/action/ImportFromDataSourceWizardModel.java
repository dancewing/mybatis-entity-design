/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.action;

import com.intellij.ui.wizard.WizardModel;
import io.entframework.med.MedBundle;
import io.entframework.med.model.Entity;

import java.util.List;

public class ImportFromDataSourceWizardModel extends WizardModel {

    public ImportFromDataSourceWizardModel() {
        super(MedBundle.message("import.from.script"));
    }

    private List<Entity> entities;

    private List<Entity> filter;

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public List<Entity> getFilter() {
        return filter;
    }

    public void setFilter(List<Entity> filter) {
        this.filter = filter;
    }

}
