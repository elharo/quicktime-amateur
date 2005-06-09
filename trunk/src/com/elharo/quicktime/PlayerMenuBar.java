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

import java.awt.Component;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.print.PrinterJob;
import java.util.Iterator;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.MovieController;

class PlayerMenuBar extends JMenuBar {

    static final int menuShortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    private static final int NUMBER_OF_WINDOW_MENU_ITEMS = 10;
    
    private PlayerFrame frame;
    private JMenu windowMenu;
    
    PlayerMenuBar(PlayerFrame frame) {
        this.frame = frame;
        initFileMenu();
        initEditMenu();
        initViewMenu();
        initWindowMenu();
        initHelpMenu();
    }
    
    private void initFileMenu() {
        
        JMenu fileMenu = new JMenu("File");
        
        fileMenu.add(new NewPlayerAction());
        
        JMenuItem newMovieRecording = new JMenuItem("New Movie Recording");
        newMovieRecording.setAccelerator(KeyStroke.getKeyStroke('N', menuShortcutKeyMask | InputEvent.ALT_MASK));        
        newMovieRecording.setEnabled(false);
        fileMenu.add(newMovieRecording);
        
        JMenuItem newAudioRecording = new JMenuItem("New Audio Recording");
        newAudioRecording.setAccelerator(KeyStroke.getKeyStroke('N', menuShortcutKeyMask | InputEvent.ALT_MASK | InputEvent.CTRL_MASK));        
        newAudioRecording.setEnabled(false);
        fileMenu.add(newAudioRecording);
        
        fileMenu.addSeparator();
        
        JMenuItem openFile = new JMenuItem("Open File...");
        openFile.setAccelerator(KeyStroke.getKeyStroke('O', menuShortcutKeyMask));        
        openFile.addActionListener(new FileOpener());
        fileMenu.add(openFile);
        
        fileMenu.add(new URLOpener());
        
        JMenuItem openImageSequence = new JMenuItem("Open Image Sequence...");
        openImageSequence.setAccelerator(KeyStroke.getKeyStroke('O', menuShortcutKeyMask | InputEvent.SHIFT_MASK));        
        openImageSequence.setEnabled(false);
        fileMenu.add(openImageSequence);
        
        // XXX Use this to play all movies in a folder in order
        JMenuItem openMovieSequence = new JMenuItem("Open Movie Sequence...");        
        openMovieSequence.setEnabled(false);
        fileMenu.add(openMovieSequence);
        
        JMenuItem openRecent = new JMenuItem("Open Recent");
        openRecent.setEnabled(false);
        fileMenu.add(openRecent);
        
        fileMenu.add(new CloseAction(frame));
        
        fileMenu.addSeparator();
        
        JMenuItem save = new JMenuItem("Save");
        save.setAccelerator(KeyStroke.getKeyStroke('S', menuShortcutKeyMask));        
        save.setEnabled(false);
        fileMenu.add(save);
        
        fileMenu.add(new SaveAsAction(frame.getMovie()));
        
        JMenuItem revertToSaved = new JMenuItem("Revert to Saved");
        revertToSaved.setEnabled(false);
        fileMenu.add(revertToSaved);
        
        fileMenu.addSeparator();
        
        JMenuItem share = new JMenuItem("Share...");
        share.setAccelerator(KeyStroke.getKeyStroke('S', menuShortcutKeyMask | InputEvent.ALT_MASK));        
        share.setEnabled(false);
        fileMenu.add(share);
        
        JMenuItem export = new JMenuItem("Export...");
        export.setAccelerator(KeyStroke.getKeyStroke('E', menuShortcutKeyMask));        
        export.setEnabled(false);
        fileMenu.add(export);
        
        JMenuItem exportFrames = new JMenuItem("Export Frames...");        
        exportFrames.setEnabled(false);
        fileMenu.add(exportFrames);
        
        fileMenu.addSeparator();
        
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        fileMenu.add(new PageSetupAction(printerJob));
        fileMenu.add(new PrintAction(printerJob, frame));
        
        this.add(fileMenu);
    }
    
    


    private void initEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        
        editMenu.add(new UndoAction(frame));
        editMenu.add(new RedoAction(frame));
        
        editMenu.addSeparator();

        editMenu.add(new CutAction(frame));
        // XXX should copy copy entire movie if nothing is selected????
        editMenu.add(new CopyAction(frame.getMovie()));
        editMenu.add(new CopyCurrentFrameAction(frame));
        editMenu.add(new PasteAction(frame));
        editMenu.add(new ClearAction(frame));
        
        editMenu.addSeparator();

        editMenu.add(new SelectAllAction(frame.getMovie()));
        editMenu.add(new SelectNoneAction(frame.getMovie()));
        
        editMenu.addSeparator();
        
        editMenu.add(new TrimToSelectionAction(frame.getController(), frame));
        
        editMenu.addSeparator();

        JMenuItem addToMovie = new JMenuItem("Add to Movie");
        addToMovie.setAccelerator(KeyStroke.getKeyStroke('V', menuShortcutKeyMask | InputEvent.ALT_MASK));
        addToMovie.setEnabled(false);
        editMenu.add(addToMovie);
        
        JMenuItem addToSelection = new JMenuItem("Add to Selection and Scale");
        addToSelection.setAccelerator(KeyStroke.getKeyStroke('V', menuShortcutKeyMask | InputEvent.ALT_MASK | InputEvent.SHIFT_MASK));
        addToSelection.setEnabled(false);
        editMenu.add(addToSelection);
        
        editMenu.addSeparator();

        JMenuItem find = new JMenuItem("Find");
        find.setEnabled(false);
        editMenu.add(find);
        
        editMenu.addSeparator();

        JMenuItem specialCharacters = new JMenuItem("Special Characters...");
        specialCharacters.setAccelerator(KeyStroke.getKeyStroke('T', menuShortcutKeyMask | InputEvent.ALT_MASK));
        specialCharacters.setEnabled(false);
        editMenu.add(specialCharacters);
        
        add(editMenu);
    }

    private void initWindowMenu() {
        windowMenu = new JMenu("Window");
        
        JMenuItem minimize = new JMenuItem("Minimize");
        minimize.setAccelerator(KeyStroke.getKeyStroke('M', menuShortcutKeyMask));
        minimize.addActionListener(
          new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                frame.setState(Frame.ICONIFIED);
            }
          }
        );
        windowMenu.add(minimize);
        
        JMenuItem zoom = new JMenuItem("Zoom");
        zoom.addActionListener(
          new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
          }
        );
        windowMenu.add(zoom);
        
        windowMenu.addSeparator();

        windowMenu.add(new MovieInfoAction(frame.getMovie()));   
        
        JMenuItem showMovieProperties = new JMenuItem("Show Movie Properties");
        showMovieProperties.setAccelerator(KeyStroke.getKeyStroke('J', menuShortcutKeyMask));
        showMovieProperties.setEnabled(false);
        windowMenu.add(showMovieProperties);                
        
        JMenuItem showAVControls = new JMenuItem("Show A/V Controls");
        showAVControls.setAccelerator(KeyStroke.getKeyStroke('K', menuShortcutKeyMask));
        showAVControls.setEnabled(false);
        windowMenu.add(showAVControls);                
        windowMenu.add(new ShowContentGuideAction());    
        
        windowMenu.addSeparator();

        JMenuItem favorites = new JMenuItem("Favorites");
        favorites.setEnabled(false);
        windowMenu.add(favorites);        
        
        windowMenu.addSeparator();
 
        windowMenu.add(new BringAllToFrontAction());
        
        windowMenu.addSeparator();
        
        WindowList list = WindowList.INSTANCE;
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            PlayerFrame frame = (PlayerFrame) iterator.next();
            windowMenu.add(new WindowMenuItem(frame));
        }        
        
        add(windowMenu);
    }
    
    
    private void initHelpMenu() {
        JMenu helpMenu = new JMenu("Help");

        JMenuItem help = new JMenuItem("Quicktime Amateur Help");
        // XXX why doesn't this work?
        // Probably has something to do with this from InputEvent API doc:
        /* Not all characters have a keycode associated with them. 
         * For example, there is no keycode for the question mark 
         * because there is no keyboard for which it appears on the primary layer. */
        help.setAccelerator(KeyStroke.getKeyStroke('?', menuShortcutKeyMask));
        help.setEnabled(false);
        helpMenu.add(help);

        JMenuItem sendFeedback = new JMenuItem("Send Feedback");
        sendFeedback.setEnabled(false);
        helpMenu.add(sendFeedback);

        // XXX This should open the user's default web browser ponted to the the 
        // java.net bug report form
        JMenuItem reportBug = new JMenuItem("Report Bug");
        reportBug.setEnabled(false);
        helpMenu.add(reportBug);

        // XXX This should open the user's default web browser ponted to the the 
        // java.net RFE form
        JMenuItem requestFeature = new JMenuItem("Request Feature");
        requestFeature.setEnabled(false);
        helpMenu.add(requestFeature);

        // XXX Check how CyberDuck does htis with PayPal
        JMenuItem donate = new JMenuItem("Donate");
        donate.setEnabled(false);
        helpMenu.add(donate);

        this.add(helpMenu);
    }

    void addWindowMenuItem(PlayerFrame frame) {
        windowMenu.add(new WindowMenuItem(frame));      
    }
    
    void removeWindowMenuItem(PlayerFrame frame) {

        Component[] items = windowMenu.getMenuComponents();
        for (int i = items.length-1; i > NUMBER_OF_WINDOW_MENU_ITEMS; i--) {
            if (items[i] instanceof WindowMenuItem) {
                WindowMenuItem item = (WindowMenuItem) items[i];
                if (item.getFrame() == frame) {
                    windowMenu.remove(item);
                    break;   
                }    
            }
            else break;
        }
        
    }    
    private void initViewMenu() {
        
        JMenu viewMenu = new JMenu("View");
        
        JMenuItem halfsize = new JMenuItem("Half Size");
        halfsize.setAccelerator(KeyStroke.getKeyStroke('0', menuShortcutKeyMask));
        halfsize.addActionListener(new SizeListener(frame, 0.5));
        viewMenu.add(halfsize);
        
        JMenuItem fullsize = new JMenuItem("Full Size");
        fullsize.setAccelerator(KeyStroke.getKeyStroke('1', menuShortcutKeyMask));
        fullsize.addActionListener(new SizeListener(frame, 1.0));
        viewMenu.add(fullsize);
        
        JMenuItem doublesize = new JMenuItem("Double Size");
        doublesize.setAccelerator(KeyStroke.getKeyStroke('2', menuShortcutKeyMask));
        doublesize.addActionListener(new SizeListener(frame, 2.0));
        viewMenu.add(doublesize);
        
        JMenuItem triplesize = new JMenuItem("Triple Size");
        triplesize.setAccelerator(KeyStroke.getKeyStroke('3', menuShortcutKeyMask));
        triplesize.addActionListener(new SizeListener(frame, 3.0));
        viewMenu.add(triplesize);
        
        JMenuItem quadrupleSize = new JMenuItem("Quadruple Size");
        quadrupleSize.setAccelerator(KeyStroke.getKeyStroke('4', menuShortcutKeyMask));
        quadrupleSize.addActionListener(new SizeListener(frame, 4.0));
        viewMenu.add(quadrupleSize);
        
        JMenuItem fullScreen = new JMenuItem("Full Screen");
        fullScreen.setAccelerator(KeyStroke.getKeyStroke('F', menuShortcutKeyMask));
        fullScreen.addActionListener(new FullScreenListener(frame));
        viewMenu.add(fullScreen);
        
        viewMenu.addSeparator();
        
        JMenuItem presentMovie = new JMenuItem("Present Movie");
        presentMovie.setAccelerator(KeyStroke.getKeyStroke('F', menuShortcutKeyMask | InputEvent.SHIFT_MASK));
        presentMovie.setEnabled(false);
        viewMenu.add(presentMovie);
        
        viewMenu.addSeparator();
        
        JMenuItem loop = new JCheckBoxMenuItem("Loop");
        loop.setAccelerator(KeyStroke.getKeyStroke('L', menuShortcutKeyMask));        
        loop.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                MovieController controller = frame.getController();
                try {
                   if (controller.getLooping()) controller.setLooping(false);
                   else controller.setLooping(true);
                }
                catch (StdQTException e) {
                    // ???? Auto-generated catch block
                    e.printStackTrace();
                }
            }});
        viewMenu.add(loop);
        
        JMenuItem loopBackAndForth = new JCheckBoxMenuItem("Loop Back and Forth");
        loopBackAndForth.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                MovieController controller = frame.getController();
                try {
                    if (controller.getLoopIsPalindrome()) controller.setLoopIsPalindrome(false);
                    else controller.setLoopIsPalindrome(true);
                }
                catch (StdQTException e) {
                    // ???? Auto-generated catch block
                    e.printStackTrace();
                }
            }});
        viewMenu.add(loopBackAndForth);
        
        final JCheckBoxMenuItem playSelectionOnly = new JCheckBoxMenuItem("Play Selection Only");
        playSelectionOnly.setAccelerator(KeyStroke.getKeyStroke('T', menuShortcutKeyMask));
        playSelectionOnly.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
                   MovieController controller = frame.getController();
                    if (playSelectionOnly.isSelected()) {
                        controller.setPlaySelection(true);
                    }
                    else {
                       controller.setPlaySelection(false);
                    }
                }
                catch (QTException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        viewMenu.add(playSelectionOnly);
        
        final JCheckBoxMenuItem playAllFrames = new JCheckBoxMenuItem("Play All Frames");
        playAllFrames.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                MovieController controller = frame.getController();
                try {
                    if (playAllFrames.isSelected()) {
                        // Setting this mutes the sound. This appears to be the normal
                        // and expected behavior. See http://lists.apple.com/archives/quicktime-users/2003/Dec/msg00061.html
                        controller.setPlayEveryFrame(true);
                    }
                    else {
                        controller.setPlayEveryFrame(false);
                    }
                }
                catch (QTException ex) {
                    ex.printStackTrace();
                }
            }
            
            });
        viewMenu.add(playAllFrames);

        viewMenu.addSeparator();

        viewMenu.add(new PlayAllMoviesAction());

        viewMenu.addSeparator();
        viewMenu.add(new GoToPosterFrameAction(frame.getController()));
        viewMenu.add(new SetPosterFrameAction(frame.getController()));

        viewMenu.addSeparator();

        JMenuItem chooseLanguage = new JMenuItem("Choose Language...");
        chooseLanguage.setEnabled(false);
        viewMenu.add(chooseLanguage);

        add(viewMenu);
    }

    
}