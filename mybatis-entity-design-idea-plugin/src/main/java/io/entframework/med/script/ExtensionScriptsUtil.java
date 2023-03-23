/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.script;

import com.intellij.database.DataGridBundle;
import com.intellij.database.extensions.Binding;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.ide.script.IdeScriptEngine;
import com.intellij.ide.script.IdeScriptEngineManager;
import com.intellij.ide.script.IdeScriptException;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.util.ExceptionUtil;
import io.entframework.med.MedBundle;
import io.entframework.med.idea.NotificationHelper;
import io.entframework.med.model.RuntimeTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ExtensionScriptsUtil {

    private ExtensionScriptsUtil() {
    }

    public static @Nullable IdeScriptEngine getEngineFor(@Nullable Project project, @NotNull RuntimeTemplate template, @Nullable PluginId pluginId,
                                                         boolean showBalloon) {

        IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(pluginId);
        plugin = plugin != null ? plugin : PluginManagerCore.getPlugin(PluginManagerCore.CORE_ID);
        IdeaPluginDescriptor descriptor = Objects.requireNonNull(plugin);
        ClassLoader loader = makeCancellable(descriptor.getPluginClassLoader());
        IdeScriptEngine engine = IdeScriptEngineManager.getInstance()
                .getEngineByFileExtension(template.getLanguage().getExtension(), loader);
        if (engine == null && showBalloon) {
            showEngineNotFoundBalloon(project, template.getLanguage().getExtension());
            return null;
        } else {
            return engine;
        }
    }

    private static ClassLoader makeCancellable(ClassLoader loader) {
        return new ClassLoader("Cancellable Engine Classloader", loader) {
            protected Object getClassLoadingLock(String className) {
                ProgressManager.checkCanceled();
                return super.getClassLoadingLock(className);
            }
        };
    }

    public static void showEngineNotFoundBalloon(final @Nullable Project project, String scriptExtension) {
        String title = DataGridBundle.message("notification.title.no.script.engine.found.for.file.extension",
                new Object[]{scriptExtension});
        String content = MedBundle.message("groovy.engine.not.found", new Object[0]);
        showError(project, title, content);
    }

    public static void showError(@Nullable Project project, @NlsContexts.NotificationTitle @NotNull String title,
                                 @NlsContexts.NotificationContent @NotNull String content) {
        NotificationHelper.getInstance().createNotification(title, content, NotificationType.ERROR).notify(project);
    }

    public static @NotNull Binder setBindings(@NotNull IdeScriptEngine engine) {
        return new Binder(engine);
    }

    public static void prepare() {
        ApplicationManager.getApplication().assertIsDispatchThread();
        FileDocumentManager.getInstance().saveAllDocuments();
    }

    public static Object evalScript(@Nullable Project project, @NotNull IdeScriptEngine engine, @NotNull String script,
                                    boolean showErrorMessage) {
        try {
            return engine.eval(script);
        } catch (IdeScriptException var7) {
            ProgressManager.checkCanceled();
            ProcessCanceledException pce = ExceptionUtil.findCause(var7, ProcessCanceledException.class);
            if (pce != null) {
                throw pce;
            } else {
                if (showErrorMessage) {
                    showScriptExecutionError(project, ExceptionUtil.getRootCause(var7));
                }
            }
        }
        return null;
    }

    public static void showScriptExecutionError(@Nullable Project project, @NotNull Throwable error) {
        NotificationHelper.getInstance()
                .createNotification(ExceptionUtil.getThrowableText(error, "com.intellij."), NotificationType.ERROR)
                .notify(project);
    }

    public static class Binder {

        private final IdeScriptEngine myEngine;

        Binder(@NotNull IdeScriptEngine engine) {
            super();
            this.myEngine = engine;
        }

        @NotNull
        public <T> Binder bind(@NotNull Binding<T> what, @Nullable T to) {
            this.myEngine.setBinding(what.name, to);
            return this;
        }

        public static <T> T get(@NotNull IdeScriptEngine engine, @NotNull Binding<T> what) {
            return (T) engine.getBinding(what.name);
        }

    }

}
