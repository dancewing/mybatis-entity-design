/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties.editor;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.util.ui.JBUI;
import io.entframework.med.dom.MyDomElement;
import io.entframework.med.view.component.MyCollapsiblePanel;
import io.entframework.med.view.properties.dom.AbstractDomElementComponent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractPropertyEditor<T extends MyDomElement> extends JPanel {

    protected transient T value;

    protected JPanel mainPanel;

    protected int myMode;

    private final Set<AbstractDomElementComponent> myDomComponents = new HashSet<>();

    protected AbstractPropertyEditor(T value) {
        super(new BorderLayout());
        setBorder(JBUI.Borders.empty());
        this.value = value;
        this.mainPanel = new JPanel(new VerticalFlowLayout(true, false));
        this.add(mainPanel, BorderLayout.CENTER);
    }

    @NotNull
    public Project getProject() {
        return value.getManager().getProject();
    }

    public void setMode(int mode) {
        this.myMode = mode;
    }

    public final void build() {
        if (this.myMode != 0) {
            buildToolbar();
        }
        buildEditor();
    }

    public abstract void buildEditor();

    public void buildToolbar() {
        DefaultActionGroup actionGroup = getActionGroup();
        if (actionGroup != null) {
            JPanel toolbarPanel = new JPanel(new GridLayout(1, 2, 2, 0));
            toolbarPanel.setBorder(JBUI.Borders.emptyBottom(1));
            ActionToolbar toolbar = ActionManager.getInstance()
                    .createActionToolbar("PropertyEditorView", actionGroup, true);
            toolbar.setTargetComponent(this);
            toolbarPanel.add(toolbar.getComponent());
            this.add(toolbarPanel, BorderLayout.NORTH);
        }
    }

    protected void addPanel(JPanel panel) {
        if (this.mainPanel.getComponents() != null && this.mainPanel.getComponents().length > 0) {
            this.mainPanel.add(new JSeparator());
        }
        this.mainPanel.add(panel);
    }

    protected DefaultActionGroup getActionGroup() {
        return null;
    }

    public JPanel createPanel(String title, @NotNull AbstractDomElementComponent<?> elementComponent) {
        elementComponent.setMode(this.myMode);
        elementComponent.build();
        myDomComponents.add(elementComponent);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(elementComponent.getComponent(), BorderLayout.NORTH);
        panel.setBorder(IdeBorderFactory.createTitledBorder(title, true, JBUI.insetsLeft(1)));
        return panel;
    }

    public MyCollapsiblePanel createCreateCollapsiblePanel(@NotNull String title, @NotNull String buttonText, @NotNull ButtonClickCallback clickCallback) {
        JButton button = new JButton(buttonText);
        button.addActionListener(e -> clickCallback.doAction());
        return new MyCollapsiblePanel(button, title);
    }

    public void add(JComponent component) {
        this.mainPanel.add(component);
    }

    public void commit() {
        myDomComponents.forEach(AbstractDomElementComponent::commit);
    }

    public boolean isValueValid() {

        return true;
    }

    public interface ButtonClickCallback {
        void doAction();
    }

    /**
     * 保存文件
     */
    public void save() {
//        XmlFile xmlFile = value.getXmlFile();
//        Project project = xmlFile.getProject();
//        MedDomUtil.save(value.getRoot(), project);
    }
}
