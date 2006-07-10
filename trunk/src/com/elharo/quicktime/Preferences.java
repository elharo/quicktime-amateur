package com.elharo.quicktime;

public class Preferences {

    private static Preferences instance = new Preferences();
    
    // XXX are these sticky over restarts? should they be?
    
    private Preferences() {}
    
    public static Preferences getInstance() {
        return instance;
    }

    public boolean getOpenMoviesInNewPlayers() {
        return true;
    }

    public boolean getUseHighQualityVideo() {
        return true;
    }

    public boolean getShowContentGuideAutomatically() {
        return false;
    }

    public boolean getPlaySoundInFrontmostPlayerOnly() {
        return true;
    }

    public boolean getPlaySoundWhenApplicationIsInBackground() {
        return true;
    }

    public int getNumberOfRecentItems() {
        return 10;
    }

    public boolean getPauseMoviesBeforeSwitchingPlayers() {
        return true;
    }

    public boolean getShowEqualizer() {
        return true;
    }

}
