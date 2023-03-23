/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

public class GeneratorObjectImpl implements GeneratorObject {
    private final JavaPackage _package;

    public GeneratorObjectImpl(JavaPackage _package) {
        this._package = _package;
    }

    @Override
    public JavaPackage getPackage() {
        return _package;
    }
}
