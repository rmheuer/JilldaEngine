package com.github.rmheuer.engine.render.shader;

import com.github.rmheuer.engine.core.nat.NativeObject;
import com.github.rmheuer.engine.core.nat.NativeObjectManager;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.render.RenderBackend;

import java.io.IOException;

public final class Shader {
    public interface Native extends NativeObject {}

    private final ShaderType type;
    private final String source;

    private Native nat;

    public Shader(ShaderType type, ResourceFile srcFile) throws IOException {
        this(type, srcFile.readAsString());
    }

    public Shader(ShaderType type, String source) {
        this.type = type;
        this.source = source;
    }

    public ShaderType getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public Native getNative(NativeObjectManager mgr) {
        if (nat == null) {
            nat = RenderBackend.get().createShaderNative(type, source);
            mgr.registerObject(nat);
        }

        return nat;
    }
}
