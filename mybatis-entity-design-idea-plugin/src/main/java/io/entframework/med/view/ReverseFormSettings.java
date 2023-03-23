/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view;

import io.entframework.med.configurable.model.ReverseSettings;

public class ReverseFormSettings extends ReverseSettings {

    private String _package;

    public String getPackage() {
        return _package;
    }

    public void setPackage(String _package) {
        this._package = _package;
    }

    public static ReverseFormSettings from(ReverseSettings settings) {
        ReverseFormSettings reverseFormSettings = new ReverseFormSettings();
        reverseFormSettings.setParent(settings.getParent());
        reverseFormSettings.setIgnoreFields(settings.getIgnoreFields());
        reverseFormSettings.setSearchString(settings.getSearchString());
        reverseFormSettings.setReplaceString(settings.getReplaceString());
        reverseFormSettings.setForceBigDecimals(settings.isForceBigDecimals());
        reverseFormSettings.setUseJSR310Types(settings.isUseJSR310Types());
        return reverseFormSettings;
    }

}
