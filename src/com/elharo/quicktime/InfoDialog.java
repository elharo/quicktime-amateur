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

import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.*;

import quicktime.QTException;
import quicktime.qd.QDRect;
import quicktime.std.StdQTConstants;
import quicktime.std.StdQTException;
import quicktime.std.movies.Movie;
import quicktime.std.movies.Track;
import quicktime.std.movies.media.HandlerInfo;
import quicktime.std.movies.media.Media;
import quicktime.std.movies.media.SoundDescription;
import quicktime.std.movies.media.SoundMedia;
import quicktime.util.QTUtils;

/** 
 * 
 * @author Elliotte Rusty Harold
 *
 */
class InfoDialog extends JDialog {

    private JPanel centerPanel = new JPanel();
    private StringBuffer html = new StringBuffer("<html> <body> <table>");

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
            Track audioTrack = movie.getIndTrackType(1, 
                    StdQTConstants.audioMediaCharacteristic, StdQTConstants.movieTrackCharacteristic);
            
            // XXX get appledatacompressorsubtype
            // Need to handle case where there's no video media
            String formatString = "";
            if (videoTrack != null) {
                HandlerInfo hi = videoTrack.getMedia().getHandlerDescription(); 
                int code = hi.subType;
                // System.err.println("0x" + Integer.toHexString(code));
                
                // XXX convert code to format string
                formatString = QTUtils.fromOSType(code);
                if (code == 1297106247) formatString = "MPEG1 Muxed";
                else if (code == 1831958048) formatString = "MPEG1 Video";
                else if (code == 1986618469) formatString = "QuickTime";
                
                QDRect size = movie.getNaturalBoundsRect();
                int movieHeight = size.getHeight();
                int movieWidth = size.getWidth();
                formatString += ", " + movieWidth + " x " + movieHeight;            
            }
            
            if (audioTrack != null) {
                // XXX Can this ever be anything else?
                SoundMedia media = (SoundMedia) audioTrack.getMedia();
                SoundDescription description = media.getSoundDescription(1);
                float rate = description.getSampleRate();
                if (videoTrack != null) formatString += ", ";
                formatString += rate/1000 + " kHz";
            }
            
            
            
            /* Media media = videoTrack.getMedia();
            int count = media.getSampleDescriptionCount();
            for (int i = 1; i <= count; i++) {
                System.err.println(i);
                System.err.println(media.getSampleDescription(i));
            } */
            
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
                double units = videoTrack.getMedia().getDuration();
                double frames = videoTrack.getMedia().getSampleCount();
                double unitsPerSecond = videoTrack.getMedia().getTimeScale();
                double expectedRate = unitsPerSecond * frames / units;
                String fps = format.format(expectedRate);
                this.addInfo("FPS", fps);
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

        
        
        // move to right hand of screen????
        this.pack();
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
