package com.github.rmheuer.sandbox.test3d;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.core.math.Vector3f;

public class Spin implements Component {
    public Vector3f speeds = new Vector3f(0, 1, 0);
}
