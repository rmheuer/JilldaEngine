package com.github.rmheuer.engine.core.ecs.system.schedule;

import com.github.rmheuer.engine.core.ecs.system.GameSystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public final class SystemScheduler {
    private final Map<Stage, List<GameSystem>> schedules;

    public SystemScheduler(Set<? extends GameSystem> systems) {
        schedules = new HashMap<>();

        for (Stage stage : Stage.values()) {
            StageScheduler scheduler = new StageScheduler(stage);
            List<GameSystem> schedule = scheduler.schedule(systems);

            System.out.println("System order for stage " + stage + ":");
            for (GameSystem system : schedule) {
                System.out.println("  - " + system.getClass().getSimpleName());
            }

            schedules.put(stage, schedule);
        }
    }

    public void doStage(Stage stage, Consumer<GameSystem> executor, boolean handlePause) {
        schedules.get(stage).forEach(executor);
    }
}
