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

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import quicktime.QTException;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.io.QTIOException;
import quicktime.std.movies.Movie;

public class FileOpener implements ActionListener {

    private static final int USER_CANCELLED = -128;

    public void actionPerformed(ActionEvent event) {
        
        String title = "The movie in the file";
        try {
            QTFile file = QTFile.standardGetFilePreview(QTFile.kStandardQTFileTypes);
            title = file.getName();
            OpenMovieFile omFile = OpenMovieFile.asRead(file);
            Movie m = Movie.fromFile(omFile);
            PlayerFrame f = new PlayerFrame(file.getName(), m);
            Runnable runner = new FrameDisplayer(f);
            EventQueue.invokeLater(runner);
        }
        catch (QTIOException ex) {
           if (ex.errorCode() == USER_CANCELLED) return;
           ex.printStackTrace();
        }
        catch (QTException ex) {
            int code = ex.errorCode();
            String errorMessage = ex.errorCodeToString();
            if (code == -2048) {
                errorMessage = title + " is not encoded in a format QuickTime understands.";
            }
            Component component = (Component) event.getSource();
            Container parent = component.getParent();
            JOptionPane.showMessageDialog(parent, errorMessage);
        }
    }

}
