package com.github.rmheuer.bintool;

import com.github.rmheuer.engine.core.serial.codec.bin.BinarySerialCodec;
import com.github.rmheuer.engine.core.serial.node.SerialArray;
import com.github.rmheuer.engine.core.serial.node.SerialBoolean;
import com.github.rmheuer.engine.core.serial.node.SerialByte;
import com.github.rmheuer.engine.core.serial.node.SerialChar;
import com.github.rmheuer.engine.core.serial.node.SerialDouble;
import com.github.rmheuer.engine.core.serial.node.SerialFloat;
import com.github.rmheuer.engine.core.serial.node.SerialInt;
import com.github.rmheuer.engine.core.serial.node.SerialLong;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.node.SerialObject;
import com.github.rmheuer.engine.core.serial.node.SerialReference;
import com.github.rmheuer.engine.core.serial.node.SerialShort;
import com.github.rmheuer.engine.core.serial.node.SerialString;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

public final class BinTool {
    public static void main(String[] args) throws Exception {
	if (args.length == 0) {
	    System.err.println("Please specify a file path.");
	    System.exit(1);
	}

	String path = args[0];
	File file = new File(path);
	SerialNode root = null;
	if (file.exists()) {
	    BinarySerialCodec codec = BinarySerialCodec.get();
	    root = codec.decode(new FileInputStream(file));
	} else {
	    if (args.length == 1) {
		System.err.println("The file does not exist, please specify a root type.");
		System.exit(1);
	    }
	    
	    if (args[1].equals("object")) {
		root = new SerialObject();
	    } else if (args[1].equals("array")) {
		root = new SerialArray();
	    } else {
		System.err.println("Invalid root type.");
		System.exit(1);
	    }
	}

	Scanner scanner = new Scanner(System.in);

	boolean running = true;
	while (running) {
	    System.out.print("> ");
	    String input = scanner.nextLine();
	    String[] tokens = input.split(" ");

	    outer: switch (tokens[0]) {
	    case "list": {
		SerialNode toList = root;
		if (tokens.length > 1) {
		    toList = evalPath(root, tokens[1]);
		}

		System.out.println(prettify(toList == null ? "null" : toList.toString()));
		break;
	    }
	    case "set": {
		SerialNode parent = root;
		if (tokens.length < 3) {
		    System.err.println("You must specify a path, type, and value if required.");
		    break;
		}

		String nodePath = tokens[1];
		String type = tokens[2];
		String value = tokens.length > 3 ? tokens[3] : null;

		String[] parts = nodePath.split("\\.");
		
		for (int i = 0; i < parts.length - 1; i++) {
		    String part = parts[i];
		    if (parent instanceof SerialObject) {
			parent = ((SerialObject) parent).get(part);
		    } else if (parent instanceof SerialArray) {
			parent = ((SerialArray) parent).get(Integer.parseInt(part));
		    } else {
			break;
		    }
		}

		SerialNode toAdd;
		switch (type) {
		case "byte": toAdd = new SerialByte(Byte.parseByte(value)); break;
		case "short": toAdd = new SerialShort(Short.parseShort(value)); break;
		case "int": toAdd = new SerialInt(Integer.parseInt(value)); break;
		case "long": toAdd = new SerialLong(Long.parseLong(value)); break;
		case "float": toAdd = new SerialFloat(Float.parseFloat(value)); break;
		case "double": toAdd = new SerialDouble(Double.parseDouble(value)); break;
		case "boolean": toAdd = new SerialBoolean(Boolean.parseBoolean(value)); break;
		case "char": toAdd = new SerialChar(value.charAt(0)); break;
		case "string": toAdd = new SerialString(value); break;
		case "object": toAdd = new SerialObject(); break;
		case "array": toAdd = new SerialArray(); break;
		case "reference": toAdd = new SerialReference(evalPath(root, value)); break;
		case "null": toAdd = null; break;
		default:
		    System.err.println("Unknown type.");
		    break outer;
		}

		if (parent instanceof SerialObject) {
		    ((SerialObject) parent).put(parts[parts.length - 1], toAdd);
		    System.out.println("Value set.");
		} else if (parent instanceof SerialArray) {
		    int index = Integer.parseInt(parts[parts.length - 1]);
		    SerialArray arr = (SerialArray) parent;
		    
		    // Make sure entries exist
		    for (int i = arr.size() - 1; i < index; i++) {
			arr.add(null);
		    }
		    
		    arr.set(index, toAdd);
		    System.out.println("Value set.");
		} else {
		    System.err.println("Invalid parent.");
		}
		break;
	    }
	    case "exit":
		running = false;
		break;
	    default:
		System.err.println("Unknown command.");
		break;
	    }
	}

	BinarySerialCodec codec = BinarySerialCodec.get();
	codec.encode(root, new FileOutputStream(file));
	System.out.println("Wrote data to file.");
    }

    private static SerialNode evalPath(SerialNode node, String path) {
	String[] parts = path.split("\\.");

	for (String part : parts) {
	    if (node instanceof SerialObject) {
		node = ((SerialObject) node).get(part);
	    } else if (node instanceof SerialArray) {
		node = ((SerialArray) node).get(Integer.parseInt(part));
	    } else {
		break;
	    }
	}

	return node;
    }

    private static String prettify(String str) {
	StringBuilder out = new StringBuilder();

	String indent = "\n";
	char[] chars = str.toCharArray();
	for (int i = 0; i < chars.length; i++) {
	    char c = chars[i];
	    if (c == '}' || c == ']') {
		indent = indent.substring(0, indent.length() - 2);
		out.append(indent);
	    }

	    out.append(c);
	    
	    if (c == '{' || c == '[') {
		indent += "  ";
		out.append(indent);
	    }
	    if (c == ',') {
		out.append(indent);
		i++; // Skip the space
	    }
	}

	return out.toString();
    }
}
