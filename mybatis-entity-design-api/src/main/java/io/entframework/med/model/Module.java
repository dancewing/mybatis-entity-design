/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.model;

import java.util.List;

public interface Module extends GeneratorObject {
    String getName();

    String getDescription();

    List<Entity> getEntities();
}
