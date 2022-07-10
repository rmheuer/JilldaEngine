package com.github.rmheuer.engine.core.ecs.system.schedule;

import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.SystemGroup;

import java.util.Set;

public final class ScheduleConstraints {
    private final Set<Class<? extends GameSystem>> before;
    private final Set<Class<? extends GameSystem>> after;
    private final Class<? extends SystemGroup> group;

    public ScheduleConstraints(Set<Class<? extends GameSystem>> before, Set<Class<? extends GameSystem>> after, Class<? extends SystemGroup> group) {
	this.before = before;
	this.after = after;
	this.group = group;
    }

    public Set<Class<? extends GameSystem>> getBefore() {
	return before;
    }

    public Set<Class<? extends GameSystem>> getAfter() {
	return after;
    }

    public Class<? extends SystemGroup> getGroup() {
	return group;
    }
}
