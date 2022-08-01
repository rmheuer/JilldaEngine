package com.github.rmheuer.engine.render.opengl.lwjgl;

import com.github.rmheuer.engine.render.opengl.OpenGL;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33C.*;

public final class LwjglOpenGL extends OpenGL {
    public LwjglOpenGL() {
        FALSE = GL_FALSE;

        UNSIGNED_BYTE = GL_UNSIGNED_BYTE;
        UNSIGNED_INT = GL_UNSIGNED_INT;
        FLOAT = GL_FLOAT;

        COLOR_BUFFER_BIT = GL_COLOR_BUFFER_BIT;
        DEPTH_BUFFER_BIT = GL_DEPTH_BUFFER_BIT;
        DEPTH_TEST = GL_DEPTH_TEST;
        BLEND = GL_BLEND;
        SRC_ALPHA = GL_SRC_ALPHA;
        ONE_MINUS_SRC_ALPHA = GL_ONE_MINUS_SRC_ALPHA;

        TEXTURE_2D = GL_TEXTURE_2D;
        TEXTURE_CUBE_MAP = GL_TEXTURE_CUBE_MAP;
        TEXTURE_CUBE_MAP_POSITIVE_X = GL_TEXTURE_CUBE_MAP_POSITIVE_X;
        TEXTURE_CUBE_MAP_POSITIVE_Y = GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
        TEXTURE_CUBE_MAP_POSITIVE_Z = GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
        TEXTURE_CUBE_MAP_NEGATIVE_X = GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
        TEXTURE_CUBE_MAP_NEGATIVE_Y = GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
        TEXTURE_CUBE_MAP_NEGATIVE_Z = GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
        TEXTURE_WRAP_R = GL_TEXTURE_WRAP_R;
        TEXTURE_WRAP_S = GL_TEXTURE_WRAP_S;
        TEXTURE_WRAP_T = GL_TEXTURE_WRAP_T;
        CLAMP_TO_EDGE = GL_CLAMP_TO_EDGE;
        RGBA = GL_RGBA;
        TEXTURE_MAG_FILTER = GL_TEXTURE_MAG_FILTER;
        TEXTURE_MIN_FILTER = GL_TEXTURE_MIN_FILTER;
        TEXTURE0 = GL_TEXTURE0;
        LINEAR = GL_LINEAR;
        NEAREST = GL_NEAREST;

        VERTEX_SHADER = GL_VERTEX_SHADER;
        FRAGMENT_SHADER = GL_FRAGMENT_SHADER;

        POINTS = GL_POINTS;
        LINE_STRIP = GL_LINE_STRIP;
        LINE_LOOP = GL_LINE_LOOP;
        LINES = GL_LINES;
        TRIANGLE_STRIP = GL_TRIANGLE_STRIP;
        TRIANGLE_FAN = GL_TRIANGLE_FAN;
        TRIANGLES = GL_TRIANGLES;

        ARRAY_BUFFER = GL_ARRAY_BUFFER;
        ELEMENT_ARRAY_BUFFER = GL_ELEMENT_ARRAY_BUFFER;
        STATIC_DRAW = GL_STATIC_DRAW;
        DYNAMIC_DRAW = GL_DYNAMIC_DRAW;
        STREAM_DRAW = GL_STREAM_DRAW;

        COMPILE_STATUS = GL_COMPILE_STATUS;
        LINK_STATUS = GL_LINK_STATUS;
    }

    @Override
    public void viewport(int x, int y, int w, int h) {
        glViewport(x, y, w, h);
    }

    @Override
    public void clearColor(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
    }

    @Override
    public void clear(int flags) {
        glClear(flags);
    }

    @Override
    public void enable(int op) {
        glEnable(op);
    }

    @Override
    public void blendFunc(int src, int dst) {
        glBlendFunc(src, dst);
    }

    @Override
    public int genTextures() {
        return glGenTextures();
    }

    @Override
    public void bindTexture(int target, int id) {
        glBindTexture(target, id);
    }

    @Override
    public void texParameteri(int target, int param, int value) {
        glTexParameteri(target, param, value);
    }

    @Override
    public void texImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, int[] pixels) {
        glTexImage2D(target, level, internalFormat, width, height, border, format, type, pixels);
    }

    @Override
    public void activeTexture(int slot) {
        glActiveTexture(slot);
    }

    @Override
    public void deleteTextures(int id) {
        glDeleteTextures(id);
    }

    @Override
    public int genVertexArrays() {
        return glGenVertexArrays();
    }

    @Override
    public int genBuffers() {
        return glGenBuffers();
    }

    @Override
    public void bindVertexArray(int id) {
        glBindVertexArray(id);
    }

    @Override
    public void vertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long pointer) {
        glVertexAttribPointer(index, size, type, normalized, stride, pointer);
    }

    @Override
    public void enableVertexAttribArray(int index) {
        glEnableVertexAttribArray(index);
    }

    @Override
    public void bindBuffer(int target, int id) {
        glBindBuffer(target, id);
    }

    @Override
    public void bufferData(int target, ByteBuffer buf, int usage) {
        glBufferData(target, buf, usage);
    }

    @Override
    public void bufferData(int target, IntBuffer buf, int usage) {
        glBufferData(target, buf, usage);
    }

    @Override
    public void deleteBuffers(int id) {
        glDeleteBuffers(id);
    }

    @Override
    public void deleteVertexArrays(int id) {
        glDeleteVertexArrays(id);
    }

    @Override
    public void drawElements(int mode, int count, int type, long indices) {
        glDrawElements(mode, count, type, indices);
    }

    @Override
    public void drawArrays(int mode, int first, int count) {
        glDrawArrays(mode, first, count);
    }

    @Override
    public int createShader(int type) {
        return glCreateShader(type);
    }

    @Override
    public void shaderSource(int id, CharSequence source) {
        glShaderSource(id, source);
    }

    @Override
    public void compileShader(int id) {
        glCompileShader(id);
    }

    @Override
    public int getShaderi(int id, int pName) {
        return glGetShaderi(id, pName);
    }

    @Override
    public String getShaderInfoLog(int id) {
        return glGetShaderInfoLog(id);
    }

    @Override
    public void deleteShader(int id) {
        glDeleteShader(id);
    }

    @Override
    public int createProgram() {
        return glCreateProgram();
    }

    @Override
    public void attachShader(int id, int shader) {
        glAttachShader(id, shader);
    }

    @Override
    public void linkProgram(int id) {
        glLinkProgram(id);
    }

    @Override
    public int getProgrami(int id, int pName) {
        return glGetProgrami(id, pName);
    }

    @Override
    public String getProgramInfoLog(int id) {
        return glGetProgramInfoLog(id);
    }

    @Override
    public void useProgram(int id) {
        glUseProgram(id);
    }

    @Override
    public void deleteProgram(int id) {
        glDeleteProgram(id);
    }

    @Override
    public int getUniformLocation(int id, String name) {
        return glGetUniformLocation(id, name);
    }

    @Override
    public void uniform1f(int loc, float f) {
        glUniform1f(loc, f);
    }

    @Override
    public void uniform2f(int loc, float f, float f2) {
        glUniform2f(loc, f, f2);
    }

    @Override
    public void uniform3f(int loc, float f, float f2, float f3) {
        glUniform3f(loc, f, f2, f3);
    }

    @Override
    public void uniform4f(int loc, float f, float f2, float f3, float f4) {
        glUniform4f(loc, f, f2, f3, f4);
    }

    @Override
    public void uniform1i(int loc, int i) {
        glUniform1i(loc, i);
    }

    @Override
    public void uniformMatrix4fv(int loc, boolean transpose, float[] data) {
        glUniformMatrix4fv(loc, transpose, data);
    }
}
