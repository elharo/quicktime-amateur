/* Copyright 2005, 2006 Elliotte Rusty Harold

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

import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageProducer;
import java.awt.print.*;
import java.lang.reflect.*;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.undo.*;

import quicktime.QTException;
import quicktime.app.view.*;
import quicktime.io.QTFile;
import quicktime.qd.Pict;
import quicktime.qd.QDRect;
import quicktime.std.StdQTConstants;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.*;
import quicktime.std.movies.media.*;

// XXX Use Pageable instead of Printable
// XXX Move Pageable impl to a separate class

public final class PlayerFrame extends JFrame implements Printable {

    public static final int PICT_HEADER_SIZE = 512;
    private Movie movie;
    private MovieController controller;
	private boolean canEdit = false;
    private int movieHeight = -1;
    private int movieWidth = -1;
    private double heightToWidth = 1.0;
    private Component c;
    private UndoManager undoer = new UndoManager();
    private QTFile file = null;
	private static JDialog about, preferences;
	private static InfoDialog info;

    private final int controlBarHeight;
    private int frameExtras = -1;

    private JFrame fullScreenFrame = null;
    private boolean fullScreen;
	static private boolean useBuiltinApplicationMenu = false;
    // XXX remove this
    static final int menuShortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    public PlayerFrame() throws QTException {
        this("Amateur Player");
        this.addWindowListener(new ActivationWatcher(this));
    }

    public PlayerFrame (String title) throws QTException {
        this(title, new Movie(StdQTConstants.newMovieActive));
        this.setSize(640, 480);
    }

    public PlayerFrame (Movie m) throws QTException {
        this("Amateur Player", m);
    }

    public PlayerFrame (String title, Movie movie) throws QTException {
        super(title);
        this.movie = movie;
        controller = new MovieController(movie);
		try {
			controller.enableEditing(true);
			canEdit = true;
		} catch (StdQTException qtex) {
			// can happen for QTVR movies
		}
        controller.setActionFilter(new ActionListener(this));
        controlBarHeight = controller.getRequiredSize().getHeight();
        setupMenuBar();
        QTComponent qc = QTFactory.makeQTComponent(controller);
        c = qc.asComponent();
        this.getContentPane().add(c);
        this.pack();
        initMovieDimensions();

        this.addWindowListener(new ActivationWatcher(this));

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(final ComponentEvent evt) {
                // Allow all sizes when there's no movie
                if (movieHeight <= 0 || movieWidth <= 0) return;

                // Make this allow resizing vertically to increase the width as well
                int newWidth = evt.getComponent().getBounds().width;
                int newHeight = evt.getComponent().getBounds().height;

                final Rectangle widthBasedSize = new Rectangle(newWidth, (int) (newWidth * heightToWidth + frameExtras));
                final Rectangle heightBasedSize = new Rectangle((int) ((newHeight - frameExtras)/heightToWidth), newHeight);

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (widthBasedSize.height * widthBasedSize.width > heightBasedSize.height * heightBasedSize.width) {
                            setSize(widthBasedSize.width, widthBasedSize.height);
                        } else {
                            setSize(heightBasedSize.width, heightBasedSize.height);
                        }
                        invalidate();
                        c.repaint();
                    }
				});
            }
        });
    }

    // XXX Logically I should split the menubar out into a separate class
    // and then InvisibleFrame can be a separate class too.
    // ???? Setting up invisible frames on the Mac would make a nice Java tip
    PlayerFrame (boolean invisible) {
        super("");
        this.setUndecorated(true);
        this.setSize(0, 0);
        controlBarHeight = 0;

		// For MacOS X use the apple.eawt extensions to support standard menus and behaviours
		// Sets useBuiltinApplicationMenu (so this code must appear early in the constructor).
		try {
			Class	AppClass = Class.forName("com.apple.eawt.Application");
			Class	AppListenerClass = Class.forName("com.apple.eawt.ApplicationListener");
			Class	AppAdapterClass = Class.forName("com.apple.eawt.ApplicationAdapter");
			Method	AddMethod = AppClass.getMethod("addApplicationListener", new Class[] {AppListenerClass});
			Method	GetAppMethod = AppClass.getMethod("getApplication", new Class[] {});
			Object	fApplication = GetAppMethod.invoke(AppClass, new Object[] {});

			Method	EnablePrefMethod = AppClass.getMethod("setEnabledPreferencesMenu", new Class[] {boolean.class});
			EnablePrefMethod.invoke(fApplication, new Object[] {Boolean.TRUE});

			Object	fApplicationAdapter = Proxy.newProxyInstance(AppAdapterClass.getClassLoader(), 
						AppAdapterClass.getInterfaces(), new OSXApplicationHandler());
			AddMethod.invoke(fApplication, new Object[] {fApplicationAdapter});
			useBuiltinApplicationMenu = true;
		} catch (Throwable e) {
			useBuiltinApplicationMenu = false;
		}

        JMenuBar menubar = new PlayerMenuBar(null, useBuiltinApplicationMenu);
        this.setJMenuBar(menubar);
        this.pack();
    }

    private void setupMenuBar() {
        PlayerMenuBar menubar = new PlayerMenuBar(this, useBuiltinApplicationMenu);
		menubar.enableEditMenu(canEdit);
        this.setJMenuBar(menubar);
    }

	@Override
    public void setVisible (boolean show) {
		if (show) {
			Iterator iterator = WindowList.INSTANCE.iterator();
			while (iterator.hasNext()) {
				PlayerFrame frame = (PlayerFrame) iterator.next();
				PlayerMenuBar menubar = (PlayerMenuBar) frame.getJMenuBar();
				menubar.addWindowMenuItem(this);
			}
			super.setVisible(true);
		} else {
			try {
				movie.stop();
			} catch (Exception e) {
				// OK. It will stop later. Or there may not be a movie.
			}
			super.setVisible(false);
			Iterator iterator = WindowList.INSTANCE.iterator();
			while (iterator.hasNext()) {
				PlayerFrame frame = (PlayerFrame) iterator.next();
				PlayerMenuBar menubar = (PlayerMenuBar) frame.getJMenuBar();
				menubar.removeWindowMenuItem(this);
			}
		}
    }

	@Override
	public void dispose() {
		super.dispose();
		if (info != null)
			info.dispose();
		try {
			controller.removeActionFilter();
		} catch (StdQTException qtex) {
			// That's OK.
		}
	}

	public void showInfoDialog() {
		if (info == null) {
			info = new InfoDialog(this);
			info.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		}
		info.setVisible(true);
	}

	public void updateInfoContent() {
		if (info != null)
			info.updateContent();
	}

    private void initMovieDimensions() throws StdQTException {
        if (movieHeight <= 0 || movieWidth <= 0) {
            QDRect size = this.movie.getBox();
            this.movieHeight = size.getHeight();
            this.movieWidth = size.getWidth();
            this.heightToWidth = ((double) movieHeight) / movieWidth;
            if (this.frameExtras == -1) {
                this.frameExtras = getSize().height - movieHeight;
            }
        }
    }

    public void play() throws StdQTException {
        if (movie != null) {
            controller.play(1.0f);
        }
    }

    boolean isFullScreen() {
        return this.fullScreen;
    }

    private void exitFullScreen() {
        GraphicsEnvironment.getLocalGraphicsEnvironment().
          getDefaultScreenDevice().setFullScreenWindow(null);
        fullScreenFrame.setVisible(false);
        fullScreenFrame.dispose();
        fullScreenFrame = null;
        this.fullScreen = false;
        this.setVisible(true);
        this.toFront();
        try {
            movie.start();
        } catch (StdQTException e) {
            // ???? Auto-generated catch block
            e.printStackTrace();
        }
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
            } else {
                fullScreenWidth = (int) (movieWidth * heightRatio);
                fullScreenHeight = (int) (movieHeight * heightRatio);
            }
            fullScreenY = (bounds.height - fullScreenHeight) / 2 ;
            fullScreenX = (bounds.width - fullScreenWidth) / 2;

            this.fullScreen = true;

            fullScreenFrame = makeFullScreenFrame();
            device.setFullScreenWindow(fullScreenFrame);
            QTComponent qc = QTFactory.makeQTComponent(movie);
            fullScreenFrame.getContentPane().add(qc.asComponent());
            fullScreenFrame.setLocation(fullScreenX, fullScreenY);
            fullScreenFrame.setSize(fullScreenWidth, fullScreenHeight);
            fullScreenFrame.setVisible(true);
            this.setVisible(false);
            movie.start();
        } catch (Exception ex) {
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

    void mute() throws StdQTException {
        if (movie.getVolume() > 0) movie.setVolume(-movie.getVolume());
    }

    void unmute() throws StdQTException {
       if (movie.getVolume() < 0) movie.setVolume(-movie.getVolume());
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
          } else {
              return new Dimension (
                componentPreferredSize.width + insets.left + insets.right,
                componentPreferredSize.height + insets.top + insets.bottom);
          }
        } catch (Exception ex) {
            return new Dimension (0,0);
        }
    }

    void undo() {
        if (undoer.canUndo()) {
            undoer.undo();
        } else {
            System.err.println("FIXME can't undo");
            return;
        }
    }

    void redo() {
        if (undoer.canRedo()) {
            undoer.redo();
        } else {
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
          } catch (QTException ex) {
              ex.printStackTrace();
          }
        }

        public void undo() throws CannotUndoException {
          super.undo();
          try {
              movie.useEditState(oldState);
              controller.movieChanged();
          } catch (QTException ex) {
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
		Movie pasted = Movie.fromScrap(0);
		movie.pasteSelection(pasted);
		MovieEditState newState = movie.newEditState();
		MovieEdit edit = new MovieEdit(oldState, newState, "Paste");
		undoer.addEdit(edit);
		this.initMovieDimensions();
		controller.movieChanged();
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
 
 	protected static void print (PlayerFrame which) {
		PrinterJob pj = PrinterJob.getPrinterJob();
		pj.setPrintable(which);
		if (pj.printDialog()) {
			try {
				pj.print();
			} catch (PrinterException pe) {
				System.err.println(pe.getMessage());
			}
		}
	}

    public int print (Graphics graphics, PageFormat format, int pageIndex) throws PrinterException {
        try {
            // XXX need to handle movies bigger than the page
            Image image = getStill();
            graphics.drawImage(image, 0, 0, movieWidth, movieHeight, this);
            if (pageIndex == 0)
				return Printable.PAGE_EXISTS;
            else return
				Printable.NO_SUCH_PAGE;
        } catch (QTException e) {
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
        int height = (int) (movieHeight * ratio) + controlBarHeight + frameExtras;
        setSize(width, height);
    }

    public void toggleFullScreenMode() {
        if (fullScreenFrame == null) {
            this.enterFullScreen();
        }
        else this.exitFullScreen();
    }

    MovieController getController() {
        return this.controller;
    }

    public void setVolume(float value) throws StdQTException {
        this.movie.setVolume(value);
    }

    public void setSpeed(float value) throws StdQTException {
        this.movie.setRate(value);
    }

    public void setBalance(float value) throws QTException {
        AudioMediaHandler handler = getAudioMediaHandler();
        handler.setBalance(value);
    }

    // should I cache this result????
    private AudioMediaHandler getAudioMediaHandler() throws QTException {
        for (int i=1; i <= movie.getTrackCount(); i++) {
            Track track = movie.getTrack(i);
            Media media = track.getMedia();
            MediaHandler handler = media.getHandler();
            if (handler instanceof AudioMediaHandler) {
                return (AudioMediaHandler) handler;
            }
        }
        return null;
    }

    public void setTreble(int value) throws QTException {
        AudioMediaHandler handler = getAudioMediaHandler();
        int[] bassAndTreble = handler.getSoundBassAndTreble();
        handler.setSoundBassAndTreble(bassAndTreble[0], value);
    }

    public void setBass(int value) throws QTException {
        AudioMediaHandler handler = getAudioMediaHandler();
        int[] bassAndTreble = handler.getSoundBassAndTreble();
        handler.setSoundBassAndTreble(value, bassAndTreble[1]);
    }

	protected static void showAboutBox() {
		if (about == null)
			 about = new AboutDialog(new JFrame());

		about.setVisible(true);
	}

	protected static void showPreferencesDialog() {
		if (preferences == null)
			 preferences = new PreferencesDialog(new JFrame());

		preferences.setVisible(true);
	}

	protected static void quit() {
		Iterator iterator = WindowList.INSTANCE.iterator();
		while (iterator.hasNext()) {
			Frame next = (Frame) iterator.next();
			// should I dispose child windows here too????
			next.setVisible(false);
			next.dispose();
		}

		RecentFileList.storeRecentFiles();
		// XXX could fix this by setting the hidden frame to exit on close
		// and then closing it instead
		System.exit(0);
	}
}

