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
import java.awt.event.*;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * @author Elliotte Rusty Harold
 */

class PreferencesDialog extends JDialog {

	private JTextField numberRecentItems;

    PreferencesDialog (JFrame frame) {
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
        other.add(getCheckbox(Preferences.USE_AWT_FILE_DIALOG));

        this.getContentPane().add(Box.createRigidArea(new Dimension(0, 20)));
        this.getContentPane().add(other);
        this.getContentPane().add(getNumberOfRecentItems());

        this.getContentPane().add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel btns = new JPanel();
		btns.setLayout(new BoxLayout(btns, BoxLayout.X_AXIS));
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) { action(); } });
		btns.add(Box.createHorizontalGlue());
		btns.add(ok);
		btns.add(Box.createHorizontalGlue());

		getContentPane().add(btns);
		getContentPane().add(Box.createRigidArea(new Dimension(0, 10)));

        // XXX position should be remembered if user moves it; could handle by
        // not disposing and recreating dialog; just hide and show

        this.pack();
        Utilities.centerOnScreen(this);
        this.setResizable(false);
    }

	private void action() {
		try {
			Preferences.setValue(Preferences.NUMBER_OF_RECENT_ITEMS,
								Integer.parseInt(numberRecentItems.getText()));
		} catch (NumberFormatException nfex) { }
		dispose();
	}

    private JPanel getPanel (String label) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
        labelPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        JLabel m = new JLabel(label);
        labelPanel.add(m);
        labelPanel.add(Box.createHorizontalGlue());
        panel.add(labelPanel);
        return panel;
    }

    private JPanel getCheckbox (final String label) {
        JPanel p1 = new JPanel();
        BoxLayout layout = new BoxLayout(p1, BoxLayout.PAGE_AXIS);
        p1.setLayout(layout);
        final JCheckBox checkbox = new JCheckBox(label);
        checkbox.setSelected(Preferences.getBooleanValue(label));
        checkbox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                Preferences.setValue(label, checkbox.isSelected());
            }
        });
        p1.add(Box.createRigidArea(new Dimension(25, 0)));
        p1.add(checkbox);
        return p1;
    }

    private JPanel getNumberOfRecentItems() {
        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout(FlowLayout.LEFT));
        p1.add(Box.createRigidArea(new Dimension(25, 0)));
        p1.add(new JLabel("Number of recent items: "));
		numberRecentItems = new JTextField(4);
		numberRecentItems.setText(""+Preferences.getIntValue(Preferences.NUMBER_OF_RECENT_ITEMS));
        p1.add(numberRecentItems);
        return p1;
    }
}
