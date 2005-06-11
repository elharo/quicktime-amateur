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
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeBase;
import quicktime.std.movies.Movie;

/** Although this class extends JFrame, it's called a Dialog
 * It really is a non-modal dialog, but we have to extend Frame to get the menu bars right.
 * Bleah.
 * 
 * @author Elliotte Rusty Harold
 *
 */
class InfoDialog extends JFrame {

    // XXX make control-w and close work with this dialog
    private JPanel southPanel = new JPanel();
    
    InfoDialog(Movie movie, String title) {
        super("Movie Info");
        this.setJMenuBar(new PlayerMenuBar(null));
        
        this.getContentPane().setLayout(new BorderLayout());
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(new JLabel(title));
        this.getContentPane().add(BorderLayout.NORTH, titlePanel);
        this.getContentPane().add(BorderLayout.CENTER, new JSeparator());
        
        southPanel.setLayout(new GridLayout(10, 2));
        this.getContentPane().add(BorderLayout.SOUTH, southPanel);
        
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(2);
        this.addInfo("Source", "????");

        try {
            movie.getDuration();
            this.addInfo("Format", "????");
        }
        catch (StdQTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }

        try {
            // how to figure out 3000 programmatically????
            String rate = format.format(movie.getTimeScale() / 3000);
            TimeBase base = movie.getTimeBase();
            System.err.println(base.getEffectiveRate());
            System.err.println(base.getRate());
            rate = format.format(movie.getTrack(1).getMedia().getTimeScale());
            this.addInfo("FPS", rate);
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
            movie.getDuration();
            this.addInfo("Playing FPS", "????");
        }
        catch (StdQTException e) {
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
            String time = String.valueOf(movie.getTime() / movie.getTimeScale());
            this.addInfo("Current Time", time);
        }
        catch (StdQTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }

        try {
            movie.getDataSize(1, 1);
            this.addInfo("Data Rate", "????");
        }
        catch (StdQTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }

        try {
            double length = movie.getDuration() / (double) movie.getTimeScale();
            this.addInfo("Duration", format.format(length) + "s");
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
    
    
    
    void addInfo(String name, String value) {
        
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JLabel nameLabel = new JLabel("<html><b>" + name + ": </b></html>");
        namePanel.add(nameLabel);
        southPanel.add(namePanel);
        
        JPanel valuePanel = new JPanel();
        valuePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel valueLabel = new JLabel(value);
        valuePanel.add(valueLabel);
        southPanel.add(valuePanel);
        
    }

}
