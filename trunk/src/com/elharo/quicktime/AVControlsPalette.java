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

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/** 
 * 
 * @author Elliotte Rusty Harold
 *
 */
class AVControlsPalette extends JDialog {

    // XXX make control-w and close work with this dialog
    private JPanel eastPanel = new JPanel();
    private JPanel westPanel = new JPanel();
    private JPanel southPanel = new JPanel();
    
    AVControlsPalette(PlayerFrame frame) {
        
        super(frame, "A/V Controls");
        this.getContentPane().setLayout(new BorderLayout());
        
        JPanel audioControls = new JPanel();
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        audioControls.setBorder(BorderFactory.createTitledBorder(border, "Audio", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
        audioControls.setBackground(Color.LIGHT_GRAY);
        audioControls.setLayout(new GridLayout(10, 1));
        
        int min = 0;
        int max = 10;

        audioControls.add(new JLabel("Volume"));
        JSlider volume = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        volume.setPaintTicks( true );
        volume.setMajorTickSpacing( 5 );
        audioControls.add(volume);
        audioControls.add(new JSeparator());

        audioControls.add(new JLabel("Bass"));
        JSlider bass = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        bass.setPaintTicks( true );
        bass.setMajorTickSpacing( 5 );
        audioControls.add(bass);
        
        audioControls.add(new JLabel("Treble"));
        JSlider treble = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        treble.setPaintTicks( true );
        treble.setMajorTickSpacing( 5 );
        audioControls.add(treble);
        
        audioControls.add(new JSeparator());

        audioControls.add(new JLabel("Balance"));
        JSlider balance = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        balance.setPaintTicks( true );
        balance.setMajorTickSpacing( 5 );
        audioControls.add(balance);
        
        westPanel.add(BorderLayout.SOUTH, audioControls);
  
        
        JPanel videoControls = new JPanel();
        videoControls.setBorder(BorderFactory.createTitledBorder(border, "Video", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
        videoControls.setBackground(Color.LIGHT_GRAY);
        videoControls.setLayout(new GridLayout(10, 1));

        videoControls.add(new JLabel("Brightness"));
        JSlider brightness = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        brightness.setPaintTicks( true );
        brightness.setMajorTickSpacing( 5 );
        videoControls.add(brightness);

        videoControls.add(new JLabel("Color"));
        JSlider color = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        color.setPaintTicks( true );
        color.setMajorTickSpacing( 5 );
        videoControls.add(color);

        videoControls.add(new JLabel("Contrast"));
        JSlider contrast = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        contrast.setPaintTicks( true );
        contrast.setMajorTickSpacing( 5 );
        videoControls.add(contrast);
        
        videoControls.add(new JLabel("Tint"));
        JSlider tint = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        tint.setPaintTicks( true );
        tint.setMajorTickSpacing( 5 );
        videoControls.add(tint);

        videoControls.add(new JButton("Reset"));
        
        eastPanel.add(BorderLayout.SOUTH, videoControls);
         
        
        JPanel playbackControls = new JPanel();
        playbackControls.setBorder(BorderFactory.createTitledBorder(border, 
          "Playback", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
        playbackControls.setBackground(Color.LIGHT_GRAY);
        
        
        JPanel jog = new JPanel();
        jog.setLayout(new GridLayout(2, 1));
        jog.add(new JLabel("Jog Shuttle"));
        JSlider jogShuttle = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        jogShuttle.setPaintTicks( true );
        jogShuttle.setMajorTickSpacing( 5 );
        jog.add(jogShuttle);
        jog.setBackground(Color.LIGHT_GRAY);
        playbackControls.add(jog);
        
        playbackControls.add(new JSeparator(SwingConstants.VERTICAL));
        
        JPanel speed = new JPanel();
        speed.setLayout(new GridLayout(2, 1));
        speed.add(new JLabel("Playback Speed"));
        JSlider playbackSpeed = new JSlider(JSlider.HORIZONTAL, 0, 6, 2 );
        playbackSpeed.setPaintTicks( true );
        playbackSpeed.setMajorTickSpacing( 2 );
        playbackSpeed.setMinorTickSpacing( 1 );
        speed.add(playbackSpeed);
        speed.setBackground(Color.LIGHT_GRAY);
        playbackControls.add(speed);
        
        // playbackControls.setLayout(new GridLayout(1, 3));
        southPanel.add(playbackControls);
        // move to right hand of screen????
        
        this.getContentPane().add(BorderLayout.EAST, eastPanel);
        this.getContentPane().add(BorderLayout.WEST, westPanel);
        this.getContentPane().add(BorderLayout.SOUTH, southPanel);
        
        this.pack();
        this.setResizable(false);
    }


}
