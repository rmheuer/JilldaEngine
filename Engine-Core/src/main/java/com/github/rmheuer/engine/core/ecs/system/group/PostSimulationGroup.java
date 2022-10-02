package com.github.rmheuer.engine.core.ecs.system.group;

import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.annotation.RunInGroup;

@After(InitializationGroup.class)
@After(SimulationGroup.class)
@RunInGroup(RootGroup.class)
public final class PostSimulationGroup implements SystemGroup {
}
