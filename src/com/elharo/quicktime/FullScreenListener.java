/*
 * Created on Jun 9, 2005
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.elharo.quicktime;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class FullScreenListener implements ActionListener {

    private PlayerFrame frame;
    
    FullScreenListener(PlayerFrame frame) {
        this.frame = frame;
    }
    
    public void actionPerformed(ActionEvent event) {
        frame.toggleFullScreenMode();
    }

}