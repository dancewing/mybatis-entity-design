/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.dom;

import com.intellij.util.xml.converters.values.NumberValueConverter;

public class IntegerConverter extends NumberValueConverter<Integer> {
    public IntegerConverter() {
        super(Integer.class, true);
    }
}
