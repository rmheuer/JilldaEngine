package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.render.mesh.MeshDataUsage;
import com.github.rmheuer.engine.render.mesh.PrimitiveType;
import com.github.rmheuer.engine.render.shader.ShaderType;
import com.github.rmheuer.engine.render.texture.TextureFilter;

import static org.lwjgl.opengl.GL33C.*;

public final class GLEnumConversions {
    public static int getGlTextureFilter(TextureFilter filter) {
        switch (filter) {
            case LINEAR:
                return GL_LINEAR;
            case NEAREST:
                return GL_NEAREST;
            default:
                throw new IllegalArgumentException(String.valueOf(filter));
        }
    }

    public static int getGlShaderType(ShaderType type) {
        switch (type) {
            case VERTEX:
                return GL_VERTEX_SHADER;
            case FRAGMENT:
                return GL_FRAGMENT_SHADER;
            default:
                throw new IllegalArgumentException("Invalid shader type: " + type);
        }
    }

    public static int getGlPrimitiveType(PrimitiveType type) {
        switch (type) {
            case POINTS: return GL_POINTS;
            case LINE_STRIP: return GL_LINE_STRIP;
            case LINE_LOOP: return GL_LINE_LOOP;
            case LINES: return GL_LINES;
            case TRIANGLE_STRIP: return GL_TRIANGLE_STRIP;
            case TRIANGLE_FAN: return GL_TRIANGLE_FAN;
            case TRIANGLES: return GL_TRIANGLES;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getGlMeshDataUsage(MeshDataUsage usage) {
        switch (usage) {
            case STATIC: return GL_STATIC_DRAW;
            case DYNAMIC: return GL_DYNAMIC_DRAW;
            case STREAM: return GL_STREAM_DRAW;
            default:
                throw new IllegalArgumentException();
        }
    }

    private GLEnumConversions() {
        throw new AssertionError();
    }
}
