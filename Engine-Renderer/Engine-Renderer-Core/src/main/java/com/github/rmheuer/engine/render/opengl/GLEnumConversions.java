package com.github.rmheuer.engine.render.opengl;

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

    private GLEnumConversions() {
        throw new AssertionError();
    }
}
