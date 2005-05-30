package com.elharo.quicktime.tests;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import quicktime.QTException;

import com.elharo.quicktime.PlayerFrame;
import com.elharo.quicktime.QuicktimeInit;

import junit.framework.TestCase;

public class PlayerFrameTest extends TestCase {
    
    private JFrame frame;
    private JMenuBar menubar;
    
    static {
        try {
            QuicktimeInit.setup();
        }
        catch (QTException e) {
            throw new RuntimeException(e);
        }   
    }
    
    protected void setUp() throws QTException {
        frame = new PlayerFrame();
        menubar = frame.getJMenuBar();
    }

    public void testTitleOfUntitledFrameIsAmateurPlayer() throws QTException {
        assertEquals("Amateur Player", frame.getTitle());
    }
    
    public void testInitialFrameIsNotTooSmall() throws QTException { 
        Dimension d = frame.getSize();
        assertTrue(d.width > 300);
        assertTrue(d.height > 300);
    }
    
    public void testAmateurPlayerHasHelpMenu() throws QTException {

        JMenu firstMenu = menubar.getMenu(4);
        assertEquals("Help", firstMenu.getText());
        
    }
    
    public void testAmateurPlayerHasFileMenu() throws QTException {
        JMenu firstMenu = menubar.getMenu(0);
        assertEquals("File", firstMenu.getText());
        
    }
    
    public void testAmateurPlayerHasEditMenu() throws QTException {
        JMenu firstMenu = menubar.getMenu(1);
        assertEquals("Edit", firstMenu.getText());
        
    }
    
    public void testAmateurPlayerHasViewMenu() throws QTException {
        JMenu firstMenu = menubar.getMenu(2);
        assertEquals("View", firstMenu.getText());
        
    }
    
    public void testAmateurPlayerHasWindowMenu() throws QTException {
        JMenu firstMenu = menubar.getMenu(3);
        assertEquals("Window", firstMenu.getText());
        
    }
    
    public void testOpenFileHasCommandKeyEquivalent() throws QTException {
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
    
    public void testMinimizeCommandKeyEquivalent() throws QTException {
        

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
    
    public void testPrintCommandKeyEquivalent() throws QTException {        
        assertCommandKeyEquivalent("Print...", 'P');
    }
    
    public void testSaveCommandKeyEquivalent() throws QTException {        
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
    
}
