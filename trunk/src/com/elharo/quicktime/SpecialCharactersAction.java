package com.elharo.quicktime;

import java.awt.event.ActionEvent;
import java.io.*;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

class SpecialCharactersAction extends AbstractAction {
    
    private static String command = 
      "/System/Library/Components/CharacterPalette.component/Contents/SharedSupport/CharPaletteServer.app/Contents/MacOS/CharPaletteServer";
    
    
    SpecialCharactersAction() {
        putValue(Action.NAME, "Special Characters...");  
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('T', PlayerFrame.menuShortcutKeyMask));
    }    
    
    
    public void actionPerformed(ActionEvent evt) {

        try {
            Process process = Runtime.getRuntime().exec("ps -awwx");
            InputStream in = new BufferedInputStream(process.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String s = null;
            while ((s = reader.readLine()) != null) {
                if (s.indexOf(command) != -1) return;
            }
            Runtime.getRuntime().exec(command);
        } 
        catch (Exception ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }

    }

    
}
