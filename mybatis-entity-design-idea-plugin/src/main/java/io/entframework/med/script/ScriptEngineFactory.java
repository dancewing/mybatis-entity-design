/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.script;

public class ScriptEngineFactory {

    private static final ScriptEngineFactory INSTANCE = new ScriptEngineFactory();

    private ScriptEngineFactory() {
    }

    public static ScriptEngineFactory getInstance() {
        return INSTANCE;
    }

    public ScriptEngine getEngine(String extension) {
        if ("vm".equals(extension)) {
            return new VelocityScriptEngine();
        } else if ("ftl".equals(extension)) {
            return new FreemarkerScriptEngine();
        } else if ("groovy".equals(extension)) {
            return new GroovyScriptEngine();
        }
        return null;
    }
}
