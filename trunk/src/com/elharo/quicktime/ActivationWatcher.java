package com.elharo.quicktime;

import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
    
    public void windowActivated(WindowEvent evt) {
        try {
            if (muted) {
                frame.unmute();
                muted = false;
            }
        }
        catch (StdQTException e) {
            // couldn't unmute
        }
    }

    public void windowDeactivated(WindowEvent evt) {
        
        try {
            if (!muted && ! frame.isFullScreen() 
                && Preferences.getInstance().getBooleanValue(Preferences.PLAY_SOUND_IN_FRONTMOST_PLAYER_ONLY)) {
                frame.mute();
                muted = true;
            }
        }
        catch (StdQTException e) {
            
        }
        
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
