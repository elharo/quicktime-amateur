/* Copyright 2005, 2006 Elliotte Rusty Harold

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

import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.*;

import quicktime.QTException;
import quicktime.qd.QDRect;
import quicktime.std.StdQTConstants;
import quicktime.std.StdQTException;
import quicktime.std.movies.Movie;
import quicktime.std.movies.TimeInfo;
import quicktime.std.movies.Track;
import quicktime.std.movies.media.HandlerInfo;
import quicktime.std.movies.media.MPEGMedia;
import quicktime.std.movies.media.Media;
import quicktime.std.movies.media.SoundDescription;
import quicktime.std.movies.media.SoundMedia;
import quicktime.std.movies.media.VideoMedia;

/** 
 * 
 * @author Elliotte Rusty Harold
 *
 */
class InfoDialog extends JDialog {

    // XXX needs to autoupdate as windows move in and out of focus
    private JPanel centerPanel = new JPanel();
    private StringBuffer html = new StringBuffer("<html> <body> <table>");
    
    // XXX make suire these are onscreen
    private int initialLeft = 485;
    private int initialTop  = 210;

    InfoDialog(PlayerFrame frame) {
        
        super(frame, "Movie Info");
        
        this.getContentPane().setLayout(new BorderLayout());
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        String title = frame.getTitle();
        JLabel titleLabel = new JLabel(title);
        titlePanel.add(titleLabel);
        
        int width = 350;
        if (titleLabel.getWidth() > 300) width = 50 + titleLabel.getWidth();
        // XXX use this width; see
        // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4348815
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));
        topPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        topPanel.add(titlePanel);
        topPanel.add(BorderLayout.CENTER, new JSeparator());
        this.getContentPane().add(BorderLayout.NORTH, topPanel);
        
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
        
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(2);
        // format.setMinimumFractionDigits(2);
        // can be null????
        try {
            this.addInfo("Source", frame.getFile().getPath());
        }
        catch (NullPointerException ex) {
            
        }
        Movie movie = frame.getMovie();
        Track videoTrack = null;
        try {
            videoTrack = movie.getIndTrackType(1, 
                    StdQTConstants.visualMediaCharacteristic, StdQTConstants.movieTrackCharacteristic);
            
            // XXX Put all this in a for loop that adds one line for each audio and
            // video media per track.
            String formatString = "";
            if (videoTrack != null) {
                Media media = videoTrack.getMedia(); // can be a GenericMedia or a VideoMedia
                HandlerInfo hi = media.getHandlerDescription(); 
                int code = hi.subType;
                
                if (code == 1297106247) formatString = "MPEG1 Muxed";
                else if (code == 1831958048) formatString = "MPEG1 Video";
                else if (code == 1986618469) {
                    int dataFormat = media.getSampleDescription(1).getDataFormat();
                    formatString += VideoFormat.getShortDescription(dataFormat);
                }
                
                QDRect size = movie.getNaturalBoundsRect();
                int movieHeight = size.getHeight();
                int movieWidth = size.getWidth();
                formatString += ", " + movieWidth + " x " + movieHeight;  
                
                if (media instanceof VideoMedia) {
                    VideoMedia vMedia = (VideoMedia) media;
                    int depth = vMedia.getImageDescription(1).getDepth();
                    if (depth > 16) formatString += ", Millions";
                    else if (depth > 8) formatString += ", Thousands";
                    else /*XXX what to put here? check the sample movie for LOC*/ ;
                }
                
            }
            
            Track audioTrack = movie.getIndTrackType(1, 
              StdQTConstants.audioMediaCharacteristic, StdQTConstants.movieTrackCharacteristic);
            if (audioTrack != null) {
                // Can this ever be anything other than a SoundMedia? 
                // Yes, it can be an MPEGMedia
                Media media = audioTrack.getMedia();
                if (media instanceof SoundMedia) {
                    SoundDescription description = ((SoundMedia) media).getSoundDescription(1);
                    
                    // XXX should I just separate video and sound formats into two completely
                    // separate items?
                    if (videoTrack != null) formatString += ",<br>";
                    
                    int formatID = description.getDataFormat();
                    formatString += SoundFormat.getShortDescription(formatID) + ", ";
                    
                    int numChannels = description.getNumberOfChannels();
                    if (numChannels <= 1) formatString += "Mono, ";
                    else formatString += "Stereo, ";
                    
                    float rate = description.getSampleRate();
                    formatString += rate/1000 + " kHz";
                }
            }
            
            this.addInfo("Format", formatString);
            // XXX see CodecName and CodecInfo classes
            // can we get one of these from a movie?
        }
        catch (QTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }

        try {
            if (videoTrack != null) {
                double expectedRate = 0.0;
                Media media = videoTrack.getMedia();
                if (media instanceof MPEGMedia) {
                    
                    // XXX This isn't perfectly accurate. It's off by a little
                    // less than a frame a second
                    // XXX may not work while movie is playing? ight even hang?
                    long frameCount = 0;
                    TimeInfo timeInfo = new TimeInfo(0, 0);
                    int timeScale = media.getTimeScale();
                    // only count first few seconds
                    double sampleLength = 12; // seconds
                    // XXX can probably use technique this to find exportable frames
                    while ((timeInfo.time >= 0) && (timeInfo.time / timeScale) < sampleLength) {
                        timeInfo = movie.getNextInterestingTime(StdQTConstants.nextTimeStep, null, timeInfo.time, 1.0f);
                        frameCount++;
                    }
                    frameCount--;
                    expectedRate = frameCount / sampleLength;
                    String fps = format.format(expectedRate);
                    this.addInfo("FPS", fps);
                }
                else {
                    // for some reason this fails on the Serenity international movie trailer
                    double units = media.getDuration();
                    double frames = media.getSampleCount();
                    double unitsPerSecond = videoTrack.getMedia().getTimeScale();
                    expectedRate = unitsPerSecond * frames / units;
                    String fps = format.format(expectedRate);
                    this.addInfo("FPS", fps);
                }
                // doesn't really work for MPEGS????
                String playingFPS = "????";
                double rate = movie.getRate();
                if (rate == 0.0) playingFPS = "(Available while playing.)";
                else {
                    playingFPS = format.format(rate * expectedRate);
                }
                this.addInfo("Playing FPS", playingFPS);
            }
        }
        catch (StdQTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }
        catch (QTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }

        try {
            int dataSize = movie.getDataSize(1, movie.getDuration());
            double mb = dataSize/(1024.0*1024.0);
            // format to two decimal places????
            String mbs = format.format(mb) + " MB";
            this.addInfo("Data Size", mbs);
        }
        catch (StdQTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }

        try {
            int duration = movie.getDuration();
            double dataSizeInKiloBits = 8 * movie.getDataSize(1, duration) / 1024;
            String dataRate = format.format(movie.getTimeScale() * dataSizeInKiloBits / duration) + " kbits/sec";
            this.addInfo("Data Rate", dataRate);
        }
        catch (StdQTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }

        try {
            double length = movie.getTime() / (double) movie.getTimeScale();
            String time = formatTime(length);
            this.addInfo("Current Time", time);
        }
        catch (StdQTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }

        try {
            double length = movie.getDuration() / (double) movie.getTimeScale();
            String time = formatTime(length);
            this.addInfo("Duration", time);
        }
        catch (StdQTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }

        
        if (videoTrack != null) {
            try {
                QDRect size = movie.getNaturalBoundsRect();
                int movieHeight = size.getHeight();
                int movieWidth = size.getWidth();
                this.addInfo("Normal Size", movieWidth + " x " + movieHeight + " pixels");
            }
            catch (StdQTException e) {
                // ???? Auto-generated catch block
                e.printStackTrace();
            }
            
            try {
                QDRect size = movie.getBox();
                int movieHeight = size.getHeight();
                int movieWidth = size.getWidth();
                this.addInfo("Current Size", movieWidth + " x " + movieHeight + " pixels");
            }
            catch (StdQTException e) {
                // ???? Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        html.append("</table> </body> </html>");
        JLabel info = new JLabel(html.toString());
        info.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
        this.getContentPane().add(BorderLayout.CENTER, info);

        this.pack();
        this.setLocation(initialLeft, initialTop);
        // XXX remember location between starts????
        
        this.setResizable(false);
    }


    private String formatTime(double length) {
        
        int hours = (int) (length / 3600);
        int minutes = (int) (length / 60) - hours*60;
        int seconds = (int) Math.floor(length % 60);
        double fraction = length - Math.floor(length);
        
        String h = Integer.toString(hours);
        if (hours < 10) h = "0" + h;
        String m = Integer.toString(minutes);
        if (minutes < 10) m = "0" + m;
        
        String s = Integer.toString(seconds);
        if (seconds < 10) s = "0" + s;
      
        String f = "00";
        if (fraction != 0) f = String.valueOf(fraction).substring(2, 4);
        
        String time = h + ":" + m + ":" + s + "." + f;
        return time;
        
    }
    
    void addInfo(String name, String value) {
        html.append("<tr><td valign='top' align='right'><b>" + name + "</b>" + ":</td>");
        html.append("<td>" + value + "</td></tr>");
    }

}
