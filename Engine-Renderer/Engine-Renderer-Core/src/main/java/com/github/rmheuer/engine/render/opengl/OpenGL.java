package com.github.rmheuer.engine.render.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public abstract class OpenGL {
    public int FALSE;

    public int UNSIGNED_BYTE;
    public int UNSIGNED_INT;
    public int FLOAT;

    public int COLOR_BUFFER_BIT;
    public int DEPTH_BUFFER_BIT;
    public int DEPTH_TEST;
    public int BLEND;
    public int SRC_ALPHA;
    public int ONE_MINUS_SRC_ALPHA;

    public int TEXTURE_2D;
    public int TEXTURE_CUBE_MAP;
    public int TEXTURE_CUBE_MAP_POSITIVE_X;
    public int TEXTURE_CUBE_MAP_POSITIVE_Y;
    public int TEXTURE_CUBE_MAP_POSITIVE_Z;
    public int TEXTURE_CUBE_MAP_NEGATIVE_X;
    public int TEXTURE_CUBE_MAP_NEGATIVE_Y;
    public int TEXTURE_CUBE_MAP_NEGATIVE_Z;
    public int TEXTURE_WRAP_R;
    public int TEXTURE_WRAP_S;
    public int TEXTURE_WRAP_T;
    public int CLAMP_TO_EDGE;
    public int RGBA;
    public int TEXTURE_MAG_FILTER;
    public int TEXTURE_MIN_FILTER;
    public int TEXTURE0;
    public int LINEAR;
    public int NEAREST;

    public int VERTEX_SHADER;
    public int FRAGMENT_SHADER;

    public int POINTS;
    public int LINE_STRIP;
    public int LINE_LOOP;
    public int LINES;
    public int TRIANGLE_STRIP;
    public int TRIANGLE_FAN;
    public int TRIANGLES;

    public int ARRAY_BUFFER;
    public int ELEMENT_ARRAY_BUFFER;
    public int STATIC_DRAW;
    public int DYNAMIC_DRAW;
    public int STREAM_DRAW;

    public int COMPILE_STATUS;
    public int LINK_STATUS;

    public abstract void viewport(int x, int y, int w, int h);
    public abstract void clearColor(float r, float g, float b, float a);
    public abstract void clear(int flags);
    public abstract void enable(int op);
    public abstract void blendFunc(int src, int dst);

    public abstract int genTextures();
    public abstract void bindTexture(int target, int id);
    public abstract void texParameteri(int target, int param, int value);
    public abstract void texImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, int[] pixels);
    public abstract void activeTexture(int slot);
    public abstract void deleteTextures(int id);

    public abstract int genVertexArrays();
    public abstract int genBuffers();
    public abstract void bindVertexArray(int id);
    public abstract void vertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long pointer);
    public abstract void enableVertexAttribArray(int index);
    public abstract void bindBuffer(int target, int id);
    public abstract void bufferData(int target, ByteBuffer buf, int usage);
    public abstract void bufferData(int target, IntBuffer buf, int usage);
    public abstract void deleteBuffers(int id);
    public abstract void deleteVertexArrays(int id);

    public abstract void drawElements(int mode, int count, int type, long indices);
    public abstract void drawArrays(int mode, int first, int count);

    public abstract int createShader(int type);
    public abstract void shaderSource(int id, CharSequence source);
    public abstract void compileShader(int id);
    public abstract int getShaderi(int id, int pName);
    public abstract String getShaderInfoLog(int id);
    public abstract void deleteShader(int id);

    public abstract int createProgram();
    public abstract void attachShader(int id, int shader);
    public abstract void linkProgram(int id);
    public abstract int getProgrami(int id, int pName);
    public abstract String getProgramInfoLog(int id);
    public abstract void useProgram(int id);
    public abstract void deleteProgram(int id);

    public abstract int getUniformLocation(int id, String name);
    public abstract void uniform1f(int loc, float f);
    public abstract void uniform2f(int loc, float f, float f2);
    public abstract void uniform3f(int loc, float f, float f2, float f3);
    public abstract void uniform4f(int loc, float f, float f2, float f3, float f4);
    public abstract void uniform1i(int loc, int i);
    public abstract void uniformMatrix4fv(int loc, boolean transpose, float[] data);
}
