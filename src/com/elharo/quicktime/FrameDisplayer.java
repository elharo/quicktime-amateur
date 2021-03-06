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

import java.awt.EventQueue;

/**
 * For an explanation of this class see John Zukowski's article at
 * http://java.sun.com/developer/JDCTechTips/2003/tt1208.html
 */
public class FrameDisplayer implements Runnable {

    private final PlayerFrame frame;

    private FrameDisplayer(PlayerFrame frame) {
        this.frame = frame;
    }

    public void run() {
        int xPosition = WindowList.getX();
        int yPosition = WindowList.getY();

        frame.setLocation(xPosition, yPosition);
        WindowList.INSTANCE.add(frame);
        frame.setVisible(true);
    }

    public static void display(PlayerFrame frame) {
        Runnable runner = new FrameDisplayer(frame);
        EventQueue.invokeLater(runner);
    }
}
