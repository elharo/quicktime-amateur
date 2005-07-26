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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class PresentMovieDialog extends JDialog {

    // XXX need to add action listeners to these buttons
    private JButton playButton = new JButton("Play");
    private JButton cancelButton = new JButton("Cancel");
    private JComboBox size = new JComboBox();
    
    PresentMovieDialog(PlayerFrame frame) {
        
        super(frame, "Present Movie");
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(cancelButton);
        buttons.add(playButton);
        this.getRootPane().setDefaultButton(playButton);
        mainPanel.add(BorderLayout.SOUTH, buttons);
        
        JPanel radios = new JPanel();
        ButtonGroup group = new ButtonGroup();
        JRadioButton movie = new JRadioButton("Movie", true);
        group.add(movie);
        JRadioButton slideshow = new JRadioButton("Slide Show");
        group.add(slideshow);
        JLabel label = new JLabel("Use the left and right arrow keys to control the slide show.");
        radios.add(movie);
        radios.add(slideshow);
        radios.add(label);
        
        radios.setLayout(new GridLayout(3, 1));
        
        mainPanel.add(BorderLayout.CENTER, radios);
        
        JPanel north = new JPanel();
        north.setLayout(new FlowLayout(FlowLayout.LEFT));
        north.add(new JLabel("Movie Size: "));
        size.addItem("Normal");
        size.addItem("Double");
        size.addItem("Half");
        size.addItem("Fullscreen");
        size.addItem("Current");
        north.add(size);
        mainPanel.add(BorderLayout.NORTH, north);
        
        GridBagLayout layout = new GridBagLayout();
        this.getContentPane().setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 20, 10, 20);
        layout.setConstraints(mainPanel, constraints);
        this.getContentPane().add(mainPanel);
        this.pack();
        this.center();
    }
    
    
    private void center() {

        Dimension screenSize = getToolkit().getScreenSize();
        Dimension dialogSize = this.getSize();
        int top = (screenSize.height - dialogSize.height)/2;
        int left = (screenSize.width - dialogSize.width)/2;
        this.setLocation(left, top);
        
    }

}
