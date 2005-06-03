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

import quicktime.QTException;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;

public class TrimToSelectionAction extends AbstractAction {
    
    private MovieController controller;

    TrimToSelectionAction(MovieController controller) {
        this.controller = controller;
        putValue(Action.NAME, "Trim To Selection");    
    } 
    
    
    public void actionPerformed(ActionEvent event) {
        try {
            Movie original = controller.getMovie();
            Movie selection = original.copySelection();
            
            // ???? what if there is no selection?
            original.setSelection(0, original.getDuration());
            original.pasteSelection(selection);
            
            // should the final result be unselected?
            original.setSelection(0, 0);
            controller.movieEdited();
        }
        catch (QTException ex) {
            // ???? Auto-generated catch block
            ex.printStackTrace();
        }
    }

}
