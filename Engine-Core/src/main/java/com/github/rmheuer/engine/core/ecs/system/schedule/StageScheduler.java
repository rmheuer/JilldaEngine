package com.github.rmheuer.engine.core.ecs.system.schedule;

import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.SystemGroup;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.annotation.Before;
import com.github.rmheuer.engine.core.ecs.system.annotation.RunInGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class StageScheduler {
    private final Stage stage;
    private final Map<Class<? extends GameSystem>, ScheduleConstraints> constraintCache;

    public StageScheduler(Stage stage) {
        this.stage = stage;
        constraintCache = new HashMap<>();
    }

    public List<GameSystem> schedule(Set<? extends GameSystem> systems) {
        Map<Class<? extends GameSystem>, GameSystem> instances = new LinkedHashMap<>();
        for (GameSystem system : systems) {
            Class<? extends GameSystem> type = system.getClass();
            if (instances.containsKey(type)) {
                throw new SchedulingException("Multiple instances of " + type);
            }

            instances.put(type, system);
        }

        Map<Class<? extends GameSystem>, Set<Class<? extends GameSystem>>> edges = new HashMap<>();
        for (Class<? extends GameSystem> systemType : instances.keySet()) {
            ScheduleConstraints constraints = getConstraints(systemType);

            Set<Class<? extends GameSystem>> afterSet = edges.computeIfAbsent(systemType, (t) -> new HashSet<>());
            afterSet.addAll(constraints.getAfter());

            for (Class<? extends GameSystem> before : constraints.getBefore()) {
                Set<Class<? extends GameSystem>> parentBeforeSet = edges.computeIfAbsent(before, (t) -> new HashSet<>());
                parentBeforeSet.add(systemType);
            }
        }

        List<Class<? extends GameSystem>> unmarked = new ArrayList<>(instances.keySet());
        Set<Class<? extends GameSystem>> tempMarked = new HashSet<>();
        List<Class<? extends GameSystem>> outputTypes = new ArrayList<>();
        while (!unmarked.isEmpty()) {
            Class<? extends GameSystem> n = unmarked.get(0);
            visit(n, edges, unmarked, tempMarked, outputTypes);
        }

        List<GameSystem> output = new ArrayList<>();
        for (Class<? extends GameSystem> type : outputTypes) {
            output.add(instances.get(type));
        }

        return output;
    }

    private void visit(
            Class<? extends GameSystem> node,
            Map<Class<? extends GameSystem>, Set<Class<? extends GameSystem>>> edges,
            List<Class<? extends GameSystem>> unmarked,
            Set<Class<? extends GameSystem>> tempMarked,
            List<Class<? extends GameSystem>> output
    ) {
        if (!unmarked.contains(node))
            return;
        if (tempMarked.contains(node))
            throw new SchedulingException("Unsolvable constraints");

        if (edges.containsKey(node)) {
            tempMarked.add(node);
            for (Class<? extends GameSystem> m : edges.get(node)) {
                visit(m, edges, unmarked, tempMarked, output);
            }
            tempMarked.remove(node);
        }

        unmarked.remove(node);
        output.add(node);
    }

    private ScheduleConstraints getConstraints(Class<? extends GameSystem> type) {
        return constraintCache.computeIfAbsent(type, this::extractConstraints);
    }

    private ScheduleConstraints extractConstraints(Class<? extends GameSystem> type) {
        Before[] befores = type.getAnnotationsByType(Before.class);
        After[] afters = type.getAnnotationsByType(After.class);
        RunInGroup[] groups = type.getAnnotationsByType(RunInGroup.class);

        Set<Class<? extends GameSystem>> beforeSet = new HashSet<>();
        for (Before before : befores) {
            if (before.stage() == stage) {
                beforeSet.add(before.before());
            }
        }

        Set<Class<? extends GameSystem>> afterSet = new HashSet<>();
        for (After after : afters) {
            if (after.stage() == stage) {
                afterSet.add(after.after());
            }
        }

        Class<? extends SystemGroup> groupType = null;
        for (RunInGroup group : groups) {
            if (group.stage() == stage) {
                if (groupType == null) {
                    groupType = group.group();
                } else {
                    throw new SchedulingException("Class is in multiple groups: " + type);
                }
            }
        }

        return new ScheduleConstraints(beforeSet, afterSet, groupType);
    }
}
