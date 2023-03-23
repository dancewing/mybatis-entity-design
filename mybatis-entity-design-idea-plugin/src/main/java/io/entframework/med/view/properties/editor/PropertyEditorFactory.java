/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.properties.editor;

import io.entframework.med.dom.*;

public final class PropertyEditorFactory {

    public static AbstractPropertyEditor<?> get(MyDomElement value) {

        if (value instanceof DomModule jaxbEntityMappings) {
            return new EntityModulePropertyEditor(jaxbEntityMappings);
        }

        if (value instanceof DomEntity jaxbEntity) {
            return new EntityPropertyEditor(jaxbEntity);
        }

        if (value instanceof DomId jaxbId) {
            return new IdPropertyEditor(jaxbId);
        }

        if (value instanceof DomBasic jaxbBasic) {
            return new BasicPropertyEditor(jaxbBasic);
        }

        if (value instanceof DomEnumAttribute domEnumAttribute) {
            return new EnumAttributePropertyEditor(domEnumAttribute);
        }

        if (value instanceof DomEnumDefinition domEnumDefinition) {
            return new EnumDefinitionPropertyEditor(domEnumDefinition);
        }

        if (value instanceof DomVersion domVersion) {
            return new VersionPropertyEditor(domVersion);
        }


        return null;
    }

}
