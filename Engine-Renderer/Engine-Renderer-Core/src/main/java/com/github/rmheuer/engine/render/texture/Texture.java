package com.github.rmheuer.engine.render.texture;

import com.github.rmheuer.engine.core.nat.NativeObject;
import com.github.rmheuer.engine.core.nat.NativeObjectManager;

public interface Texture {
    interface Native extends NativeObject {
        void bindToSlot(int slot);
    }

    Native getNative(NativeObjectManager mgr);
}
