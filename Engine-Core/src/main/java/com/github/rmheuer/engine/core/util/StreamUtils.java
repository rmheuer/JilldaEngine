package com.github.rmheuer.engine.core.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class StreamUtils {
    public static byte[] readToByteArray(InputStream in) throws IOException {
	ByteArrayOutputStream b = new ByteArrayOutputStream();
	copyStreams(in, b);
	return b.toByteArray();
    }

    public static void copyStreams(InputStream in, OutputStream out) throws IOException {
	byte[] buffer = new byte[1024];
	int read;
	while ((read = in.read(buffer)) > 0) {
	    out.write(buffer, 0, read);
	}
	in.close();
	out.close();
    }

    private StreamUtils() {
	throw new AssertionError();
    }
}
