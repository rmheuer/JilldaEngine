package com.github.rmheuer.engine.core.ecs.system.group;

import com.github.rmheuer.engine.core.ecs.system.annotation.RunInGroup;

@RunInGroup(RootGroup.class)
public final class SimulationGroup implements SystemGroup {
}
