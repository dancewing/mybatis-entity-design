/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.configurable.ui;

import com.github.hykes.codegen.provider.DefaultProviderImpl;
import com.github.hykes.codegen.provider.FileProviderFactory;
import com.intellij.icons.AllIcons;
import com.intellij.ide.extensionResources.ExtensionsRootType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.options.ConfigurableUi;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.ui.JBColor;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.util.ui.JBUI;
import io.entframework.med.MedPluginId;
import io.entframework.med.configurable.TemplateEvent;
import io.entframework.med.configurable.TemplateListener;
import io.entframework.med.configurable.model.Templates;
import io.entframework.med.configurable.ui.action.*;
import io.entframework.med.configurable.ui.editor.TemplateEditorUI;
import io.entframework.med.idea.NotificationHelper;
import io.entframework.med.model.CodeGroup;
import io.entframework.med.model.CodeTemplate;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class TemplatesUI extends JBPanel implements ConfigurableUi<Templates>, Disposable {

    private static final Logger LOGGER = Logger.getInstance(DefaultProviderImpl.class);
    private Tree templateTree;
    private ToolbarDecorator toolbarDecorator;
    private TemplateEditorUI templateEditor;
    private JBSplitter mySplitter;
    @NotNull
    private final Project myProject;

    public TemplatesUI(@NotNull Project project) {
        this.myProject = project;
        init();
        MessageBusConnection connect = myProject.getMessageBus().connect(this);
        connect.subscribe(TemplateListener.TOPIC, new TemplateListener() {
            @Override
            public void templateChanged(TemplateEvent event) {
                CodeTemplate codeTemplate = event.getTemplate();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
                if (node != null) {
                    if (node.getUserObject() instanceof CodeTemplate template) {
                        copyValueFromUI(template, codeTemplate);
                        templateTree.updateUI();
                    }
                }
            }
        });
    }

    @Override
    public boolean isModified(@NotNull Templates templates) {
        DefaultMutableTreeNode topNode = (DefaultMutableTreeNode) templateTree.getModel().getRoot();
        // 获取映射map
        Map<String, List<CodeTemplate>> templateMap = templates.getTemplatesMap();

        // root的判断, 数量判断, name?
        List<CodeGroup> groups = templates.getGroups();
        if (topNode.getChildCount() != groups.size()) {
            return true;
        }

        Enumeration<TreeNode> enumeration = topNode.children();
        while (enumeration.hasMoreElements()) {
            // 模板判断, 数量判断
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
            CodeGroup group = (CodeGroup) node.getUserObject();
            if (node.getChildCount() != templateMap.get(group.getId()).size()) {
                return true;
            }

            Enumeration<TreeNode> childEnum = node.children();
            while (childEnum.hasMoreElements()) {
                // 模板内容判断
                DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) childEnum.nextElement();
                CodeTemplate template = (CodeTemplate) childNode.getUserObject();
                CodeTemplate tmp = templates.getTemplate(template.getId(), group.getId());
                if (tmp != null && template.getId().equals(tmp.getId()) && !template.equals(tmp)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void apply(@NotNull Templates settings) throws ConfigurationException {
        DefaultTreeModel treeModel = (DefaultTreeModel) templateTree.getModel();
        DefaultMutableTreeNode topNode = (DefaultMutableTreeNode) treeModel.getRoot();

        List<CodeGroup> groups = new ArrayList<>();
        Enumeration<TreeNode> enumeration = topNode.children();
        // 获取所有组
        while (enumeration.hasMoreElements()) {
            List<CodeTemplate> templates = new ArrayList<>();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
            Enumeration<TreeNode> childEnum = node.children();
            // 获取所有模板
            while (childEnum.hasMoreElements()) {
                DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) childEnum.nextElement();
                CodeTemplate template = (CodeTemplate) childNode.getUserObject();
                templates.add(template);

            }
            CodeGroup group = (CodeGroup) node.getUserObject();
            group.setTemplates(templates);
            groups.add(group);
        }
        settings.setGroups(groups);
    }

    @Override
    public void reset(@NotNull Templates settings) {
        setTemplates(settings);
    }

    private void init() {
        setLayout(new BorderLayout());

        // 新建文件树
        templateTree = new Tree();
        templateTree.putClientProperty("JTree.lineStyle", "Horizontal");
        templateTree.setRootVisible(true);
        templateTree.setShowsRootHandles(true);
        templateTree.setPreferredSize(new Dimension(100, -1));
        templateTree.setMaximumSize(new Dimension(100, -1));
        templateTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        templateTree.setCellRenderer(new TemplateTreeCellRenderer());
        // 文件数节点选择事件
        templateTree.addTreeSelectionListener(it -> {
            // cache last editor content
            if (it.getOldLeadSelectionPath() != null) {
                Object lastPathComponent = it.getOldLeadSelectionPath().getLastPathComponent();
                if (lastPathComponent instanceof DefaultMutableTreeNode lastNode) {
                    Object lastObject = lastNode.getUserObject();
                    if (lastObject instanceof CodeTemplate template) {
                        //get current code template
                        CodeTemplate codeTemplate = templateEditor.getCodeTemplate();
                        copyValueFromUI(template, codeTemplate);
                    }
                }
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
            if (node == null) {
                return;
            }

            Object object = node.getUserObject();
            if (object instanceof CodeTemplate template) {
                templateEditor.getRootPanel().setVisible(true);
                templateEditor.refresh(template);
            } else {
                templateEditor.getRootPanel().setVisible(false);
            }
        });
        // 工具栏
        toolbarDecorator = ToolbarDecorator.createDecorator(templateTree)
                .setAddAction(new TemplateAddAction(this))
                .setRemoveAction(new TemplateRemoveAction(this))
                .setEditAction(new TemplateEditAction(this))
                .addExtraAction(new TemplateImportAction(this))
                .addExtraAction(new TemplateExportAction(this))
                .addExtraAction(new TemplateValidateAction(this))
                .setEditActionUpdater(it -> {
                    // 只能允许CodeRoot, CodeGroup在树中编辑
                    final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) templateTree
                            .getLastSelectedPathComponent();
                    return selectedNode != null && (selectedNode.getUserObject() instanceof CodeGroup);
                })
                .setAddActionUpdater(it -> {
                    final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) templateTree
                            .getLastSelectedPathComponent();
                    // return selectedNode != null;
                    return true;
                })
                .setRemoveActionUpdater(it -> {
                    // 允许CodeRoot、CodeGroup、CodeTemplate删除
                    final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) templateTree
                            .getLastSelectedPathComponent();
                    return selectedNode != null && selectedNode.getParent() != null;
                });
        JPanel templatesPanel = toolbarDecorator.createPanel();
        templatesPanel.setPreferredSize(JBUI.size(240, 100));

        templateEditor = new TemplateEditorUI(myProject);

        mySplitter = new JBSplitter(false, 0.5f, 0.15f, 0.85f);
        mySplitter.setSplitterProportionKey("MybatisTemplatesPanel.SplitterProportionKey");
        mySplitter.setFirstComponent(templatesPanel);
        mySplitter.setSecondComponent(templateEditor.getRootPanel());
        mySplitter.setDividerWidth(2);
        mySplitter.getDivider()
                .setBackground(JBColor.lazy(() -> EditorColorsManager.getInstance()
                        .getGlobalScheme()
                        .getColor(EditorColors.PREVIEW_BORDER_COLOR)));

        add(mySplitter, BorderLayout.CENTER);
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton reloadExtensionButton = new JButton("Reload default templates");
        reloadExtensionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ExtensionsRootType.getInstance().extractBundledResources(MedPluginId.get(), "");
                    Path directory = ExtensionsRootType.getInstance().findResourceDirectory(MedPluginId.get(), "", false);
                    VfsUtil.findFile(directory, true);
                } catch (IOException ex) {
                    NotificationHelper.getInstance().notifyException(ex, myProject);
                }
            }
        });
        topPanel.add(reloadExtensionButton, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * 将模板以tree的方式展开
     */
    private void setTemplates(Templates templates) {
        // 获取roots
        List<CodeGroup> groups = templates.getGroups();
        if (groups == null) {
            return;
        }
        // 获取组和模板, 转换成tree
        DefaultMutableTreeNode tree = new DefaultMutableTreeNode();
        groups.forEach(group -> {
            DefaultMutableTreeNode treeGroup = new DefaultMutableTreeNode(group);
            group.getTemplates().forEach(template -> {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(template);
                treeGroup.add(node);
            });
            tree.add(treeGroup);
        });
        templateTree.setModel(new DefaultTreeModel(tree, false));
        templateTree.setRootVisible(false);
    }

    @Override
    public @NotNull JComponent getComponent() {
        return this;
    }

    public static class TemplateTreeCellRenderer extends DefaultTreeCellRenderer {

        private static final long serialVersionUID = -6564861041507376828L;

        /**
         * 重写父类DefaultTreeCellRenderer的方法
         */
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                      boolean isLeaf, int row, boolean hasFocus) {
            // 选中
            if (selected) {
                setForeground(getTextSelectionColor());
            } else {
                setForeground(getTextNonSelectionColor());
            }
            // TreeNode
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
            Object obj = treeNode.getUserObject();
            if (obj instanceof CodeGroup group) {
                DefaultTreeCellRenderer tempCellRenderer = new DefaultTreeCellRenderer();
                tempCellRenderer.setOpenIcon(AllIcons.Nodes.Folder);
                tempCellRenderer.setClosedIcon(AllIcons.Nodes.Folder);
                tempCellRenderer.setLeafIcon(AllIcons.Nodes.Folder);
                return tempCellRenderer.getTreeCellRendererComponent(tree, group.getName(), selected, expanded, false,
                        row, hasFocus);
            } else if (obj instanceof CodeTemplate node) {
                DefaultTreeCellRenderer tempCellRenderer = new DefaultTreeCellRenderer();
                FileType fileType = FileProviderFactory.getFileType(node.getExtension());
                tempCellRenderer.setOpenIcon(fileType.getIcon());
                tempCellRenderer.setClosedIcon(fileType.getIcon());
                tempCellRenderer.setLeafIcon(fileType.getIcon());
                return tempCellRenderer.getTreeCellRendererComponent(tree, node.getDisplay(), selected, expanded, true,
                        row, hasFocus);
            } else {
                String text = (String) obj;
                DefaultTreeCellRenderer tempCellRenderer = new DefaultTreeCellRenderer();
                return tempCellRenderer.getTreeCellRendererComponent(tree, text, selected, expanded, false, row,
                        hasFocus);
            }
        }

    }

    @Override
    public void dispose() {

    }

    private void copyValueFromUI(CodeTemplate target, CodeTemplate source) {
        target.setDisplay(source.getDisplay());
        target.setExtension(source.getExtension());
        target.setFileName(source.getFileName());
        target.setPackage(source.getPackage());
        target.setGroup(source.getGroup());
        target.setLevel(source.getLevel());
        target.setLanguage(source.getLanguage());
    }

    public Tree getTemplateTree() {
        return templateTree;
    }

    public TemplateEditorUI getTemplateEditor() {
        return templateEditor;
    }

    public Project getProject() {
        return myProject;
    }

}
