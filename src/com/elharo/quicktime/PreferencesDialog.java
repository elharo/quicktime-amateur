/* Copyright 2006 Elliotte Rusty Harold

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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;


/** 
 * 
 * @author Elliotte Rusty Harold
 *
 */
class PreferencesDialog extends JDialog {

    
    PreferencesDialog(PlayerFrame frame) {
        
        super(frame, "General");
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        
        JPanel movies = getPanel("Movies:");
        movies.add(getCheckbox(Preferences.OPEN_MOVIES_IN_NEW_PLAYERS));
        movies.add(getCheckbox(Preferences.AUTOMATICALLY_PLAY_MOVIES_WHEN_OPENED));
        movies.add(getCheckbox(Preferences.USE_HIGH_QUALITY_VIDEO));
        
        this.getContentPane().add(Box.createRigidArea(new Dimension(0, 20)));
        this.getContentPane().add(movies);
        
        JPanel sound = getPanel("Sound:");
        sound.add(getCheckbox(Preferences.PLAY_SOUND_IN_FRONTMOST_PLAYER_ONLY));
        sound.add(getCheckbox(Preferences.PLAY_SOUND_WHEN_APPLICATION_IS_IN_BACKGROUND));
        sound.add(getCheckbox(Preferences.SHOW_EQUALIZER));
        
        this.getContentPane().add(Box.createRigidArea(new Dimension(0, 20)));
        this.getContentPane().add(sound);
        
        JPanel other = getPanel("Other:");
        other.add(getCheckbox(Preferences.SHOW_CONTENT_GUIDE_AUTOMATICALLY));
        other.add(getCheckbox(Preferences.PAUSE_MOVIES_BEFORE_SWITCHING_USERS));
        other.add(getNumberOfRecentItems());

        this.getContentPane().add(Box.createRigidArea(new Dimension(0, 20)));
        this.getContentPane().add(other);        
        
        // XXX There's extra space at the bottom of the dialog I need to get rid of
        // XXX The items are not packed closely enough together
        // XXX position should be remembered if user moves it; could handle by
        // not disposing and recreating dialog; just hide and show
        
        this.pack();
        
        Utilities.centerOnScreen(this);

        this.setResizable(false);
        
    }

    private JPanel getPanel(String label) {

        JPanel movies = new JPanel();
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
        movies.setLayout(new BoxLayout(movies, BoxLayout.Y_AXIS));
        labelPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        JLabel m = new JLabel(label);
        labelPanel.add(m);
        labelPanel.add(Box.createHorizontalGlue());
        movies.add(labelPanel);
        return movies;
    }

    private JPanel getCheckbox(final String label) {

        JPanel p1 = new JPanel();
        BoxLayout layout = new BoxLayout(p1, BoxLayout.PAGE_AXIS);
        p1.setLayout(layout);
        final JCheckBox checkbox = new JCheckBox(label);
        checkbox.setSelected(Preferences.getInstance().getBooleanValue(label));
        checkbox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent event) {
                Preferences.getInstance().setValue(label, checkbox.isSelected());
            }
            
        });
        
        p1.add(Box.createRigidArea(new Dimension(25, 0)));
        p1.add(checkbox);
        return p1;
    }

    private JPanel getNumberOfRecentItems() {

        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPopupMenu choice = new JPopupMenu(Preferences.NUMBER_OF_RECENT_ITEMS);
        choice.add("None");
        choice.add("5");
        choice.add("10");
        choice.add("15");
        choice.add("20");
        choice.add("30");
        choice.add("50");
// XXX        choice.setSelected();
        
// XXX need an itemListener
        
        p1.add(Box.createRigidArea(new Dimension(25, 0)));
        p1.add(choice);
        return p1;
    }



}
