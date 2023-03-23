/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

import com.intellij.openapi.util.text.StringUtil;

public class JavaPackageImpl implements JavaPackage {
    private String packagePath;

    private JavaPackageImpl(String packagePath) {
        this.packagePath = packagePath;
    }

    @Override
    public JavaPackage getParent() {
        String parentPath = StringUtil.substringBeforeLast(packagePath, ".");
        return new JavaPackageImpl(parentPath);
    }

    public static JavaPackageImpl of(String packagePath) {
        return new JavaPackageImpl(packagePath);
    }

    @Override
    public String toString() {
        return packagePath;
    }

    @Override
    public String getValue() {
        return packagePath;
    }
}
