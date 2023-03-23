/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties.component;

import com.intellij.ui.components.fields.IntegerField;
import com.intellij.util.xml.ui.BaseModifiableControl;
import com.intellij.util.xml.ui.DomWrapper;
import org.jetbrains.annotations.Nullable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class NumberControl extends BaseModifiableControl<IntegerField, Integer> {
    private boolean myUndefined;
    private final boolean myCommitOnEveryChange;

    public NumberControl(DomWrapper<Integer> domWrapper, boolean commitOnEveryChange) {
        super(domWrapper);
        this.myCommitOnEveryChange = commitOnEveryChange;
    }

    @Override
    protected IntegerField createMainComponent(IntegerField boundedComponent) {
        IntegerField integerField = boundedComponent == null ? new IntegerField() : boundedComponent;
        integerField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myUndefined = false;
                setModified();
                if (myCommitOnEveryChange) {
                    commit();
                    reset();
                }
            }
        });

        integerField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                myUndefined = false;
                setModified();
                if (myCommitOnEveryChange) {
                    commit();
                    reset();
                }
            }
        });

        return integerField;
    }

    @Override
    protected @Nullable Integer getValue() {
        if (myUndefined) {
            return null;
        }
        Integer value = getComponent().getValue();
        if (value != null) {
            Integer defaultValue = getComponent().getDefaultValue();
            if (defaultValue.equals(value)) {
                return null;
            }
        }
        return value;
    }

    @Override
    protected void setValue(Integer value) {
        myUndefined = value == null;
        if (value != null) {
            getComponent().setValue(value);
        }
    }
}
