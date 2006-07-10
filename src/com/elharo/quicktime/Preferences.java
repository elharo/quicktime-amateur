package com.elharo.quicktime;

public class Preferences {

    private static Preferences instance = new Preferences();
    
    // XXX are these sticky over restarts? should they be?
    // defaults
    private boolean openMoviesInNewPlayers = true;
    private int     numberOfRecentItems = 10;
    private boolean pauseMoviesBeforeSwitchingPlayers = true;
    private boolean showContentGuideAutomatically = false;
    private boolean showEqualizer = true;
    private boolean playSoundWhenApplicationIsInBackground = true;
    private boolean playSoundInFrontmostPlayerOnly = true;
    private boolean useHighQualityVideo = true;
    
    
    private Preferences() {}
    
    public static Preferences getInstance() {
        return instance;
    }

    public boolean getOpenMoviesInNewPlayers() {
        return openMoviesInNewPlayers;
    }

    public boolean getUseHighQualityVideo() {
        return this.useHighQualityVideo;
    }

    public boolean getShowContentGuideAutomatically() {
        return this.showContentGuideAutomatically ;
    }

    public boolean getPlaySoundInFrontmostPlayerOnly() {
        return this.playSoundInFrontmostPlayerOnly;
    }

    public boolean getPlaySoundWhenApplicationIsInBackground() {
        return this.playSoundWhenApplicationIsInBackground;
    }

    public int getNumberOfRecentItems() {
        return this.numberOfRecentItems;
    }

    public boolean getPauseMoviesBeforeSwitchingPlayers() {
        return this.pauseMoviesBeforeSwitchingPlayers;
    }

    public boolean getShowEqualizer() {
        return this.showEqualizer;
    }

    public void setOpenMoviesInNewPlayers(boolean b) {
        this.openMoviesInNewPlayers = b;
    }

    public void setNumberOfRecentItems(int i) {
        this.numberOfRecentItems = i;
        
    }

    public void setPauseMoviesBeforeSwitchingPlayers(boolean b) {
        this.pauseMoviesBeforeSwitchingPlayers  = b;
        
    }

    public void setShowContentGuideAutomatically(boolean b) {
        this.showContentGuideAutomatically = b;
    }

    public void setShowEqualizer(boolean b) {
        this.showEqualizer  = b;
    }

    public void setPlaySoundWhenApplicationIsInBackground(boolean b) {
        this.playSoundWhenApplicationIsInBackground = b;
    }

    public void setPlaySoundInFrontmostPlayerOnly(boolean b) {
        this.playSoundInFrontmostPlayerOnly = b;
    }

    public void setUseHighQualityVideo(boolean b) {
        this.useHighQualityVideo = b;
    }

}
