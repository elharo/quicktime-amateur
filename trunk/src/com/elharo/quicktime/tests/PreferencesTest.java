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
        assertTrue(preferences.getOpenMoviesInNewPlayers());
    }
    
    public void testUseHighQualityVideo() {
        assertTrue(preferences.getUseHighQualityVideo());
    }
    
    public void testPlaySoundInFrontmostPlayerOnly() {
        assertTrue(preferences.getPlaySoundInFrontmostPlayerOnly());
    }
    
    public void testPlaySoundWhenApplicationIsInBackground() {
        assertTrue(preferences.getPlaySoundWhenApplicationIsInBackground());
    }
    
     public void testShowEqualizer() {
        assertTrue(preferences.getShowEqualizer());
    }
    
     public void testShowContentGuideAutomatically() {
        assertFalse(preferences.getShowContentGuideAutomatically());
    }
    
     public void testPauseMoviesBeforeSwitchingPlayers() {
        assertTrue(preferences.getPauseMoviesBeforeSwitchingPlayers());
    }
    
     public void testGetNumberOfRecentItems() {
        assertEquals(10, preferences.getNumberOfRecentItems());
    }
    
    
    
    
}
