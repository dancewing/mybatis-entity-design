/*
 * Copyright © 2023 Yuriy Artamonov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the “Software”), to deal in the Software without
 * restriction, including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.entframework.mybatis.med.test.generate;

import com.github.hykes.codegen.constants.Defaults;
import com.intellij.ide.extensionResources.ExtensionsRootType;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import io.entframework.med.MedPluginId;
import io.entframework.med.model.CodeGroup;
import io.entframework.med.model.CodeTemplate;
import io.entframework.med.model.GeneratorRuntime;
import io.entframework.med.model.RuntimeTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGeneratorTest extends BasePlatformTestCase {

    @Override
    protected String getTestDataPath() {
        return "testData/CodeGenerator";
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Path resourceDirectory = ExtensionsRootType.getInstance().findResourceDirectory(MedPluginId.get(), "", false);
        FileUtil.delete(resourceDirectory);
    }

    protected GeneratorRuntime getGeneratorRuntime() throws Exception {
//        MedJaxbService medJaxbService = MedJaxbService.getInstance();
//        myFixture.configureByFiles("entities.xml");
//        PsiFile psiFile = myFixture.getFile();
//        JaxbMed jaxbMed = medJaxbService.toObject(psiFile.getVirtualFile().getInputStream());
//        JaxbModule jaxbEntityMappings = jaxbMed.getModules().get(0);
//        GeneratorRuntime generatorRuntime = MedDomUtil.convertToRuntime(jaxbEntityMappings, jaxbEntityMappings.getEntities());
//        return generatorRuntime;
        return null;
    }

    protected List<RuntimeTemplate> getTemplates() {
        List<CodeGroup> defaultGroups = Defaults.getDefaultGroups();
        List<CodeTemplate> codeTemplates = new ArrayList<>();
        defaultGroups.forEach(group -> codeTemplates.addAll(group.getTemplates()));
        List<RuntimeTemplate> runtimeTemplates = new ArrayList<>();
        codeTemplates.forEach(codeTemplate -> {
            RuntimeTemplate template = new RuntimeTemplate(codeTemplate);
            template.setDirectory("/Users/jeff_qian/IdeaProjects/mb-g-test/src/main/java");
            template.setContent(loadContent(codeTemplate.getTemplatePath()));
            runtimeTemplates.add(template);
        });
        return runtimeTemplates;
    }

    private String loadContent(String path) {
        try {
            return FileUtil.loadTextAndClose(Defaults.class.getResourceAsStream("/extensions/" + path));
        } catch (IOException e) {
            // ignore
        }
        return null;
    }

}
