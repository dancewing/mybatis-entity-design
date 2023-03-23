/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public final class MedBundle extends DynamicBundle {

    public static final @NonNls String BUNDLE = "messages.MEDBundle";

    public static final MedBundle INSTANCE = new MedBundle();

    public MedBundle() {
        super(BUNDLE);
    }

    public static @NotNull @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key,
                                               @NotNull Object... params) {
        return INSTANCE.getMessage(key, params);
    }

}
