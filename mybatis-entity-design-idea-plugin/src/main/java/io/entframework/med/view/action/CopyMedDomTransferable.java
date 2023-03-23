/*******************************************************************************
 * Copyright (c) 2023. Licensed under the ApacheLicense,Version2.0.
 ******************************************************************************/

package io.entframework.med.view.action;

import com.intellij.util.ArrayUtilRt;
import io.entframework.med.dom.MyDomElement;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

public class CopyMedDomTransferable implements Transferable {

    private List<MyDomElement> elements;

    public CopyMedDomTransferable(List<MyDomElement> elements) {
        this.elements = elements;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{CopyAction.ourFlavor};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return ArrayUtilRt.find(getTransferDataFlavors(), flavor) != -1;
    }

    @Override
    @NotNull
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor)) {
            return elements;
        }
        throw new UnsupportedFlavorException(flavor);
    }
}
