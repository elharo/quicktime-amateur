package com.elharo.quicktime;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SizeListener implements ActionListener {
    
    private double ratio;
    private PlayerFrame frame;

    SizeListener(PlayerFrame frame, double ratio) {
        this.ratio = ratio;
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent event) {
        frame.resize(ratio);
    }

}
    
