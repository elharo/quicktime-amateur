/*
 * Created on Jun 1, 2005
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.elharo.quicktime;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

class BringAllToFrontAction extends AbstractAction {

    BringAllToFrontAction() {
        putValue(Action.NAME, "Bring All To Front");  
    }
    
    
    public void actionPerformed(ActionEvent event) {
        Iterator windows = WindowList.INSTANCE.iterator();
        while (windows.hasNext()) {
            Window w = (Window) windows.next();
            w.toFront();
        }
    }

}
