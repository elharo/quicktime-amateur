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

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

import javax.swing.JMenuBar;

import quicktime.QTException;
import quicktime.io.QTFile;

import com.elharo.quicktime.FileOpener;
import com.elharo.quicktime.PlayerFrame;

/**
 * These are some very fragile tests mostly designed as examples for a class
 * I'm working on.
 * 
 * @author Elliotte Rusty Harold
 *
 */
public class RobotTests extends GUITestCase {
    
    private PlayerFrame frame;
    private JMenuBar menubar;
    
    protected void setUp() throws QTException {
        frame = new PlayerFrame();
        menubar = frame.getJMenuBar();
    }

    protected void tearDown() {
        frame.setVisible(false);
        frame.dispose();
    }

 
    public void testMinimizeButton() throws AWTException {
       
        frame.setVisible(true);
        
        Point p = frame.getLocationOnScreen();
        Robot r = new Robot();
        r.setAutoWaitForIdle(true);
        r.mouseMove(p.x+35, p.y+10);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.delay(1000); // needs a couple of seconds for the state to update
        int state = frame.getExtendedState();
        assertEquals(Frame.ICONIFIED, state);
        
    }
    
    public void testOpenPNGFile() throws QTException, AWTException {
        
       QTFile file = new QTFile("data/pureblue.png");
       FileOpener.openFile(file);
       Robot r = new Robot();
       r.delay(5000);
       Color expected = new Color(0, 0, 255);
       Color actual = r.getPixelColor(50, 100);
       assertEquals(expected, actual);
       
    }
    
}
