/*
 * Created on May 28, 2005
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.elharo.quicktime;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import quicktime.QTException;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.io.QTIOException;
import quicktime.std.movies.Movie;

public class FileOpener implements ActionListener {

    private static final int USER_CANCELLED = -128;

    public void actionPerformed(ActionEvent evt) {
        
        try {
            QTFile file = QTFile.standardGetFilePreview(QTFile.kStandardQTFileTypes);
            OpenMovieFile omFile = OpenMovieFile.asRead(file);
            Movie m = Movie.fromFile(omFile);
            PlayerFrame f = new PlayerFrame(file.getName(), m);
            WindowList.INSTANCE.add(f);
            f.show();
        }
        catch (QTIOException ex) {
           if (ex.errorCode() == USER_CANCELLED) return;
           ex.printStackTrace();
        }
        catch (QTException ex) {
           // ???? do better
           ex.printStackTrace();
        }
    }

}
