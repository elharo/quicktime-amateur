package com.elharo.quicktime;

import java.util.HashMap;

import quicktime.util.QTUtils;


/**
 * See https://developer.apple.com/documentation/QuickTime/QTFF/QTFFChap3/chapter_4_section_2.html#//apple_ref/doc/uid/TP40000939-CH205-74522
 */
public class VideoFormat {

    private static HashMap codes = new HashMap();
    
    // XXX Use singleton instead
    // XXX duplicates SoundFormat, refactor
    static {
        // revise these all to use addType
        codes.put(new Integer(0x706e6720), new VideoFormat(0x706e6720, "Portable Network Graphics", "PNG"));
        addType("pdf ", "Portable Document Format", "PDF");
        addType("gif ", "Graphics Interchange Format", "GIF");
        addType("qdrw", "PICT", "PICT");
        codes.put(new Integer(0x61766331), new VideoFormat(0x61766331, "H.264 Decoder", "H.264 Decoder"));
        codes.put(new Integer(0x53565133), new VideoFormat(0x53565133, "", "Sorenson Video 3 Decompressor"));
        codes.put(new Integer(0x6d703476), new VideoFormat(0x6d703476, "Apple MPEG4 Decompressor", "Apple MPEG 4 Decompressor"));
        codes.put(new Integer(QTUtils.toOSType("cvid")), new VideoFormat("cvid", "Cinepak", "Cinepak"));
        codes.put(new Integer(QTUtils.toOSType("jpeg")), new VideoFormat("jpeg", "JPEG", "JPEG"));
        codes.put(new Integer(QTUtils.toOSType("raw ")), new VideoFormat("raw ", "Uncompressed RGB", "Uncompressed RGB"));
        codes.put(new Integer(QTUtils.toOSType("Yuv2")), new VideoFormat("Yuv2", "Uncompressed YUV422", "Uncompressed YUV422"));
        codes.put(new Integer(QTUtils.toOSType("smc ")), new VideoFormat("smc ", "Graphics", "Graphics"));
        codes.put(new Integer(QTUtils.toOSType("rle ")), new VideoFormat("rle ", "Animation", "Animation"));
        codes.put(new Integer(QTUtils.toOSType("rpza")), new VideoFormat("rpza", "Apple video", "Apple video"));
        codes.put(new Integer(QTUtils.toOSType("kpcd")), new VideoFormat("kpcd", "Kodak Photo CD", "Kodak Photo CD"));
        codes.put(new Integer(QTUtils.toOSType("mpeg")), new VideoFormat("mpeg", "MPEG", "MPEG"));
        codes.put(new Integer(QTUtils.toOSType("mjpa")), new VideoFormat("mjpa", "Motion-JPEG (new VideoFormat(format A)", "Motion-JPEG (new VideoFormat(format A)"));
        codes.put(new Integer(QTUtils.toOSType("mjpb")), new VideoFormat("mjpb", "Motion-JPEG (new VideoFormat(format B)", "Motion-JPEG (new VideoFormat(format B)"));
        codes.put(new Integer(QTUtils.toOSType("svqi")), new VideoFormat("svqi", "Sorenson video", "Sorenson video"));
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
        if (format == null) return "Unrecognized format " + QTUtils.fromOSType(code);
        return format.shortDescription;
    }
    
    private static void addType(String code, String description, String shortDescription) {
        codes.put(new Integer(QTUtils.toOSType(code)), new VideoFormat(code, description, shortDescription));
    }
    
}
