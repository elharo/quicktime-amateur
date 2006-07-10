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
        
        JPanel movies = new JPanel();
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
        movies.setLayout(new BoxLayout(movies, BoxLayout.Y_AXIS));
        labelPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        JLabel m = new JLabel("Movies:");
        labelPanel.add(m);
        labelPanel.add(Box.createHorizontalGlue());
        movies.add(labelPanel);
        
        JPanel p1 = new JPanel();
        final JCheckBox openMoviesInNewPlayers = new JCheckBox("Open movies in new players");
        openMoviesInNewPlayers.setEnabled(Preferences.getInstance().getOpenMoviesInNewPlayers());
        openMoviesInNewPlayers.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent event) {
                Preferences.getInstance().setOpenMoviesInNewPlayers(openMoviesInNewPlayers.isSelected());
            }
            
        });
        
        p1.add(Box.createRigidArea(new Dimension(25, 0)));
        p1.add(openMoviesInNewPlayers);
        movies.add(p1);
        
        this.getContentPane().add(Box.createRigidArea(new Dimension(0, 20)));
        this.getContentPane().add(movies);
        
        JPanel sound = new JPanel();
        sound.add(new JLabel("Sound:"));
        this.getContentPane().add(sound);
        
        JPanel other = new JPanel();
        other.add(new JLabel("Other:"));
        this.getContentPane().add(other);        
        
        this.pack();
        this.setResizable(false);
        
    }



}
