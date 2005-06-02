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

import quicktime.QTException;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;

class CopyAction extends AbstractAction {

    private Movie movie;

    CopyAction(Movie movie) {
        this.movie = movie;
        putValue(Action.NAME, "Copy");  
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('C', PlayerFrame.menuShortcutKeyMask));  
    } 
    
    
    public void actionPerformed(ActionEvent arg0) {
        try {
            Movie copy = movie.copySelection();
            copy.putOnScrap(0);
        }
        catch (QTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }
    }

}
