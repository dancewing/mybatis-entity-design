/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties.editor;

import com.intellij.openapi.actionSystem.DefaultActionGroup;
import io.entframework.med.dom.DomEntity;
import io.entframework.med.view.properties.action.AddTableAction;
import io.entframework.med.view.properties.dom.MedDomEntityForm;
import io.entframework.med.view.properties.dom.MedDomTableForm;

public class EntityPropertyEditor extends AbstractPropertyEditor<DomEntity> {

    public EntityPropertyEditor(DomEntity value) {
        super(value);
    }

    @Override
    public DefaultActionGroup getActionGroup() {
        if (value.getTable() == null) {
            DefaultActionGroup actionGroup = new DefaultActionGroup();
            actionGroup.add(AddTableAction.getInstance());
            return actionGroup;
        }
        return null;
    }

    @Override
    public void buildEditor() {
        addPanel(createPanel("Basic", new MedDomEntityForm(value)));
        if (value.getTable() != null) {
            addPanel(createPanel("Table", new MedDomTableForm(value.getTable())));
        } else {
            addPanel(createCreateCollapsiblePanel("Table", "Add Table", () -> {
//                String name = value.getName();
//                JaxbTable jaxbTable = ObjectFactoryUtil.getInstance().createJaxbTable();
//                jaxbTable.setName(TextUtil.camelToUnderline(name));
//                value.setTable(jaxbTable);
//                save();
//                getProject().getService(PropertyManager.class).treeNodeSelected(value, null);
            }));
        }
    }

}
