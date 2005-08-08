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
package com.elharo.quicktime.tests;

import quicktime.QTException;
import junit.framework.TestCase;

import java.awt.*;

import javax.swing.JMenuItem;

import com.elharo.quicktime.FrameDisplayer;
import com.elharo.quicktime.PlayerFrame;
import com.elharo.quicktime.QuicktimeInit;

public class AVDialogTest extends GUITestCase {

    private Container dialog;
    private PlayerFrame frame;
    
    static {
        try {
            QuicktimeInit.setup();
        }
        catch (QTException ex) {
            throw new RuntimeException(ex);
        }   
    }
    
    protected void setUp() throws QTException {
        frame = new PlayerFrame();
        FrameDisplayer.display(frame);
        
        JMenuItem presentMovie = findJMenuItem(frame.getJMenuBar(), "Show A/V Controls");
        presentMovie.doClick();
        
        dialog = findDialogByTitle("Audio Controls");
    }
    
    protected void tearDown() {
        frame.setVisible(false);
        frame.dispose();
    }
    
    public void testListComponents() {   
        listComponents(dialog);
    }

    private static void listComponents(Container container) {
        Component[] components = container.getComponents();
        for (int i = 0; i < components.length; i++) {
            System.out.println(components[i]);
            if (components[i] instanceof Container) {
                listComponents((Container) components[i]);
            }
        }
        
    }
    
}
