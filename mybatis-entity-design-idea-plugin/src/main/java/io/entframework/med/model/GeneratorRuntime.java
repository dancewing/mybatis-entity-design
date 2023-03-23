/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class GeneratorRuntime {

    private Module module;
    private List<Enum> enums;

    @NotNull
    public Module getModule() {
        return module;
    }

    public void setModule(ModuleImpl module) {
        this.module = module;
    }

    public List<Entity> getEntities() {
        if (this.module != null) {
            return this.module.getEntities();
        }
        return Collections.emptyList();
    }

    public List<Enum> getEnums() {
        if (enums == null) {
            return Collections.emptyList();
        }
        return enums;
    }

    public void setEnums(List<Enum> enums) {
        this.enums = enums;
    }
}
