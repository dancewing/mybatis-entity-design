/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.ui.JBColor;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.PopupHandler;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.util.ui.tree.TreeUtil;
import com.intellij.util.xml.DomElementNavigationProvider;
import com.intellij.util.xml.DomElementsNavigationManager;
import io.entframework.med.dom.DomMed;
import io.entframework.med.dom.MyDomElement;
import io.entframework.med.util.DomSupport;
import io.entframework.med.util.DomTreeNodeUtil;
import io.entframework.med.util.MedFileUtil;
import io.entframework.med.view.action.*;
import io.entframework.med.view.properties.PropertyEvent;
import io.entframework.med.view.properties.PropertyListener;
import io.entframework.med.view.properties.PropertyPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseListener;

public class MybatisDesignerWindowPanel extends SimpleToolWindowPanel implements Disposable {

    private static final Logger logger = Logger.getInstance(MybatisDesignerWindowPanel.class);

    private SimpleTree objectTree;

    private TreePath currentTreePath;

    private PropertyPanel propertyEditor;

    private JBSplitter mySplitter;

    private final Project myProject;

    public MybatisDesignerWindowPanel(@NotNull Project myProject) {
        super(true, true);
        this.myProject = myProject;

        objectTree = new SimpleTree();
        objectTree.setRootVisible(true);
        objectTree.setShowsRootHandles(true);
        objectTree.addTreeWillExpandListener(getTreeWillExpandListener());
        objectTree.addMouseListener(getMouseListener());
        objectTree.setCellRenderer(MedTreeCellRenderer.RENDERER);
        objectTree.addTreeSelectionListener(getTreeSelectionListener());

        DefaultActionGroup actionGroup = new DefaultActionGroup(
                RemoveNodeAction.getInstance(),
                ImportPopupAction.getInstance(),
                CopyAction.getInstance(),
                PasteAction.getInstance(),
                ShowSqlAction.getInstance());
        ActionToolbar toolbar = ActionManager.getInstance()
                .createActionToolbar("MybatisDesignToolView", actionGroup, true);
        toolbar.setTargetComponent(objectTree);
        setToolbar(toolbar.getComponent());

        JScrollPane scrollPane = ScrollPaneFactory.createScrollPane(objectTree, true);

        propertyEditor = new PropertyPanel(myProject, objectTree);

        mySplitter = new JBSplitter(true, 0.5f, 0.15f, 0.85f);
        mySplitter.setSplitterProportionKey("MybatisStructureWindowPanel.SplitterProportionKey");
        mySplitter.setFirstComponent(scrollPane);
        mySplitter.setSecondComponent(propertyEditor);
        mySplitter.setDividerWidth(2);
        mySplitter.getDivider()
                .setBackground(JBColor.lazy(() -> EditorColorsManager.getInstance()
                        .getGlobalScheme()
                        .getColor(EditorColors.PREVIEW_BORDER_COLOR)));

        setContent(mySplitter);

        // 监听UML Diagram中节点选中事件
        myProject.getMessageBus().connect().subscribe(PropertyListener.TOPIC, new PropertyListener() {
            @Override
            public void diagramNodeSelected(PropertyEvent event) {
                MyDomElement baseElement = event.getSource();
                DefaultMutableTreeNode treeNode = TreeUtil.findNodeWithObject((DefaultMutableTreeNode) objectTree.getModel().getRoot(), baseElement);
                if (treeNode != null) {
                    TreeUtil.selectNode(objectTree, treeNode);
                }
            }
        });
    }

    public void init() {
        ApplicationManager.getApplication().invokeLater(this::initData);
    }

    public void reset() {
        objectTree.setModel(null);
        propertyEditor.removeAll();
        updateUI();
    }

    public void setPropertyEditor(JComponent component) {
        propertyEditor.removeAll();
        propertyEditor.add(component);
        propertyEditor.updateUI();
    }

    public void initData() {
        currentTreePath = null;
        propertyEditor.removeAll();

        VirtualFile currentOpened = MedFileUtil.getCurrentOpened(myProject);
        if (currentOpened != null) {
            PsiFile psiFile = PsiManager.getInstance(myProject).findFile(currentOpened);
            if (psiFile instanceof XmlFile xmlFile) {
                DomMed domMed = DomSupport.getMed(xmlFile);
                if (domMed != null) {
                    DefaultMutableTreeNode root = new DefaultMutableTreeNode(domMed);
                    DomTreeNodeUtil.convert(root, domMed);
                    objectTree.setModel(new DefaultTreeModel(root));
                    objectTree.updateUI();
                }
            }
        }
    }

    @Override
    public void dispose() {
        // Disposer.dispose(objectTree);
    }

    public SimpleTree getTree() {
        return objectTree;
    }

    public TreeWillExpandListener getTreeWillExpandListener() {

        return new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
                // NOOP
            }
        };
    }

    public TreeSelectionListener getTreeSelectionListener() {
        return e -> {
            TreePath newLeadSelectionPath = e.getNewLeadSelectionPath();
            if (newLeadSelectionPath != null) {
                if (!newLeadSelectionPath.equals(currentTreePath)) {
                    currentTreePath = newLeadSelectionPath;
                    treeNavigate(currentTreePath);
                    Object lastSelectedPathComponent = currentTreePath.getLastPathComponent();
                    if (lastSelectedPathComponent instanceof DefaultMutableTreeNode treeNode) {
                        if (treeNode.getUserObject() instanceof MyDomElement domElement) {
                            DomElementNavigationProvider navigateProvider = DomElementsNavigationManager.getManager(myProject).getDomElementsNavigateProvider(DomElementsNavigationManager.DEFAULT_PROVIDER_NAME);
                            if (navigateProvider!=null) {
                                navigateProvider.navigate(domElement, false);
                            }
                        }
                    }
                }
            }
        };
    }

    public MouseListener getMouseListener() {
        return new PopupHandler() {
            @Override
            public void invokePopup(Component comp, int x, int y) {
                JTree source = (JTree) comp;
                TreePath path = source.getClosestPathForLocation(x, y);

                // save current tree path for popup action
                currentTreePath = path;
                if (path == null) {
                    return;
                }

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                Object userObject = node.getUserObject();
                DefaultActionGroup popupActionGroup = new DefaultActionGroup();
                if (userObject instanceof MyDomElement baseElement) {
                    popupActionGroup.addAll(NewNodeActionFactory.createAction(baseElement, objectTree, node));

                    if (!(userObject instanceof DomMed)) {
                        popupActionGroup.add(Separator.getInstance());
                        popupActionGroup.add(CopyAction.getInstance());
                        popupActionGroup.add(Separator.getInstance());
                        popupActionGroup.add(RemoveNodeAction.getInstance());
                    }
                }

                if (popupActionGroup.getChildrenCount() > 0) {
                    ActionPopupMenu actionPopupMenu = ActionManager.getInstance()
                            .createActionPopupMenu("Mybatis.MED.Popup", popupActionGroup);
                    actionPopupMenu.setTargetComponent(objectTree);
                    JPopupMenu popupMenu = actionPopupMenu.getComponent();
                    popupMenu.show(source, x, y);
                }

            }
        };
    }

    public void treeNavigate(TreePath treePath) {
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
        Object userObject = treeNode.getUserObject();
        if (userObject instanceof MyDomElement domElement) {
            propertyEditor.treeNodeSelected(domElement);
        }
    }

}
