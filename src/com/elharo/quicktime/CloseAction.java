/*
 * Created on May 31, 2005
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.elharo.quicktime;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

class CloseAction extends AbstractAction {

    private Frame frame;
    
    CloseAction(PlayerFrame frame) {
        this.frame = frame;
        putValue(Action.NAME, "Close");  
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('W', PlayerFrame.menuShortcutKeyMask));  
        
    }

    public void actionPerformed(ActionEvent event) {
        frame.hide();
        frame.dispose();
        WindowList.INSTANCE.remove(frame);
    }

}
