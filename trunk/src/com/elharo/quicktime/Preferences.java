package com.elharo.quicktime;

import java.util.HashMap;
import java.util.Map;

public class Preferences {

    private static Preferences instance = new Preferences();

    
    private Map prefs = new HashMap();
    
    // XXX are these sticky over restarts? should they be?
    // defaults

    public final static String AUTOMATICALLY_PLAY_MOVIES_WHEN_OPENED = "Automatically play movies when opened";
    public final static String OPEN_MOVIES_IN_NEW_PLAYERS = "Open movies in new players";
    public final static String NUMBER_OF_RECENT_ITEMS = "Number Of recent items";
    public final static String PAUSE_MOVIES_BEFORE_SWITCHING_PLAYERS = "Pause movies before switching players";
    public final static String SHOW_CONTENT_GUIDE_AUTOMATICALLY = "Show content guide automatically";
    public final static String SHOW_EQUALIZER = "Show equalizer";
    public final static String PLAY_SOUND_WHEN_APPLICATION_IS_IN_BACKGROUND = "Play sound when application is in background";
    public final static String PLAY_SOUND_IN_FRONTMOST_PLAYER_ONLY = "Play sound in frontmost player only";
    public final static String USE_HIGH_QUALITY_VIDEO = "Use high quality video";
    
    
    
    private Preferences() {
        
        prefs.put(OPEN_MOVIES_IN_NEW_PLAYERS, Boolean.TRUE);
        prefs.put(USE_HIGH_QUALITY_VIDEO, Boolean.TRUE);
        prefs.put(PLAY_SOUND_IN_FRONTMOST_PLAYER_ONLY, Boolean.TRUE);
        prefs.put(SHOW_EQUALIZER, Boolean.TRUE);
        prefs.put(PLAY_SOUND_WHEN_APPLICATION_IS_IN_BACKGROUND, Boolean.TRUE);
        prefs.put(SHOW_CONTENT_GUIDE_AUTOMATICALLY, Boolean.FALSE);
        prefs.put(PAUSE_MOVIES_BEFORE_SWITCHING_PLAYERS, Boolean.TRUE);
        prefs.put(NUMBER_OF_RECENT_ITEMS, new Integer(10));
        prefs.put(AUTOMATICALLY_PLAY_MOVIES_WHEN_OPENED, Boolean.FALSE);
        
    }
    
    public static Preferences getInstance() {
        return instance;
    }
    
    public boolean getBooleanValue(String key) {
        Boolean result = (Boolean) prefs.get(key);
        // possible NullPointerException here????
        return result.booleanValue();
    }

    public int getIntValue(String key) {
        Integer result = (Integer) prefs.get(key);
        // possible NullPointerException here????
        return result.intValue();
    }

    public void setValue(String key, boolean value) {
        prefs.put(key, Boolean.valueOf(value));
    }

    public void setValue(String key, int value) {
        prefs.put(key, Integer.valueOf(value));
    }

}
