/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.service;

import io.entframework.med.model.Field;
import io.entframework.med.model.JavaType;

public interface JavaTypeResolver {


    JavaType calculateJavaType(Field column);

    String calculateJdbcTypeName(Field column);
}
