package com.elharo.quicktime;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

class ImageSequenceDialog extends JDialog {

    private JComboBox fps = new JComboBox();
    private JButton ok = new JButton("OK");
    
    ImageSequenceDialog(Frame parent) {
        super(parent, true);
        this.setTitle("Image Sequence");
        this.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
        this.getContentPane().add(new JLabel("Frame Rate: "));
        
        fps.addItem(new FPS(60));
        fps.addItem(new FPS(50));
        fps.addItem(new FPS(30));
        fps.addItem(new FPS(29.97));
        fps.addItem(new FPS(25));
        fps.addItem(new FPS(24));
        fps.addItem(new FPS(23.976));
        fps.addItem(new FPS(15));
        fps.addItem(new FPS(12));
        fps.addItem(new FPS(10));
        fps.addItem(new FPS(3));
        fps.addItem(new FPS(2));
        fps.addItem(new FPS(1));
        fps.addItem(new FPS(0.5));
        fps.addItem(new FPS(1.0/3));
        fps.addItem(new FPS(0.25));
        fps.addItem(new FPS(0.20));
        fps.addItem(new FPS(0.10));
        fps.addItem(new FPS(1.0/30));
        fps.addItem(new FPS(1.0/60));
        
        this.getContentPane().add(fps);
        this.getContentPane().add(ok);
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
            
        });
        this.pack();
    }
    
    
    double getRate() {
        FPS rate = (FPS) fps.getSelectedItem();
        return rate.rate;
    }
    
    private static class FPS {
     
        private double rate;
        
        FPS(double rate) {
            this.rate = rate;
        }
        
        public String toString() {
            if (rate < 1) return 1.0/rate + " seconds per frame";
            return rate + " frames per second";
        }
        
    }
    
}
