package com.github.rmheuer.engine.render.texture;

import com.github.rmheuer.engine.core.asset.Asset;

public abstract class Texture extends Asset {
    public abstract void bindToSlot(int slot);
}
