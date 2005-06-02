/*
 * Created on Jun 2, 2005
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.elharo.quicktime;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import quicktime.std.StdQTException;
import quicktime.std.movies.MovieController;

class UndoAction extends AbstractAction {

    private MovieController controller;

    UndoAction(MovieController controller) {
        this.controller = controller;
        putValue(Action.NAME, "Undo");  
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('Z', PlayerFrame.menuShortcutKeyMask));  
    } 
    
    
    public void actionPerformed(ActionEvent event) {
        try {
            controller.undo();
        }
        catch (StdQTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }
    }

}
