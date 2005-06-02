/*
 * Created on Jun 2, 2005
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

import quicktime.QTException;
import quicktime.std.movies.MovieController;

class PasteAction extends AbstractAction {

    private MovieController controller;
    private Frame frame;

    PasteAction(MovieController controller, Frame container) {
        this.controller = controller;
        this.frame = container;
        putValue(Action.NAME, "Paste");  
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('V', PlayerFrame.menuShortcutKeyMask));  
    } 
    
    
    public void actionPerformed(ActionEvent event) {
        try {
            controller.paste();
            frame.pack();
        }
        catch (QTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }
    }

}
