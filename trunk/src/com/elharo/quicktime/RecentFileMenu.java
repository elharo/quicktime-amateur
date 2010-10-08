package com.elharo.quicktime;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenu;

class RecentFileMenu extends JMenu {
    
    // xxx synchronize this????
    private static List<RecentFileMenu> instances = new ArrayList<RecentFileMenu>();

    RecentFileMenu() {
        super("Recent Files");
        instances.add(this);
        updateMe();
    }
    
    protected void finalize() {
        instances.remove(this);
    }

    static void update() {
        // threading issues
		for (RecentFileMenu menu : instances) {
            menu.removeAll();
            menu.updateMe();
        }
    }
    
    private void updateMe() {
		for (File f : RecentFileList.INSTANCE.values()) {
            insert(new RecentFileAction(f), 0);
        }
        // XXX add clear menu if necessary
    }
}
