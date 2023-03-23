/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.util.messages.MessageBusConnection;
import io.entframework.med.dom.MyDomElement;
import io.entframework.med.view.component.ContainerPanel;
import io.entframework.med.view.properties.editor.AbstractPropertyEditor;
import io.entframework.med.view.properties.editor.PropertyEditorFactory;
import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PropertyPanel extends ContainerPanel implements Disposable {

    private Project myProject;
    private MessageBusConnection myConnection;

    public PropertyPanel(@NotNull Project myProject, @NotNull SimpleTree objectTree) {
        this.myProject = myProject;
    }

    public void treeNodeSelected(MyDomElement baseElement) {
        AbstractPropertyEditor<?> abstractPropertyEditor = PropertyEditorFactory.get(baseElement);
        this.removeAll();
        if (abstractPropertyEditor != null) {
            abstractPropertyEditor.setMode(1);
            abstractPropertyEditor.build();
            add(abstractPropertyEditor);
        }
        this.updateUI();
    }

    @Override
    public void dispose() {
        if (myConnection != null) {
            Disposer.dispose(myConnection);
        }
    }

    public class MyMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
        }
    }

}
