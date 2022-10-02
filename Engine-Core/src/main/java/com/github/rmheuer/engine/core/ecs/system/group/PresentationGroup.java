package com.github.rmheuer.engine.core.ecs.system.group;

import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.annotation.RunInGroup;

@RunInGroup(RootGroup.class)
@After(InitializationGroup.class)
@After(SimulationGroup.class)
@After(PostSimulationGroup.class)
public final class PresentationGroup implements SystemGroup {
}
