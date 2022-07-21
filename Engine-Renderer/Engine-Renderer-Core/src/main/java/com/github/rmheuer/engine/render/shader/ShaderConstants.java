package com.github.rmheuer.engine.render.shader;

public final class ShaderConstants {
    public static final String UNIFORM_NAME_PROJECTION = "u_Projection";
    public static final String UNIFORM_NAME_VIEW       = "u_View";
    public static final String UNIFORM_NAME_TRANSFORM  = "u_Transform";

    private ShaderConstants() {
        throw new AssertionError();
    }
}
