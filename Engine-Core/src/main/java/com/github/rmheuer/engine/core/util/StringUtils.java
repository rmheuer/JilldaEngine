package com.github.rmheuer.engine.core.util;

public final class StringUtils {
    public static String escapeChar(char c) {
		if (c == '\\') {
			return "\\\\";
		} else if (c >= 32 && c < 127) {
	    return String.valueOf(c);
	} else if (c == '\n') {
	    return "\\n";
	} else if (c == '\t') {
	    return "\\t";
	} else if (c == '\r') {
	    return "\\r";
	} else if (c == '\b') {
	    return "\\b";
	} else if (c == '\f') {
	    return "\\f";
	} else {
	    return String.format("\\u%04x", (int) c);
	}
    }

    public static String escapeString(String str) {
	StringBuilder out = new StringBuilder();
	for (char c : str.toCharArray()) {
	    out.append(escapeChar(c));
	}
	return out.toString();
    }

    public static String quoteChar(char c) {
	if (c == '\'') {
	    return "'\\''";
	} else {
	    return "'" + escapeChar(c) + "'";
	}
    }

    public static String quoteString(String str) {
	return "\"" + escapeString(str).replace("\"", "\\\"") + "\"";
    }
    
    private StringUtils() {
	throw new AssertionError();
    }
}
