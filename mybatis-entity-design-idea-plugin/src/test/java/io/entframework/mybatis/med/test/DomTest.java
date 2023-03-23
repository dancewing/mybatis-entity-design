/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.mybatis.med.test;

import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import io.entframework.med.dom.DomMed;

public class DomTest extends BasePlatformTestCase {

    protected String getTestDataPath() {
        return "testData/DomTest";
    }

    public void testDomLoad() {
        DomMed med1 = getMed();
        DomMed med2 = getMed();
        System.out.println(med1.equals(med2));
    }

    public DomMed getMed() {
        myFixture.configureByFiles("orm.xml");
        PsiFile psiFile = myFixture.getFile();
        if (psiFile instanceof XmlFile xmlFile) {
            DomManager domManager = DomManager.getDomManager(getProject());
            DomFileElement<DomMed> element = domManager.getFileElement(xmlFile, DomMed.class);
            if (element!=null) {
                return  element.getRootElement();
            }
        }
        return null;
    }
}
