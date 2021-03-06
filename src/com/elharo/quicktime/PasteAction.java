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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import quicktime.QTException;

class PasteAction extends AbstractAction {

    private PlayerFrame frame;

    PasteAction(PlayerFrame frame) {
        this.frame = frame;
        putValue(Action.NAME, "Paste");
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('V', PlayerFrame.menuShortcutKeyMask));
        if (frame == null)
			this.setEnabled(false);
    }

    public void actionPerformed(ActionEvent event) {
        try {
            frame.undoablePaste();
            frame.pack();
        } catch (QTException qtex) {
			if (qtex.errorCode() == -102) {
				JOptionPane.showMessageDialog(null, "The contents of the clipboard can not be pasted into a movie.", "Error", JOptionPane.ERROR_MESSAGE); 
			} else {
				qtex.printStackTrace();
			}
        }
    }
}
