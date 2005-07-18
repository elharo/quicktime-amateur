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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

/**
 * @author Elliotte Rusty Harold
 * @version 1.0d1
 */
class AboutDialog extends JDialog {

    AboutDialog(JFrame parent) {
        super(parent, "About Amateur");
        
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(BorderLayout.CENTER, makeMainPane());
        this.getContentPane().add(BorderLayout.WEST, new JPanel());
        this.getContentPane().add(BorderLayout.EAST, new JPanel());

        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }
    
    private static Font font = new Font("Dialog", Font.PLAIN, 12);
    
    
    private JPanel makeMainPane() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(
          BorderLayout.NORTH, 
          makeTopPane()
        );
        JTextComponent information = new JTextArea();
        information.setText("Amateur is free software; you can redistribute it and/or modify\n" +
                "it under the terms of the GNU General Public License as published \n" +
                "by the Free Software Foundation; either version 2 of the License, \n" +
                "or (at your option) any later version.\n\n" +
                "Amateur is distributed in the hope that it will be useful,\n" +
                "but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
                "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
                "GNU General Public License for more details.\n\n" +
                "You should have received a copy of the GNU General Public License\n" +
                "along with XQuisitor; if not, write to the\n\n" +
                "Free Software Foundation, Inc. \n" +
                "59 Temple Place, Suite 330\n" +
                "Boston, MA  02111-1307\nUSA");
        information.setEditable(false);
        information.setBackground(this.getBackground());
        panel.add(BorderLayout.CENTER, information);        
        JPanel okPanel = new JPanel();
        okPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setVisible(false);
                dispose();
            }
        });
        okPanel.add(ok);
        getRootPane().setDefaultButton(ok);
        panel.add(BorderLayout.SOUTH, okPanel);
        
        return panel;
    }
    
    private JPanel makeTopPane() {
        JPanel panel = new JPanel();
        LayoutManager layout = new GridLayout(5, 1);
        panel.setLayout(layout);
        panel.add(new JLabel());
        JLabel title = new JLabel("Amateur");
        panel.add(title);
        
        JLabel copyright = new JLabel("Copyright 2005 Elliotte Rusty Harold");
        copyright.setFont(font);
        panel.add(copyright);
        
        JLabel version = new JLabel("Version: 1.0d1");
        version.setFont(font);
        panel.add(version);
        
        return panel;  
    }

}
