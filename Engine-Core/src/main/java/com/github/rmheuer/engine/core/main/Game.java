package com.github.rmheuer.engine.core.main;

import com.github.rmheuer.engine.core.asset.AssetManager;
import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.schedule.Stage;
import com.github.rmheuer.engine.core.ecs.system.schedule.SystemScheduler;
import com.github.rmheuer.engine.core.event.Event;
import com.github.rmheuer.engine.core.event.EventDispatcher;
import com.github.rmheuer.engine.core.input.InputManager;
import com.github.rmheuer.engine.core.serial.codec.bin.BinarySerialCodec;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.obj.ObjectSerializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class Game {
    private static final Game INSTANCE = new Game();

    private float fixedUpdatesPerSecond;
    private int sleepInterval;
    private boolean running;

    private World world;
    private SystemScheduler schedule;
    private Queue<Event> eventQueue;
    private InputManager inputManager;
    private AssetManager assetManager;

    public static Game get() {
        return INSTANCE;
    }

    private Game() {
        fixedUpdatesPerSecond = 60;
        sleepInterval = 5;
    }

    private Set<GameSystem> loadSystems() {
        // TODO: Use Resource API once it is written

        try {
            SerialNode node = BinarySerialCodec.get()
                    .decode(getClass().getClassLoader().getResourceAsStream("systems.bin"));

            System.out.println(node);
            GameSystem[] systems = ObjectSerializer.get().deserialize(node, GameSystem[].class);
            System.out.println(Arrays.toString(systems));

            return new HashSet<>(Arrays.asList(systems));
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptySet();
        }
    }

    private void init() {
        eventQueue = new ConcurrentLinkedQueue<>();
        inputManager = new InputManager();
        assetManager = new AssetManager();

        Set<GameSystem> systems = loadSystems();
        world = new World(systems);

        world.init();
    }

    private void update(float delta) {
        world.update(delta);
    }

    private void fixedUpdate() {
        world.fixedUpdate();
    }

    private void close() {
        world.close();
    }

    public void run() {
        running = true;
        init();

        long previousTime = System.nanoTime();
        float unprocessedTime = 0;
        while (running) {
            long currentTime = System.nanoTime();
            long passedTime = currentTime - previousTime;
            float delta = passedTime / 1_000_000_000.0f;
            unprocessedTime += delta;
            previousTime = currentTime;

            Event event;
            while ((event = eventQueue.poll()) != null) {
                dispatchEvent(event);
            }

            float secondsPerFixedUpdate = 1 / fixedUpdatesPerSecond;
            while (unprocessedTime > secondsPerFixedUpdate) {
                fixedUpdate();

                unprocessedTime -= secondsPerFixedUpdate;
            }

            update(delta);

            if (sleepInterval != 0) {
                try {
                    Thread.sleep(sleepInterval);
                } catch (InterruptedException e) {
                    running = false;
                }
            }
        }

        close();
    }

    // Queues an event to be dispatched next frame
    public void postEvent(Event event) {
        eventQueue.add(event);
    }

    private void dispatchEvent(Event event) {
        EventDispatcher dispatch = new EventDispatcher(event);
        world.onEvent(dispatch);
    }

    // Dispatches an event immediately
    public void postImmediateEvent(Event event) {
        dispatchEvent(event);
    }

    public void stop() {
        running = false;
    }

    public float getFixedUpdatesPerSecond() {
        return fixedUpdatesPerSecond;
    }

    public void setFixedUpdatesPerSecond(float fixedUpdatesPerSecond) {
        this.fixedUpdatesPerSecond = fixedUpdatesPerSecond;
    }

    public int getSleepInterval() {
        return sleepInterval;
    }

    public void setSleepInterval(int sleepInterval) {
        this.sleepInterval = sleepInterval;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
