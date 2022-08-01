package com.github.rmheuer.engine.render.shader;

import com.github.rmheuer.engine.core.nat.NativeObject;
import com.github.rmheuer.engine.core.nat.NativeObjectManager;
import com.github.rmheuer.engine.render.RenderBackend;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class ShaderProgram {
    private final Shader[] shaders;
    private final Map<String, ShaderUniform> uniforms;
    private Native nat;

    public ShaderProgram(Shader... shaders) {
        this.shaders = shaders;
        uniforms = new HashMap<>();
    }

    public Shader[] getShaders() {
        return Arrays.copyOf(shaders, shaders.length);
    }

    public ShaderUniform getUniform(String name) {
        return uniforms.computeIfAbsent(name, ShaderUniform::new);
    }

    public Collection<ShaderUniform> getUniforms() {
        return uniforms.values();
    }

    public interface Native extends NativeObject {
        void bind();
        void unbind();

        void updateUniformValues();
    }

    public Native getNative(NativeObjectManager mgr) {
        if (nat == null) {
            Shader.Native[] shaderNatives = new Shader.Native[shaders.length];
            for (int i = 0; i < shaders.length; i++) {
                shaderNatives[i] = shaders[i].getNative(mgr);
            }
            nat = RenderBackend.get().createShaderProgramNative(this, shaderNatives);
            mgr.registerObject(nat);
        }

        return nat;
    }
}
