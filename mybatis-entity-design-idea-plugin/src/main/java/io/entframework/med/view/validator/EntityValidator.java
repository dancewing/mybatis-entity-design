/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.validator;

import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.text.StringUtil;
import io.entframework.med.dom.DomEntity;
import io.entframework.med.dom.DomModule;
import io.entframework.med.util.DomSupport;

import java.util.Optional;

public class EntityValidator implements InputValidator {

    private final DomModule parent;
    private final DomEntity child;

    public EntityValidator(DomModule parent, DomEntity child) {
        this.parent = parent;
        this.child = child;
    }

    @Override
    public boolean checkInput(@NlsSafe String inputString) {
        if (StringUtil.isEmptyOrSpaces(inputString)) {
            return false;
        }

        Optional<DomEntity> check = parent.getEntities().stream().filter(domEntity -> {
            if (child!=null) {
                return !domEntity.equals(child);
            }
            return true;
        }).filter(domEntity -> StringUtil.equals(inputString, DomSupport.getValue(domEntity.getName(), ""))).findAny();

        return check.isEmpty();
    }

    @Override
    public boolean canClose(@NlsSafe String inputString) {
        return true;
    }
}
