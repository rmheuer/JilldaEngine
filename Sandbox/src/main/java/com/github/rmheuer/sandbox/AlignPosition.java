package com.github.rmheuer.sandbox;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.core.transform.Transform;

public final class AlignPosition implements Component {
    public Transform target;

    public AlignPosition() {}

    public AlignPosition(Transform target) {
        this.target = target;
    }
}
