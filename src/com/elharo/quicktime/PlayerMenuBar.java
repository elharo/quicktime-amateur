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

import java.awt.Component;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.print.PrinterJob;
import java.util.Iterator;

import javax.swing.Action;
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
    private JMenu windowMenu, editMenu;

    private Action copyAction;
    private Action cutAction;
    private Action clearAction;
    private Action trimToSelectionAction;
    private Action avControls;

    PlayerMenuBar (PlayerFrame frame, boolean useBuiltinApplicationMenu) {
        this.frame = frame;
        initFileMenu(useBuiltinApplicationMenu);
        initEditMenu();
        initViewMenu();
        initWindowMenu();
        initHelpMenu(useBuiltinApplicationMenu);
    }

	// need to turn off edit menu for QTVR movies
	public void enableEditMenu (boolean canEdit) {
		editMenu.setEnabled(canEdit);
	}

    private void initFileMenu (boolean useBuiltinApplicationMenu) {
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
        fileMenu.add(new ImageSequenceOpener(frame));

        // XXX Use this to play all movies in a folder in order
        JMenuItem openMovieSequence = new JMenuItem("Open Movie Sequence...");
        openMovieSequence.setEnabled(false);
        fileMenu.add(openMovieSequence);

        fileMenu.add(new RecentFileMenu());

        Action closeAction = new CloseAction(frame);
        fileMenu.add(closeAction);

        fileMenu.addSeparator();

        fileMenu.add(new SaveAction(frame));

        if (frame != null) {
			fileMenu.add(new SaveAsAction(frame));
			fileMenu.add(new SaveAsAudioAction(frame, SaveAsAudioAction.AIFF));
			fileMenu.add(new SaveAsAudioAction(frame, SaveAsAudioAction.WAV));
        } else {
			fileMenu.add(new SaveAsAction(null));
			fileMenu.add(new SaveAsAudioAction(null, SaveAsAudioAction.AIFF));
			fileMenu.add(new SaveAsAudioAction(null, SaveAsAudioAction.WAV));
		}

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

        Action exportFramesAction = new ExportFramesAction(frame);
        fileMenu.add(exportFramesAction);

        fileMenu.addSeparator();

        PrinterJob printerJob = PrinterJob.getPrinterJob();
        Action pageSetupAction = new PageSetupAction(printerJob);
        if (frame == null) pageSetupAction.setEnabled(false);
        fileMenu.add(pageSetupAction);
        Action printAction = new PrintAction(printerJob, frame);
        if (frame == null) printAction.setEnabled(false);
        fileMenu.add(printAction);

		if (! Main.isMac) {
			fileMenu.addSeparator();
			JMenuItem quit = new JMenuItem("Exit");
			quit.setAccelerator(KeyStroke.getKeyStroke('X', menuShortcutKeyMask));
        	quit.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent event) {
					PlayerFrame.quit();
				}
			});
			fileMenu.add(quit);
		}

        this.add(fileMenu);
    }

    private void initEditMenu() {
        editMenu = new JMenu("Edit");

        editMenu.add(new UndoAction(frame));
        editMenu.add(new RedoAction(frame));

        editMenu.addSeparator();

        cutAction = new CutAction(frame);
        editMenu.add(cutAction);
        copyAction = new CopyAction(frame);
        editMenu.add(copyAction);
        editMenu.add(new CopyCurrentFrameAction(frame));
        editMenu.add(new PasteAction(frame));
        clearAction = new ClearAction(frame);
        editMenu.add(clearAction);

        editMenu.addSeparator();

        editMenu.add(new SelectAllAction(frame));
        editMenu.add(new SelectNoneAction(frame));

        editMenu.addSeparator();

        trimToSelectionAction = new TrimToSelectionAction(frame);
        editMenu.add(trimToSelectionAction);

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

		if (Main.isMac) {
			editMenu.addSeparator();

			editMenu.add(new SpecialCharactersAction());
		}

        add(editMenu);

        deselection();
    }

    // XXX cut. copy, and clear should always be enabled
    // and act on current frame if no selection is available
    void deselection() {
        this.copyAction.setEnabled(false);
        this.cutAction.setEnabled(false);
        this.clearAction.setEnabled(false);
        this.trimToSelectionAction.setEnabled(false);
    }

    void selection() {
        this.copyAction.setEnabled(true);
        this.cutAction.setEnabled(true);
        this.clearAction.setEnabled(true);
        this.trimToSelectionAction.setEnabled(true);
    }

    private void initWindowMenu() {
        windowMenu = new JMenu("Window");

        JMenuItem minimize = new JMenuItem("Minimize");
        minimize.setAccelerator(KeyStroke.getKeyStroke('M', menuShortcutKeyMask));
        if (frame == null) minimize.setEnabled(false);
        else {
            minimize.addActionListener(
              new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    frame.setState(Frame.ICONIFIED);
                }
              }
            );
        }
        windowMenu.add(minimize);

        JMenuItem zoom = new JMenuItem("Zoom");
        if (frame == null) zoom.setEnabled(false);
        else {
            zoom.addActionListener(
              new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    // XXX This needs to respect the ratio
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
              }
            );
        }
        windowMenu.add(zoom);

        windowMenu.addSeparator();

        windowMenu.add(new MovieInfoAction(frame));

        JMenuItem showMovieProperties = new JMenuItem("Show Movie Properties");
        showMovieProperties.setAccelerator(KeyStroke.getKeyStroke('J', menuShortcutKeyMask));
        showMovieProperties.setEnabled(false);
        windowMenu.add(showMovieProperties);

        // This is a little different than QuickTime Player.
        // We're disabling the menu item. They disable the individual controls.
        avControls = new AVControlsAction(frame);
        windowMenu.add(avControls);
        if (frame == null) avControls.setEnabled(false);

        windowMenu.add(new ShowContentGuideAction());

        windowMenu.addSeparator();

        JMenuItem favorites = new JMenuItem("Favorites");
        favorites.setEnabled(false);
        windowMenu.add(favorites);

        windowMenu.addSeparator();

        Action bringAllToFront = new BringAllToFrontAction();
        if (frame == null) bringAllToFront.setEnabled(false);
        windowMenu.add(bringAllToFront);

        windowMenu.addSeparator();

        WindowList list = WindowList.INSTANCE;
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            PlayerFrame frame = (PlayerFrame) iterator.next();
            windowMenu.add(new WindowMenuItem(frame));
        }

        add(windowMenu);
    }

    private void initHelpMenu (boolean useBuiltinApplicationMenu) {
        JMenu helpMenu = new JMenu("Help");

		if (! useBuiltinApplicationMenu) {
			JMenuItem about = new JMenuItem("About");
			about.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent event) {
					PlayerFrame.showAboutBox();
				}
			});
			helpMenu.add(about);

			JMenuItem options = new JMenuItem("Options...");
			options.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent event) {
					PlayerFrame.showPreferencesDialog();
				}
			});
			helpMenu.add(options);

			helpMenu.addSeparator();
		}

        JMenuItem help = new JMenuItem("Amateur Help");
        help.setAccelerator(KeyStroke.getKeyStroke(Character.valueOf('?'), menuShortcutKeyMask));
        help.setEnabled(false);
        helpMenu.add(help);

        helpMenu.add(new OpenURLAction("Send Feedback", "mailto:elharo@metalab.unc.edu?Subject=Amateur"));

         helpMenu.add(
          new OpenURLAction(
            "Show Known Issues",
            "https://amateur.dev.java.net/issues/buglist.cgi?issue_status=UNCONFIRMED&issue_status=NEW&issue_status=STARTED&issue_status=REOPENED"
          )
        );

        // Open the default web browser pointed to the java.net bug report form
        helpMenu.add(new OpenURLAction("Report Bug",
          "https://amateur.dev.java.net/issues/enter_bug.cgi?issue_type=DEFECT"));

        // Open the user's default web browser pointed to the java.net RFE form
        helpMenu.add(new OpenURLAction("Request Feature",
          "https://amateur.dev.java.net/issues/enter_bug.cgi?issue_type=FEATURE"));

        // XXX Should I just send all donations to MSF?
        helpMenu.add(new OpenURLAction("Donate",
          "https://www.paypal.com/xclick/business=elharo%40macfaq.com&item_name=Amateur&no_shipping=1&return=https%3A//amateur.dev.java.net/thankyou.html&currency_code=USD"));

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
        if (frame == null) viewMenu.setEnabled(false);

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

        Action presentMovieAction = new PresentMovieAction(frame);
		// disabling this menu, because it doesn't do anything right now
		presentMovieAction.setEnabled(false);
        viewMenu.add(presentMovieAction);

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

        final JCheckBoxMenuItem keepOnTop = new JCheckBoxMenuItem("Keep on Top");
		keepOnTop.addActionListener(new ActionListener() {
			private boolean alwaysOnTop = false;

			public void actionPerformed(ActionEvent event) {
				alwaysOnTop = ! alwaysOnTop;
				frame.setAlwaysOnTop(alwaysOnTop);
				keepOnTop.setState(alwaysOnTop);
			}
		});
        viewMenu.add(keepOnTop);

        final JCheckBoxMenuItem playSelectionOnly = new JCheckBoxMenuItem("Play Selection Only");
        playSelectionOnly.setAccelerator(KeyStroke.getKeyStroke('T', menuShortcutKeyMask));
        playSelectionOnly.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
                   MovieController controller = frame.getController();
                    if (playSelectionOnly.isSelected()) {
                        controller.setPlaySelection(true);
                    } else {
                       controller.setPlaySelection(false);
                    }
                } catch (QTException ex) {
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
                        // and expected behavior.
                        // See http://lists.apple.com/archives/quicktime-users/2003/Dec/msg00061.html
                        controller.setPlayEveryFrame(true);
                    } else {
                        controller.setPlayEveryFrame(false);
                    }
                } catch (QTException ex) {
                    ex.printStackTrace();
                }
            }
            });
        viewMenu.add(playAllFrames);

        viewMenu.addSeparator();

        viewMenu.add(new PlayAllMoviesAction());

        viewMenu.addSeparator();
        viewMenu.add(new GoToPosterFrameAction(frame));
        viewMenu.add(new SetPosterFrameAction(frame));

        viewMenu.addSeparator();

        JMenuItem chooseLanguage = new JMenuItem("Choose Language...");
        chooseLanguage.setEnabled(false);
        viewMenu.add(chooseLanguage);

        add(viewMenu);
    }
}
