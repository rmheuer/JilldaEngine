package com.github.rmheuer.engine.core.main;

import com.github.rmheuer.engine.core.EngineVersion;
import com.github.rmheuer.engine.core.Time;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.event.Event;
import com.github.rmheuer.engine.core.input.InputManager;
import com.github.rmheuer.engine.core.layer.Layer;
import com.github.rmheuer.engine.core.layer.LayerRegistry;
import com.github.rmheuer.engine.core.module.CoreModule;
import com.github.rmheuer.engine.core.module.GameModule;
import com.github.rmheuer.engine.core.module.ModuleRegistry;
import com.github.rmheuer.engine.core.profile.FixedProfileStage;
import com.github.rmheuer.engine.core.profile.ProfileNode;
import com.github.rmheuer.engine.core.profile.Profiler;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.resource.jar.JarResourceFile;
import com.github.rmheuer.engine.core.serial2.json.JsonCodec;
import com.github.rmheuer.engine.core.serial2.node.SerialArray;
import com.github.rmheuer.engine.core.serial2.node.SerialNode;
import com.github.rmheuer.engine.core.serial2.node.SerialObject;
import com.github.rmheuer.engine.core.serial2.node.TextualNode;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class Game {
    public static final ResourceFile CONFIG_FILE = new JarResourceFile("game.json");

    private static final Game INSTANCE = new Game();

    private final Map<FixedProfileStage, ProfileNode> stageProfileData;
    private final Map<Class<? extends Event>, ProfileNode> eventProfileData;
    private Profiler profiler;

    private String[] commandLineArgs;
    private float fixedUpdatesPerSecond;
    private float maxFixedUpdateBacklog;
    private int sleepInterval;
    private boolean running;

    private List<GameModule> modules;
    private List<Layer> layers;

    private Queue<Event> eventQueue;
    private InputManager inputManager;

    public static Game get() {
        return INSTANCE;
    }

    private Game() {
        setFixedUpdatesPerSecond(60);
        sleepInterval = 5;
        maxFixedUpdateBacklog = 1;

        stageProfileData = new EnumMap<>(FixedProfileStage.class);
        eventProfileData = new HashMap<>();
    }

    // TODO: Maybe deserialize as unique object instead of one instance per class
    @SuppressWarnings("unchecked")
    private void loadConfig() {
        SerialObject config;
        try {
            config = (SerialObject) JsonCodec.get().decode(CONFIG_FILE.readAsStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file", e);
        }

        if (config.containsKey("fixedUpdatesPerSecond")) {
            fixedUpdatesPerSecond = config.getFloat("fixedUpdatesPerSecond");
        }

        if (config.containsKey("modules")) {
            SerialArray modulesArr = config.getArray("modules");
            for (SerialNode node : modulesArr) {
                String className = ((TextualNode) node).getString();
                Class<? extends GameModule> clazz;
                try {
                    Class<?> rawClass = Class.forName(className);
                    if (!GameModule.class.isAssignableFrom(rawClass)) {
                        System.err.println("Not a module: " + className);
                        continue;
                    }

                    clazz = (Class<? extends GameModule>) rawClass;
                } catch (ClassNotFoundException e) {
                    System.err.println("Could not load module: " + className);
                    continue;
                }
                modules.add(ModuleRegistry.getInstance(clazz));
            }
        }

        if (config.containsKey("layers")) {
            SerialArray systemsArr = config.getArray("layers");
            for (SerialNode node : systemsArr) {
                String className = ((TextualNode) node).getString();
                Class<? extends Layer> clazz;
                try {
                    Class<?> rawClass = Class.forName(className);
                    if (!Layer.class.isAssignableFrom(rawClass)) {
                        System.err.println("Not a layer: " + className);
                        continue;
                    }

                    clazz = (Class<? extends Layer>) rawClass;
                } catch (ClassNotFoundException e) {
                    System.err.println("Could not load layer: " + className);
                    continue;
                }
                layers.add(LayerRegistry.getInstance(clazz));
            }
        }
    }

    private void init() {
        System.out.println("Starting engine version " + EngineVersion.VERSION);

        eventQueue = new ConcurrentLinkedQueue<>();
        inputManager = new InputManager();

        modules = new ArrayList<>();
        layers = new ArrayList<>();
        modules.add(ModuleRegistry.getInstance(CoreModule.class));

        Profiler temp = profiler;
        profiler = new Profiler();

        profiler.begin("Init");
        profiler.push("Load configuration");
        loadConfig();
        profiler.pop();

        profiler.push("Initialize modules");
        for (GameModule module : modules) {
            profiler.push(module.getClass().getSimpleName());
            module.init();
            profiler.pop();
        }
        profiler.pop();

        profiler.push("Initialize layers");
        for (Layer layer : layers) {
            profiler.push(layer.getClass().getSimpleName());
            layer.init();
            profiler.pop();
        }
        profiler.pop();
        profiler.end();
        stageProfileData.put(FixedProfileStage.INIT, profiler.getData());

        profiler = temp;
    }

    private void update(float delta) {
        Profiler temp = profiler;
        profiler = new Profiler();
        profiler.begin("Update");

        profiler.push("Pre-update modules");
        for (GameModule module : modules) {
            profiler.push(module.getClass().getSimpleName());
            module.preUpdate();
            profiler.pop();
        }
        profiler.pop();

        profiler.push("Update layers");
        for (Layer layer : layers) {
            profiler.push(layer.getClass().getSimpleName());
            layer.update(delta);
            profiler.pop();
        }
        profiler.pop();

        profiler.push("Post-update modules");
        for (GameModule module : modules) {
            profiler.push(module.getClass().getSimpleName());
            module.postUpdate();
            profiler.pop();
        }
        profiler.pop();
        profiler.end();

        stageProfileData.put(FixedProfileStage.UPDATE, profiler.getData());
        profiler = temp;
    }

    private void fixedUpdate() {
        Profiler temp = profiler;
        profiler = new Profiler();
        profiler.begin("Fixed update");

        profiler.push("Pre-fixed-update modules");
        for (GameModule module : modules) {
            profiler.push(module.getClass().getSimpleName());
            module.preFixedUpdate();
            profiler.pop();
        }
        profiler.pop();

        profiler.push("Fixed-update layers");
        for (Layer layer : layers) {
            profiler.push(layer.getClass().getSimpleName());
            layer.fixedUpdate();
            profiler.pop();
        }
        profiler.pop();

        profiler.push("Post-fixed-update modules");
        for (GameModule module : modules) {
            profiler.push(module.getClass().getSimpleName());
            module.postFixedUpdate();
            profiler.pop();
        }
        profiler.pop();
        profiler.end();
        stageProfileData.put(FixedProfileStage.FIXED_UPDATE, profiler.getData());
        profiler = temp;
    }

    private void close() {
        // Not worth profiling here - nothing will be running to read
        // the results at this point!
        for (int i = layers.size() - 1; i >= 0; i--) {
            layers.get(i).close();
        }

        for (GameModule module : modules) {
            module.close();
        }
    }

    public void run(String[] args) {
        commandLineArgs = args;

        running = true;
        init();

        long previousTime = System.nanoTime();
        float unprocessedTime = 0;
        while (running) {
            profiler = new Profiler();
            profiler.begin("Main");

            long currentTime = System.nanoTime();
            long passedTime = currentTime - previousTime;
            float delta = passedTime / 1_000_000_000.0f;
            unprocessedTime += delta;
            previousTime = currentTime;
            Time.setDelta(delta);

            if (unprocessedTime > maxFixedUpdateBacklog) {
                unprocessedTime = maxFixedUpdateBacklog;
            }

            profiler.push("Dispatch queued events");
            Event event;
            while ((event = eventQueue.poll()) != null) {
                dispatchEvent(event);
            }
            profiler.pop();

            profiler.push("Fixed update");
            float secondsPerFixedUpdate = Time.getFixedDelta();
            while (unprocessedTime > secondsPerFixedUpdate) {
                fixedUpdate();

                unprocessedTime -= secondsPerFixedUpdate;
            }
            profiler.pop();

            profiler.push("Update");
            update(delta);
            profiler.pop();

            profiler.push("Sleep");
            if (sleepInterval != 0) {
                try {
                    Thread.sleep(sleepInterval);
                } catch (InterruptedException e) {
                    running = false;
                }
            }
            profiler.pop();
            profiler.end();
            stageProfileData.put(FixedProfileStage.MAIN_LOOP, profiler.getData());
        }

        close();
    }

    public String[] getCommandLineArgs() {
        return commandLineArgs.clone();
    }

    // Queues an event to be dispatched next frame
    public void postGlobalEvent(Event event) {
        eventQueue.add(event);
    }

    private void dispatchEvent(Event event) {
        Profiler temp = profiler;
        profiler = new Profiler();
        profiler.begin(event.getClass().getSimpleName());
        for (int i = layers.size() - 1; i >= 0; i--) {
            Layer layer = layers.get(i);
            profiler.push(layer.getClass().getSimpleName());
            layer.onEvent(event);
            profiler.pop();
        }
        profiler.end();
        eventProfileData.put(event.getClass(), profiler.getData());
        profiler = temp;
    }

    // Dispatches an event immediately
    public void postGlobalImmediateEvent(Event event) {
        dispatchEvent(event);
    }

    public void stop() {
        running = false;
    }

    public Profiler getProfiler() {
        return profiler;
    }

    public Map<FixedProfileStage, ProfileNode> getStageProfileData() {
        return new EnumMap<>(stageProfileData);
    }

    public Map<Class<? extends Event>, ProfileNode> getEventProfileData() {
        return new HashMap<>(eventProfileData);
    }

    public float getFixedUpdatesPerSecond() {
        return fixedUpdatesPerSecond;
    }

    public void setFixedUpdatesPerSecond(float fixedUpdatesPerSecond) {
        this.fixedUpdatesPerSecond = fixedUpdatesPerSecond;
        Time.setFixedDelta(1 / fixedUpdatesPerSecond);
    }

    public int getSleepInterval() {
        return sleepInterval;
    }

    public void setSleepInterval(int sleepInterval) {
        this.sleepInterval = sleepInterval;
    }

    public float getMaxFixedUpdateBacklog() {
        return maxFixedUpdateBacklog;
    }

    public void setMaxFixedUpdateBacklog(float maxFixedUpdateBacklog) {
        this.maxFixedUpdateBacklog = maxFixedUpdateBacklog;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public Set<Class<? extends GameSystem>> getAllSystems() {
        Set<Class<? extends GameSystem>> systems = new HashSet<>();
        for (GameModule module : modules) {
            systems.addAll(module.getSystems());
        }
        return systems;
    }
}
