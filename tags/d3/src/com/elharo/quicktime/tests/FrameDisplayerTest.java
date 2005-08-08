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

import java.awt.Point;

import junit.framework.TestCase;
import quicktime.QTException;

import com.elharo.quicktime.FrameDisplayer;
import com.elharo.quicktime.PlayerFrame;
import com.elharo.quicktime.QuicktimeInit;

public class FrameDisplayerTest extends TestCase {
    
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
    }

    protected void tearDown() {
        frame.dispose();
    }

    public void testOffset() throws QTException, InterruptedException {
        FrameDisplayer.display(frame);
        PlayerFrame frame2 = new PlayerFrame();
        FrameDisplayer.display(frame2);
        PlayerFrame frame3 = new PlayerFrame();
        FrameDisplayer.display(frame3);
        
        // wait for frames to display
        while (! frame3.isVisible() ) Thread.sleep(100);
        
        Point location1 = frame.getLocation();
        Point location2 = frame2.getLocation();
        Point location3 = frame3.getLocation();
        assertTrue(location1.x < location2.x);
        assertTrue(location2.x < location3.x);
        assertTrue(location1.y < location2.y);
        assertTrue(location2.y < location3.y);
        frame2.dispose();
        frame3.dispose();
    }

    
}
