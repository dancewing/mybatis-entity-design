/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.dom;

import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.EnumConverter;
import com.intellij.util.xml.ResolvingConverter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.JDBCType;
import java.util.Collection;
import java.util.List;

public class JdbcTypeConverter extends ResolvingConverter<JDBCType> {

    private static EnumConverter<JDBCType> enumConverter = EnumConverter.createEnumConverter(JDBCType.class);

    @Override
    public @Nullable JDBCType fromString(@Nullable @NonNls String s, ConvertContext context) {
        return enumConverter.fromString(s, context);
    }

    @Override
    public @Nullable String toString(@Nullable JDBCType jdbcType, ConvertContext context) {
        return enumConverter.toString(jdbcType, context);
    }

    @Override
    public @NotNull Collection<? extends JDBCType> getVariants(ConvertContext context) {
        return List.of(JDBCType.values());
    }
}
