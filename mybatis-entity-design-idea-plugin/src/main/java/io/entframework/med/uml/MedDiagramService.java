/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.uml;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.Service;

@Service(Service.Level.PROJECT)
public final class MedDiagramService implements Disposable {

    @Override
    public void dispose() {
    }

}
