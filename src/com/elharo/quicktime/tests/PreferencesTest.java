package com.elharo.quicktime.tests;

import com.elharo.quicktime.Preferences;

import junit.framework.TestCase;

public class PreferencesTest extends TestCase {

    
    private Preferences preferences = Preferences.getInstance();
    
    public void testNotNull() {
        assertNotNull(preferences);
    }   
    
    public void testSingleton() {
        assertTrue(Preferences.getInstance() == Preferences.getInstance());
    }
    
    // we're testing initial defaults here. Does this make this order dependent????
    public void testOpenMoviesInNewPlayers() {
        assertTrue(preferences.getBooleanValue(Preferences.OPEN_MOVIES_IN_NEW_PLAYERS));
    }
    
    public void testUseHighQualityVideo() {
        assertTrue(preferences.getBooleanValue(Preferences.USE_HIGH_QUALITY_VIDEO));
    }
    
    public void testPlaySoundInFrontmostPlayerOnly() {
        assertTrue(preferences.getBooleanValue(Preferences.PLAY_SOUND_IN_FRONTMOST_PLAYER_ONLY));
    }
    
    public void testPlaySoundWhenApplicationIsInBackground() {
        assertTrue(preferences.getBooleanValue(Preferences.PLAY_SOUND_WHEN_APPLICATION_IS_IN_BACKGROUND));
    }
    
     public void testShowEqualizer() {
        assertTrue(preferences.getBooleanValue(Preferences.SHOW_EQUALIZER));
    }
    
     public void testShowContentGuideAutomatically() {
        assertFalse(preferences.getBooleanValue(Preferences.SHOW_CONTENT_GUIDE_AUTOMATICALLY));
    }
    
     public void testPauseMoviesBeforeSwitchingPlayers() {
        assertTrue(preferences.getBooleanValue(Preferences.PAUSE_MOVIES_BEFORE_SWITCHING_PLAYERS));
    }
    
     public void testGetNumberOfRecentItems() {
        assertEquals(10, preferences.getIntValue(Preferences.NUMBER_OF_RECENT_ITEMS));
    }
    
    public void testSetOpenMoviesInNewPlayers() {
        preferences.setValue(Preferences.OPEN_MOVIES_IN_NEW_PLAYERS, false);
        assertFalse(preferences.getBooleanValue(Preferences.OPEN_MOVIES_IN_NEW_PLAYERS));
    }
    
    public void testSetUseHighQualityVideo() {
        preferences.setValue(Preferences.USE_HIGH_QUALITY_VIDEO, false);
        assertFalse(preferences.getBooleanValue(Preferences.USE_HIGH_QUALITY_VIDEO));
    }
    
    public void testSetPlaySoundInFrontmostPlayerOnly() {
        preferences.setValue(Preferences.PLAY_SOUND_IN_FRONTMOST_PLAYER_ONLY, false);
        assertFalse(preferences.getBooleanValue(Preferences.PLAY_SOUND_IN_FRONTMOST_PLAYER_ONLY));
    }
    
    public void testSetPlaySoundWhenApplicationIsInBackground() {
        preferences.setValue(Preferences.PLAY_SOUND_WHEN_APPLICATION_IS_IN_BACKGROUND, false);
        assertFalse(preferences.getBooleanValue(Preferences.PLAY_SOUND_WHEN_APPLICATION_IS_IN_BACKGROUND));
    }
    
     public void testSetShowEqualizer() {
        preferences.setValue(Preferences.SHOW_EQUALIZER, false);
        assertFalse(preferences.getBooleanValue(Preferences.SHOW_EQUALIZER));
    }
    
     public void testSetShowContentGuideAutomatically() {
        preferences.setValue(Preferences.SHOW_CONTENT_GUIDE_AUTOMATICALLY, true);
        assertTrue(preferences.getBooleanValue(Preferences.SHOW_CONTENT_GUIDE_AUTOMATICALLY));
    }
    
     public void testSetPauseMoviesBeforeSwitchingPlayers() {
        preferences.setValue(Preferences.PAUSE_MOVIES_BEFORE_SWITCHING_PLAYERS, true);
        assertTrue(preferences.getBooleanValue(Preferences.PAUSE_MOVIES_BEFORE_SWITCHING_PLAYERS));
    }
    
     public void testSetNumberOfRecentItems() {
        preferences.setValue(Preferences.NUMBER_OF_RECENT_ITEMS, 5);
        assertEquals(5, preferences.getIntValue(Preferences.NUMBER_OF_RECENT_ITEMS));
    }
    
        
    
    
}
