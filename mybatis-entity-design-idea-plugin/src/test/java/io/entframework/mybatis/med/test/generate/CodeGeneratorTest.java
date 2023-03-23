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

import com.intellij.openapi.project.Project;
import io.entframework.med.model.GeneratorRuntime;
import io.entframework.med.model.RuntimeTemplate;
import io.entframework.med.service.FileWriteService;
import io.entframework.med.service.TemplateProcessService;

import java.util.List;

public class CodeGeneratorTest extends AbstractGeneratorTest {

    public void testGenerator() throws Exception {
        Project project = getProject();
        GeneratorRuntime runtime = getGeneratorRuntime();
        List<RuntimeTemplate> templates = getTemplates();
        TemplateProcessService templateProcessService = project.getService(TemplateProcessService.class);
        List<RuntimeTemplate> results = templateProcessService.process(runtime, templates);
        FileWriteService fileWriteService = project.getService(FileWriteService.class);
        results.forEach(fileWriteService::write);
    }

}
