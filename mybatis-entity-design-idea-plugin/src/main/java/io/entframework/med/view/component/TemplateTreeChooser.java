/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.component;

import com.intellij.icons.AllIcons;
import com.intellij.ide.extensionResources.ExtensionsRootType;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.treeStructure.SimpleTree;
import io.entframework.med.MedPluginId;
import io.entframework.med.view.ProjectDialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TemplateTreeChooser extends ProjectDialogWrapper {
    private JScrollPane contentPanel;
    private SimpleTree fileTree;

    public TemplateTreeChooser(@Nullable Project project) {
        super(project);

        fileTree = new SimpleTree();
        fileTree.putClientProperty("JTree.lineStyle", "Horizontal");
        fileTree.setRootVisible(false);
        fileTree.setShowsRootHandles(true);
        fileTree.setPreferredSize(new Dimension(100, -1));
        fileTree.setMaximumSize(new Dimension(100, -1));
        fileTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        fileTree.setCellRenderer(new TemplateTreeCellRenderer());

        Path rootPath = getRootPath();
        if (rootPath != null) {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode();
            getTreeNode(rootPath, root);
            fileTree.setModel(new DefaultTreeModel(root));
        }

        contentPanel = ScrollPaneFactory.createScrollPane(fileTree, true);

        setSize(100, 200);
        init();
    }

    private void getTreeNode(Path directory, DefaultMutableTreeNode parent) {
        List<Path> paths = getTemplates(directory);
        Path root = getRootPath();
        if (root == null) {
            return;
        }
        for (Path path : paths) {
            TemplatePath templatePath;
            String p = StringUtil.substringAfter(path.toString(), root.toString());
            if (Files.isDirectory(path)) {
                templatePath = new TemplatePath(p, path.getFileName().toString(), true);
            } else {
                templatePath = new TemplatePath(p, path.getFileName().toString(), false);
            }
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(templatePath);
            if (Files.isDirectory(path)) {
                child.setAllowsChildren(true);
                getTreeNode(path, child);
            } else {
                child.setAllowsChildren(false);
            }
            parent.add(child);
        }
    }

    @Override
    protected @NotNull List<ValidationInfo> doValidateAll() {
        List<ValidationInfo> validationInfos = new ArrayList<>();
        if (fileTree.getLastSelectedPathComponent() instanceof DefaultMutableTreeNode treeNode) {
            if (treeNode.getUserObject() instanceof TemplatePath templatePath) {
                if (templatePath.isDirectory()) {
                    validationInfos.add(new ValidationInfo("Please choose a template"));
                }
            }
        }
        return validationInfos;
    }

    public String getTemplatePath() {
        if (fileTree.getLastSelectedPathComponent() instanceof DefaultMutableTreeNode treeNode) {
            if (treeNode.getUserObject() instanceof TemplatePath templatePath) {
                if (!templatePath.isDirectory()) {
                    return templatePath.getPath();
                }
            }
        }
        return null;
    }

    private Path getRootPath() {
        try {
            return ExtensionsRootType.getInstance().findResourceDirectory(MedPluginId.get(), "", true);
        } catch (IOException e) {
            return null;
        }
    }

    private static @NotNull List<Path> getTemplates(Path directory) {
        try {
            try (Stream<Path> stream = Files.list(directory)) {
                return stream
                        .filter(regularFileFilter())
                        .sorted((f1, f2) -> {
                            String f1Name = f1 == null ? null : f1.getFileName().toString();
                            String f2Name = f2 == null ? null : f2.getFileName().toString();
                            return StringUtil.compare(f1Name, f2Name, false);
                        })
                        .collect(Collectors.toList());
            }
        } catch (IOException ignore) {
        }
        return Collections.emptyList();
    }

    public static @NotNull Predicate<Path> regularFileFilter() {
        return file -> {
            try {
                if (Files.isHidden(file)) {
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
            return true;
        };
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return contentPanel;
    }


    private static class TemplatePath {
        private String path;
        private String name;
        private boolean directory;

        public TemplatePath(String path, String name) {
            this.path = path;
            this.name = name;
        }

        public TemplatePath(String path, String name, boolean directory) {
            this.path = path;
            this.name = name;
            this.directory = directory;
        }

        public String getPath() {
            return path;
        }

        public String getName() {
            return name;
        }

        public boolean isDirectory() {
            return directory;
        }
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
            if (obj instanceof TemplatePath templatePath) {
                DefaultTreeCellRenderer tempCellRenderer = new DefaultTreeCellRenderer();
                if (templatePath.isDirectory()) {
                    tempCellRenderer.setOpenIcon(AllIcons.Nodes.Folder);
                    tempCellRenderer.setClosedIcon(AllIcons.Nodes.Folder);
                    tempCellRenderer.setLeafIcon(AllIcons.Nodes.Folder);
                } else {
                    String fileType = StringUtil.substringAfter(templatePath.getName(), ".").toLowerCase();
                    FileType stdFileType = FileTypeManager.getInstance().getFileTypeByExtension(fileType);
                    tempCellRenderer.setOpenIcon(stdFileType.getIcon());
                    tempCellRenderer.setClosedIcon(stdFileType.getIcon());
                    tempCellRenderer.setLeafIcon(stdFileType.getIcon());
                    tempCellRenderer.setIcon(stdFileType.getIcon());
                }

                return tempCellRenderer.getTreeCellRendererComponent(tree, templatePath.getName(), selected, expanded, false,
                        row, hasFocus);
            }
            return null;
        }

    }
}
