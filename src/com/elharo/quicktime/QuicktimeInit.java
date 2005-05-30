package com.elharo.quicktime;

import quicktime.*;

public class QuicktimeInit {

    private static QuicktimeInit singleton;
    
    private QuicktimeInit( ) throws QTException {
        QTSession.open();
    }
  
    public static void setup() throws QTException {
      
      if (singleton == null) {
          singleton = new QuicktimeInit();
          Thread shutdownHook = new Thread() {
            public void run() {
                QTSession.close();
            }
         };
         Runtime.getRuntime().addShutdownHook(shutdownHook);
      }
      
    }

}