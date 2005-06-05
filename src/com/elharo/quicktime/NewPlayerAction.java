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
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import quicktime.QTException;

class NewPlayerAction extends AbstractAction {
    
    private static int untitledCount = 1;

    NewPlayerAction() {
        putValue(Action.NAME, "New Player");  
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('N', PlayerFrame.menuShortcutKeyMask));  
    }
    
    public void actionPerformed(ActionEvent event) {
        try {
            PlayerFrame f = new PlayerFrame("Untitled " + untitledCount++);
            Runnable runner = new FrameDisplayer(f);
            EventQueue.invokeLater(runner);
            
        }
        catch (QTException ex) {
            // ???? Auto-generated catch block
            ex.printStackTrace();
        }
    }

}
