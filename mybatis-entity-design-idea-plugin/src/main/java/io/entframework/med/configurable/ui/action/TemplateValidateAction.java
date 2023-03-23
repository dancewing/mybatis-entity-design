/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.ui.action;

import com.github.hykes.codegen.utils.StringUtils;
import com.github.hykes.codegen.utils.VelocityUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import io.entframework.med.configurable.ui.TemplatesUI;
import io.entframework.med.configurable.ui.editor.TemplateEditorUI;
import io.entframework.med.model.CodeGroup;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;

public class TemplateValidateAction extends BaseTemplateAction {

    private static final Logger LOGGER = Logger.getInstance(TemplateValidateAction.class);

    public TemplateValidateAction(TemplatesUI templatesUI) {
        super(() -> "Validate", AllIcons.Actions.StartDebugger, templatesUI);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        TemplateEditorUI templateEditor = templatesUI.getTemplateEditor();
        if (templateEditor != null) {
            String template = templateEditor.getTemplateContent();
            if (template != null) {
                try {
                    VelocityContext velocityContext = new VelocityContext();
                    velocityContext.put("model", "Test");
                    VelocityUtil.evaluate(velocityContext, template);
                    Messages.showInfoMessage("validate success", "INFO");
                } catch (ResourceNotFoundException re) {
                    Messages.showInfoMessage("couldn't find the template", "ERROR");
                    LOGGER.error(StringUtils.getStackTraceAsString(re));
                } catch (ParseErrorException pe) {
                    Messages.showInfoMessage("syntax error: problem parsing the template", "ERROR");
                    LOGGER.error(StringUtils.getStackTraceAsString(pe));
                } catch (MethodInvocationException me) {
                    Messages.showInfoMessage("something invoked in the template", "ERROR");
                    LOGGER.error(StringUtils.getStackTraceAsString(me));
                } catch (Exception ex) {
                    Messages.showInfoMessage("validate fail", "ERROR");
                    LOGGER.error(StringUtils.getStackTraceAsString(ex));
                }
            }
        } else {
            LOGGER.warn("not found template .");
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
        if (node != null) {
            Object object = node.getUserObject();
            e.getPresentation().setEnabled(!(object instanceof CodeGroup));
        }
    }
}
