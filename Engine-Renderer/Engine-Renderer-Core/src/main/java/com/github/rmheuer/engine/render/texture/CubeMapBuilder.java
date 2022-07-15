package com.github.rmheuer.engine.render.texture;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.render.RendererAPI;

import java.io.IOException;

public final class CubeMapBuilder {
    private final TextureSettings settings;
    private TextureData posX, negX, posY, negY, posZ, negZ;

    public CubeMapBuilder(TextureSettings settings) {
        this.settings = settings;
    }

    public CubeMapBuilder setPositiveXFace(TextureData posX) {
        this.posX = posX;
        return this;
    }

    public CubeMapBuilder setPositiveXFace(ResourceFile posX) throws IOException {
        this.posX = TextureData.decode(posX);
        return this;
    }

    public CubeMapBuilder setNegativeXFace(TextureData negX) {
        this.negX = negX;
        return this;
    }

    public CubeMapBuilder setNegativeXFace(ResourceFile negX) throws IOException {
        this.negX = TextureData.decode(negX);
        return this;
    }

    public CubeMapBuilder setPositiveYFace(TextureData posY) {
        this.posY = posY;
        return this;
    }

    public CubeMapBuilder setPositiveYFace(ResourceFile posY) throws IOException {
        this.posY = TextureData.decode(posY);
        return this;
    }

    public CubeMapBuilder setNegativeYFace(TextureData negY) {
        this.negY = negY;
        return this;
    }

    public CubeMapBuilder setNegativeYFace(ResourceFile negY) throws IOException {
        this.negY = TextureData.decode(negY);
        return this;
    }

    public CubeMapBuilder setPositiveZFace(TextureData posZ) {
        this.posZ = posZ;
        return this;
    }

    public CubeMapBuilder setPositiveZFace(ResourceFile posZ) throws IOException {
        this.posZ = TextureData.decode(posZ);
        return this;
    }

    public CubeMapBuilder setNegativeZFace(TextureData negZ) {
        this.negZ = negZ;
        return this;
    }

    public CubeMapBuilder setNegativeZFace(ResourceFile negZ) throws IOException {
        this.negZ = TextureData.decode(negZ);
        return this;
    }

    public CubeMap build() {
        if (posX == null || negX == null ||
            posY == null || negY == null ||
            posZ == null || negZ == null) {
            throw new IllegalStateException("Not all faces set");
        }

        return RendererAPI.getBackend().createCubeMap(settings, posX, negX, posY, negY, posZ, negZ);
    }
}
