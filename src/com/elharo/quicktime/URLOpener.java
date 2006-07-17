/* Copyright 2005, 2006 Elliotte Rusty Harold

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

You can contact Elliotte Rusty Harold by sending e-mail to
elharo@metalab.unc.edu. Please include the word "Amateur" in the
subject line. The Amateur home page is located at http://www.elharo.com/amateur/
*/
package com.elharo.quicktime;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import quicktime.QTException;
import quicktime.std.StdQTConstants;
import quicktime.std.movies.Movie;
import quicktime.std.movies.media.DataRef;

class URLOpener extends AbstractAction {
    
    // Test with http://www.vids-c.co.uk/vids/jem/jem002.mov
    
    URLOpener() {
        putValue(Action.NAME, "Open URL...");  
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('U', PlayerFrame.menuShortcutKeyMask));  
    } 
    
    public void actionPerformed(ActionEvent event) {
        Component source = (JMenuItem) event.getSource();
        Container parent = source.getParent();
        String url = JOptionPane.showInputDialog(parent, "Movie URL:", "Open URL", JOptionPane.PLAIN_MESSAGE);
        if (url == null) return; // User cancelled
        openURL(url);
    }

    static void openURL(String url) {

        try {
            DataRef ref = new DataRef(url);
            Movie m = Movie.fromDataRef(ref, StdQTConstants.newMovieActive);
            PlayerFrame f = new PlayerFrame("Amateur Player", m);
            f.pack();
            FrameDisplayer.display(f);
            m.start();
        }
        catch (QTException ex) {
            // ???? do better
            ex.printStackTrace();
        }
    }

}
