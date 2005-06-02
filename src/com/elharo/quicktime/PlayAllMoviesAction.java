/*
 * Created on Jun 2, 2005
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.elharo.quicktime;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import quicktime.std.StdQTException;

public class PlayAllMoviesAction extends AbstractAction {

    public PlayAllMoviesAction() {
        putValue(Action.NAME, "Play All Movies");  
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, PlayerFrame.menuShortcutKeyMask));  
    }


    public void actionPerformed(ActionEvent arg0) {
        Iterator iterator = WindowList.INSTANCE.iterator();
        while (iterator.hasNext()) {
            PlayerFrame frame = (PlayerFrame) iterator.next();
            try {
                frame.play();
            }
            catch (StdQTException e) {
                // ???? Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
