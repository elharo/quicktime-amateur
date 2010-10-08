package com.elharo.quicktime;

import java.util.HashMap;

import quicktime.util.QTUtils;


/**
 * See https://developer.apple.com/documentation/QuickTime/QTFF/QTFFChap3/chapter_4_section_2.html#//apple_ref/doc/uid/TP40000939-CH205-74522
 */
public class VideoFormat {

    private static HashMap<Integer, VideoFormat> codes = new HashMap<Integer, VideoFormat>();

    // XXX Use singleton instead
    // XXX duplicates SoundFormat, refactor
    static {
        // revise these all to use addType
        codes.put(Integer.valueOf(0x706e6720), new VideoFormat(0x706e6720, "Portable Network Graphics", "PNG"));
        addType("pdf ", "Portable Document Format", "PDF");
        addType("gif ", "Graphics Interchange Format", "GIF");
        addType("qdrw", "PICT", "PICT");
        addType("8BPS", "Adobe Photoshop", "Photoshop");
        codes.put(Integer.valueOf(0x61766331), new VideoFormat(0x61766331, "H.264 Decoder", "H.264 Decoder"));
        codes.put(Integer.valueOf(0x53565133), new VideoFormat(0x53565133, "", "Sorenson Video 3 Decompressor"));
        codes.put(Integer.valueOf(0x6d703476), new VideoFormat(0x6d703476, "Apple MPEG4 Decompressor", "Apple MPEG 4 Decompressor"));
        codes.put(Integer.valueOf(QTUtils.toOSType("cvid")), new VideoFormat("cvid", "Cinepak", "Cinepak"));
        codes.put(Integer.valueOf(QTUtils.toOSType("jpeg")), new VideoFormat("jpeg", "JPEG", "JPEG"));
        codes.put(Integer.valueOf(QTUtils.toOSType("raw ")), new VideoFormat("raw ", "Uncompressed RGB", "Uncompressed RGB"));
        codes.put(Integer.valueOf(QTUtils.toOSType("Yuv2")), new VideoFormat("Yuv2", "Uncompressed YUV422", "Uncompressed YUV422"));
        codes.put(Integer.valueOf(QTUtils.toOSType("smc ")), new VideoFormat("smc ", "Graphics", "Graphics"));
        codes.put(Integer.valueOf(QTUtils.toOSType("rle ")), new VideoFormat("rle ", "Animation", "Animation"));
        codes.put(Integer.valueOf(QTUtils.toOSType("rpza")), new VideoFormat("rpza", "Apple video", "Apple video"));
        codes.put(Integer.valueOf(QTUtils.toOSType("kpcd")), new VideoFormat("kpcd", "Kodak Photo CD", "Kodak Photo CD"));
        codes.put(Integer.valueOf(QTUtils.toOSType("mpeg")), new VideoFormat("mpeg", "MPEG", "MPEG"));
        codes.put(Integer.valueOf(QTUtils.toOSType("mjpa")), new VideoFormat("mjpa", "Motion-JPEG (new VideoFormat(format A)", "Motion-JPEG (new VideoFormat(format A)"));
        codes.put(Integer.valueOf(QTUtils.toOSType("mjpb")), new VideoFormat("mjpb", "Motion-JPEG (new VideoFormat(format B)", "Motion-JPEG (new VideoFormat(format B)"));
        codes.put(Integer.valueOf(QTUtils.toOSType("svqi")), new VideoFormat("svqi", "Sorenson video", "Sorenson video"));
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
        codes.put(Integer.valueOf(code), this);
    }

    public static String getShortDescription(int code) {
        VideoFormat format = codes.get(Integer.valueOf(code));
        if (format == null) return "Unrecognized format " + QTUtils.fromOSType(code);
        return format.shortDescription;
    }

    private static void addType(String code, String description, String shortDescription) {
        codes.put(Integer.valueOf(QTUtils.toOSType(code)), new VideoFormat(code, description, shortDescription));
    }

}
