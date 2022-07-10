package com.github.rmheuer.engine.core.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class ResourceUtils {
    public static String[] splitPath(String path) {
        List<String> out = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        char[] chars = path.toCharArray();

        boolean escaped = false;
        for (char c : chars) {
            if (c == '\\') {
                escaped = true;
                continue;
            }

            if (c == ResourceFile.SEPARATOR) {
                if (!escaped) {
                    out.add(builder.toString());
                    builder.replace(0, builder.length(), "");
                } else {
                    builder.append(c);
                }
            } else {
                if (escaped)
                    builder.append('\\');
                builder.append(c);
            }
            escaped = false;
        }
        out.add(builder.toString());

        String[] arr = new String[out.size()];
        out.toArray(arr);
        return arr;
    }

    public static String convertNativePathToResourcePath(String nativePath) {
        // If the native separator character is already correct, the path doesn't
        // need to change
        if (File.separatorChar == ResourceFile.SEPARATOR)
            return nativePath;

        // TODO-IMPORTANT: Account for escaping
        return nativePath.replace(File.separatorChar, ResourceFile.SEPARATOR);
    }

    private ResourceUtils() {
        throw new AssertionError();
    }
}
