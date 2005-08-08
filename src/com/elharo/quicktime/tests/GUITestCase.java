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
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import quicktime.QTException;

import com.elharo.quicktime.QuicktimeInit;

import junit.framework.TestCase;

public abstract class GUITestCase extends TestCase {
    
    static {
        try {
            QuicktimeInit.setup();
        }
        catch (QTException ex) {
            throw new RuntimeException(ex);
        }   
    }

    protected static Dialog findDialogByTitle(String title) {
        
        Frame[] allFrames = Frame.getFrames();
        for (int i = 0; i < allFrames.length; i++) {
            Window[] owned = allFrames[i].getOwnedWindows();
            for (int j = 0; j < owned.length; j++) {
                Dialog dialog = (Dialog) owned[j];
                if (title.equals(dialog.getTitle())) {
                    return dialog;
                }
            }
        }
        
        return null;
    }

    public JMenuItem findJMenuItem(JMenuBar menubar, String name) {
        
        for (int menu = 0; menu < menubar.getMenuCount(); menu++) {
            Component[] menuitems = menubar.getMenu(menu).getMenuComponents();
            for (int i = 0; i < menuitems.length; i++) {
                if (menuitems[i] instanceof JMenuItem) {
                    JMenuItem item = (JMenuItem) menuitems[i];
                    if (item.getText().equals(name)) {
                        return item;
                    }
                }
            }
        }
        return null;
        
    }    
}
