/*
 * Created on May 31, 2005
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.elharo.quicktime;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import quicktime.QTException;
import quicktime.app.players.QTPlayer;

class NewPlayerAction extends AbstractAction {
    
    private static int untitledCount = 1;

    NewPlayerAction() {
        putValue(Action.NAME, "New Player");  
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('N', PlayerFrame.menuShortcutKeyMask));  
    }
    
    public void actionPerformed(ActionEvent event) {
        try {
            PlayerFrame f = new PlayerFrame("Untitled " + untitledCount++);
            WindowList.INSTANCE.add(f);
            f.show();
            
        }
        catch (QTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }
    }

}