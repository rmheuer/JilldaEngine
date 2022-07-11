package com.github.rmheuer.engine.render;

public final class RendererAPI {
    private static RenderBackend backend;

    public static void setBackend(RenderBackend backend) {
        RendererAPI.backend = backend;
    }

    public static RenderBackend getBackend() {
        return backend;
    }
}
