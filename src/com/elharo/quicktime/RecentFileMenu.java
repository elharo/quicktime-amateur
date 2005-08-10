package com.elharo.quicktime;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenu;

class RecentFileMenu extends JMenu {
    
    // xxx synchronize this????
    private static List instances = new ArrayList();

    RecentFileMenu() {
        super("Recent Files");
        update();
        instances.add(this);
    }
    
    public void finalize() {
        instances.remove(this);
    }

    static void update() {
        // threading issues
        Iterator iterator = instances.iterator();
        while (iterator.hasNext()) {
            RecentFileMenu menu = (RecentFileMenu) iterator.next();
            menu.removeAll();
            Iterator files = Main.recentFileList.iterator();
            while (files.hasNext()) {
                File f = (File) files.next();
                menu.add(new RecentFileAction(f));
            }
        }
    }
    
    
}
