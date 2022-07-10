package com.github.rmheuer.engine.render.shader;

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
}
