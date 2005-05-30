/*
 * Created on May 28, 2005
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.elharo.quicktime;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import quicktime.QTException;
import quicktime.std.StdQTConstants;
import quicktime.std.movies.Movie;
import quicktime.std.movies.media.DataRef;

class URLOpener implements ActionListener {

    public void actionPerformed(ActionEvent event) {

        String url = JOptionPane.showInputDialog (event.getSource(), "Enter URL");
        try {
            DataRef dr = new DataRef (url);
            Movie m = Movie.fromDataRef (dr, StdQTConstants.newMovieActive);
            PlayerFrame f = new PlayerFrame("Amateur Player", m);
            f.pack();
            f.show();
            m.prePreroll(0, 1.0f);
            m.preroll(0, 1.0f);
            m.start();
        }
        catch (QTException ex) {
            // ???? do better
            ex.printStackTrace();
        }
    }

}
