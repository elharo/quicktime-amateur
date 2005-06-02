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

class ClearAction extends AbstractAction {

    private Movie movie;

    ClearAction(Movie movie) {
        this.movie = movie;
        putValue(Action.NAME, "Delete");  
    } 
    
    
    public void actionPerformed(ActionEvent event) {
        try {
            movie.clearSelection();
        }
        catch (QTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }
    }

}
