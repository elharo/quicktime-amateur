/*
 * Created on Jun 5, 2005
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.elharo.quicktime;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

class ClipboardImage implements Transferable {

    private Image image;
    
    ClipboardImage(Image image) {
        this.image = image;
    }


    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] supports = {DataFlavor.imageFlavor};
        return supports;
    }


    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DataFlavor.imageFlavor.equals(flavor);
    }


    public Object getTransferData(DataFlavor flavor)
      throws UnsupportedFlavorException {
        if (!DataFlavor.imageFlavor.equals(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        return image;
    }

}
