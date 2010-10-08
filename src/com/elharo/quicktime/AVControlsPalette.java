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

    private final static Font labelFont = new Font("Lucida Grande", Font.PLAIN, 10);

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

        Color bgColor = new Color(232, 232, 232);

        this.getContentPane().setLayout(new BorderLayout());

        // XXX need to align labels with left edges of sliders
        // XXX need to make two vertical panels same size
        // XXX need to set initial values to current movie's volume
        // XXX need to watch for window changes; MVC

        JPanel audioControls = new JPanel();
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        audioControls.setBorder(BorderFactory.createTitledBorder(
          border, "Audio", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, labelFont));
        audioControls.setBackground(bgColor);
        // XXX this needs to go. We need a better layout manager.
        audioControls.setLayout(new BoxLayout(audioControls, BoxLayout.PAGE_AXIS));

        int min = 0;
        int max = 10;

        audioControls.add(makeSliderLabel("Volume"));

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
                    } catch (StdQTException e) {
                        // ???? Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        });
        audioControls.add(volume);

        audioControls.add(makeSliderLabel("Balance"));
        final JSlider balance = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        balance.setPaintTicks( true );
        balance.setMajorTickSpacing( 5 );

        // An L label on left and R label on right
        Dictionary<Integer, JLabel> balanceLabels = new Hashtable<Integer, JLabel>();
        JLabel left = new JLabel("L");
        left.setFont(labelFont);
        balanceLabels.put(Integer.valueOf(min), left);
        JLabel right = new JLabel("R");
        right.setFont(labelFont);
        balanceLabels.put(Integer.valueOf(max), right);
        balance.setLabelTable(balanceLabels);
        balance.setPaintLabels(true);
        balance.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent evt) {
                float value = (balance.getValue() - 5)/ 10.0F;
                PlayerFrame front = WindowList.INSTANCE.getFrontmostWindow();
                if (front != null) {
                    try {
                        front.setBalance(value);
                    } catch (QTException e) {
                        // ???? Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        audioControls.add(balance);

        audioControls.add(makeSliderLabel("Bass"));
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

        });
		audioControls.add(bass);

        audioControls.add(makeSliderLabel("Treble"));
        final JSlider treble = new JSlider(JSlider.HORIZONTAL, -256, 256, 0 );
        treble.setPaintTicks( true );
        treble.setMajorTickSpacing( 256 );
        treble.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent evt) {
                PlayerFrame front = WindowList.INSTANCE.getFrontmostWindow();
                if (front != null) {
                    try {
                        front.setTreble(treble.getValue());
                    } catch (QTException e) {
                        // ???? Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        audioControls.add(treble);

        audioControls.add(makeSliderLabel("Pitch Shift"));
        JSlider pitchShift = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        pitchShift.setPaintTicks( true );
        pitchShift.setMajorTickSpacing( 1 );
        pitchShift.setEnabled(false); // XXX
        audioControls.add(pitchShift);

        westPanel.add(BorderLayout.SOUTH, audioControls);

        JPanel videoControls = new JPanel();
        videoControls.setBorder(BorderFactory.createTitledBorder(border, "Video",
          TitledBorder.LEFT, TitledBorder.ABOVE_TOP, labelFont));
        videoControls.setBackground(bgColor);
        videoControls.setLayout(new BoxLayout(videoControls, BoxLayout.PAGE_AXIS));

        videoControls.add(makeSliderLabel("Brightness"));
        JSlider brightness = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        brightness.setPaintTicks( true );
        brightness.setMajorTickSpacing( 5 );
        brightness.setEnabled(false); // XXX
        videoControls.add(brightness);

        videoControls.add(makeSliderLabel("Color"));
        JSlider color = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        color.setPaintTicks( true );
        color.setMajorTickSpacing( 5 );
        color.setEnabled(false); // XXX
        videoControls.add(color);

        videoControls.add(makeSliderLabel("Contrast"));
        JSlider contrast = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        contrast.setPaintTicks( true );
        contrast.setMajorTickSpacing( 5 );
        contrast.setEnabled(false); // XXX
        videoControls.add(contrast);

        videoControls.add(makeSliderLabel("Tint"));
        JSlider tint = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        tint.setPaintTicks( true );
        tint.setMajorTickSpacing( 5 );
        tint.setEnabled(false); // XXX
        videoControls.add(tint);

        JPanel tPanel = new JPanel();
        tPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        tPanel.setBackground(bgColor);
        JButton reset = new JButton("Reset");
        reset.setFont(labelFont);
        reset.setBackground(bgColor);
        tPanel.add(reset);
        videoControls.add(tPanel);

        eastPanel.add(BorderLayout.SOUTH, videoControls);

        JPanel playbackControls = new JPanel();
        playbackControls.setBorder(BorderFactory.createTitledBorder(border,
          "Playback", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, labelFont));
        playbackControls.setBackground(bgColor);

        JPanel jog = new JPanel();
        jog.setLayout(new BoxLayout(jog, BoxLayout.PAGE_AXIS));
        jog.add(makeSliderLabel("Jog Shuttle"));
        JSlider jogShuttle = new JSlider(JSlider.HORIZONTAL, min, max, 5 );
        jogShuttle.setPaintTicks( true );
        jogShuttle.setMajorTickSpacing( 5 );
        jog.add(jogShuttle);
        jog.setBackground(bgColor);
        Dictionary<Integer, JLabel> jogLabels = new Hashtable<Integer, JLabel>();
        JLabel reverse = new JLabel("Reverse");
        reverse.setFont(labelFont);
        jogLabels.put(Integer.valueOf(min), reverse);
        JLabel forward = new JLabel("Forward");
        forward.setFont(labelFont);
        jogLabels.put(Integer.valueOf(max), forward);
        jogShuttle.setLabelTable(jogLabels);
        jogShuttle.setPaintLabels(true);
        jogShuttle.setEnabled(false); // XXX
        playbackControls.add(jog);

        playbackControls.add(new JSeparator(SwingConstants.VERTICAL));

        JPanel speed = new JPanel();
        speed.setLayout(new BoxLayout(speed, BoxLayout.PAGE_AXIS));
        speed.add(makeSliderLabel("Playback Speed"));
        final JSlider playbackSpeed = new JSlider(JSlider.HORIZONTAL, 1, 6, 2 );
        playbackSpeed.setPaintTicks( true );
        playbackSpeed.setMajorTickSpacing( 2 );
        playbackSpeed.setMinorTickSpacing( 1 );
        speed.add(playbackSpeed);
        speed.setBackground(bgColor);

        Dictionary<Integer, JLabel> speedLabels = new Hashtable<Integer, JLabel>();
        JLabel half = new JLabel("1/2x");
        half.setFont(labelFont);
        speedLabels.put(Integer.valueOf(1), half);
        JLabel one = new JLabel("1x");
        one.setFont(labelFont);
        speedLabels.put(Integer.valueOf(2), one);
        JLabel two = new JLabel("2x");
        two.setFont(labelFont);
        speedLabels.put(Integer.valueOf(4), two);
        JLabel three = new JLabel("3x");
        three.setFont(labelFont);
        speedLabels.put(Integer.valueOf(6), three);
        playbackSpeed.setLabelTable(speedLabels);
        playbackSpeed.setPaintLabels(true);

        playbackSpeed.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent evt) {
                float value = playbackSpeed.getValue() / 2.0F;
                PlayerFrame front = WindowList.INSTANCE.getFrontmostWindow();
                if (front != null) {
                    try {
                        front.setSpeed(value);
                    } catch (StdQTException e) {
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

    private JPanel makeSliderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(labelFont);
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPanel.add(label);
        return labelPanel;
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }
}
