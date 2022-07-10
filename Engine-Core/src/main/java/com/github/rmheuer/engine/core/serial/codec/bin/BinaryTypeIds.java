package com.github.rmheuer.engine.core.serial.codec.bin;

public final class BinaryTypeIds {
    public static final byte ID_BYTE      = 0;
    public static final byte ID_SHORT     = 1;
    public static final byte ID_INT       = 2;
    public static final byte ID_LONG      = 3;
    public static final byte ID_FLOAT     = 4;
    public static final byte ID_DOUBLE    = 5;
    public static final byte ID_BOOLEAN   = 6;
    public static final byte ID_CHAR      = 7;
    public static final byte ID_STRING    = 8;
    public static final byte ID_OBJECT    = 9;
    public static final byte ID_ARRAY     = 10;
    public static final byte ID_REFERENCE = 11;
    public static final byte ID_NULL      = 12;

    private BinaryTypeIds() {
	throw new AssertionError();
    }
}
