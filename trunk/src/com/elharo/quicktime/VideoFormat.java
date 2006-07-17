package com.elharo.quicktime;

import java.util.HashMap;

import quicktime.util.QTUtils;


/**
 * See https://developer.apple.com/documentation/QuickTime/QTFF/QTFFChap3/chapter_4_section_3.html
 * especially the table at the bottom
 */
public class VideoFormat {

    private static HashMap codes = new HashMap();
    
    // XXX Use singleton instead
    // XXX duplicates SoundFormat, refactor
    static {
        codes.put(new Integer(0x53565133), new VideoFormat(0x53565133, "", "Sorenson Video 3 Decompressor"));
        codes.put(new Integer(0x6d703476), new VideoFormat(0x6d703476, "Apple MPEG4 Decompressor", "Apple MPEG 4 Decompressor"));
    }
    
    private int code;
    private String description;
    private String shortDescription;
    
    private VideoFormat(String code, String description, String shortDescription) {
        this(QTUtils.toOSType(code), description, shortDescription);
    }

    private VideoFormat(int code, String description, String shortDescription) {
        this.code = code;
        this.description = description;
        this.shortDescription = shortDescription;
        codes.put(new Integer(code), this);
    }

    public static String getShortDescription(int code) {
        VideoFormat format = (VideoFormat) codes.get(new Integer(code));
        if (format == null) return "Unrecognized format + 0x" + Integer.toHexString(code);
        return format.shortDescription;
    }
    
}
