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
import quicktime.util.QTUtils;

/** 
 * 
 * @author Elliotte Rusty Harold
 *
 */
class InfoDialog extends JDialog {

    private JPanel eastPanel = new JPanel();
    private JPanel westPanel = new JPanel();

    InfoDialog(PlayerFrame frame) {
        
        super(frame, "Movie Info");
        
        this.getContentPane().setLayout(new BorderLayout());
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        String title = frame.getTitle();
        titlePanel.add(new JLabel(title));
        this.getContentPane().add(BorderLayout.NORTH, titlePanel);
        this.getContentPane().add(BorderLayout.CENTER, new JSeparator());
        
        eastPanel.setLayout(new GridLayout(10, 1));
        westPanel.setLayout(new GridLayout(10, 1));
        this.getContentPane().add(BorderLayout.EAST, eastPanel);
        this.getContentPane().add(BorderLayout.WEST, westPanel);
        
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(2);
        // can be null????
        try {
            this.addInfo("Source", "<html><body><p>" + frame.getFile().getPath() + "</p></body></html>");
        }
        catch (NullPointerException ex) {
            
        }
        Movie movie = frame.getMovie();
        try {
            Track videoTrack = movie.getIndTrackType(1, 
                    StdQTConstants.visualMediaCharacteristic, StdQTConstants.movieTrackCharacteristic);
            HandlerInfo hi = videoTrack.getMedia().getHandlerDescription(); 
            int code = hi.subType;
            // XXX convert code to format string
            String formatString = QTUtils.fromOSType(code);
            this.addInfo("Format", formatString);
            // XXX see CodecName and CodecInfo classes
            // can we get one of these from a movie?
        }
        catch (QTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }

        try {
            Track videoTrack = movie.getIndTrackType(1, 
              StdQTConstants.visualMediaCharacteristic, StdQTConstants.movieTrackCharacteristic);
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
    
    private final static Font labelFont = new Font("Lucida Grande", Font.PLAIN, 11);
    private final static Font nameFont = new Font("Lucida Grande", Font.BOLD, 11);
    
    void addInfo(String name, String value) {
        
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JLabel nameLabel = new JLabel(name + ": ");
        nameLabel.setFont(nameFont);
        namePanel.add(nameLabel);
        westPanel.add(namePanel);
        
        JPanel valuePanel = new JPanel();
        valuePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(labelFont);
        valuePanel.add(valueLabel);
        eastPanel.add(valuePanel);
        
    }

}
