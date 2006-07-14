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
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import quicktime.QTException;
import quicktime.std.StdQTException;

/** 
 * 
 * @author Elliotte Rusty Harold
 *
 */
final class AVControlsPalette extends JDialog {

    // XXX make control-w and close work with this dialog
    // requires showing menubar; perhaps we can even hack this
    // by using this as the null window and just hiding it off the screen
    
    // need to disable controls when there's no front window
    
    private JPanel eastPanel = new JPanel();
    private JPanel westPanel = new JPanel();
    private JPanel southPanel = new JPanel();
    
    // XXX move initial location to center of screen
    
    // this method is vastly too long. It needs some serious refactoring; 
    // probably at least one method or class
    AVControlsPalette(Frame parent) {
        
        super(parent);
        
        this.setTitle("A/V Controls");
        // this.setAlwaysOnTop(true); only in 1.5 and this only works in *all* apps
        // we need an always on top just when player is in front
        
        Font labelFont = new Font("Helvetica", Font.BOLD, 11);
        Font sliderLabelFont = new Font("Helvetica", Font.PLAIN, 11);
        
        Color bgColor = new Color(225, 225, 225);
        
        this.getContentPane().setLayout(new BorderLayout());
        
        // XXX need to align labels with left edges of sliders
        // XXX need to make two vertical panels same size
        // XXX need to set initial values to current movie's volume
        // XXX need to watch for window changes; MVC
        
        JPanel audioControls = new JPanel();
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        audioControls.setBorder(BorderFactory.createTitledBorder(
          border, "Audio", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
        audioControls.setBackground(bgColor);
        audioControls.setLayout(new GridLayout(10, 1));
        
        int min = 0;
        int max = 10;
        
        JLabel volumeLabel = new JLabel("Volume");
        volumeLabel.setFont(labelFont);
        audioControls.add(volumeLabel);
        final JSlider volume = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        volume.setPaintTicks( true );
        volume.setMajorTickSpacing( 5 );
        volume.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent evt) {
                float value = volume.getValue() / 10.0F;
                PlayerFrame front = WindowList.INSTANCE.getFrontmostWindow();
                if (front != null) {
                    try {
                        front.setVolume(value);
                    }
                    catch (StdQTException e) {
                        // ???? Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            
        });
        audioControls.add(volume);

        JLabel balanceLabel = new JLabel("Balance");
        balanceLabel.setFont(labelFont);
        audioControls.add(balanceLabel);
        final JSlider balance = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        balance.setPaintTicks( true );
        balance.setMajorTickSpacing( 5 );
        
        // An L label on left and R label on right
        Dictionary balanceLabels = new Hashtable();
        JLabel left = new JLabel("L");
        left.setFont(sliderLabelFont);
        balanceLabels.put(new Integer(min), left);
        JLabel right = new JLabel("R");
        right.setFont(sliderLabelFont);
        balanceLabels.put(new Integer(max), right);
        balance.setLabelTable(balanceLabels);
        balance.setPaintLabels(true);
        balance.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent evt) {
                float value = (balance.getValue() - 5)/ 10.0F;
                PlayerFrame front = WindowList.INSTANCE.getFrontmostWindow();
                if (front != null) {
                    try {
                        front.setBalance(value);
                    }
                    catch (QTException e) {
                        // ???? Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            
        });      
        audioControls.add(balance);
        
        JLabel bassLabel = new JLabel("Bass");
        bassLabel.setFont(labelFont);
        audioControls.add(bassLabel);
        final JSlider bass = new JSlider(JSlider.HORIZONTAL, -256, 256, 0 );
        bass.setPaintTicks( true );
        bass.setMajorTickSpacing( 256 );
        bass.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent evt) {
                PlayerFrame front = WindowList.INSTANCE.getFrontmostWindow();
                if (front != null) {
                    try {
                        front.setBass(bass.getValue());
                    }
                    catch (QTException e) {
                        // ???? Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            
        });           audioControls.add(bass);
        
        JLabel trebleLabel = new JLabel("Treble");
        trebleLabel.setFont(labelFont);
        audioControls.add(trebleLabel);
        final JSlider treble = new JSlider(JSlider.HORIZONTAL, -256, 256, 0 );
        treble.setPaintTicks( true );
        treble.setMajorTickSpacing( 256 );
        treble.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent evt) {
                PlayerFrame front = WindowList.INSTANCE.getFrontmostWindow();
                if (front != null) {
                    try {
                        front.setTreble(treble.getValue());
                    }
                    catch (QTException e) {
                        // ???? Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            
        });   
        audioControls.add(treble);
        
        JLabel pitchShiftLabel = new JLabel("Pitch Shift");
        pitchShiftLabel.setFont(labelFont);
        audioControls.add(pitchShiftLabel);
        JSlider pitchShift = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        pitchShift.setPaintTicks( true );
        pitchShift.setMajorTickSpacing( 1 );
        audioControls.add(pitchShift);
        
        westPanel.add(BorderLayout.SOUTH, audioControls);  
        
        JPanel videoControls = new JPanel();
        videoControls.setBorder(BorderFactory.createTitledBorder(border, "Video", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
        videoControls.setBackground(bgColor);
        videoControls.setLayout(new GridLayout(10, 1));

        JLabel brightnessLabel = new JLabel("Brightness");
        brightnessLabel.setFont(labelFont);
        videoControls.add(brightnessLabel);
        JSlider brightness = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        brightness.setPaintTicks( true );
        brightness.setMajorTickSpacing( 5 );
        videoControls.add(brightness);

        JLabel colorLabel = new JLabel("Color");
        colorLabel.setFont(labelFont);
        videoControls.add(colorLabel);
        JSlider color = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        color.setPaintTicks( true );
        color.setMajorTickSpacing( 5 );
        videoControls.add(color);

        JLabel contrastLabel = new JLabel("Contrast");
        contrastLabel.setFont(labelFont);
        videoControls.add(contrastLabel);
        JSlider contrast = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        contrast.setPaintTicks( true );
        contrast.setMajorTickSpacing( 5 );
        videoControls.add(contrast);
        
        JLabel tintLabel = new JLabel("Tint");
        tintLabel.setFont(labelFont);
        videoControls.add(tintLabel);
        JSlider tint = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        tint.setPaintTicks( true );
        tint.setMajorTickSpacing( 5 );
        videoControls.add(tint);

        JPanel tPanel = new JPanel();
        tPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        tPanel.setBackground(bgColor);
        JButton reset = new JButton("Reset");
        reset.setBackground(bgColor);
        tPanel.add(reset);
        videoControls.add(tPanel);
        
        eastPanel.add(BorderLayout.SOUTH, videoControls);         
        
        JPanel playbackControls = new JPanel();
        playbackControls.setBorder(BorderFactory.createTitledBorder(border, 
          "Playback", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
        playbackControls.setBackground(bgColor);
               
        JPanel jog = new JPanel();
        jog.setLayout(new GridLayout(2, 1));
        JLabel jogLabel = new JLabel("Jog Shuttle");
        jogLabel.setFont(labelFont);
        jog.add(jogLabel);
        JSlider jogShuttle = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        jogShuttle.setPaintTicks( true );
        jogShuttle.setMajorTickSpacing( 5 );
        jog.add(jogShuttle);
        jog.setBackground(bgColor);
        Dictionary jogLabels = new Hashtable();
        JLabel reverse = new JLabel("Reverse");
        reverse.setFont(sliderLabelFont);
        jogLabels.put(new Integer(min), reverse);
        JLabel forward = new JLabel("Forward");
        forward.setFont(sliderLabelFont);
        jogLabels.put(new Integer(max), forward);
        jogShuttle.setLabelTable(jogLabels);
        jogShuttle.setPaintLabels(true);
        playbackControls.add(jog);
        
        playbackControls.add(new JSeparator(SwingConstants.VERTICAL));
        
        JPanel speed = new JPanel();
        speed.setLayout(new GridLayout(2, 1));
        JLabel speedLabel = new JLabel("Playback Speed");
        speedLabel.setFont(labelFont);
        speed.add(speedLabel);
        final JSlider playbackSpeed = new JSlider(JSlider.HORIZONTAL, 1, 6, 2 );
        playbackSpeed.setPaintTicks( true );
        playbackSpeed.setMajorTickSpacing( 2 );
        playbackSpeed.setMinorTickSpacing( 1 );
        speed.add(playbackSpeed);
        speed.setBackground(bgColor);

        Dictionary speedLabels = new Hashtable();
        JLabel half = new JLabel("1/2x");
        half.setFont(sliderLabelFont);
        speedLabels.put(new Integer(1), half);
        JLabel one = new JLabel("1x");
        one.setFont(sliderLabelFont);
        speedLabels.put(new Integer(2), one);
        JLabel two = new JLabel("2x");
        two.setFont(sliderLabelFont);
        speedLabels.put(new Integer(4), two);
        JLabel three = new JLabel("3x");
        three.setFont(sliderLabelFont);
        speedLabels.put(new Integer(6), three);
        playbackSpeed.setLabelTable(speedLabels);
        playbackSpeed.setPaintLabels(true);
        
        playbackSpeed.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent evt) {
                float value = playbackSpeed.getValue() / 2.0F;
                PlayerFrame front = WindowList.INSTANCE.getFrontmostWindow();
                if (front != null) {
                    try {
                        front.setSpeed(value);
                    }
                    catch (StdQTException e) {
                        // ???? Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            
        });
        
        playbackControls.add(speed);
        
        // playbackControls.setLayout(new GridLayout(1, 3));
        southPanel.add(playbackControls);
        // move to right hand of screen????
        
        this.getContentPane().add(BorderLayout.EAST, eastPanel);
        this.getContentPane().add(BorderLayout.WEST, westPanel);
        this.getContentPane().add(BorderLayout.SOUTH, southPanel);
        
        this.pack();
        this.setResizable(false);
        
        Utilities.centerOnScreen(this);
        
    }


}
