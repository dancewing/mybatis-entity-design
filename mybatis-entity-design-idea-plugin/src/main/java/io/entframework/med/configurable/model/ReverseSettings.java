/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.model;

public class ReverseSettings {

    private String parent;

    private String ignoreFields;

    private String searchString;

    private String replaceString;

    private boolean forceBigDecimals;

    private boolean useJSR310Types;

    public String getParent() {
        return parent;
    }

    public String getIgnoreFields() {
        return ignoreFields;
    }

    public String getSearchString() {
        return searchString;
    }

    public String getReplaceString() {
        return replaceString;
    }

    public boolean isForceBigDecimals() {
        return forceBigDecimals;
    }

    public boolean isUseJSR310Types() {
        return useJSR310Types;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setIgnoreFields(String ignoreFields) {
        this.ignoreFields = ignoreFields;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public void setReplaceString(String replaceString) {
        this.replaceString = replaceString;
    }

    public void setForceBigDecimals(boolean forceBigDecimals) {
        this.forceBigDecimals = forceBigDecimals;
    }

    public void setUseJSR310Types(boolean useJSR310Types) {
        this.useJSR310Types = useJSR310Types;
    }

}
