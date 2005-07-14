/* Copyright 2005 Elliotte Rusty Harold

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

import java.awt.*;

import javax.swing.*;

import quicktime.QTException;
import quicktime.qd.QDRect;
import quicktime.std.StdQTConstants;
import quicktime.std.StdQTException;
import quicktime.std.movies.Movie;
import quicktime.std.movies.Track;

/** 
 * Although this class extends JFrame, it's called a Dialog
 * It really is a non-modal dialog, but we have to extend 
 * JFrame to get the menu bars right.
 * Bleah.
 * 
 * @author Elliotte Rusty Harold
 *
 */
class AVControlsPalette extends JFrame {

    // XXX make control-w and close work with this dialog
    private JPanel eastPanel = new JPanel();
    private JPanel westPanel = new JPanel();
    
    AVControlsPalette(PlayerFrame frame) {
        
        super("Audio Controls");

        // move to right hand of screen????
        
        this.add(BorderLayout.EAST, eastPanel);
        this.add(BorderLayout.WEST, westPanel);
        
        this.pack();
        this.setResizable(false);
    }


}
