/*
 * Created on May 31, 2005
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.elharo.quicktime;

import java.util.ArrayList;
import java.util.List;

class WindowList {

    private List windows = new ArrayList();
    
    void add(PlayerFrame frame) {
        windows.add(frame);
    }
    
    private WindowList() {}
    
    static WindowList INSTANCE = new WindowList();
    
    
}
