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

import java.awt.Frame;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class WindowList {

    private List windows = new ArrayList();
    // for keeping track of where to put windows
    private static int total = 0;
    // ???? There should be a way to get these values programmatically
    private static final int OFFSET = 23;
    private static final int MENU_BAR_HEIGHT = 23;
    private static int nextX = 0;
    private static int xStart = 0; // or just keep a column count????
    private static int nextY = MENU_BAR_HEIGHT;
    
    
    void add(PlayerFrame frame) {
        windows.add(frame);
        total++;
        nextX += OFFSET;
        nextY += OFFSET;
        int screenHeight = frame.getToolkit().getScreenSize().height;
        if (screenHeight < nextY + frame.getSize().height) {
            xStart += OFFSET;
            nextY = MENU_BAR_HEIGHT + xStart;
            nextX = xStart;
        }
        
    }
    
    static int getX() {
        return nextX;
    }
    
    static int getY() {
        return nextY;
    }
    
    private WindowList() {}
    
    static WindowList INSTANCE = new WindowList();
    
    
    Iterator iterator() {
        return windows.iterator();
    }

    void remove(Frame frame) {
        windows.remove(frame);
        if (windows.size() == 0) {
            total = 0;
            nextX = 0;
            nextY = MENU_BAR_HEIGHT;;
        }
    }
    
    static int getTotal() {
        return total;
    }
    
}
