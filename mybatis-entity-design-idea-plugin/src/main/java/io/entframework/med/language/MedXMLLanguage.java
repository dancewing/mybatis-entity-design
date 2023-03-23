/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.language;

import com.intellij.lang.xml.XMLLanguage;

public class MedXMLLanguage extends XMLLanguage {

    public static final MedXMLLanguage INSTANCE = new MedXMLLanguage();

    protected MedXMLLanguage() {
        super(XMLLanguage.INSTANCE, "MED", "application/xml", "text/xml");
    }

}
