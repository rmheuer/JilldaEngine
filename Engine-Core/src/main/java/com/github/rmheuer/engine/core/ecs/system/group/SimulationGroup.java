package com.github.rmheuer.engine.core.ecs.system.group;

import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.annotation.RunInGroup;

@RunInGroup(RootGroup.class)
@After(InitializationGroup.class)
public final class SimulationGroup implements SystemGroup {
}
