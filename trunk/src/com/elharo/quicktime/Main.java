/* Copyright 2005 Elliotte Rusty Harold

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

You can contact Elliotte Rusty Harold by sending e-mail to
elharo@metalab.unc.edu. Please include the word "Amateur" in the
subject line. The Amateur home page is located at http://www.elharo.com/amateur/
*/
package com.elharo.quicktime;

import java.awt.EventQueue;

import quicktime.*;

public class Main {

	public final static String OS = System.getProperty("os.name");
	public final static boolean isMac = (OS.indexOf("Mac") != -1);
	public final static boolean isWin = (OS.indexOf("Win") != -1);

    public static void main (String[] args) throws QTException {
		if (isMac) {
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Amateur");
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("apple.awt.showGrowBox", "true");
			System.setProperty("com.apple.eawt.CocoaComponent.CompatibilityMode", "false");
		}

		setupQuickTime();

        // the first frame is just for the menu bar
		PlayerFrame hidden = null;
		if (isMac) {
			hidden = new PlayerFrame(true);
		} else {
			hidden = new PlayerFrame();
		}
		hidden.setVisible(true);
    }

    private static void setupQuickTime() throws QTException {
		QTSession.open();

		Thread shutdownHook = new Thread() {
			public void run() {
				QTSession.close();
			}
		};
		Runtime.getRuntime().addShutdownHook(shutdownHook);
    }
}

