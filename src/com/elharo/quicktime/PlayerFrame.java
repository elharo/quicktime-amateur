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
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.ImageProducer;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Iterator;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import quicktime.QTException;
import quicktime.app.view.GraphicsImporterDrawer;
import quicktime.app.view.QTComponent;
import quicktime.app.view.QTFactory;
import quicktime.app.view.QTImageProducer;
import quicktime.qd.Pict;
import quicktime.qd.QDRect;
import quicktime.std.StdQTConstants;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;
import quicktime.std.movies.MovieEditState;
import quicktime.std.movies.media.DataRef;

// XXX Use Pageable instead of Printable
// XXX Move Pageable impl to a separate class
public final class PlayerFrame extends JFrame implements Printable {
    
    private static final int PICT_HEADER_SIZE = 512;
    private Movie movie;
    private MovieController controller;
    private int movieHeight = -1;
    private int movieWidth = -1;
    private Component c;
    private boolean fullScreen = false;
    private JMenu windowMenu;
    private UndoManager undoer = new UndoManager();
    private PrinterJob printerJob = PrinterJob.getPrinterJob();
    
    private final int CONTROL_BAR_HEIGHT;
    private int frameExtras;
    static final int menuShortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    private static final int NUMBER_OF_WINDOW_MENU_ITEMS = 10;

    public PlayerFrame() throws QTException {
        this("Amateur Player");
    }

    public PlayerFrame(String title) throws QTException {
        this(title, new Movie(StdQTConstants.newMovieActive));
        this.setSize(640, 480);
    }

    public PlayerFrame(String title, Movie m) throws QTException {
        super(title);
        this.movie = m;
        controller = new MovieController(m);
        CONTROL_BAR_HEIGHT = controller.getRequiredSize().getHeight();
        setupMenuBar();
        QTComponent qc = QTFactory.makeQTComponent(controller);
        c = qc.asComponent();
        this.getContentPane().add(c);
        this.pack();
        initMovieDimensions();
        this.controller.enableEditing(true);
        new MacOSHandler(this);
    }

    // XXX Logically I should split the menubar out into a separate class
    // and then InvisibleFrame can be a separate class too.
    // ???? Setting up invisible frames on the Mac would make a nice Java tip
    PlayerFrame(boolean invisible) {
        super("");
        this.setUndecorated(true);
        this.setSize(0, 0);
        CONTROL_BAR_HEIGHT = 0;
        setupMenuBar();
        this.pack();
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

        JMenuItem sendFeedback = new JMenuItem("Send Feedback");
        sendFeedback.setEnabled(false);
        helpMenu.add(sendFeedback);

        menubar.add(helpMenu);
    }

    private void initWindowMenu(JMenuBar menubar) {
        windowMenu = new JMenu("Window");
        
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

        windowMenu.add(new MovieInfoAction(movie));   
        
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
            this.windowMenu.add(new WindowMenuItem(frame));
        }        
        
        menubar.add(windowMenu);
    }
    
    public void show() {
        WindowList list = WindowList.INSTANCE;
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            PlayerFrame frame = (PlayerFrame) iterator.next();
            frame.windowMenu.add(new WindowMenuItem(this));
        }
        super.show();
    }
    

    public void hide() {
        super.hide();
        Iterator iterator = WindowList.INSTANCE.iterator();
        while (iterator.hasNext()) {
            PlayerFrame frame = (PlayerFrame) iterator.next();
            frame.removeWindowMenuItem(this);
        }        
    }

    private void removeWindowMenuItem(PlayerFrame frame) {

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
        
        JMenuItem triplesize = new JMenuItem("Triple Size");
        triplesize.setAccelerator(KeyStroke.getKeyStroke('3', menuShortcutKeyMask));
        triplesize.addActionListener(new SizeListener(3.0));
        viewMenu.add(triplesize);
        
        JMenuItem quadrupleSize = new JMenuItem("Quadruple Size");
        quadrupleSize.setAccelerator(KeyStroke.getKeyStroke('4', menuShortcutKeyMask));
        quadrupleSize.addActionListener(new SizeListener(4.0));
        viewMenu.add(quadrupleSize);
        
        JMenuItem fullScreen = new JMenuItem("Full Screen");
        fullScreen.setAccelerator(KeyStroke.getKeyStroke('F', menuShortcutKeyMask));
        fullScreen.addActionListener(new FullScreenListener(this));
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
        
        final JCheckBoxMenuItem playSelectionOnly = new JCheckBoxMenuItem("Play Selection Only");
        playSelectionOnly.setAccelerator(KeyStroke.getKeyStroke('T', menuShortcutKeyMask));
        playSelectionOnly.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
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

        viewMenu.add(new GoToPosterFrameAction(controller));
        viewMenu.add(new SetPosterFrameAction(controller));

        viewMenu.addSeparator();

        JMenuItem chooseLanguage = new JMenuItem("Choose Language...");
        chooseLanguage.setEnabled(false);
        viewMenu.add(chooseLanguage);

        menubar.add(viewMenu);
    }

    private void initEditMenu(JMenuBar menubar) {
        JMenu editMenu = new JMenu("Edit");
        
        editMenu.add(new UndoAction(this));
        editMenu.add(new RedoAction(this));
        
        editMenu.addSeparator();

        editMenu.add(new CutAction(this));
        editMenu.add(new CopyAction(movie));
        editMenu.add(new CopyCurrentFrameAction(this));
        editMenu.add(new PasteAction(this));
        editMenu.add(new ClearAction(this));
        
        editMenu.addSeparator();

        editMenu.add(new SelectAllAction(movie));
        editMenu.add(new SelectNoneAction(movie));
        
        editMenu.addSeparator();
        
        // XXX make this undoable
        editMenu.add(new TrimToSelectionAction(controller, this));
        
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
        
        fileMenu.add(new CloseAction(this));
        
        fileMenu.addSeparator();
        
        JMenuItem save = new JMenuItem("Save");
        save.setAccelerator(KeyStroke.getKeyStroke('S', menuShortcutKeyMask));        
        save.setEnabled(false);
        fileMenu.add(save);
        
        fileMenu.add(new SaveAsAction(movie));
        
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
        
        fileMenu.add(new PageSetupAction(printerJob));
        fileMenu.add(new PrintAction(printerJob, this));
        
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

        private JFrame frame;
        private JFrame fullScreenFrame;
        
        FullScreenListener(JFrame f) {
            this.frame = f;
        }
        
        private JFrame makeFullScreenFrame() {
            JFrame fullScreenFrame = new JFrame();
            fullScreenFrame.setUndecorated(true);
            fullScreenFrame.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent event) {
                    if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        exitFullScreen();
                    }
                }
            });
            return fullScreenFrame;
        }
        
        
        public void actionPerformed(ActionEvent event) {
            
            try {
                if (fullScreen) exitFullScreen();
                else {
                    GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                    Rectangle bounds = device.getDefaultConfiguration().getBounds();
                    
                    double widthRatio = bounds.width / (double) movieWidth;
                    double heightRatio = bounds.height / (double) movieHeight ;
                    
                    int fullScreenWidth = movieWidth;
                    int fullScreenHeight = movieHeight;
                    if (widthRatio < heightRatio) {
                        fullScreenWidth = (int) (movieWidth * widthRatio);
                        fullScreenHeight = (int) (movieHeight * widthRatio);
                    }
                    else {
                        fullScreenWidth = (int) (movieWidth * heightRatio);
                        fullScreenHeight = (int) (movieHeight * heightRatio);
                    }
                    
                    // need to center the fullscreen movie vertically or horizontally????
                    
                    frame.setVisible(false);
                    fullScreenFrame = makeFullScreenFrame();
                    device.setFullScreenWindow(fullScreenFrame);
                    frame.remove(c);
                    QTComponent qc = QTFactory.makeQTComponent(movie);
                    fullScreenFrame.getContentPane().add(qc.asComponent());
                    fullScreenFrame.setSize(fullScreenWidth, fullScreenHeight);
                    fullScreenFrame.setVisible(true);
                    movie.start();
                    fullScreen = true;
                }
            }
            catch (Exception ex) {
                // XXX do better
                ex.printStackTrace();
            }
        }

        private void exitFullScreen() {
            fullScreen = false;
            GraphicsEnvironment.getLocalGraphicsEnvironment().
              getDefaultScreenDevice().setFullScreenWindow(null);
            fullScreenFrame.setVisible(false);
            fullScreenFrame.remove(c);
            frame.getContentPane().add(c);
            fullScreenFrame = null;
            frame.setVisible(true);
            frame.toFront();
        }

    }

    public void play() throws StdQTException {
        if (movie != null) {
            controller.play(1.0f);
        }
    }
    
    
    public Dimension getPreferredSize() {

        try {
          QDRect controllerRect = controller.getBounds();
          Dimension componentPreferredSize = c.getPreferredSize( );
          Insets insets = this.getInsets();
          if (controllerRect.getHeight() > componentPreferredSize.height) {
              return new Dimension (
                controllerRect.getWidth() + insets.left + insets.right,
                controllerRect.getHeight() + insets.top + insets.bottom);
          } 
          else {
              return new Dimension (
                componentPreferredSize.width + insets.left + insets.right,
                componentPreferredSize.height + insets.top + insets.bottom);
          }
        } 
        catch (Exception ex) {
            return new Dimension (0,0);
        }
    }

    void undo() {
        if (undoer.canUndo()) {
            undoer.undo();
        }
        else {
            System.err.println("FIXME can't undo");
            return;
        }
    }
  

    void redo() {
        if (undoer.canRedo()) {
            undoer.redo();
        }
        else {
            System.err.println("FIXME can't redo");
            return;
        }
    }
  

    // ???? move to separate class
    class MovieEdit extends AbstractUndoableEdit {

        private MovieEditState oldState;
        private MovieEditState newState;
        private String name;
    
        MovieEdit(MovieEditState oldState, MovieEditState newState, String name) {
            this.oldState = oldState;
            this.newState = newState;
            this.name = name;
        }
    
        public String getPresentationName() {
            return name;
        }
    
        public void redo() throws CannotRedoException {
          super.redo();
          try {
              movie.useEditState(newState);
              controller.movieChanged();
          } 
          catch (QTException ex) {
              ex.printStackTrace();
          }
        }
    
        public void undo() throws CannotUndoException {
          super.undo();
          try {
              movie.useEditState(oldState);
              controller.movieChanged();
          } 
          catch (QTException ex) {
              ex.printStackTrace();
          }
        }
    
        public void die() {
          oldState = null;
          newState = null;
        }

    }

    void undoableCut() throws QTException {
        
        MovieEditState oldState = movie.newEditState();
        Movie cut = movie.cutSelection();
        MovieEditState newState = movie.newEditState();
        MovieEdit edit = new MovieEdit(oldState, newState, "Cut");
        undoer.addEdit(edit);
        cut.putOnScrap(0);
        controller.movieChanged();
        
    }
     
    void undoableClear() throws QTException {
        
        MovieEditState oldState = movie.newEditState();
        movie.clearSelection();
        MovieEditState newState = movie.newEditState();
        MovieEdit edit = new MovieEdit(oldState, newState, "Clear");
        undoer.addEdit(edit);
        controller.movieChanged();
        
    }
     
    void undoablePaste() throws QTException {
        
        MovieEditState oldState = movie.newEditState();
        Movie pasted = Movie.fromScrap(0);
        movie.pasteSelection(pasted);
        MovieEditState newState = movie.newEditState();
        MovieEdit edit = new MovieEdit(oldState, newState, "Paste");
        undoer.addEdit (edit);
        controller.movieChanged();
        this.pack();
        
    }
    
    Image getStill() throws QTException {

          boolean wasPlaying = false;
          if (movie.getRate() > 0) {
              movie.stop();
              wasPlaying = true;
          }

          Pict oldPict = movie.getPict(movie.getTime());
          int pictSize = oldPict.getSize();
          byte[] dataAndHeader = new byte[pictSize + PICT_HEADER_SIZE];
          oldPict.copyToArray(0, dataAndHeader, PICT_HEADER_SIZE, pictSize);
          Pict newPict = new Pict(dataAndHeader);

          GraphicsImporter importer = new GraphicsImporter(StdQTConstants.kQTFileTypePicture);
          DataRef ref = new DataRef(newPict, StdQTConstants.kDataRefQTFileTypeTag, "PICT");
          importer.setDataReference(ref);
          Dimension movieDim = new Dimension(movieWidth, movieHeight);
          GraphicsImporterDrawer drawer = new GraphicsImporterDrawer(importer);
          ImageProducer producer = new QTImageProducer(drawer, movieDim);
 
          Image image = getToolkit().createImage(producer);
          if (wasPlaying) movie.start();
          
          return image;
          
  }

    public int print(Graphics graphics, PageFormat format, int pageIndex) 
      throws PrinterException {
        try {
            // XXX need to handle movies bigger than the page
            Image image = getStill();
            graphics.drawImage(image, 0, 0, movieWidth, movieHeight, this);
            if (pageIndex == 0) return Printable.PAGE_EXISTS;
            else return Printable.NO_SUCH_PAGE;
        }
        catch (QTException e) {
            throw new PrinterException(e.getMessage());
        }
        
    }

    void addEdit(MovieEdit edit) {
        undoer.addEdit(edit);
    }    
    
}
