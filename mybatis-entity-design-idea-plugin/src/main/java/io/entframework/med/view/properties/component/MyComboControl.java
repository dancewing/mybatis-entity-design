/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties.component;

import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Factory;
import com.intellij.ui.JBColor;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.highlighting.DomElementAnnotationsManager;
import com.intellij.util.xml.highlighting.DomElementProblemDescriptor;
import com.intellij.util.xml.highlighting.DomElementsProblemsHolder;
import com.intellij.util.xml.ui.BaseModifiableControl;
import com.intellij.util.xml.ui.DomFixedWrapper;
import com.intellij.util.xml.ui.DomWrapper;
import com.intellij.util.xml.ui.TooltipUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyComboControl extends BaseModifiableControl<JComboBox<Enum>, Enum> {
    private final Factory<? extends List<Enum>> myDataFactory;
    private boolean myNullable;
    private final boolean myCommitOnEveryChange;
    private final ItemListener myCommitListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                setModified();
                if (myCommitOnEveryChange) {
                    commit();
                }
            }
        }
    };

    public MyComboControl(final GenericDomValue genericDomValue, final Factory<? extends List<Enum>> dataFactory, boolean commitOnEveryChange) {
        this(new DomFixedWrapper<>(genericDomValue), dataFactory, commitOnEveryChange);
    }

    public MyComboControl(final DomWrapper<Enum> domWrapper, final Factory<? extends List<Enum>> dataFactory, boolean commitOnEveryChange) {
        super(domWrapper);
        myDataFactory = dataFactory;
        this.myCommitOnEveryChange = commitOnEveryChange;
        reset();
    }

    public MyComboControl(final DomWrapper<Enum> domWrapper, final Class<? extends Enum<?>> aClass, boolean commitOnEveryChange) {
        super(domWrapper);
        myDataFactory = createEnumFactory(aClass);
        this.myCommitOnEveryChange = commitOnEveryChange;
        reset();
    }

    public final boolean isNullable() {
        return myNullable;
    }

    public final void setNullable(final boolean nullable) {
        myNullable = nullable;
    }

    static Factory<List<Enum>> createEnumFactory(final Class<? extends Enum> aClass) {
        return () -> List.of(aClass.getEnumConstants());
    }

    static JComboBox<Enum> initComboBox(final JComboBox<Enum> comboBox,
                                                      final Condition<? super Enum> validity) {
        comboBox.setEditable(false);
       // comboBox.setPrototypeDisplayValue(new ComboBoxItem(null, null));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                final Enum pair = (Enum)value;
                if (pair!=null) {
                    setText(pair.name());
                }
                if (!validity.value(pair)) {
                    setFont(getFont().deriveFont(Font.ITALIC));
                    setForeground(JBColor.RED);
                }
                //setPreferredSize(new Dimension(-1, dimension.height));
                return this;
            }
        });
        return comboBox;
    }

    @Override
    protected JComboBox<Enum> createMainComponent(final JComboBox<Enum> boundedComponent) {
        return initComboBox(boundedComponent == null ? new JComboBox<>() : boundedComponent, object -> isValidValue(object));
    }

    public boolean isValidValue(final Enum object) {
        return myNullable && object == null;
    }

    private boolean dataChanged(List<Enum> newData) {
        final JComboBox<Enum> comboBox = getComponent();
        final int size = comboBox.getItemCount();
        final List<Enum> oldData = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            oldData.add(comboBox.getItemAt(i));
        }

        if (myNullable) {
            final LinkedList<Enum> list = new LinkedList<>(newData);
            list.addFirst(null);
            newData = list;
        }

        return !newData.equals(oldData);
    }

    @Override
    protected void doReset() {
        final List<Enum> data = myDataFactory.create();
        final JComboBox<Enum> comboBox = getComponent();
        comboBox.removeItemListener(myCommitListener);
        try {
            if (!dataChanged(data)) {
                super.doReset();
                return;
            }

            final Enum oldValue = getValue();
            comboBox.removeAllItems();
            if (myNullable) {
                comboBox.addItem(null);
            }
            for (final Enum s : data) {
                comboBox.addItem(s);
            }
            setValue(oldValue);
            super.doReset();
        }
        finally {
            comboBox.addItemListener(myCommitListener);
        }
    }

    @Override
    @Nullable
    protected final Enum getValue() {
        final Enum pair = (Enum) getComponent().getSelectedItem();
        return pair == null ? null : pair;
    }

    @Override
    protected final void setValue(final Enum value) {
        final JComboBox<Enum> component = getComponent();
        if (!isValidValue(value)) {
            component.setEditable(true);
        }
        component.setSelectedItem(value);
        component.setEditable(false);
    }


    @Override
    protected void updateComponent() {
        final DomElement domElement = getDomElement();
        if (domElement == null || !domElement.isValid()) return;

        final JComboBox<Enum> comboBox = getComponent();

        final Project project = getProject();
        ApplicationManager.getApplication().invokeLater(() -> {
            if (!project.isOpen()) return;
            if (!getDomWrapper().isValid()) return;

            final DomElement domElement1 = getDomElement();
            if (domElement1 == null || !domElement1.isValid()) return;

            final DomElementAnnotationsManager manager = DomElementAnnotationsManager.getInstance(project);
            final DomElementsProblemsHolder holder = manager.getCachedProblemHolder(domElement1);
            final List<DomElementProblemDescriptor> errorProblems = holder.getProblems(domElement1);
            final List<DomElementProblemDescriptor> warningProblems = holder.getProblems(domElement1, true, HighlightSeverity.WARNING);

            Color background = getDefaultBackground();
            comboBox.setToolTipText(null);

            if (errorProblems.size() > 0) {
                background = getErrorBackground();
                comboBox.setToolTipText(TooltipUtils.getTooltipText(errorProblems));
            }
            else if (warningProblems.size() > 0) {
                background = getWarningBackground();
                comboBox.setToolTipText(TooltipUtils.getTooltipText(warningProblems));
            }

            final Enum pair = (Enum)comboBox.getSelectedItem();
            background = pair != null ? getDefaultBackground() : background;

            comboBox.setBackground(background);
            comboBox.getEditor().getEditorComponent().setBackground(background);
        });
    }
}
