package com.github.rmheuer.engine.core.ecs.entity;

import com.github.rmheuer.engine.core.ecs.component.Component;

public final class Name implements Component {
    private String name;

    public Name() {
        name = "Unnamed Entity";
    }

    public Name(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
