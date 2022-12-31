package com.github.rmheuer.engine.core.main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.system.Configuration.*;

public final class Main {
    public static void main(String[] args) {
	if (restartJVM(args))
	    return;

	if (System.getProperty("engine.debug.lwjgl") != null) {
            DEBUG.set(true);
            DEBUG_FUNCTIONS.set(true);
            DEBUG_LOADER.set(true);
            DEBUG_MEMORY_ALLOCATOR.set(true);
            DEBUG_STACK.set(true);
            DEBUG_STREAM.set(true);
        }

        Game.get().run(args);
    }

    private static boolean restartJVM(String[] args) {
	String osName = System.getProperty("os.name");

	if (!osName.startsWith("Mac") && !osName.startsWith("Darwin"))
	    return false;

	String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
	String env = System.getenv("JAVA_STARTED_ON_FIRST_THREAD_" + pid);

	if (env != null && env.equals("1"))
	    return false;
	
	System.out.println("Running on MacOS, restarting JVM with -XstartOnFirstThread");

	String separator = System.getProperty("file.separator");
	String classpath = System.getProperty("java.class.path");
	String mainClass = Main.class.getName();
	String jvmPath = System.getProperty("java.home")
	    + separator
	    + "bin"
	    + separator
	    + "java";

	List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();

	List<String> jvmArgs = new ArrayList<>();
	jvmArgs.add(jvmPath);
	jvmArgs.add("-XstartOnFirstThread");
	jvmArgs.addAll(inputArguments);
	jvmArgs.add("-cp");
	jvmArgs.add(classpath);
	jvmArgs.add(mainClass);
	jvmArgs.addAll(Arrays.asList(args));

	StringBuilder builder = new StringBuilder();
	for (String arg : jvmArgs) {
	    builder.append(arg);
	    builder.append(" ");
	}
	System.out.println("Starting new JVM with command: " + builder.toString());

	try {
	    ProcessBuilder processBuilder = new ProcessBuilder(jvmArgs);
	    processBuilder.redirectErrorStream(true);
	    Process process = processBuilder.start();

	    InputStream is = process.getInputStream();
	    InputStreamReader isr = new InputStreamReader(is);
	    BufferedReader br = new BufferedReader(isr);

	    String line;
	    while ((line = br.readLine()) != null) {
		System.out.println(line);
	    }

	    int exit = process.waitFor();
	    System.out.println("JVM exited with code " + exit);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return true;
    }

    private Main() {
        throw new AssertionError();
    }
}
