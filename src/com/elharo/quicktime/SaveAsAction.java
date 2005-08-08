/* Copyright 2005 Elliotte Rusty Harold

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

You can contact Elliotte Rusty Harold by sending e-mail to
elharo@metalab.unc.edu. Please include the word "Amateur" in the
subject line. The Amateur home page is located at http://www.elharo.com/amateur/
*/
package com.elharo.quicktime;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

import javax.swing.*;

import quicktime.QTException;
import quicktime.io.*;
import quicktime.std.*;
import quicktime.std.movies.Movie;

class SaveAsAction extends AbstractAction {

    private PlayerFrame frame;
    
    SaveAsAction(PlayerFrame frame) {
        this.frame = frame;
        if (frame == null) this.setEnabled(false);
        putValue(Action.NAME, "Save As...");  
        putValue(Action.ACCELERATOR_KEY, 
                 KeyStroke.getKeyStroke('S', PlayerFrame.menuShortcutKeyMask | InputEvent.SHIFT_MASK));  
    } 
    
    
    public void actionPerformed(ActionEvent event) {      
        saveFrameAs(frame);
    }


    static void saveFrameAs(PlayerFrame frame) {

        // XXX need to provide means to choose output format
        // and various other QuickTimeSettings
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(frame.getFile());
        int chosen = chooser.showSaveDialog(frame);
        if (chosen != JFileChooser.APPROVE_OPTION) {
            return;
        }
        QTFile file = new QTFile(chooser.getSelectedFile());
        saveMovieIntoFile(frame, file);
    }


    static void saveMovieIntoFile(PlayerFrame frame, QTFile file) {

        Movie movie = frame.getMovie();
        // XXX is null?
        
        int flags = 
          StdQTConstants.createMovieFileDontCreateResFile |
          StdQTConstants.createMovieFileDeleteCurFile;
        try {
            movie.setProgressProc();
            movie.convertToFile(file, 
              StdQTConstants.kQTFileTypeMovie, // adjust according to type????
              AmateurConstants.CREATOR_CODE,
              IOConstants.smSystemScript, 
              flags);
            // ???? why does this lose sound when converting an MPEG?
            frame.setFile(file);
        }
        catch (StdQTException ex) {
            if (ex.errorCode() == -128) {
                // user cancelled
                return;
            }
            // ???? Auto-generated catch block
            ex.printStackTrace();
        }
        catch (QTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
