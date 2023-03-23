/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

import java.util.List;

public class ModuleImpl extends GeneratorObjectImpl implements Module {
    private String name;
    private String description;

    private List<Entity> entities;

    public ModuleImpl(JavaPackageImpl _package, String name) {
        super(_package);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<Entity> getEntities() {
        return entities;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }
}
