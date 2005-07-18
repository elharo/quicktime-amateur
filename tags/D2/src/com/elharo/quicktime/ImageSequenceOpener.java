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
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.*;

import javax.swing.*;

import quicktime.*;
import quicktime.io.QTFile;
import quicktime.io.QTIOException;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.*;
import quicktime.std.image.CSequence;
import quicktime.std.image.CodecComponent;
import quicktime.std.image.CompressedFrameInfo;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.std.image.QTImage;
import quicktime.std.movies.*;
import quicktime.std.movies.media.VideoMedia;
import quicktime.util.QTHandle;
import quicktime.util.QTUtils;
import quicktime.util.RawEncodedImage;

class ImageSequenceOpener extends AbstractAction {
    
    private static int[] types = new int[4];
    
    static {
        types[0] = QTUtils.toOSType("JPEG");
        types[1] = QTUtils.toOSType("GIFf");
        types[2] = QTUtils.toOSType("PICT");
        types[3] = QTUtils.toOSType("PNGf");
    }
    
    ImageSequenceOpener() {
        putValue(Action.NAME, "Open Image Sequence...");
        putValue(
          Action.ACCELERATOR_KEY, 
          KeyStroke.getKeyStroke('O', PlayerFrame.menuShortcutKeyMask | InputEvent.SHIFT_MASK)
        );  
    } 
    
    public void actionPerformed(ActionEvent event) {

        try {
            QTFile firstImage = QTFile.standardGetFilePreview(types);
            
            File[] chosen = firstImage.getParentFile().listFiles(ImageFileFilter.INSTANCE);
            int delay = getDelay();

            // XXX should I delete the temp file later?
            Movie movie = Movie.createMovieFile(
              new QTFile(File.createTempFile(firstImage.getName(), "mov")),
              StdQTConstants.kMoviePlayer, // XXX change this to Amateur code
              StdQTConstants.createMovieFileDeleteCurFile |
              StdQTConstants.createMovieFileDontCreateResFile);
            
            GraphicsImporter importer = new GraphicsImporter(firstImage);
            
            int trackWidth  = importer.getBoundsRect().getWidth();
            int trackHeight = importer.getBoundsRect().getHeight();
            int volume = 0;
            QDRect bounds = new QDRect(0, 0, trackWidth, trackHeight);
            
            Track videoTrack = movie.addTrack(trackWidth, trackHeight, volume);
            int unitsPerSecond = 30; // timeScale
            QDGraphics offscreen = new QDGraphics(bounds);
            importer.setGWorld(offscreen, null);
            VideoMedia videoMedia = new VideoMedia(videoTrack, unitsPerSecond);            
            videoMedia.beginEdits();
             
            int sorensonCODEC = QTUtils.toOSType("SVQ3");
            int rawImageSize = QTImage.getMaxCompressionSize(offscreen, bounds, 
              offscreen.getPixMap().getPixelSize(), StdQTConstants.codecHighQuality, 
              sorensonCODEC, CodecComponent.bestFidelityCodec);
            QTHandle imageHandle = new QTHandle(rawImageSize, true);
            imageHandle.lock(); // XXX Do I ever need to unlock this?
            RawEncodedImage rawImage = RawEncodedImage.fromQTHandle(imageHandle);
             
            CSequence seq = new CSequence (offscreen, bounds, offscreen.getPixMap().getPixelSize( ),
              sorensonCODEC, CodecComponent.bestFidelityCodec,
              StdQTConstants.codecHighQuality, 0, // no temporal compression 
              0, null, StdQTConstants.codecFlagUpdatePrevious);
             
            ImageDescription imgDesc = seq.getDescription();

            ProgressMonitor monitor = new ProgressMonitor(
              (Component) event.getSource(), "Building movie", "test", 0, chosen.length-1);

            Matrix matrix = new Matrix();
            for (int i=0; i < chosen.length; i++) { 
                QTFile imageFile = new QTFile(chosen[i]);
                monitor.setNote("Processing " + imageFile.getName());
                
                // If a file does not match the original size we need to 
                // resize it
                importer.setDataFile(imageFile);
                QDRect newBounds = importer.getBoundsRect();
                matrix.map(newBounds, bounds);
                importer.setMatrix(matrix);
                importer.setDestRect(bounds);
                importer.draw();
                CompressedFrameInfo cfInfo = seq.compressFrame(offscreen, bounds, 
                  StdQTConstants.codecFlagUpdatePrevious, rawImage);
                boolean syncSample = cfInfo.getSimilarity() == 0;
                int flags = syncSample ? 0 : StdQTConstants.mediaSampleNotSync;
                videoMedia.addSample(imageHandle, 0, cfInfo.getDataSize(), delay,
                  imgDesc, 1, flags);
                monitor.setProgress(i);
            }
                   
            videoMedia.endEdits();
            videoTrack.insertMedia(0,  0,  videoMedia.getDuration(),  1); 
            
            PlayerFrame f = new PlayerFrame(movie);
            f.show();
            // start playing????
        }
        catch (QTIOException ex) {
            if (ex.errorCode() == -128) {
                // User cancelled
            }
            else ex.printStackTrace();
        }
        catch (QTException ex) {
            // ???? Auto-generated catch block
            ex.printStackTrace();
        }
        catch (IOException ex) {
            // ???? Could not create temp file
            ex.printStackTrace();
        }
        
    }

    private int getDelay() {
        // ???? replace with dialog asking user for number of seconds; 
        // convert to current time scale
        return 90;
    } 

}
