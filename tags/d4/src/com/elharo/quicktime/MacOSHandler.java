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

import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Frame;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;
import com.apple.eawt.Application;

/**
 * @author Elliotte Rusty Harold
 * @version 1.0d3
 */
public class MacOSHandler extends Application {

    private Dialog about;
    
    public MacOSHandler(PlayerFrame frame) {
        about = new AboutDialog(frame);
        addApplicationListener(new AboutBoxHandler());
    }

    class AboutBoxHandler extends ApplicationAdapter {
        // what else can I handle here????
        
        public void handleAbout(ApplicationEvent event) {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    about.setVisible(true);
                }
            });
            event.setHandled(true);
        }
        
        public void handleQuit(ApplicationEvent event) {
            Iterator iterator = WindowList.INSTANCE.iterator();
            while (iterator.hasNext()) {
                Frame next = (Frame) iterator.next();
                next.setVisible(false);
                next.dispose();
            }
            
            RecentFileList.INSTANCE.storeRecentFiles();
            // XXX could fix this by setting the hidden frame to exit on close 
            // and then closing it instead
            System.exit(0);
        }
        
    }


        
    
    
}