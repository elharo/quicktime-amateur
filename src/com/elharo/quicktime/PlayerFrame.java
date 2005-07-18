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
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.ImageProducer;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import quicktime.QTException;
import quicktime.app.view.GraphicsImporterDrawer;
import quicktime.app.view.QTComponent;
import quicktime.app.view.QTFactory;
import quicktime.app.view.QTImageProducer;
import quicktime.io.QTFile;
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
    
    static final int PICT_HEADER_SIZE = 512;
    private Movie movie;
    private MovieController controller;
    private int movieHeight = -1;
    private int movieWidth = -1;
    private double heightToWidth = 1.0;
    private Component c;
    private UndoManager undoer = new UndoManager();
    private QTFile file = null;
    
    private final int CONTROL_BAR_HEIGHT;
    private int frameExtras;
    // XXX remove this
    static final int menuShortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    private JFrame fullScreenFrame = null;

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
        controller.setActionFilter(new SelectionListener(this));
        CONTROL_BAR_HEIGHT = controller.getRequiredSize().getHeight();
        setupMenuBar();
        QTComponent qc = QTFactory.makeQTComponent(controller);
        c = qc.asComponent();
        this.getContentPane().add(c);
        this.pack();
        initMovieDimensions();
        this.controller.enableEditing(true);
        new MacOSHandler(this);
        
        this.addComponentListener( new ComponentAdapter() {
            
            public void componentResized(final ComponentEvent evt) {

                // Make this allow resizing vertically to increase the width as well
                int newWidth = evt.getComponent().getBounds().width;
                int newHeight = evt.getComponent().getBounds().height;

                final Rectangle widthBasedSize = new Rectangle(newWidth, (int) (newWidth * heightToWidth + frameExtras));
                final Rectangle heightBasedSize = new Rectangle((int) ((newHeight - frameExtras)/heightToWidth), newHeight);
                
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (widthBasedSize.height * widthBasedSize.width > heightBasedSize.height * heightBasedSize.width) {
                            setSize( widthBasedSize.width, widthBasedSize.height );
                        }
                        else {
                            setSize( heightBasedSize.width, heightBasedSize.height );
                        }                                 
                    }
                 } );
            }
        });

        
    }

    // XXX Logically I should split the menubar out into a separate class
    // and then InvisibleFrame can be a separate class too.
    // ???? Setting up invisible frames on the Mac would make a nice Java tip
    PlayerFrame(boolean invisible) {
        super("");
        this.setUndecorated(true);
        this.setSize(0, 0);
        CONTROL_BAR_HEIGHT = 0;
        JMenuBar menubar = new PlayerMenuBar(null);
        this.setJMenuBar(menubar);
        this.pack();
    }

    public PlayerFrame(Movie m) throws QTException {
        this("Amateur Player", m);
    }

    private void setupMenuBar() {
        JMenuBar menubar = new PlayerMenuBar(this);
        this.setJMenuBar(menubar);
    }

    
    public void show() {
        WindowList list = WindowList.INSTANCE;
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            PlayerFrame frame = (PlayerFrame) iterator.next();
            PlayerMenuBar menubar = (PlayerMenuBar) frame.getJMenuBar();
            menubar.addWindowMenuItem(this);
        }
        super.show();
    }
    

    public void hide() {
        super.hide();
        Iterator iterator = WindowList.INSTANCE.iterator();
        while (iterator.hasNext()) {
            PlayerFrame frame = (PlayerFrame) iterator.next();
            PlayerMenuBar menubar = (PlayerMenuBar) frame.getJMenuBar();
            menubar.removeWindowMenuItem(this);
        }        
    }

    
    private void initMovieDimensions() throws StdQTException {
        if (movieHeight == -1 || movieWidth == -1) {
            QDRect size = this.movie.getBox();
            this.movieHeight = size.getHeight();
            this.movieWidth = size.getWidth();
            this.heightToWidth = ((double) movieHeight) / movieWidth;
            this.frameExtras = getSize().height - movieHeight;
        }
    }



    public void play() throws StdQTException {
        if (movie != null) {
            controller.play(1.0f);
        }
    }
    
    private void exitFullScreen() {
        GraphicsEnvironment.getLocalGraphicsEnvironment().
          getDefaultScreenDevice().setFullScreenWindow(null);
        fullScreenFrame.setVisible(false);
        fullScreenFrame.remove(c);
        this.getContentPane().add(c);
        fullScreenFrame = null;
        this.setVisible(true);
        this.toFront();
    }


    
    
    private void enterFullScreen() {
        try {
            GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            Rectangle bounds = device.getDefaultConfiguration().getBounds();
            
            double widthRatio = bounds.width / (double) movieWidth;
            double heightRatio = bounds.height / (double) movieHeight ;
            
            int fullScreenWidth;
            int fullScreenHeight;
            int fullScreenX;
            int fullScreenY;
            if (widthRatio < heightRatio) {
                fullScreenWidth = (int) (movieWidth * widthRatio);
                fullScreenHeight = (int) (movieHeight * widthRatio);
            }
            else {
                fullScreenWidth = (int) (movieWidth * heightRatio);
                fullScreenHeight = (int) (movieHeight * heightRatio);
            }
            fullScreenY = (bounds.height - fullScreenHeight) / 2 ;
            fullScreenX = (bounds.width - fullScreenWidth) / 2;
            System.err.println(fullScreenX + " " + fullScreenY);
            
            this.setVisible(false);
            fullScreenFrame = makeFullScreenFrame();
            device.setFullScreenWindow(fullScreenFrame);
            this.remove(c);
            QTComponent qc = QTFactory.makeQTComponent(movie);
            fullScreenFrame.getContentPane().add(qc.asComponent());
            fullScreenFrame.setLocation(fullScreenX, fullScreenY);
            fullScreenFrame.setSize(fullScreenWidth, fullScreenHeight);
            fullScreenFrame.setVisible(true);
            movie.start();
        }
        catch (Exception ex) {
            // XXX do better
            ex.printStackTrace();
        }
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
        
        System.err.println(movie.getSelection().duration);
        System.err.println(movie.getSelection().time);
        MovieEditState oldState = movie.newEditState();
        movie.clearSelection();
        MovieEditState newState = movie.newEditState();
        MovieEdit edit = new MovieEdit(oldState, newState, "Clear");
        undoer.addEdit(edit);
        controller.movieChanged();
        
    }
     
    void undoablePaste() throws QTException {
        
        MovieEditState oldState = movie.newEditState();
        // XXX need to catch StdQTException with error code -102 here
        // in case user tries to paste something that isn't movie
        // Honestly if there's no movie on the clipboard we should just disable 
        // the Paste menu item 
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
    
    QTFile getFile() {
        return this.file;
    }
    
    void setFile(QTFile file) {
        this.file = file;
    }

    Movie getMovie() {
        return this.movie;
    }

    public void resize(double ratio) {
        int width = (int) (movieWidth * ratio);
        int height = (int) (movieHeight * ratio) + CONTROL_BAR_HEIGHT + frameExtras;
        setSize(width, height);
    }

    public void toggleFullScreenMode() {
        if (fullScreenFrame == null) this.enterFullScreen();
        else this.exitFullScreen();
    }

    MovieController getController() {
        return this.controller;
    }
    
}
