package com.elharo.quicktime;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import quicktime.*;
import quicktime.app.view.*;
import quicktime.qd.GDevice;
import quicktime.qd.QDDimension;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.std.movies.*;
import javax.swing.*;

public final class PlayerFrame extends JFrame {
    
    private Movie movie;
    private MovieController controller;
    private int movieHeight = -1;
    private int movieWidth = -1;
    private Component c;
    private boolean fullScreen = false;
    
    private final static int CONTROL_BAR_HEIGHT = 16;
    // surely there's  way to get the height of the title bar programmatically????
    private int frameExtras;
    static final int menuShortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    public PlayerFrame() throws QTException {
        this("Amateur Player");
    }

    public PlayerFrame(String title) throws QTException {
        this(title, new Movie());
        this.setSize(640, 480);
    }

    public PlayerFrame(String title, Movie m) throws QTException {
        super(title);
        setupMenuBar();
        this.movie = m;
        controller = new MovieController(m);
        QTComponent qc = QTFactory.makeQTComponent(controller);
        c = qc.asComponent();
        this.getContentPane().add(c);
        this.pack();
        initMovieDimensions();
    }

    public PlayerFrame(Movie m) throws QTException {
        this("Amateur Player", m);
    }

    private void setupMenuBar() {
        JMenuBar menubar = new JMenuBar();
        initFileMenu(menubar);
        initEditMenu(menubar);
        initViewMenu(menubar);
        initWindowMenu(menubar);
        initHelpMenu(menubar);
        this.setJMenuBar(menubar);
    }
    
    private void initHelpMenu(JMenuBar menubar) {
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

        menubar.add(helpMenu);
    }

    private void initWindowMenu(JMenuBar menubar) {
        JMenu windowMenu = new JMenu("Window");
        
        JMenuItem minimize = new JMenuItem("Minimize");
        minimize.setAccelerator(KeyStroke.getKeyStroke('M', menuShortcutKeyMask));
        minimize.addActionListener(
          new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setState(Frame.ICONIFIED);
            }
          }
        );
        windowMenu.add(minimize);
        
        JMenuItem zoom = new JMenuItem("Zoom");
        zoom.addActionListener(
          new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
          }
        );
        windowMenu.add(zoom);
        
        windowMenu.addSeparator();

        JMenuItem showMovieInfo = new JMenuItem("Show Movie Info");
        showMovieInfo.setAccelerator(KeyStroke.getKeyStroke('I', menuShortcutKeyMask));
        showMovieInfo.setEnabled(false);
        windowMenu.add(showMovieInfo);                
        
        JMenuItem showMovieProperties = new JMenuItem("Show Movie Properties");
        showMovieProperties.setAccelerator(KeyStroke.getKeyStroke('J', menuShortcutKeyMask));
        showMovieProperties.setEnabled(false);
        windowMenu.add(showMovieProperties);                
        
        JMenuItem showAVControls = new JMenuItem("Show A/V Controls");
        showAVControls.setAccelerator(KeyStroke.getKeyStroke('K', menuShortcutKeyMask));
        showAVControls.setEnabled(false);
        windowMenu.add(showAVControls);                
        
        JMenuItem showContentGuide = new JMenuItem("Show Content Guide");
        showContentGuide.setAccelerator(KeyStroke.getKeyStroke('G', menuShortcutKeyMask | InputEvent.ALT_MASK));
        showContentGuide.setEnabled(false);
        windowMenu.add(showContentGuide);                
        
        windowMenu.addSeparator();

        JMenuItem favorites = new JMenuItem("Favorites");
        favorites.setEnabled(false);
        windowMenu.add(favorites);        
        
        windowMenu.addSeparator();
 
        JMenuItem bringAllToFront = new JMenuItem("Bring All To Front");
        bringAllToFront.setEnabled(false);
        windowMenu.add(bringAllToFront);
        
        menubar.add(windowMenu);
    }

    private void initViewMenu(JMenuBar menubar) {
        
        JMenu viewMenu = new JMenu("View");
        
        JMenuItem halfsize = new JMenuItem("Half Size");
        halfsize.setAccelerator(KeyStroke.getKeyStroke('0', menuShortcutKeyMask));
        halfsize.addActionListener(new SizeListener(0.5));
        viewMenu.add(halfsize);
        
        JMenuItem fullsize = new JMenuItem("Full Size");
        fullsize.setAccelerator(KeyStroke.getKeyStroke('1', menuShortcutKeyMask));
        fullsize.addActionListener(new SizeListener(1.0));
        viewMenu.add(fullsize);
        
        JMenuItem doublesize = new JMenuItem("Double Size");
        doublesize.setAccelerator(KeyStroke.getKeyStroke('2', menuShortcutKeyMask));
        doublesize.addActionListener(new SizeListener(2.0));
        viewMenu.add(doublesize);
        
        JMenuItem fullScreen = new JMenuItem("Full Screen");
        fullScreen.setAccelerator(KeyStroke.getKeyStroke('F', menuShortcutKeyMask));
        fullScreen.addActionListener(new FullScreenListener());
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
        
        JMenuItem playSelectionOnly = new JMenuItem("Play Selection Only");
        playSelectionOnly.setAccelerator(KeyStroke.getKeyStroke('T', menuShortcutKeyMask));
        playSelectionOnly.setEnabled(false);
        viewMenu.add(playSelectionOnly);
        
        JMenuItem playAllFrames = new JMenuItem("Play All Frames");
        playAllFrames.setEnabled(false);
        viewMenu.add(playAllFrames);

        viewMenu.addSeparator();

        JMenuItem playAllMovies = new JMenuItem("Play All Movies");
        playAllMovies.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, menuShortcutKeyMask));
        playAllMovies.setEnabled(false);
        viewMenu.add(playAllMovies);

        viewMenu.addSeparator();

        JMenuItem gotoPosterFrame = new JMenuItem("Go To Poster Frame");
        gotoPosterFrame.setEnabled(false);
        viewMenu.add(gotoPosterFrame);

        JMenuItem setPosterFrame = new JMenuItem("Set Poster Frame");
        setPosterFrame.setEnabled(false);
        viewMenu.add(setPosterFrame);

        viewMenu.addSeparator();

        JMenuItem chooseLanguage = new JMenuItem("Choose Language...");
        chooseLanguage.setEnabled(false);
        viewMenu.add(chooseLanguage);

        menubar.add(viewMenu);
    }

    private void initEditMenu(JMenuBar menubar) {
        JMenu editMenu = new JMenu("Edit");
        
        JMenuItem undo = new JMenuItem("Undo");
        undo.setEnabled(false);
        undo.setAccelerator(KeyStroke.getKeyStroke('Z', menuShortcutKeyMask, false));
        editMenu.add(undo);
        
        JMenuItem redo = new JMenuItem("Redo");
        redo.setEnabled(false);
        editMenu.add(redo);
        
        editMenu.addSeparator();

        JMenuItem cut = new JMenuItem("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke('X', menuShortcutKeyMask, false));
        cut.setEnabled(false);
        editMenu.add(cut);
        
        JMenuItem copy = new JMenuItem("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke('C', menuShortcutKeyMask, false));
        copy.setEnabled(false);
        editMenu.add(copy);
        
        JMenuItem paste = new JMenuItem("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke('V', menuShortcutKeyMask, false));
        paste.setEnabled(false);
        editMenu.add(paste);
        
        JMenuItem delete = new JMenuItem("Delete");
        delete.setEnabled(false);
        editMenu.add(delete);
        
        editMenu.addSeparator();

        JMenuItem selectAll = new JMenuItem("Select All");
        selectAll.setAccelerator(KeyStroke.getKeyStroke('A', menuShortcutKeyMask));
        selectAll.setEnabled(false);
        editMenu.add(selectAll);
        
        JMenuItem selectNone = new JMenuItem("Select None");
        selectNone.setAccelerator(KeyStroke.getKeyStroke('B', menuShortcutKeyMask, false));
        selectNone.setEnabled(false);
        editMenu.add(selectNone);
        
        editMenu.addSeparator();
        
        JMenuItem trimToSelection = new JMenuItem("Trim to Selection");
        trimToSelection.setEnabled(false);
        editMenu.add(trimToSelection);
        
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
        
        menubar.add(editMenu);
    }
    
    private void initMovieDimensions() throws StdQTException {
        if (movieHeight == -1 || movieWidth == -1) {
            QDRect size = this.movie.getBox();
            this.movieHeight = size.getHeight();
            this.movieWidth = size.getWidth();
            this.frameExtras = getSize().height - CONTROL_BAR_HEIGHT - movieHeight;
        }
    }

    private void initFileMenu(JMenuBar menubar) {
        
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
        
        JMenuItem openURL = new JMenuItem("Open URL...");
        openURL.setAccelerator(KeyStroke.getKeyStroke('U', menuShortcutKeyMask));        
        openURL.addActionListener(new URLOpener());
        fileMenu.add(openURL);
        
        JMenuItem openImageSequence = new JMenuItem("Open Image Sequence...");
        openImageSequence.setAccelerator(KeyStroke.getKeyStroke('O', menuShortcutKeyMask | InputEvent.SHIFT_MASK));        
        openImageSequence.setEnabled(false);
        fileMenu.add(openImageSequence);
        
        JMenuItem openRecent = new JMenuItem("Open Recent");
        openRecent.setEnabled(false);
        fileMenu.add(openRecent);
        
        JMenuItem close = new JMenuItem("Close");
        close.setAccelerator(KeyStroke.getKeyStroke('W', menuShortcutKeyMask));        
        close.setEnabled(false);
        fileMenu.add(close);
        
        fileMenu.addSeparator();
        
        JMenuItem save = new JMenuItem("Save");
        save.setAccelerator(KeyStroke.getKeyStroke('S', menuShortcutKeyMask));        
        save.setEnabled(false);
        fileMenu.add(save);
        
        JMenuItem saveAs = new JMenuItem("Save As...");
        saveAs.setAccelerator(KeyStroke.getKeyStroke('S', menuShortcutKeyMask | InputEvent.SHIFT_MASK));        
        saveAs.setEnabled(false);
        fileMenu.add(saveAs);
        
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
        
        fileMenu.addSeparator();
        
        JMenuItem pageSetup = new JMenuItem("Page Setup...");
        pageSetup.setAccelerator(KeyStroke.getKeyStroke('P', menuShortcutKeyMask | InputEvent.SHIFT_MASK));        
        pageSetup.setEnabled(false);
        fileMenu.add(pageSetup);
        
        JMenuItem print = new JMenuItem("Print");
        print.setAccelerator(KeyStroke.getKeyStroke('P', menuShortcutKeyMask));        
        print.setEnabled(false);
        fileMenu.add(print);
        
        menubar.add(fileMenu);
    }

    private class SizeListener implements ActionListener {
    
        private double ratio;
    
        SizeListener(double ratio) {
            this.ratio = ratio;
        }
    
        public void actionPerformed(ActionEvent event) {
            int width = (int) (movieWidth * ratio);
            int height = (int) (movieHeight * ratio) + CONTROL_BAR_HEIGHT + frameExtras;
            setSize(width, height);
        }

    }
    
    private class FullScreenListener implements ActionListener {
    
        private FullScreen f = new FullScreen();
        
        public void actionPerformed(ActionEvent event) {
            if (fullScreen) {
                fullScreen = false;
                try {
                    f.endFullScreen();
                }
                catch (StdQTException e) {
                    // ???? Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else {
                
                try {
                    f.beginFullScreen(GDevice.get(), movieWidth, movieHeight, 0); // int constants????
                    // XXX need to maintain ratio
                    fullScreen = true;
                }
                catch (StdQTException e) {
                    // ???? Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    
}
