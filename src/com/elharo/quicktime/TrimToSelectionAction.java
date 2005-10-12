/*
 * Created on Jun 3, 2005
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.elharo.quicktime;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.elharo.quicktime.PlayerFrame.MovieEdit;

import quicktime.QTException;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;
import quicktime.std.movies.MovieEditState;

public class TrimToSelectionAction extends AbstractAction {
    
    private MovieController controller;
    private PlayerFrame frame;

    TrimToSelectionAction(PlayerFrame frame) {
        this.frame = frame;
        if (frame == null) this.setEnabled(false);
        else this.controller = frame.getController();
        putValue(Action.NAME, "Trim To Selection");
    } 
    
    
    public void actionPerformed(ActionEvent event) {
        try {
            Movie original = controller.getMovie();
            MovieEditState oldState = original.newEditState();
            Movie selection = original.copySelection();
            
            // ???? what if there is no selection?
            original.setSelection(0, original.getDuration());
            original.pasteSelection(selection);
            
            // should the final result be unselected?
            original.setSelection(0, 0);
            MovieEditState newState = original.newEditState();
            MovieEdit edit = frame.new MovieEdit(oldState, newState, "Trim To Selection");
            frame.addEdit(edit);
            controller.movieEdited();
        }
        catch (QTException ex) {
            // ???? Auto-generated catch block
            ex.printStackTrace();
        }
    }

}
