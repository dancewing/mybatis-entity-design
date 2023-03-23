/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.action;


@FunctionalInterface
public interface CreateCallback {
    void call(String inputName);
}
