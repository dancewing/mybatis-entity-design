/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties.dom;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ReflectionUtil;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomUtil;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.highlighting.DomElementAnnotationsManager;
import com.intellij.util.xml.reflect.AbstractDomChildrenDescription;
import com.intellij.util.xml.reflect.DomAttributeChildDescription;
import com.intellij.util.xml.reflect.DomChildrenDescription;
import com.intellij.util.xml.reflect.DomFixedChildDescription;
import com.intellij.util.xml.ui.BasicDomElementComponent;
import com.intellij.util.xml.ui.DomFixedWrapper;
import com.intellij.util.xml.ui.DomUIControl;
import com.intellij.util.xml.ui.DomUIFactory;
import io.entframework.med.dom.MyDomElement;
import io.entframework.med.util.TextUtil;
import io.entframework.med.view.properties.component.MyComboControl;
import io.entframework.med.view.properties.component.NumberControl;
import io.entframework.med.view.properties.component.SingleLineTextControl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.JDBCType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractDomElementComponent<T extends MyDomElement> extends BasicDomElementComponent<T> {

    private static final Logger LOG = Logger.getInstance(AbstractDomElementComponent.class);
    private Project myProject;
    private int myMode;

    protected AbstractDomElementComponent(T domElement) {
        super(domElement);
        this.myProject = domElement.getManager().getProject();
    }

    public final void build() {
        bindAttributes();
        afterBuild();
    }

    protected void afterBuild(){}

    public void setMode(int mode) {
        this.myMode = mode;
    }

    protected void bindAttributes() {
        bindAttributes(getDomElement());
    }

    protected final void bindAttributes(final DomElement domElement) {
        if (domElement == null)
            return;

        DomElementAnnotationsManager.getInstance(domElement.getManager().getProject()).addHighlightingListener(new DomElementAnnotationsManager.DomHighlightingListener() {
            @Override
            public void highlightingFinished(@NotNull final DomFileElement element) {
                ApplicationManager.getApplication().invokeLater(() -> {
                    if (getComponent().isShowing() && element.isValid()) {
                        updateHighlighting();
                    }
                });
            }
        }, this);

        List<? extends AbstractDomChildrenDescription> childrenDescriptions = domElement.getGenericInfo().getChildrenDescriptions();
        Map<String, DomChildrenDescription> childDescriptionMap = childrenDescriptions.stream()
                .filter(abstractDomChildrenDescription -> abstractDomChildrenDescription instanceof DomChildrenDescription)
                .map(abstractDomChildrenDescription -> (DomChildrenDescription) abstractDomChildrenDescription)
                .collect(Collectors.toMap(DomChildrenDescription::getName, domAttributeChildDescription -> domAttributeChildDescription));
        for (Field field : getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                String propertyName = lowerCaseFirst(StringUtil.trimStart(field.getName(), "my"));
                Object fieldValue = field.get(this);
                DomChildrenDescription domChildrenDescription = childDescriptionMap.get(propertyName);
                if (fieldValue instanceof JComponent boundComponent) {
                    if (domChildrenDescription instanceof DomAttributeChildDescription<?> description) {
                        final GenericDomValue<?> element = domElement.getManager().createStableValue(
                                () -> domElement.isValid() ? (GenericDomValue<?>)description.getValues(domElement).get(0) : null);
                        Type type = DomUtil.getGenericValueParameter(element.getDomElementType());
                        DomUIControl<GenericDomValue<?>> uiControl = createUIControl(type, element);
                        doBind(uiControl, boundComponent);
                    }
                    if (domChildrenDescription instanceof DomFixedChildDescription && DomUtil.isGenericValueType(domChildrenDescription.getType())) {
                        if ((domChildrenDescription.getValues(domElement)).size() == 1) {
                            final GenericDomValue<?> element = domElement.getManager().createStableValue(
                                    () -> domElement.isValid() ? (GenericDomValue<?>)domChildrenDescription.getValues(domElement).get(0) : null);
                            Type type = DomUtil.getGenericValueParameter(element.getDomElementType());
                            DomUIControl<GenericDomValue<?>> uiControl = createUIControl(type, element);
                            doBind(uiControl, boundComponent);
                        }
                    }
                }

            } catch (Exception e) {
                LOG.error(e);
            }
        }
        reset();
    }

    private DomUIControl<GenericDomValue<?>> createUIControl(Type type, GenericDomValue element) {
        DomUIControl uiControl;
        final Class rawType = ReflectionUtil.getRawType(type);
        if (type.equals(String.class)) {
            uiControl = new SingleLineTextControl(new DomFixedWrapper(element), commitOnEveryChange(element));
        } else if (JDBCType.class.isAssignableFrom(rawType)) {
            uiControl = new MyComboControl(new DomFixedWrapper(element), JDBCType.class,  commitOnEveryChange(element));
        } else if (type.equals(Integer.class)) {
            uiControl = new NumberControl(new DomFixedWrapper<>(element), commitOnEveryChange(element));
        } else {
            uiControl = DomUIFactory.createControl(element, commitOnEveryChange(element));
        }
        return uiControl;
    }

    protected boolean commitOnEveryChange(GenericDomValue element) {
        return this.myMode == 1;
    }


    private String lowerCaseFirst(String text) {
        if (text.length() == 1) {
            return text;
        }
        return TextUtil.camelToRest(StringUtil.toLowerCase(text.substring(0, 1)) + text.substring(1));
    }

}
