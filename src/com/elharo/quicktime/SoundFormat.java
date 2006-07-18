package com.elharo.quicktime;

import java.util.HashMap;

import quicktime.util.QTUtils;


/**
 * See https://developer.apple.com/documentation/QuickTime/QTFF/QTFFChap3/chapter_4_section_3.html
 * especially the table at the bottom
 */
public class SoundFormat {

    private static HashMap codes = new HashMap();

    // XXX Use singleton instead
    // XXX duplicates VideoFormat, refactor
    static {
        addType("samr", "AMR Narrowband", "AMR");
        addType("alac", "Apple Lossless", "Apple Lossless");
        addType("vdva", "DVC", "DVC");
        addType("qclq", "3GP QCELP", "QCELP");
   }    
    
    public final static SoundFormat AAC = new SoundFormat(0x6d703461, "AAC", "AAC");
    public final static SoundFormat SoundNotCompressed = new SoundFormat("NONE", "Sound is not compressed", "Uncompressed");
    public final static SoundFormat _8BitOffsetBinaryFormat = new SoundFormat("raw ", 
      "Samples are stored uncompressed, in offset-binary format (values range from 0 to 255; 128 is silence). These are stored as 8-bit offset binaries.",
      "8-bit binary");
    public final static SoundFormat _16BitBigEndianFormat = new SoundFormat("twos", 
       "Samples are stored uncompressed, in two’s-complement format (sample values range from -128 to 127 for 8-bit audio, and -32768 to 32767 for 1- bit audio; 0 is always silence). These samples are stored in 16-bit big-endian format.", 
        "16-bit Integer (Big Endian)");
    public final static SoundFormat _16BitLittleEndianFormat = new SoundFormat("sowt", "16-bit little-endian, two's-complement, AVI uncompressed",
        "16-bit Integer (Little Endian)");
    public final static SoundFormat MACE3Compression = new SoundFormat("MAC3 ", "Samples have been compressed using MACE 3:1. (Obsolete.)", "MACE 3:1");
    public final static SoundFormat MACE6Compression = new SoundFormat("MAC6 ", "Samples have been compressed using MACE 6:1. (Obsolete.)", "MACE 6:1");
    public final static SoundFormat IMACompression = new SoundFormat("ima4 ", "Samples have been compressed using IMA 4:1.", "IMA 4:1");
    public final static SoundFormat Float32Format = new SoundFormat("fl32", "32-bit floating point", "32-bit Floating Point");
    public final static SoundFormat Float64Format = new SoundFormat("fl64", "64-bit floating point", "64-bit Floating Point");
    public final static SoundFormat _24BitFormat = new SoundFormat("in24", "24-bit integer", "24-bit Integer");
    public final static SoundFormat _32BitFormat = new SoundFormat("in32", "32-bit integer", "32-bit integer");
    public final static SoundFormat ULawCompression = new SoundFormat("ulaw", "μLaw 2:1", "μLaw 2:1");
    public final static SoundFormat ALawCompression = new SoundFormat("alaw", "μLaw 2:1", "μlaw 2:1");
    public final static SoundFormat MicrosoftADPCMFormat= new SoundFormat(0x6D730002, "Microsoft ADPCM-ACM code 2", "Microsoft ADPCM-ACM code 2");
    public final static SoundFormat DVIIntelIMAFormat= new SoundFormat(0x6D730011, "DVI/Intel IMAADPCM-ACM code 17", "DVI/Intel IMAADPCM-ACM code 17");
    public final static SoundFormat DVAudioFormat = new SoundFormat("dvca", "DV Audio", "DV Audio");
    public final static SoundFormat QDesignCompression = new SoundFormat("QDMC", "QDesign music", "QDesign music");
    public final static SoundFormat unnamed = new SoundFormat("QDM2", "QDesign music version 2 (no constant). ", "QDesign music version 2");
    public final static SoundFormat QUALCOMMCompression = new SoundFormat("Qclp", "QUALCOMM PureVoice", "QUALCOMM PureVoice");
    public final static SoundFormat MPEGLayer3Format = new SoundFormat(0x6D730055, "MPEG layer 3, CBR only (pre- QT4.1)", "MP3 Constant Bit Rate");
    public final static SoundFormat FullMPEGLay3Format = new SoundFormat(".mp3", "MPEG layer 3, CBR & VBR (QT4.1 and later)", "MP3 Variable Bit Rate");
    
    private int code;
    private String description;
    private String shortDescription;
    
    private SoundFormat(String code, String description, String shortDescription) {
        this(QTUtils.toOSType(code), description, shortDescription);
    }

    private SoundFormat(int code, String description, String shortDescription) {
        this.code = code;
        this.description = description;
        this.shortDescription = shortDescription;
        codes.put(new Integer(code), this);
    }

    public static String getShortDescription(int code) {
        SoundFormat format = (SoundFormat) codes.get(new Integer(code));
        if (format == null) return "Unrecognized format + 0x" + Integer.toHexString(code);
        return format.shortDescription;
    }

    private static void addType(String code, String description, String shortDescription) {
        codes.put(new Integer(QTUtils.toOSType(code)), new SoundFormat(code, description, shortDescription));
    }    
    
}
