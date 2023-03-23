/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

import java.util.List;

public interface JavaType extends Comparable<JavaType> {
    boolean isExplicitlyImported();

    String getFullyQualifiedName();

    String getFullyQualifiedNameWithoutTypeParameters();

    List<String> getImportList();

    String getPackageName();

    String getShortName();

    String getShortNameWithoutTypeArguments();

    void addTypeArgument(JavaType type);

    List<JavaType> getTypeArguments();
}
