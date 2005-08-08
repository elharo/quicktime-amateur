
package com.elharo.quicktime;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;

public class RecentFileAction extends AbstractAction {
    
    public RecentFileAction(File f) {
        putValue(Action.NAME, f.getName());  
    }

    public void actionPerformed(ActionEvent evt) {
    }

}
