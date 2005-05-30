package com.elharo.quicktime;

import quicktime.QTException;

public class Main {

    public static void main(String[] args) throws QTException {
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Amateur");
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.showGrowBox", "true");
        QuicktimeInit.setup();
        PlayerFrame frame = new PlayerFrame();
        frame.show();
    }

}
