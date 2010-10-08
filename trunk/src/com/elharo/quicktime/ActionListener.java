package com.elharo.quicktime;

import quicktime.std.*;
import quicktime.std.clocks.*;
import quicktime.std.movies.*;

class ActionListener extends ActionFilter {

    private PlayerFrame frame;

    ActionListener (PlayerFrame frame) {
        this.frame = frame;
    }
/*
	@Override
    public boolean execute (MovieController controller, int action) {
        if (action == StdQTConstants.mcActionControllerSizeChanged) {
			System.out.println("controller size changed");
// This causes a hang.
//			frame.updateInfoContent();
		}
        return super.execute(controller, action);
    }
*/
	@Override
    public boolean execute (MovieController controller, int action, TimeRecord tr) {
        if (action == StdQTConstants.mcActionSetSelectionDuration) {
            PlayerMenuBar mb = (PlayerMenuBar) (frame.getJMenuBar());
            if (tr.getValue() == 0)
				mb.deselection();
            else
				mb.selection();
        } else if (action == StdQTConstants.mcActionGoToTime) {
			// happens if the user clicks on the controller, or uses the left arrow/right arrow keys
			frame.updateInfoContent();
		}
        return super.execute(controller, action, tr);
    }
}
