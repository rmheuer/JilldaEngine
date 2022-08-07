package com.github.rmheuer.engine.core.serial2.json;

import com.github.rmheuer.engine.core.serial2.node.NumericNode;
import com.github.rmheuer.engine.core.serial2.node.SerialArray;
import com.github.rmheuer.engine.core.serial2.node.SerialBoolean;
import com.github.rmheuer.engine.core.serial2.node.SerialDouble;
import com.github.rmheuer.engine.core.serial2.node.SerialLong;
import com.github.rmheuer.engine.core.serial2.node.SerialNode;
import com.github.rmheuer.engine.core.serial2.node.SerialNull;
import com.github.rmheuer.engine.core.serial2.node.SerialObject;
import com.github.rmheuer.engine.core.serial2.node.SerialRef;
import com.github.rmheuer.engine.core.serial2.node.SerialString;
import com.github.rmheuer.engine.core.serial2.node.TraversableNode;
import com.github.rmheuer.engine.core.util.ExceptionalPeekableStream;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class JsonDecoder {
    private static final class IncompleteRef {
        final String absoluteTargetPath;
        final SerialRef ref;

        public IncompleteRef(String absoluteTargetPath, SerialRef ref) {
            this.absoluteTargetPath = absoluteTargetPath;
            this.ref = ref;
        }
    }

    private final ExceptionalPeekableStream<Character, IOException> in;
    private final Set<IncompleteRef> incompleteRefs;

    public JsonDecoder(Reader in) {
        this.in = new ExceptionalPeekableStream<>(() -> (char) in.read());
        incompleteRefs = new HashSet<>();
    }

    private int fromHex(char c) throws IOException {
        if (c >= '0' && c <= '9') return c - '0';
        if (c >= 'a' && c <= 'f') return c - 'a' + 10;
        if (c >= 'A' && c <= 'F') return c - 'A' + 10;
        throw new IOException("Invalid hex char: '" + c + "'");
    }

    private char fromHex(char a, char b, char c, char d) throws IOException {
        int out = fromHex(d);
        out |= fromHex(c) << 4;
        out |= fromHex(b) << 8;
        out |= fromHex(a) << 12;
        return (char) out;
    }

    private String readString() throws IOException {
        expect('"');
        StringBuilder builder = new StringBuilder();

        while (in.peek() != '"') {
            char c = in.next();

            if (c == '\\') {
                char escape = in.next();
                switch (escape) {
                    case '"': c = '"'; break;
                    case '\\': c = '\\'; break;
                    case '/': c = '/'; break;
                    case 'b': c = '\b'; break;
                    case 'f': c = '\f'; break;
                    case 'n': c = '\n'; break;
                    case 'r': c = '\r'; break;
                    case 't': c = '\t'; break;
                    case 'u': c = fromHex(in.next(), in.next(), in.next(), in.next()); break;
                    default:
                        throw new IOException("Invalid escape char: '" + escape + "'");
                }
            }

            builder.append(c);
        }

        expect('"');
        return builder.toString();
    }

    private String toAbsolute(String path, String rel) {
        String[] pathPartsArr = path.split(TraversableNode.PATH_SEPARATOR);
        List<String> pathParts = new ArrayList<>(Arrays.asList(pathPartsArr));

        String[] relParts = rel.split(TraversableNode.PATH_SEPARATOR);
        for (String part : relParts) {
            if (part.equals(TraversableNode.PARENT_TOKEN)) {
                pathParts.remove(pathParts.size() - 1);
            } else {
                pathParts.add(part);
            }
        }

        StringBuilder out = new StringBuilder();
        boolean separator = false;
        for (String part : pathParts) {
            if (separator) out.append(TraversableNode.PATH_SEPARATOR);
            else separator = true;

            out.append(part);
        }

        return out.toString();
    }

    private SerialNode decodeObject(String path) throws IOException {
        expect('{');
        SerialObject obj = new SerialObject();
        boolean expectComma = false;
        while (peek() != '}') {
            if (expectComma) expect(',');
            else expectComma = true;
            String key = readString();
            expect(':');
            SerialNode value = decodeNode(path.equals("") ? key : path + TraversableNode.PATH_SEPARATOR + key);
            obj.put(key, value);
        }
        expect('}');

        if (obj.containsKey("$ref")) {
            String relativePath = obj.getString("$ref");
            String absolutePath = toAbsolute(path, relativePath);
            SerialRef ref = new SerialRef();
            incompleteRefs.add(new IncompleteRef(absolutePath, ref));
            return ref;
        }

        return obj;
    }

    private SerialArray decodeArray(String path) throws IOException {
        expect('[');
        SerialArray arr = new SerialArray();
        boolean expectComma = false;
        int index = 0;
        while (peek() != ']') {
            if (expectComma) expect(',');
            else expectComma = true;

            SerialNode elem = decodeNode(path.equals("") ? String.valueOf(index) : path + TraversableNode.PATH_SEPARATOR + index);
            arr.add(elem);

            index++;
        }
        expect(']');
        return arr;
    }

    private boolean isDigit(char c) {
        return c >= '0' && c < '9';
    }

    private int fromDigit(char c) throws IOException {
        if (c >= '0' && c < '9') return c - '0';
        throw new IOException("Not a digit: '" + c + "'");
    }

    private int powerOfTen(int pow) {
        int base = 10;
        int result = 1;
        while (true) {
            if ((pow & 1) != 0)
                result *= base;
            pow >>= 1;
            if (pow == 0)
                break;
            base *= base;
        }
        return result;
    }

    private NumericNode decodeNumber() throws IOException {
        boolean negative = peek() == '-';
        if (negative)
            in.next();

        if (in.peek() == 'N') {
            expectString("NaN");
            return new SerialDouble(Double.NaN);
        }
        if (in.peek() == 'I') {
            expectString("Infinity");
            return new SerialDouble(negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
        }

        long whole = 0;
        if (in.peek() != '0') {
            do {
                whole *= 10;
                whole += fromDigit(in.next());
            } while (isDigit(in.peek()));
        } else {
            expect('0');
        }

        boolean hasDecimal = false;
        long decimal = 0;
        long decimalSize = 1;
        if (in.peek() == '.') {
            in.next(); // Consume decimal point
            hasDecimal = true;
            do {
                decimal *= 10;
                decimalSize *= 10;
                decimal += fromDigit(in.next());
            } while (isDigit(in.peek()));
        }

        short exponentShift = 0;
        boolean exponentNegative = false;
        if (in.peek() == 'e' || in.peek() == 'E') {
            in.next(); // Consume e
            hasDecimal = true;

            char sign = in.peek();
            if (sign == '-') {
                exponentNegative = true;
                in.next();
            } else if (sign == '+') {
                in.next();
            }

            do {
                exponentShift *= 10;
                exponentShift += fromDigit(in.next());
            } while (isDigit(in.peek()));
        }

        if (hasDecimal) {
            double value = whole + decimal / (double) decimalSize;
            if (negative)
                value = -value;
            int shift = powerOfTen(exponentShift);
            if (exponentNegative)
                value /= shift;
            else
                value *= shift;
            return new SerialDouble(value);
        } else {
            return new SerialLong(negative ? -whole : whole);
        }
    }

    private SerialString decodeString() throws IOException {
        return new SerialString(readString());
    }

    private boolean isNumberStartingChar(char c) {
        if (c >= '0' && c <= '9') return true;
        if (c == '-') return true;

        // For Infinity and NaN (not spec compliant!)
        return c == 'I' || c == 'N';
    }

    private SerialNode decodeNode(String path) throws IOException {
        char p = peek();
        if (p == '{') return decodeObject(path);
        if (p == '[') return decodeArray(path);
        if (p == '"') return decodeString();
        if (p == 't') {
            expectString("true");
            return new SerialBoolean(true);
        }
        if (p == 'f') {
            expectString("false");
            return new SerialBoolean(false);
        }
        if (p == 'n') {
            expectString("null");
            return new SerialNull();
        }
        if (isNumberStartingChar(p)) return decodeNumber();

        throw new IOException("Unexpected character: " + p);
    }

    public SerialNode decode() throws IOException {
        SerialNode node = decodeNode("");

        if (node instanceof TraversableNode) {
            TraversableNode t = (TraversableNode) node;
            for (IncompleteRef ref : incompleteRefs) {
                SerialNode actualTarget = t.getByPath(ref.absoluteTargetPath);
                ref.ref.setTarget(actualTarget);
            }
            incompleteRefs.clear();
        }

        return node;
    }

    private void skipWhitespace() throws IOException {
        while (Character.isWhitespace(in.peek()))
            in.next();
    }

    private char peek() throws IOException {
        skipWhitespace();
        return in.peek();
    }

    private void expect(char c) throws IOException {
        skipWhitespace();
        char read = in.next();
        if (read != c)
            throw new IOException("Expected '" + c + "', found '" + read + "'");
    }

    private void expectString(String str) throws IOException {
        for (char c : str.toCharArray()) {
            expect(c);
        }
    }
}
