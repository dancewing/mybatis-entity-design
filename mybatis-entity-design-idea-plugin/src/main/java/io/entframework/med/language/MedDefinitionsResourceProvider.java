/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.language;

import com.intellij.javaee.ResourceRegistrar;
import com.intellij.javaee.StandardResourceProvider;
import io.entframework.med.MedConstants;

public class MedDefinitionsResourceProvider implements StandardResourceProvider {
    public static final String MYBATIS_MAPPING_BINDINGS_1_0 = "https://mybatis-generator.oss-cn-shanghai.aliyuncs.com/mybatis-mapping-bindings-1.0.dtd";

    @Override
    public void registerResources(ResourceRegistrar registrar) {
        registrar.addStdResource(MYBATIS_MAPPING_BINDINGS_1_0, MedConstants.XSD_VERSION, "/dtd/mybatis-mapping-bindings-1.0.dtd",
                MedDefinitionsResourceProvider.class);
    }

}
