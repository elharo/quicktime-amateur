package com.elharo.quicktime;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

class SpecialCharactersAction extends AbstractAction {
    
    
    SpecialCharactersAction() {
        putValue(Action.NAME, "Special Characters...");  
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('T', PlayerFrame.menuShortcutKeyMask));
    }    
    
    
    public void actionPerformed(ActionEvent evt) {

        try {
            Runtime.getRuntime().exec(
              "/System/Library/Components/CharacterPalette.component/Contents/SharedSupport/CharPaletteServer.app/Contents/MacOS/CharPaletteServer");
        } 
        catch (Exception ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }

    }

    
}
