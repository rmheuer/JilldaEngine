package com.github.rmheuer.engine.render.shader;

import com.github.rmheuer.engine.core.util.SizeOf;

public enum AttribType {
    FLOAT(1),
    VEC2(2),
    VEC3(3),
    VEC4(4);

    private final int elemCount;

    AttribType(int elemCount) {
	this.elemCount = elemCount;
    }

    public int getElemCount() {
	return elemCount;
    }

    public int sizeOf() {
        return elemCount * SizeOf.FLOAT;
    }
}
