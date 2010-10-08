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

import java.io.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

class RecentFileList {

	static final long serialVersionUID = -5187479505814131385L;

    static LinkedHashMap<File, File> INSTANCE = new LinkedHashMap<File, File>() {
		@Override
		protected boolean removeEldestEntry (Map.Entry eldest) {
			return INSTANCE.size() > MAX_FILES;
		}
	};

    private final static int MAX_FILES = 10;

	static {
        loadRecentFiles();
    }

    static void add (File f) {
        if (INSTANCE.containsKey(f)) return;
        INSTANCE.put(f, f);
        RecentFileMenu.update();
    }

    static void storeRecentFiles() {
		int index = 0;
		for (File f : INSTANCE.values()) {
			Preferences.setValue("recentFiles."+index, escapeXML(f.getAbsolutePath()));
			index++;
		}
		Preferences.setValue("recentFiles.count", index);
/*
        File home = new File(System.getProperty("user.home"));
        File library = new File(home, "Library");
        File prefs = new File(library, "Preferences");
        if (prefs.exists()) {
            File prefxml = new File(prefs, "com.elharo.amateur.RecentFiles.xml");
            try {
                FileOutputStream out = new FileOutputStream(prefxml);
                OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
                writer.write("<?xml version='1.0'?>\r\n");
                writer.write("<RecentFiles>\r\n");
                Iterator iterator = this.iterator();
                while (iterator.hasNext()) {
                    File f = (File) iterator.next();
                    writer.write("  <File>");
                    writer.write(escapeXML(f.getAbsolutePath()));
                    writer.write("</File>\r\n");
                }
                writer.write("</RecentFiles>\r\n");
                writer.flush();
                writer.close();
            }
            catch (IOException e) {
                // ???? Auto-generated catch block
                e.printStackTrace();
            }
        }
*/
    }

    private static String escapeXML (String s) {
        // what about C0 characters????
        StringBuffer buffer = new StringBuffer(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '&':
                    buffer.append("&amp;");
                    break;
                case '<':
                    buffer.append("&lt;");
                    break;
                case '>':
                    buffer.append("&gt;");
                    break;
                default:
                    buffer.append(c);
            }
        }
        return buffer.toString();
    }

    static void loadRecentFiles() {
		int count = Preferences.getIntValue("recentFiles.count");
		for (int index=0; index<count; index++) {
			File recent = new File(Preferences.getStringValue("recentFiles."+index));
			if (recent.exists() && ! INSTANCE.containsKey(recent))
				INSTANCE.put(recent, recent);
		}
/*
        File home = new File(System.getProperty("user.home"));
        File library = new File(home, "Library");
        File prefs = new File(library, "Preferences");
        File prefxml = new File(prefs, "com.elharo.amateur.RecentFiles.xml");
        if (prefxml.exists()) {
            try {
                // could be quite a bit more robust????
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(prefxml);
                NodeList files = doc.getElementsByTagName("File");
                for (int i = 0; i < files.getLength(); i++) {
                    Node file = files.item(i);
                    Node child = file.getFirstChild();
                    String path = child.getNodeValue();
                    File recent = new File(path);
                    if (recent.exists() && ! containsKey(recent)) {
                        put(recent, recent);
                    }
                }
            }
            catch (IOException e) {
                // ???? Auto-generated catch block
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                // ???? Auto-generated catch block
                e.printStackTrace();
            }
            catch (ParserConfigurationException e) {
                // ???? Auto-generated catch block
                e.printStackTrace();
            }
            catch (SAXException e) {
                // ???? Auto-generated catch block
                e.printStackTrace();
            }
        }
*/
    }
}
