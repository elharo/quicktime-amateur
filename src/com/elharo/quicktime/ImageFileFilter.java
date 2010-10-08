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

import java.io.File;
import java.io.FileFilter;

public class ImageFileFilter implements FileFilter {

    public static final ImageFileFilter INSTANCE = new ImageFileFilter();

    private ImageFileFilter() {}

    public boolean accept(File file) {
        // ???? check Mac types?

        String name = file.getName();
        if (name.endsWith(".jpg")) return true;
        if (name.endsWith(".gif")) return true;
        if (name.endsWith(".png")) return true;
        if (name.endsWith(".jpeg")) return true;
        if (name.endsWith(".pct")) return true;
        if (name.endsWith(".pict")) return true;
        if (name.endsWith(".psd")) return true;
        return false;
    }
}
