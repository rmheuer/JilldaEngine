package com.github.rmheuer.engine.render.texture;

public enum TextureFilter {
    /**
     * Selects the nearest pixel when interpolating.
     */
    NEAREST,

    /**
     * Uses linear interpolation to blend between pixels
     * when interpolating.
     */
    LINEAR
}
