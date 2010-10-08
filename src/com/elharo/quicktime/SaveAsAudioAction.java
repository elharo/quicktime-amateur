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

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;

import quicktime.QTException;
import quicktime.io.*;
import quicktime.std.*;
import quicktime.std.movies.Movie;

class SaveAsAudioAction extends AbstractAction {

	public static final int AIFF = 		1;
	public static final int WAV = 		2;

    private PlayerFrame frame;
	private int format;

    SaveAsAudioAction (PlayerFrame frame, int format) {
        this.frame = frame;
        this.format = format;
        if (frame == null)
			this.setEnabled(false);
		if (format == AIFF)
			putValue(Action.NAME, "Save As AIFF...");
		else if (format == WAV)
			putValue(Action.NAME, "Save As WAV...");
		else
			throw new RuntimeException("SaveAsAudioAction: unsupported filr format: "+format);
    }

    public void actionPerformed (ActionEvent event) {
        // TODO: need to provide means to choose output format
        // and various other QuickTimeSettings

		File input = null;
		String currentDir = Preferences.getStringValue(Preferences.CURRENT_DIRECTORY);

		if (Preferences.getBooleanValue(Preferences.USE_AWT_FILE_DIALOG)) {
			FileDialog fd = new FileDialog(new Frame(), "Choose file to save to", FileDialog.SAVE);
			if (currentDir == null)
				fd.setDirectory(System.getProperty("user.dir"));
			else
				fd.setDirectory(currentDir);
			fd.setVisible(true);
			if (fd.getFile() == null)
				return;
			currentDir = fd.getDirectory();
			input = new File(currentDir, fd.getFile());
		} else {
			JFileChooser fd;
			if (currentDir == null)
				fd = new JFileChooser(System.getProperty("user.dir"));
			else
				fd = new JFileChooser(currentDir);
			fd.setDialogTitle("Choose file to save to");
			if (fd.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
				return;
			currentDir = fd.getCurrentDirectory().getAbsolutePath();
			input = fd.getSelectedFile();
		}

		Preferences.setValue(Preferences.CURRENT_DIRECTORY, currentDir);

        QTFile file = new QTFile(input);
		if (format == AIFF)
			SaveAsAction.saveMovieIntoFile(frame, file, StdQTConstants.kQTFileTypeAIFF);
		else if (format == WAV)
			SaveAsAction.saveMovieIntoFile(frame, file, StdQTConstants.kQTFileTypeWave);
	}
}
