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
package com.elharo.quicktime.tests;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import quicktime.QTException;

import com.elharo.quicktime.FrameDisplayer;
import com.elharo.quicktime.PlayerFrame;
import com.elharo.quicktime.QuicktimeInit;

import junit.framework.TestCase;

public class PlayerFrameTest extends TestCase {
    
    private PlayerFrame frame;
    private JMenuBar menubar;
    
    static {
        try {
            QuicktimeInit.setup();
        }
        catch (QTException ex) {
            throw new RuntimeException(ex);
        }   
    }
    
    protected void setUp() throws QTException {
        frame = new PlayerFrame();
        menubar = frame.getJMenuBar();
    }

    protected void tearDown() {
        frame.dispose();
    }

    public void testTitleOfUntitledFrameIsAmateurPlayer() {
        assertEquals("Amateur Player", frame.getTitle());
    }
    
    public void testInitialFrameIsNotTooSmall() { 
        Dimension d = frame.getSize();
        assertTrue(d.width > 300);
        assertTrue(d.height > 300);
    }
    
    public void testAmateurPlayerHasHelpMenu() {

        JMenu firstMenu = menubar.getMenu(4);
        assertEquals("Help", firstMenu.getText());
        
    }
    
    public void testAmateurPlayerHasFileMenu() {
        JMenu firstMenu = menubar.getMenu(0);
        assertEquals("File", firstMenu.getText());
        
    }
    
    public void testAmateurPlayerHasEditMenu() {
        JMenu firstMenu = menubar.getMenu(1);
        assertEquals("Edit", firstMenu.getText());
        
    }
    
    public void testAmateurPlayerHasViewMenu() {
        JMenu firstMenu = menubar.getMenu(2);
        assertEquals("View", firstMenu.getText());
        
    }
    
    public void testAmateurPlayerHasWindowMenu() {
        JMenu firstMenu = menubar.getMenu(3);
        assertEquals("Window", firstMenu.getText());
        
    }
    
    public void testOpenFileHasCommandKeyEquivalent() {
        Component[] fileitems = menubar.getMenu(0).getMenuComponents();
        for (int i = 0; i < fileitems.length; i++) {
            if (fileitems[i] instanceof JMenuItem) {
                JMenuItem item = (JMenuItem) fileitems[i];
                if (item.getText().equals("Open File...")) {
                    assertEquals('O', item.getAccelerator().getKeyCode());
                }
            }
        }
        
    }
    
    public void testMinimizeCommandKeyEquivalent() {
       
        Component[] fileitems = menubar.getMenu(3).getMenuComponents();
        for (int i = 0; i < fileitems.length; i++) {
            if (fileitems[i] instanceof JMenuItem) {
                JMenuItem item = (JMenuItem) fileitems[i];
                if (item.getText().equals("Minimize")) {
                    assertEquals('M', item.getAccelerator().getKeyCode());
                }
            }
        }
        
    }
    
    private JMenuItem findJMenuItem(String name) {
        
        for (int menu = 0; menu < menubar.getMenuCount(); menu++) {
            Component[] menuitems = menubar.getMenu(menu).getMenuComponents();
            for (int i = 0; i < menuitems.length; i++) {
                if (menuitems[i] instanceof JMenuItem) {
                    JMenuItem item = (JMenuItem) menuitems[i];
                    if (item.getText().equals(name)) {
                        return item;
                    }
                }
            }
        }
        return null;
    }
    
    
    public void testPrintCommandKeyEquivalent() {        
        assertCommandKeyEquivalent("Print...", 'P');
    }
    
    public void testSaveCommandKeyEquivalent() {        
        assertCommandKeyEquivalent("Save", 'S');
    }
    
    private void assertCommandKeyEquivalent(String menuName, char keyCode) {
        
        for (int i = 0; i < menubar.getMenuCount(); i++) {
            JMenu menu = menubar.getMenu(i);
            Component[] fileitems = menu.getMenuComponents();
            for (int j = 0; j < fileitems.length; j++) {
                if (fileitems[j] instanceof JMenuItem) {
                    JMenuItem item = (JMenuItem) fileitems[j];
                    if (item.getText().equals(menuName)) {
                        assertEquals(keyCode, item.getAccelerator().getKeyCode());
                    }
                }
            }
        }
        
    }
    
    
    public void testCloseWindow() throws InterruptedException {
        
        FrameDisplayer.display(frame);
        while (! frame.isVisible() ) Thread.sleep(100);
        
        JMenuItem closeMenuItem = findJMenuItem("Close");
        closeMenuItem.doClick();
        assertFalse(frame.isVisible());
        
    }
    
    
    public void testNewMenuItem() {
        int oldNumberOfWindows = Frame.getFrames().length;
        JMenuItem newMenuItem = findJMenuItem("New Player");
        newMenuItem.doClick();
        int newNumberOfWindows = Frame.getFrames().length;
        assertEquals(oldNumberOfWindows+1, newNumberOfWindows);
    }
    
}
