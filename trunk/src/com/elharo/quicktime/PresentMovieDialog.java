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
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class PresentMovieDialog extends JDialog {

    private JButton playButton = new JButton("Play");
    private JButton cancelButton = new JButton("Cancel");
    private JComboBox size = new JComboBox();
    
    PresentMovieDialog() {
        this.setUndecorated(true);
        this.setLayout(new BorderLayout());
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(cancelButton);
        buttons.add(playButton);
        this.getContentPane().add(BorderLayout.SOUTH, buttons);
        
        JPanel radios = new JPanel();
        ButtonGroup group = new ButtonGroup();
        JRadioButton movie = new JRadioButton("Movie", true);
        group.add(movie);
        JRadioButton slideshow = new JRadioButton("Slide Show");
        group.add(slideshow);
        JLabel label = new JLabel("Use the left and right arrows to direct the slide show");
        radios.add(movie);
        radios.add(slideshow);
        radios.add(label);
        
        radios.setLayout(new GridLayout(3, 1));
        
        
        this.getContentPane().add(BorderLayout.CENTER, radios);
        
        /* size.("Normal");
        size.add("Double");
        size.add("Half");
        size.add("Fullscreen");
        size.add("Current"); */
        this.getContentPane().add(BorderLayout.NORTH, size);
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


    /**
     * Just for testing
     */
    public static void main(String[] args) {
        PresentMovieDialog dialog = new PresentMovieDialog();
        dialog.show();
    }

}
