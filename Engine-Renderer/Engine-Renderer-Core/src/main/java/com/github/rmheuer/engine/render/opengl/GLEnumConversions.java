package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.render.*;
import com.github.rmheuer.engine.render.mesh.MeshDataUsage;
import com.github.rmheuer.engine.render.mesh.PrimitiveType;
import com.github.rmheuer.engine.render.shader.ShaderType;
import com.github.rmheuer.engine.render.texture.TextureFilter;

public final class GLEnumConversions {
    public static int getGlTextureFilter(OpenGL gl, TextureFilter filter) {
        switch (filter) {
            case LINEAR:
                return gl.LINEAR;
            case NEAREST:
                return gl.NEAREST;
            default:
                throw new IllegalArgumentException(String.valueOf(filter));
        }
    }

    public static int getGlShaderType(OpenGL gl, ShaderType type) {
        switch (type) {
            case VERTEX:
                return gl.VERTEX_SHADER;
            case FRAGMENT:
                return gl.FRAGMENT_SHADER;
            default:
                throw new IllegalArgumentException("Invalid shader type: " + type);
        }
    }

    public static int getGlPrimitiveType(OpenGL gl, PrimitiveType type) {
        switch (type) {
            case POINTS: return gl.POINTS;
            case LINE_STRIP: return gl.LINE_STRIP;
            case LINE_LOOP: return gl.LINE_LOOP;
            case LINES: return gl.LINES;
            case TRIANGLE_STRIP: return gl.TRIANGLE_STRIP;
            case TRIANGLE_FAN: return gl.TRIANGLE_FAN;
            case TRIANGLES: return gl.TRIANGLES;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getGlMeshDataUsage(OpenGL gl, MeshDataUsage usage) {
        switch (usage) {
            case STATIC: return gl.STATIC_DRAW;
            case DYNAMIC: return gl.DYNAMIC_DRAW;
            case STREAM: return gl.STREAM_DRAW;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getGlBufferType(OpenGL gl, BufferType type) {
        switch (type) {
            case COLOR: return gl.COLOR_BUFFER_BIT;
            case DEPTH: return gl.DEPTH_BUFFER_BIT;
            case STENCIL: return gl.STENCIL_BUFFER_BIT;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getGlWindingOrder(OpenGL gl, WindingOrder order) {
        switch (order) {
            case CLOCKWISE: return gl.CW;
            case COUNTERCLOCKWISE: return gl.CCW;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getGlCullMode(OpenGL gl, CullMode mode) {
        switch (mode) {
            case FRONT: return gl.FRONT;
            case BACK: return gl.BACK;
            case FRONT_AND_BACK: return gl.FRONT_AND_BACK;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getGlStencilFunc(OpenGL gl, StencilFunc func) {
        switch (func) {
            case NEVER: return gl.NEVER;
            case LESS: return gl.LESS;
            case GREATER: return gl.GREATER;
            case LEQUAL: return gl.LEQUAL;
            case GEQUAL: return gl.GEQUAL;
            case EQUAL: return gl.EQUAL;
            case NOTEQUAL: return gl.NOTEQUAL;
            case ALWAYS: return gl.ALWAYS;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getGlStencilOp(OpenGL gl, StencilOp op) {
        switch (op) {
            case KEEP: return gl.KEEP;
            case ZERO: return gl.ZERO;
            case REPLACE: return gl.REPLACE;
            case INCREMENT: return gl.INCR;
            case INCREMENT_WRAP: return gl.INCR_WRAP;
            case DECREMENT: return gl.DECR;
            case DECREMENT_WRAP: return gl.DECR_WRAP;
            case INVERT: return gl.INVERT;
            default:
                throw new IllegalArgumentException();
        }
    }

    private GLEnumConversions() {
        throw new AssertionError();
    }
}
