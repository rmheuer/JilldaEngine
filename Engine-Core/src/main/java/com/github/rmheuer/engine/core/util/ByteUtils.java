package com.github.rmheuer.engine.core.util;

public final class ByteUtils {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) { return bytesToHex(bytes, 0, bytes.length); }
    public static String bytesToHex(byte[] bytes, int start, int end) {
	int len = end - start;
	char[] hexChars = new char[len * 2];
	for (int j = 0; j < len; j++) {
	    int v = bytes[j + start] & 0xFF;
	    hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	    hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	}
	return new String(hexChars);
    }

    public static void printByteArrayAsHex(byte[] bytes) { printByteArrayAsHex(bytes, 80); }
    public static void printByteArrayAsHex(byte[] bytes, int maxLineLen) {
	int bytesPerLine = maxLineLen / 2;
	for (int i = 0; i <= bytes.length / bytesPerLine; i++) {
	    int start = i * bytesPerLine;
	    int end = Math.min((i + 1) * bytesPerLine, bytes.length);
	    System.out.println(bytesToHex(bytes, start, end));
	    
	    for (int j = start; j < end; j++) {
		byte b = bytes[j];
		if (b >= 32 && b < 127) {
		    System.out.print((char) b);
		} else {
		    System.out.print('.');
		}
		System.out.print(' ');
	    }
	    System.out.println();
	}
    }

    private ByteUtils() {
	throw new AssertionError();
    }
}
