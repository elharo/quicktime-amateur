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
import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import quicktime.QTException;
import quicktime.io.*;
import quicktime.std.*;
import quicktime.std.movies.Movie;

class SaveAsAction extends AbstractAction {

    private Movie movie;
    
    SaveAsAction(Movie movie) {
        this.movie = movie;
        if (movie == null) this.setEnabled(false);
        putValue(Action.NAME, "Save As...");  
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('S', PlayerFrame.menuShortcutKeyMask | InputEvent.SHIFT_MASK));  
    } 
    
    
    public void actionPerformed(ActionEvent event) {
        
        // ???? need to read window title from window and use that as default name
        QTFile file = new QTFile(new File("testsave.mov"));
        int flags = 
          StdQTConstants.createMovieFileDontCreateResFile |
          StdQTConstants.createMovieFileDeleteCurFile |
          StdQTConstants.showUserSettingsDialog;
        System.err.println(file);
        try {
            movie.setProgressProc();
            // does this next line return a file? It doesn't seem to change the file argument????
            movie.convertToFile(file, 
              StdQTConstants.kQTFileTypeMovie, 
              StdQTConstants.kMoviePlayer, // ???? change this to Amateur
              IOConstants.smSystemScript, 
              flags);
            // ???? why does this lose sound when converting an MPEG?
            JMenuItem source = (JMenuItem) event.getSource();
            PlayerFrame frame = (PlayerFrame) source.getTopLevelAncestor();
        System.err.println(file);
         byte[] data =   file.getFSSpec(true, QTFile.kReadPermission);
         System.out.println(new String(data, "MacRoman"));
            //frame.setFile(????);
        }
        catch (StdQTException e) {
            if (e.errorCode() == -128) {
                // user cancelled
                return;
            }
            // ???? Auto-generated catch block
            e.printStackTrace();
          }
          catch (QTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
          }
        catch (UnsupportedEncodingException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
