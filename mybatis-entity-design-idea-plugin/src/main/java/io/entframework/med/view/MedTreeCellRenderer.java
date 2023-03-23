/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view;

import com.intellij.ui.ColoredTreeCellRenderer;
import io.entframework.med.dom.DescribableDomElement;
import io.entframework.med.dom.DomMed;
import io.entframework.med.dom.MyDomElement;
import io.entframework.med.util.DomSupport;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class MedTreeCellRenderer extends ColoredTreeCellRenderer {

    public static final MedTreeCellRenderer RENDERER = new MedTreeCellRenderer();

    @Override
    public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected, boolean expanded,
                                      boolean leaf, int row, boolean hasFocus) {
        if (value instanceof DefaultMutableTreeNode treeNode) {
            Object userObject = treeNode.getUserObject();

            if (userObject instanceof MyDomElement domElement) {
                setIcon(domElement.getIcon());
            }

            if (userObject instanceof DomMed) {
                append("root");
            } else if (userObject instanceof DescribableDomElement describableDomElement) {
                append(DomSupport.getValue(describableDomElement.getName(), ""));
            }

            if (userObject instanceof String str) {
                append(str);
            }
        }
    }

}
