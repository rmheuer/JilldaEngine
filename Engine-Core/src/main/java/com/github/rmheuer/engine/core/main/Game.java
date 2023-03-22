package com.github.rmheuer.engine.core.main;

import com.github.rmheuer.engine.core.EngineVersion;
import com.github.rmheuer.engine.core.Time;
import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.event.Event;
import com.github.rmheuer.engine.core.input.InputManager;
import com.github.rmheuer.engine.core.module.CoreModule;
import com.github.rmheuer.engine.core.module.GameModule;
import com.github.rmheuer.engine.core.profile.FixedProfileStage;
import com.github.rmheuer.engine.core.profile.ProfileNode;
import com.github.rmheuer.engine.core.profile.Profiler;
import com.github.rmheuer.engine.core.util.InstanceMap;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public final class Game {
    public static final class GameBuilder {
        private final Set<Class<? extends GameModule>> modules;
        private final Map<Class<? extends GameModule>, Set<Consumer<? extends GameModule>>> moduleInitializers;

        private int sleepInterval;
        private float fixedUpdatesPerSecond;
        private float maxFixedUpdateBacklog;

        private GameBuilder() {
            modules = new HashSet<>();
            moduleInitializers = new HashMap<>();

            sleepInterval = 5;
            fixedUpdatesPerSecond = 60;
            maxFixedUpdateBacklog = 1;

            modules.add(CoreModule.class);
        }

        public GameBuilder addModule(Class<? extends GameModule> module) {
            modules.add(module);
            return this;
        }

        public <T extends GameModule> GameBuilder addModule(Class<T> module, Consumer<T> initFn) {
            addModule(module);
            moduleInitializers.computeIfAbsent(module, (k) -> new HashSet<>()).add(initFn);
            return this;
        }

        public GameBuilder setSleepInterval(int sleepInterval) {
            this.sleepInterval = sleepInterval;
            return this;
        }

        public GameBuilder setFixedUpdatesPerSecond(float fixedUpdatesPerSecond) {
            this.fixedUpdatesPerSecond = fixedUpdatesPerSecond;
            return this;
        }

        public GameBuilder setMaxFixedUpdateBacklog(float maxFixedUpdateBacklog) {
            this.maxFixedUpdateBacklog = maxFixedUpdateBacklog;
            return this;
        }

        public Game build() {
            return new Game(this);
        }
    }

    public static final class WorldBuilder {
        private final Game game;

        private final Set<Class<? extends GameSystem>> systems;

        private WorldBuilder(Game game) {
            this.game = game;
            systems = new HashSet<>();
        }

        public WorldBuilder addSystem(Class<? extends GameSystem> system) {
            systems.add(system);
            return this;
        }

        public World build() {
            World world = new World(systems);
            game.addWorld(world);
            return world;
        }
    }

    public static GameBuilder builder() {
        return new GameBuilder();
    }

    private static Game INSTANCE;

    private final Map<FixedProfileStage, ProfileNode> stageProfileData;
    private final Map<Class<? extends Event>, ProfileNode> eventProfileData;
    private Profiler profiler;

    private final InstanceMap<GameModule> modules;

    private final Queue<Event> eventQueue;
    private final InputManager inputManager;

    private final List<World> worlds;
    private final List<World> uninitializedWorlds;

    private float fixedUpdatesPerSecond;
    private float maxFixedUpdateBacklog;
    private int sleepInterval;
    private boolean running;

    public static Game get() {
        return INSTANCE;
    }

    private Game(GameBuilder builder) {
        if (INSTANCE != null)
            throw new IllegalStateException("Game already running");
        INSTANCE = this;

        setFixedUpdatesPerSecond(builder.fixedUpdatesPerSecond);
        sleepInterval = builder.sleepInterval;
        maxFixedUpdateBacklog = builder.maxFixedUpdateBacklog;

        stageProfileData = new EnumMap<>(FixedProfileStage.class);
        eventProfileData = new HashMap<>();

        eventQueue = new ConcurrentLinkedQueue<>();
        inputManager = new InputManager();

        modules = new InstanceMap<>();

        worlds = new ArrayList<>();
        uninitializedWorlds = new ArrayList<>();

        // Initialize modules
        Queue<Class<? extends GameModule>> moduleQueue = new ArrayDeque<>(builder.modules);
        while (!moduleQueue.isEmpty()) {
            Class<? extends GameModule> type = moduleQueue.remove();

            if (modules.hasInstance(type))
                continue;

            GameModule instance;
            try {
                Constructor<? extends GameModule> constructor = type.getConstructor();
                instance = constructor.newInstance();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Could not find no-args constructor in " + type, e);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to instantiate module: " + type, e);
            }

            modules.setInstance(instance);
            Set<Consumer<? extends GameModule>> initializers = builder.moduleInitializers.get(type);
            if (initializers != null) {
                for (Consumer<? extends GameModule> initializer : initializers) {
                    callInitializer(initializer, instance);
                }
            }

            moduleQueue.addAll(instance.getDependencies());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void callInitializer(Consumer<? extends GameModule> initFn, GameModule instance) {
        ((Consumer) initFn).accept(instance);
    }

    public WorldBuilder worldBuilder() {
        WorldBuilder builder = new WorldBuilder(this);
        for (GameModule module : modules.instances()) {
            module.initializeWorld(builder);
        }
        return builder;
    }

    private void init() {
        System.out.println("Starting engine version " + EngineVersion.VERSION);

        Profiler temp = profiler;
        profiler = new Profiler();

        profiler.begin("Init");

        profiler.push("Initialize modules");
        for (GameModule module : modules.instances()) {
            profiler.push(module.getClass().getSimpleName());
            module.init();
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

        Collection<GameModule> modules = this.modules.instances();

        profiler.push("Pre-update modules");
        for (GameModule module : modules) {
            profiler.push(module.getClass().getSimpleName());
            module.preUpdate();
            profiler.pop();
        }
        profiler.pop();

        profiler.push("Update worlds");
        for (World world : worlds) {
            world.update(delta);
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

        Collection<GameModule> modules = this.modules.instances();

        profiler.push("Pre-fixed-update modules");
        for (GameModule module : modules) {
            profiler.push(module.getClass().getSimpleName());
            module.preFixedUpdate();
            profiler.pop();
        }
        profiler.pop();

        profiler.push("Fixed-update worlds");
        for (World world : worlds) {
            world.fixedUpdate();
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

        for (World world : worlds) {
            world.close();
        }

        for (GameModule module : modules.instances()) {
            module.close();
        }
    }

    public void run() {
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

            profiler.push("Initialize new worlds");
            for (World world : uninitializedWorlds) {
                world.init();
                worlds.add(world);
            }
            uninitializedWorlds.clear();
            profiler.pop();

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

    private void addWorld(World world) {
        uninitializedWorlds.add(world);
    }

    // Queues an event to be dispatched next frame
    public void postGlobalEvent(Event event) {
        eventQueue.add(event);
    }

    private void dispatchEvent(Event event) {
        Profiler temp = profiler;
        profiler = new Profiler();
        profiler.begin(event.getClass().getSimpleName());

        for (World world : worlds) {
            world.postImmediateEvent(event);
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

    public <T extends GameModule> T getModule(Class<T> type) {
        return modules.getInstance(type);
    }
}
