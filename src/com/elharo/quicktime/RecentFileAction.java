
package com.elharo.quicktime;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;

import quicktime.QTException;
import quicktime.io.QTFile;

public class RecentFileAction extends AbstractAction {
    
    private QTFile recentFile;
    
    public RecentFileAction(File f) {
        recentFile = new QTFile(f);
        putValue(Action.NAME, f.getName());  
    }

    public void actionPerformed(ActionEvent evt) {
        try {
            FileOpener.openFile(recentFile);
        }
        catch (QTException ex) {
            // ???? Auto-generated catch block
            ex.printStackTrace();
        }
    }

}
