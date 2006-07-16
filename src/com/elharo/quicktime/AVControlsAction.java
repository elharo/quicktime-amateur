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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

class AVControlsAction extends AbstractAction {
    
    
    // XXX implement change from Show A/V controls to Hide A/V controls in all windows'
    // menubars when the A/V controls are shown
    
    // XXX this is flaky because we don't really use separate dialogs for each frame
    private PlayerFrame frame;
    
    AVControlsAction(PlayerFrame frame) {
        this.frame = frame;
        putValue(Action.NAME, "Show A/V Controls");  
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('K', PlayerFrame.menuShortcutKeyMask));  
    } 
    
    
    public void actionPerformed(ActionEvent event) {        
       
        // cache this in the frame????
        JDialog dialog = new AVControlsPalette(frame);
        // use event queue????
        dialog.setVisible(true);
        
    }

}
