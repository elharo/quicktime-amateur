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
import java.io.File;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import quicktime.QTException;
import quicktime.io.QTFile;
import quicktime.qd.Pict;
import quicktime.std.StdQTConstants;
import quicktime.std.comp.ComponentDescription;
import quicktime.std.comp.ComponentIdentifier;
import quicktime.std.image.GraphicsExporter;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.Movie;
import quicktime.std.movies.TimeInfo;
import quicktime.std.movies.Track;
import quicktime.std.movies.media.DataRef;

public class ExportFramesAction extends AbstractAction {
    
    private static class ExportFormat {

        String name;
        int code;

        ExportFormat(String name, int subType) {
            this.name = name;
            this.code = code;
        }
        
        public String toString() {return this.name; }
        
        String getExtension() {
            return this.name.toLowerCase();
        }

    }

    private PlayerFrame frame;

    ExportFramesAction(PlayerFrame frame) {
        this.frame = frame;
        if (frame == null) this.setEnabled(false);
        putValue(Action.NAME, "Export Frames...");    
    } 
    
    
    public void actionPerformed(ActionEvent event) {
        
        boolean wasPlaying = false;
        try {
            Movie movie = frame.getMovie();
            
            if (movie.getRate() > 0) {
                movie.stop();
                wasPlaying = true;
            }
            // ask user for folder????
            File framesFolder = new File("frames");
            framesFolder.mkdir();

            
            Vector choices = new Vector();
            ComponentDescription description = new ComponentDescription(StdQTConstants.graphicsExporterComponentType);
            for (ComponentIdentifier identifier = ComponentIdentifier.find(null, description); 
                 identifier != null; 
                 identifier = ComponentIdentifier.find(identifier, description)) {
                choices.add(new ExportFormat(identifier.getInfo().getName(), identifier.getInfo().getSubType()));
            }
            
            JComboBox exportCombo = new JComboBox(choices);
            JOptionPane.showMessageDialog(frame, exportCombo, "Choose export format", JOptionPane.PLAIN_MESSAGE);
            ExportFormat format = (ExportFormat) exportCombo.getSelectedItem();
            GraphicsExporter exporter = new GraphicsExporter(format.code);
            /* if CallComponentCanDo ( myGraphicsExporter , kGraphicsExportRequestSettingsSelect ) {
                exporter.requestSettings();
            } */
            
            /* for (int i = 1; i <= movie.getTrackCount(); i++) {
                Track track = movie.getTrack(i);
                System.out.println(track.getClass());
            } */
            
            Track videoTrack = movie.getIndTrackType(1, 
              StdQTConstants.visualMediaCharacteristic, StdQTConstants.movieTrackCharacteristic);
            GraphicsImporter importer = new GraphicsImporter(StdQTConstants.kQTFileTypePicture);
            
            // setup a progress bar????
            int time = 0;
            while (time >= 0) {
                TimeInfo ti = videoTrack.getNextInterestingTime(StdQTConstants.nextTimeMediaSample, time, movie.getRate());
                time = ti.time;
                System.out.println(time);
                Pict oldPict = movie.getPict(time);
                int pictSize = oldPict.getSize();
                byte[] dataAndHeader = new byte[pictSize + PlayerFrame.PICT_HEADER_SIZE];
                oldPict.copyToArray(0, dataAndHeader, PlayerFrame.PICT_HEADER_SIZE, pictSize);
                Pict newPict = new Pict(dataAndHeader);
                DataRef ref = new DataRef(newPict, StdQTConstants.kDataRefQTFileTypeTag, "PICT");
                importer.setDataReference(ref);
                exporter.setInputGraphicsImporter(importer);
                QTFile file = new QTFile(new File(framesFolder, frame.getTitle() + time + "." + format.getExtension()));
                System.out.println(file.getAbsolutePath());
                exporter.setOutputFile(file);
                //importer.exportImageFile(format.code, -1, file, -1);
                exporter.doExport();
            }
            
            if (wasPlaying) movie.start();
        }
        catch (QTException ex) {
            // ???? Auto-generated catch block
            ex.printStackTrace();
        }
    }

}
