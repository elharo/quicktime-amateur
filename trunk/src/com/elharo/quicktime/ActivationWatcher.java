package com.elharo.quicktime;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Frame;

import quicktime.std.StdQTException;

/**
 * This class manages the activation and deactivation of windows, 
 * for instance killing sound in inactive windows.
 */
class ActivationWatcher implements WindowListener {
    
    private PlayerFrame frame;
    
    ActivationWatcher(PlayerFrame frame) {
        this.frame = frame;
    }

    private boolean muted = false;
    
    // XXX also need to check focused?
    public void windowActivated(WindowEvent evt) {
        WindowList.INSTANCE.moveToFront(frame);
    }

    public void windowDeactivated(WindowEvent evt) {
        
    }

    public void windowClosed(WindowEvent evt) {

    }


    public void windowClosing(WindowEvent evt) {

    }




    public void windowDeiconified(WindowEvent evt) {

    }


    public void windowIconified(WindowEvent evt) {

    }


    public void windowOpened(WindowEvent evt) {

    }

}
