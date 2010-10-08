package com.elharo.quicktime;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;

public class Preferences {

	private static java.util.prefs.Preferences prefRoot = null;

	static {
		prefRoot = java.util.prefs.Preferences.userNodeForPackage(Preferences.class);
//		currentDir = prefRoot.get("currentDir", null);
//		useAWTFileDialog = prefRoot.getBoolean("useAWTFileDialog", true);
	}

    public final static String AUTOMATICALLY_PLAY_MOVIES_WHEN_OPENED = "Automatically play movies when opened";
    public final static String OPEN_MOVIES_IN_NEW_PLAYERS = "Open movies in new players";
    public final static String NUMBER_OF_RECENT_ITEMS = "Number Of recent items";
    public final static String PAUSE_MOVIES_BEFORE_SWITCHING_USERS = "Pause movies before switching users";
    public final static String SHOW_CONTENT_GUIDE_AUTOMATICALLY = "Show Content Guide automatically";
    public final static String SHOW_EQUALIZER = "Show equalizer";
    public final static String PLAY_SOUND_WHEN_APPLICATION_IS_IN_BACKGROUND = "Play sound when application is in background";
    public final static String PLAY_SOUND_IN_FRONTMOST_PLAYER_ONLY = "Play sound in frontmost player only";
    public final static String USE_HIGH_QUALITY_VIDEO = "Use high quality video setting when available";
    public final static String USE_AWT_FILE_DIALOG = "Use AWT FileDialog instead of Swing JFileChooser";
    public final static String CURRENT_DIRECTORY = "Directory used for the last I/O operation";

/*
    private Preferences() {
        prefs.put(OPEN_MOVIES_IN_NEW_PLAYERS, Boolean.TRUE);
        prefs.put(USE_HIGH_QUALITY_VIDEO, Boolean.TRUE);
        prefs.put(PLAY_SOUND_IN_FRONTMOST_PLAYER_ONLY, Boolean.TRUE);
        prefs.put(SHOW_EQUALIZER, Boolean.TRUE);
        prefs.put(PLAY_SOUND_WHEN_APPLICATION_IS_IN_BACKGROUND, Boolean.TRUE);
        prefs.put(SHOW_CONTENT_GUIDE_AUTOMATICALLY, Boolean.FALSE);
        prefs.put(PAUSE_MOVIES_BEFORE_SWITCHING_USERS, Boolean.TRUE);
        prefs.put(NUMBER_OF_RECENT_ITEMS, new Integer(10));
        prefs.put(AUTOMATICALLY_PLAY_MOVIES_WHEN_OPENED, Boolean.FALSE);
        prefs.put(USE_AWT_FILE_DIALOG, Boolean.TRUE);
    }
*/
    public static boolean getBooleanValue (String key) {
        return prefRoot.getBoolean(key, true);
    }

    public static int getIntValue (String key) {
        return prefRoot.getInt(key, 0);
    }

    public static String getStringValue (String key) {
        return prefRoot.get(key, null);
    }

    public static void setValue (String key, boolean value) {
        prefRoot.putBoolean(key, value);
		flush();
    }

    public static void setValue (String key, int value) {
        prefRoot.putInt(key, value);
		flush();
	}

    public static void setValue (String key, String value) {
        prefRoot.put(key, value);
		flush();
	}

	private static void flush() {
		try {
			prefRoot.flush();
		} catch (BackingStoreException bsex) {
			System.err.println("problem saving preferences: " + bsex.getMessage());
		}
    }
}

