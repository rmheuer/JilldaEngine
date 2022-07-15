package com.github.rmheuer.vulkantest;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.util.shaderc.Shaderc.*;

public final class ShaderCompiler {
    public enum Type {
        Vertex,
        Fragment
    }

    private static long compiler;

    public static void initialize() {
        compiler = shaderc_compiler_initialize();
    }

    public static ByteBuffer compile(ResourceFile res, Type type) {
        String src;
        try {
            src = res.readAsString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long compiler = shaderc_compiler_initialize();
        long result = shaderc_compile_into_spv(compiler, src, type == Type.Vertex ? shaderc_glsl_vertex_shader : shaderc_glsl_fragment_shader, res.getName(), "main", 0L);

        if (shaderc_result_get_num_warnings(result) > 0 || shaderc_result_get_num_errors(result) > 0) {
            System.err.println(shaderc_result_get_error_message(result));
            throw new RuntimeException("Shader compilation failed");
        }

        ByteBuffer data = shaderc_result_get_bytes(result);
        ByteBuffer copy = BufferUtils.createByteBuffer(data.capacity());
        byte[] buf = new byte[data.capacity()];
        data.get(buf);
        copy.put(buf);
        copy.flip();

        shaderc_result_release(result);
        return copy;
    }

    public static void cleanUp() {
        shaderc_compiler_release(compiler);
    }
}
