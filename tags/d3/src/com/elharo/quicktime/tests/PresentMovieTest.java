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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;

import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import quicktime.QTException;

import com.elharo.quicktime.FrameDisplayer;
import com.elharo.quicktime.PlayerFrame;
import com.elharo.quicktime.QuicktimeInit;

public class PresentMovieTest extends GUITestCase {
    
    private PlayerFrame frame;
    private JMenuBar menubar;
    
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
        menubar = frame.getJMenuBar();
    }

    protected void tearDown() {
        frame.setVisible(false);
        frame.dispose();
    }
    
    
    public void testPresentMovieDialog() {
        
        JMenuItem presentMovie = findJMenuItem(menubar, "Present Movie...");
        presentMovie.doClick();
        
        Dialog dialog = findDialogByTitle("Present Movie");
        assertNotNull("No Present Movie dialog", dialog);
        
        assertTrue(dialog.isShowing());
        
        JButton cancel = findButtonByLabel(dialog, "Cancel");
        assertNotNull("No cancel button", cancel);
        
        cancel.doClick();
        assertFalse(dialog.isShowing());
    }

    private JButton findButtonByLabel(Container container, String label) {

        Component[] components = container.getComponents();
        for (int i = 0; i < components.length; i++) {
            Component component = components[i];
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (label.equals(button.getText())) return button;
            }
            else if (component instanceof Container) {
                JButton button = findButtonByLabel((Container) component, label);
                if (button != null) return button;
            }
        }
        return null;
    }

    
}
