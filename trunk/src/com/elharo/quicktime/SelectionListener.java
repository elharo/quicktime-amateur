package com.elharo.quicktime;

import quicktime.std.*;
import quicktime.std.clocks.*;
import quicktime.std.movies.*;

class SelectionListener extends ActionFilter {
    
    private PlayerFrame frame;
    
    SelectionListener(PlayerFrame frame) {
        this.frame = frame;
    }

    public boolean execute(MovieController controller, int action, TimeRecord tr) {
        
        if (action == StdQTConstants.mcActionSetSelectionDuration) {
            PlayerMenuBar mb = (PlayerMenuBar) (frame.getJMenuBar());
            if (tr.getValue() == 0) mb.deselection();
            else mb.selection();
        }
        return super.execute(controller, action, tr);
        
    }
   
}