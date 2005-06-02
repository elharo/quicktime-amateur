/*
 * Created on Jun 2, 2005
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.elharo.quicktime;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

class WindowMenuItem extends JMenuItem {

    private PlayerFrame frame;

    WindowMenuItem(PlayerFrame f) {
        super(f.getTitle());
        this.frame = f;
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                 frame.toFront();
            }}
        );
    }

    PlayerFrame getFrame() {
        return frame;
    }

}
