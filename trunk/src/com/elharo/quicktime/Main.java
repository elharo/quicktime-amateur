package com.elharo.quicktime;

import quicktime.QTException;

public class Main {

    public static void main(String[] args) throws QTException {
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Amateur");
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.showGrowBox", "true");
        QuicktimeInit.setup();
        PlayerFrame frame = new PlayerFrame();
        WindowList.INSTANCE.add(frame);
        // move this off the screen
        // first frame is just for menu bar
        frame.setSize(1, 1);
        frame.setLocation(10000, 10000);
        frame.show();
    }

}
